package ucp.greves.model.line.canton;

import java.util.Observable;

import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.exceptions.railway.RailWayNotDefinedException;
import ucp.greves.model.exceptions.station.StationNotFoundException;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.RoadMap;
import ucp.greves.model.line.station.StationDecorator;
import ucp.greves.model.line.station.HasStation;
import ucp.greves.model.line.station.HasNotStation;
import ucp.greves.model.line.station.Station;
import ucp.greves.model.train.Train;

public class Canton extends Observable {

	protected int id;
	protected int length;
	protected Train occupyingTrain = null;

	private Canton nextCanton;

	private StationDecorator station;
	private int positionStation;
	private int railWay;
	
	public Canton(Canton nextCanton, int railWay, int length) {
		this(length);
		this.nextCanton = nextCanton;
		this.railWay = railWay;
	}

	public Canton(Canton nextCanton, int length) {
		this(nextCanton, -1, length);
	}

	protected Canton(int length) {
		this.id = Line.register_canton(this);
		buildCanton(length);
	}
	
	private void buildCanton(int length){
		this.length = length;
		station = new HasNotStation();
		positionStation = -1;
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
	
	public void enterInStation(){
		try {
			station.waitInStation();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public int getStationPosition(){
		return positionStation;
	}

	public void setStation(Station station, int position) {
		this.station = new HasStation(station);
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
