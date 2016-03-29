package ucp.greves.data.train;

import java.util.ArrayList;
import java.util.Observable;

import ucp.greves.data.exceptions.canton.TerminusException;
import ucp.greves.data.exceptions.station.StationNotFoundException;
import ucp.greves.data.exceptions.train.TrainIsNotInACanton;
import ucp.greves.data.exceptions.train.TrainIsNotInThisCanton;
import ucp.greves.data.line.canton.Canton;
import ucp.greves.data.line.roadMap.RoadMap;
import ucp.greves.data.line.station.DepositeryStation;
import ucp.greves.model.line.Line;
import ucp.greves.model.simulation.SimulationInfo;
import ucp.greves.model.train.ModifiedTrainInformation;

/**
 * Train is a thread.
 * It follows a roadmap and knows its current canton
 *
 */
public class Train extends Observable implements Runnable {
	private int trainID;
	private RoadMap roadMap;
	private volatile int position = 0;
	private Canton currentCanton;
	private int nextStation;

	private volatile boolean hasArrived;

	private volatile boolean isRemoved;
	private volatile DepositeryStation removeStation;

	/**
	 * Creates a Train and registers it
	 * 
	 * @param startCanton
	 * 		(Canton) The canton where the trains starts
	 * @param map
	 * 		(RoadMap) The roadmap the train follows
	 * 
	 * @see Line#register_train(Train)
	 */
	public Train(Canton startCanton, RoadMap map) {

		this.trainID = Line.register_train(this);
		currentCanton = startCanton;
		this.roadMap = map;
		hasArrived = false;
		isRemoved = false;
		removeStation = null;
		position = Line.getRailWays().get(map.getRailwaysIDs().get(0)).getLength();
		nextStation = map.getStations().get(0);
	}

	/**
	 * @return (Integer) Returns the id of the train
	 */
	public int getTrainID() {
		return this.trainID;
	}

	/**
	 * Sets the train ID
	 * @param value
	 * 		(Integer) The new ID value of the train
	 */
	public void setTrainID(int value) {
		this.trainID = value;
	}

	/**
	 * @return (RoadMap) Returns the roadmap the train follows
	 */
	public RoadMap getRoadMap() {
		return this.roadMap;
	}

	/**
	 * Set the roadmap the train follows
	 * @param value
	 * 		(RoadMap) The new roadmap
	 */
	public void setRoadMap(RoadMap value) {
		this.roadMap = value;
	}

	/**
	 * @return (Integer) Returns the position of the train on the current railway
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Sets the train at a specific position on the railway
	 * @param position
	 * 		(Integer) The new position
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return (Canton) Returns the canton where the train is
	 */
	public Canton getCurrentCanton() {
		return currentCanton;
	}

	/**
	 * Sets the train at a specific canton
	 * @param currentCanton
	 * 		(Canton) The new canton where the train will be
	 */
	public void setCurrentCanton(Canton currentCanton) {
		this.currentCanton = currentCanton;
	}

	@Override
	public void run() {
		currentCanton.enter(this);
		while (!hasArrived() && !isRemoved()) {
			try {
				SimulationInfo.waitFrameTime();
			} catch (InterruptedException ie) {
				System.err.println(ie.getMessage());
			}
			if (position - currentCanton.getTrainSpeed(position) <= currentCanton.getEndPoint()) {
				try {
					Canton nextCanton = currentCanton.getNextCanton(roadMap);
					nextCanton.enter(this);
				} catch (TerminusException e) {
					hasArrived = true;
					Line.register_arrived_train(trainID);
					position = 0;
				}
			} else {
				updatePosition();
			}
			this.setChanged();
			this.notifyObservers();
			
		}
		currentCanton.exit();
		currentCanton = null;
		if (isRemoved()) {
			removeStation.stockTrain(this);
		}
	}

	/**
	 * Stocks the train at the station
	 * @param station
	 * 		(DepositeryStation) The station where the train will be stocked
	 */
	public void remove(DepositeryStation station) {
		removeStation = station;
		station.stockTrain(this);
		isRemoved = true;
		hasArrived = true;
		Line.register_arrived_train(trainID);
	}
	
	/**
	 * @return (Integer) Returns the ID of the next {@link ucp.greves.data.line.station.Station} the train needs to stop
	 * @throws StationNotFoundException if there isn't any next station
	 */
	public int nextStation() throws StationNotFoundException{
		if(nextStation == -1){
			throw new StationNotFoundException();
		}
		
		return nextStation;
	}
	
	/**
	 * @return (ArrayList<Integer>) Returns the list of IDs of the next stations the train needs to stop
	 * @throws StationNotFoundException if there isn't any station
	 * 
	 * @see ucp.greves.data.line.station.Station
	 */
	public ArrayList<Integer> nextStations() throws StationNotFoundException{
		if(nextStation == -1){
			throw new StationNotFoundException();
		}
		ArrayList<Integer> stations = roadMap.getStations();
		ArrayList<Integer> next = new ArrayList<Integer>();
		int actualPos = stations.indexOf(nextStation);
		
		for(int i = actualPos; i < stations.size(); i++){
			next.add(stations.get(i));
		}
		
		return next;
	}

	/**
	 * Blocks the canton where the train is
	 */
	public void blockTrain() {
		currentCanton.blockCanton();
	}

	/**
	 * Unlocks the canton where the train is
	 */
	public void unblockTrain() {
		currentCanton.removeProblem();
	}

	/**
	 * @return (boolean) Returns if the train has arrived or not
	 */
	public boolean hasArrived() {
		return hasArrived;
	}

	/**
	 * @return (boolean) Returns if the train is removed or not
	 */
	public boolean isRemoved() {
		return isRemoved;
	}

	/**
	 * Updates the position of the train
	 */
	public void updatePosition() {
		ModifiedTrainInformation informations;
		if(currentCanton.hasStation()){
			informations = currentCanton.updatedTrainPosition(position,	roadMap.cross(currentCanton.getId()));
		}
		else{
			informations = currentCanton.updatedTrainPosition(position,	false);
		}
		position -= informations.getUpdatedPosition();
		
		if(informations.getStationCrossed()){
			currentCanton.enterInStation(this);
			ArrayList<Integer> stationList = roadMap.getStations();
			int actualStationPos = stationList.indexOf(nextStation);
			if(actualStationPos == (stationList.size()-1)){
				nextStation = -1;
			}
			else{
				nextStation = stationList.get(actualStationPos + 1);
			}
		}
		
		this.setChanged();
	}

	/**
	 * @return (Integer) Returns the position of the train in the canton
	 */
	public int positionInCanton() throws TrainIsNotInACanton{
		if(currentCanton == null){
			throw new TrainIsNotInACanton();
		}
		return currentCanton.getStartPoint() - position;
	}
	
	/**
	 * @param canton 
	 * 				(Canton) Verify if the train is in this canton
	 * @return (Integer) Returns the position of the train in the canton
	 */
	public int positionInCanton(Canton canton) throws TrainIsNotInThisCanton{
		int result = canton.getStartPoint() - position;
		if(!canton.equals(currentCanton) || (result < 0) || (result > canton.getLength())){
			throw new TrainIsNotInThisCanton() ;
		}
		return result;
	}
	
	/**
	 * @return (Integer) Returns the speed of the train
	 */
	public int getSpeed(){
		return currentCanton.getTrainSpeed(position);
	}
	
	@Override
	public String toString() {
		return "Train [speed=" + currentCanton.getTrainSpeed(position) + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Train other = (Train) obj;
		if (trainID != other.trainID)
			return false;
		return true;
	}
	
	

}
