package ucp.greves.model.line.canton;

import java.util.Observable;

import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.configuration.ConfigurationEnvironmentElement;
import ucp.greves.model.exceptions.PropertyNotFoundException;
import ucp.greves.model.exceptions.canton.CantonIsBlockedException;
import ucp.greves.model.exceptions.canton.CantonIsEmptyException;
import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.exceptions.railway.RailWayNotDefinedException;
import ucp.greves.model.exceptions.station.StationNotFoundException;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.RoadMap;
import ucp.greves.model.line.station.HasNotStation;
import ucp.greves.model.line.station.HasStation;
import ucp.greves.model.line.station.Station;
import ucp.greves.model.line.station.StationDecorator;
import ucp.greves.model.simulation.SimulationInfo;
import ucp.greves.model.train.ModifiedTrainInformation;
import ucp.greves.model.train.Train;

public class Canton extends Observable {

	protected int id;
	protected int length;
	protected Train occupyingTrain = null;

	private Canton nextCanton;

	private StationDecorator station;
	private int positionStation;
	private int railWay;
	
	private int trainSpeed;
	private int slowDownSpeed;
	private volatile CantonState state;

	private final static int SPEED_MAX_DEFAULT = 100;
	private final static int SPEED_STATION_DEFAULT = 15;
	private final static int SPEED_SLOWDOWN_DEFAULT = 100;
	private final static int DISTANCE_TO_STATION_DEFAULT = 100;

	private final static int SPEED_MAX = setSpeedMax();
	private final static int SPEED_STATION = setSpeedStation();
	private final static int SPEED_SLOWDOWN = setSpeedSlowDown();
	private final static int DISTANCE_TO_STATION = setDistanceToStation();
	
	public Canton(Canton nextCanton, int railWay, int length) {
		this(railWay, length);
		this.nextCanton = nextCanton;
	}

	public Canton(Canton nextCanton, int length) {
		this(nextCanton, -1, length);
	}
	
	protected Canton(int railWay, int length) {
		this(length);
		this.railWay = railWay;
	}

	protected Canton(int length) {
		this.id = Line.register_canton(this);
		buildCanton(length);
		
		trainSpeed = SPEED_MAX;
	}
	
	private void buildCanton(int length){
		this.length = length;
		station = new HasNotStation();
		positionStation = -1;
	}
	
	public Canton getNextCanton(int rw) throws TerminusException {
		return nextCanton;
	}

	public Canton getNextCanton(RoadMap road) throws TerminusException {
		return nextCanton;
	}

	public int getStartPoint() {
		return nextCanton.getStartPoint() + length;
	}

	public int getLength() {
		return length;
	}

