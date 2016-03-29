package ucp.greves.data.line.canton;

import java.util.Observable;

import ucp.greves.data.exceptions.PropertyNotFoundException;
import ucp.greves.data.exceptions.canton.CantonIsBlockedException;
import ucp.greves.data.exceptions.canton.CantonIsEmptyException;
import ucp.greves.data.exceptions.canton.TerminusException;
import ucp.greves.data.exceptions.railway.RailWayNotDefinedException;
import ucp.greves.data.exceptions.station.StationNotFoundException;
import ucp.greves.data.line.roadMap.RoadMap;
import ucp.greves.data.line.station.Station;
import ucp.greves.data.train.Train;
import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.configuration.ConfigurationEnvironmentElement;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.station.HasNotStation;
import ucp.greves.model.line.station.HasStation;
import ucp.greves.model.line.station.StationDecorator;
import ucp.greves.model.simulation.SimulationInfo;
import ucp.greves.model.train.ModifiedTrainInformation;

/**
 * This class represents a part of a railway. Only 1 train per canton is authorized
 * 
 * @see ucp.greves.data.line.canton.Terminus
 * {@link ucp.greves.data.line.canton.CantonState}
 * @see ucp.greves.data.line.railWay.RailWay
 */
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
	
	/**
	 * Creates a Canton
	 * 
	 * @param nextCanton
	 * 		(Canton) The next canton he is attached to
	 * @param railWay
	 * 		(Integer) The id of the railway he is on
	 * @param length
	 * 		(Integer) the size of this canton
	 */
	public Canton(Canton nextCanton, int railWay, int length) {
		this(railWay, length);
		this.nextCanton = nextCanton;
	}

	/**
	 * Creates a Canton
	 * 
	 * @param nextCanton
	 * 		(Canton) The next canton he is attached to
	 * @param length
	 * 		(Integer) The size of this canton
	 */
	public Canton(Canton nextCanton, int length) {
		this(nextCanton, -1, length);
	}
	
	/**
	 * Creates a Canton
	 * 
	 * @param railWay
	 * 		(Integer) The id of the railway he is on
	 * @param length
	 * 		(Integer) The size of the canton
	 */
	protected Canton(int railWay, int length) {
		this(length);
		this.railWay = railWay;
	}

	/**
	 * Creates a Canton and registers it
	 * 
	 * @param length
	 * 		(Integer) The size of the canton
	 */
	protected Canton(int length) {
		this.id = Line.register_canton(this);
		this.state = CantonState.NO_PROBLEM;
		buildCanton(length);
		
		trainSpeed = SPEED_MAX;
	}
	
	/**
	 * Rebuilds a Canton
	 * 
	 * @param length
	 * 		(Integer) The new length of the canton
	 */
	private void buildCanton(int length){
		this.length = length;
		station = new HasNotStation();
		positionStation = -1;
	}
	
	/**
	 * Gets the canton where the train should go after arriving at the end of this one
	 * 
	 * @param rw
	 * 		(Integer) The id of the railway
	 * @return 
	 * 		(Canton) The canton following this one on the railway
	 * @throws TerminusException
	 */
	public Canton getNextCanton(int rw) throws TerminusException {
		return nextCanton;
	}

	/**
	 * Gets the canton where the train should go after arriving at the end of this one
	 * 
	 * @param road
	 * 		(RoadMap) The roadmap the train follows
	 * @return
	 * 		(Canton) The canton following this one on the roadmap
	 * @throws TerminusException
	 */
	public Canton getNextCanton(RoadMap road) throws TerminusException {
		return nextCanton;
	}

	/**
	 * 
	 * @return 
	 * 		(Integer) Returns the total length from the start of this canton to the end of the railway
	 */
	public int getStartPoint() {
		return nextCanton.getStartPoint() + length;
	}

	/**
	 * 
	 * @return 
	 * 		(Integer) Returns the length of the canton
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Let a train enter this canton and make it move
	 * If the canton is occupied, it blocks the train
	 * 
	 * @param train
	 * 		(Train) The train to enter this canton
	 */
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
		
		if(state == CantonState.BLOCKED){
			train.setPosition(getStartPoint() - 1);
		}
		
		while(state == CantonState.BLOCKED && !train.hasArrived()){
			try {
				SimulationInfo.waitFrameTime();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		int diffPosition = getStartPoint() - train.getPosition();
		train.setPosition(getStartPoint(), false);

		if (ConfigurationEnvironment.inDebug()) {
			System.err.println("Canton changed successfully");
		}
		Canton oldCanton = train.getCurrentCanton();
		train.setCurrentCanton(this);

		oldCanton.exit();
		occupyingTrain = train;
		this.setChanged();
		this.notifyObservers();

	}
	
	/**
	 * Tests if a train can enter a canton
	 * 
	 * @param position
	 * 		(Integer) The position on the railway of the simulated train
	 * @return
	 * 		(Integer) Returns the new position on the railway of the simulated train
	 * @throws CantonIsBlockedException
	 */
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
	
	/**
	 * 
	 * @param position
	 * @param crossStation
	 * @return
	 * 
	 * @see ucp.greves.model.train.ModifiedTrainInformation
	 */
	public ModifiedTrainInformation updatedTrainPosition(int position, boolean crossStation){
		return updatedTrainPosition(position, crossStation, getTrainSpeed(position));
	}
	
	/**
	 * 
	 * @param position
	 * @param crossStation
	 * @param int distance
	 * @return
	 * 
	 * @see ucp.greves.model.train.ModifiedTrainInformation
	 */
	public ModifiedTrainInformation updatedTrainPosition(int position, boolean crossStation, int distance){
		int startPoint = getStartPoint();
		int positionOnCanton = startPoint - position;
		ModifiedTrainInformation informations = new ModifiedTrainInformation(distance);
		informations.setStationCrossed(false);
		
		if (crossStation && hasStation()) {
			if (positionOnCanton > positionStation && (positionOnCanton - distance) <= positionStation) {
				informations.setUpdatedPosition(positionOnCanton - positionStation);
				informations.setStationCrossed(true);
			}
		}
		
		return informations;
	}
	
	/**
	 * Slows down the speed of the canton
	 */
	public void createSlowDown(){
		state = CantonState.SLOWSDOWN;
		slowDownSpeed = SPEED_SLOWDOWN;
	}
	
	/**
	 * Slows down the speed of the canton
	 * 
	 * @param newSpeed
	 * 		(Integer) The new speed during the slow down
	 */
	public void createSlowDown(int newSpeed){
		state = CantonState.SLOWSDOWN;
		slowDownSpeed = newSpeed;
	}
	
	/**
	 * Blocks the canton
	 */
	public void blockCanton(){
		state = CantonState.BLOCKED;
	}
	
	/**
	 * Puts the canton to its normal state
	 */
	public void removeProblem(){
		state = CantonState.NO_PROBLEM;
	}
	
	/**
	 * Gets the speed of the train on this canton
	 * 
	 * @param position
	 * 		(Integer) The position of the train
	 * @return
	 * 		(Integer) Returns the speed the train should have on this position
	 */
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
	
	/**
	 * Gets the state of the canton
	 * @return
	 * 		(CantonState) Returns the state of the canton
	 */
	public CantonState getState(){
		return state;
	}

	/**
	 * Makes a train exit the canton
	 */
	public synchronized void exit() {
		occupyingTrain = null;
		if (ConfigurationEnvironment.inDebug()) {
			System.err.println("Canton freed !");
		}
		this.setChanged();
		this.notifyObservers();
		notify();
	}

	/**
	 * Checks if the canton is free
	 * @return
	 * 		(boolean) Returns if there is a train on the canton or not
	 */
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

	/**
	 * 
	 * @return
	 * 		(Integer) Returns the id of the canton when it was created
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @return
	 * 		(Integer) Returns the distance from the beginning of the next canton to the end of the railway
	 */
	public int getEndPoint() {
		return nextCanton.getStartPoint() + 1;
	}
	
	/**
	 * Makes a train enter the station
	 * 
	 * @param train
	 * 		(Train) The train to enter the station
	 */
	public void enterInStation(Train train){
		station.enter(train);
	}
	
	/**
	 * 
	 * @return
	 * 		(Integer) Returns the position of the Station in the canton
	 */
	public int getStationPosition() throws StationNotFoundException{
		return positionStation;
	}

	/**
	 * Creates a station in the canton
	 * @param station
	 * 		(Station) The station to create
	 * @param position
	 * 		(Integer) The position on the canton where the station is
	 * 
	 * @see ucp.greves.data.line.station.Station
	 */
	public void setStation(Station station, int position) {
		this.station = new HasStation(station);
		this.positionStation = position;
	}

	/**
	 * Checks if the canton has a station
	 * @return
	 * 		(boolean) Returns if the canton has a station or not
	 */
	public boolean hasStation() {
		return station.hasStation();
	}
	
	/**
	 * Gets the station on the canton
	 * 
	 * @return
	 * 		(Station) The station of the canton
	 * @throws StationNotFoundException if there is no station on this canton
	 * 
	 * @see ucp.greves.data.line.station.Station
	 */
	public Station getStation() throws StationNotFoundException{
		return station.getStation();
	}
	
	/**
	 * Gets the railway the canton is on
	 * 
	 * @return
	 * 		(Integer) The id of the railway
	 * @throws RailWayNotDefinedException if the canton is not attached to the railway
	 * 
	 * @see ucp.greves.data.line.railWay.RailWay
	 */
	public int getRailWay() throws RailWayNotDefinedException{
		if(railWay == -1){
			throw new RailWayNotDefinedException("RailWay is not define for canton " + getId());
		}
		return railWay;
	}
	
	/**
	 * Sets the railway for this canton
	 * 
	 * @param railWay
	 * 		(Integer) The id of the railway to attach this canton to
	 */
	public void setRailWay(int railWay){
		this.railWay = railWay;
	}
	
	/**
	 * Sets the maximum speed for this canton, following the environment configuration
	 * 
	 * @return
	 * 		(Integer) The maximum speed for this canton
	 * 
	 * @see ucp.greves.model.configuration.ConfigurationEnvironment
	 */
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

	/**
	 * Sets the current speed for this canton, following the environment configuration
	 * 
	 * @return
	 * 		(Integer) The current speed for this canton
	 * 
	 * @see ucp.greves.model.configuration.ConfigurationEnvironment
	 */
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

	/**
	 * Sets the speed for this canton when he is on slow down, following the environment configuration
	 * 
	 * @return
	 * 		(Integer) The slow down speed for this canton
	 * 
	 * @see ucp.greves.model.configuration.ConfigurationEnvironment
	 */
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

	/**
	 * Sets the speed of the train when arriving to the station, following the environment configuration
	 * 
	 * @return
	 * 		(Integer) The speed of the train when arriving to the station
	 * 
	 * @see ucp.greves.model.configuration.ConfigurationEnvironment
	 */
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