	public synchronized void enter(Train train) {
		if (occupyingTrain != null) {
			if (ConfigurationEnvironment.inDebug()) {
				System.err.println(toString() + " occupied !");
			}
			// Train stopped just before canton start point !
			train.setPosition(getStartPoint() - 1);
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
		}
		
		while(state == CantonState.BLOCKED && !train.hasArrived()){
			try {
				SimulationInfo.waitFrameTime();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		int trainPosition = train.getPosition();
		if (trainPosition < 0) {
			train.setPosition(trainPosition + getStartPoint());
		}

		if (ConfigurationEnvironment.inDebug()) {
			System.err.println("Canton changed successfully");
		}
		Canton oldCanton = train.getCurrentCanton();
		train.setCurrentCanton(this);
		train.updatePosition();

		oldCanton.exit();
		occupyingTrain = train;
		this.setChanged();
		this.notifyObservers();

	}
	
	public int simulateEnter(int position) throws CantonIsBlockedException{
		int newPosition = position;
		if (occupyingTrain != null || (this.getState() == CantonState.BLOCKED)) {
			throw new CantonIsBlockedException();
		}

		if (position < 0) {
			newPosition = position + getStartPoint();
		}

		if (ConfigurationEnvironment.inDebug()) {
			System.err.println("Canton changed successfully");
		}

		return newPosition;
	}
	
	public ModifiedTrainInformation updatedTrainPosition(int position, boolean crossStation){
		int startPoint = getStartPoint();
		int positionOnCanton = startPoint - position;
		int speed = getTrainSpeed(position);
		ModifiedTrainInformation informations = new ModifiedTrainInformation(speed);
		informations.setStationCrossed(false);
		
		if (crossStation) {
			if (positionOnCanton > positionStation && (positionOnCanton - speed) <= positionStation) {
				informations.setUpdatedPosition(positionOnCanton - positionStation);
				informations.setStationCrossed(true);
			}
		}
		
		return informations;
	}
	
	public void createSlowDown(){
		state = CantonState.SLOWSDOWN;
		slowDownSpeed = SPEED_SLOWDOWN;
	}
	
	public void createSlowDown(int newSpeed){
		state = CantonState.SLOWSDOWN;
		slowDownSpeed = newSpeed;
	}
	
	public void blockCanton(){
		state = CantonState.BLOCKED;
	}
	
	public void removeProblem(){
		state = CantonState.NO_PROBLEM;
	}
	
	public int getTrainSpeed(int position){
		if(state == CantonState.BLOCKED){
			return 0;
		}
		int startPoint = getStartPoint();
		int positionOnCanton = startPoint - position;
		int speed = 0;
		int defaultSpeed;
		
		if(state == CantonState.SLOWSDOWN){
			defaultSpeed = slowDownSpeed;
		}
		else{
			defaultSpeed = trainSpeed;
		}
		
		if (hasStation() && (Math.abs(positionOnCanton - positionStation) <= DISTANCE_TO_STATION)) {
			if(SPEED_STATION <= defaultSpeed){
				speed = SPEED_STATION;
			}
			else{
				speed = defaultSpeed;
			}
		} else {
			speed = defaultSpeed;
		}
		return speed;
	}
	
	public CantonState getState(){
		return state;
	}

	public synchronized void exit() {
		occupyingTrain = null;
		notify();
		if (ConfigurationEnvironment.inDebug()) {
			System.err.println("Canton freed !");
		}
		this.setChanged();
		this.notifyObservers();
	}

	public boolean isFree() {
		return occupyingTrain == null;
	}
	
	/**
	 * return the train on the Canton
	 * 
	 * @throws CantonIsEmptyException
	 * 
	 */
	public synchronized Train getOccupyingTrain() throws CantonIsEmptyException{
		if(this.isFree()){
			throw new CantonIsEmptyException();
		}else{
			return this.occupyingTrain;
		}
	}
	

	@Override
	public String toString() {
		return "Canton [id=" + id + "]";
	}

	public int getId() {
		return id;
	}

	public int getEndPoint() {
		return nextCanton.getStartPoint() + 1;
	}
	
	public void enterInStation(Train train){
		station.enter(train);
	}
	
	public int getStationPosition(){
		return positionStation;
	}

	public void setStation(Station station, int position) {
		this.station = new HasStation(station);
		this.positionStation = position;
	}

	public boolean hasStation() {
		return station.hasStation();
	}
	
	public Station getStation() throws StationNotFoundException{
		return station.getStation();
	}
	
	public int getRailWay() throws RailWayNotDefinedException{
		if(railWay == -1){
			throw new RailWayNotDefinedException("RailWay is not define for canton " + getId());
		}
		return railWay;
	}
	
	public void setRailWay(int railWay){
		this.railWay = railWay;
	}
	
	private static int setSpeedMax() {
		int s = SPEED_MAX_DEFAULT;
		try {
			ConfigurationEnvironmentElement speedElement = ConfigurationEnvironment.getInstance()
					.getProperty("train_speed_max");
			if (!speedElement.getType().equals(Integer.class)) {
				System.err.println(
						"Train speed max has not the right type, default value " + SPEED_MAX_DEFAULT + " used");
			} else {
				s = (Integer) speedElement.getValue();
			}
		} catch (PropertyNotFoundException e) {
			System.err.println("Train speed max not defined, default value " + SPEED_MAX_DEFAULT + " used");
		}
		return s;
	}

	private static int setSpeedStation() {
		int s = SPEED_STATION_DEFAULT;
		try {
			ConfigurationEnvironmentElement speedElement = ConfigurationEnvironment.getInstance()
					.getProperty("train_speed_station");
			if (!speedElement.getType().equals(Integer.class)) {
				System.err.println(
						"Train speed station has not the right type, default value " + SPEED_STATION_DEFAULT + " used");
			} else {
				s = (Integer) speedElement.getValue();
			}
		} catch (PropertyNotFoundException e) {
			System.err.println("Train speed station not defined, default value " + SPEED_STATION_DEFAULT + " used");
		}
		return s;
	}
	
	private static int setSpeedSlowDown() {
		int s = SPEED_SLOWDOWN_DEFAULT;
		try {
			ConfigurationEnvironmentElement speedElement = ConfigurationEnvironment.getInstance()
					.getProperty("slow_down_speed");
			if (!speedElement.getType().equals(Integer.class)) {
				System.err.println(
						"Slow down speed has not the right type, default value " + SPEED_STATION_DEFAULT + " used");
			} else {
				s = (Integer) speedElement.getValue();
			}
		} catch (PropertyNotFoundException e) {
			System.err.println("Slow down speed not defined, default value " + SPEED_STATION_DEFAULT + " used");
		}
		return s;
	}

	private static int setDistanceToStation() {
		int d = DISTANCE_TO_STATION_DEFAULT;
		try {
			ConfigurationEnvironmentElement speedElement = ConfigurationEnvironment.getInstance()
					.getProperty("slower_distance_to_station");
			if (!speedElement.getType().equals(Integer.class)) {
				System.err.println("Distance to station has not the right type, default value "
						+ DISTANCE_TO_STATION_DEFAULT + " used");
			} else {
				d = (Integer) speedElement.getValue();
			}
		} catch (PropertyNotFoundException e) {
			System.err
					.println("Distance to station not defined, default value " + DISTANCE_TO_STATION_DEFAULT + " used");
		}
		return d;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Canton other = (Canton) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
