package ucp.greves.data.train;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

import ucp.greves.data.exceptions.BadControlInformationException;
import ucp.greves.data.exceptions.canton.TerminusException;
import ucp.greves.data.exceptions.railway.RailWayNotExistException;
import ucp.greves.data.exceptions.roadmap.BadRoadMapException;
import ucp.greves.data.exceptions.roadmap.EmptyRoadMapException;
import ucp.greves.data.exceptions.roadmap.RoadMapNameNotExistException;
import ucp.greves.data.exceptions.station.StationNotFoundException;
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
		if(map.getStations().size() != 0){
			nextStation = map.getStations().get(0);
		}
		else{
			nextStation = -1;
		}
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
		setPosition(position, true);
	}

	/**
	 * Sets the train at a specific position on the railway
	 * @param position
	 * 		(Integer) The new position
	 * @param notify
	 * 		(Boolean) if train has to notify observers
	 */
	public void setPosition(int position, boolean notify) {
		this.position = position;
		if(notify){
			this.setChanged();
			this.notifyObservers(false);
		}
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
			this.notifyObservers(false);
			
		}
		currentCanton.exit();
		currentCanton = null;
		if (isRemoved()) {
			removeStation.stockTrain(this);
		}
	}
	
	public void notifyOnExit(){
		this.setChanged();
		this.notifyObservers(true);
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

		updatePositionProcess(informations);
	}
	
	/**
	 * Updates the position of the train with a specific distance
	 * @param distance
	 * 				(Integer) The distance traveled
	 */
	public void updatePosition(int distance) {
		ModifiedTrainInformation informations;
		if(currentCanton.hasStation()){
			informations = currentCanton.updatedTrainPosition(position,	roadMap.cross(currentCanton.getId()), distance);
		}
		else{
			informations = currentCanton.updatedTrainPosition(position,	false, distance);
		}
		updatePositionProcess(informations);
	}
	
	/**
	 * Process of updatePosition function
	 * @param informations
	 * 					(ModifiedTrainInformation) informations of process
	 */
	private void updatePositionProcess(ModifiedTrainInformation informations){
		position -= informations.getUpdatedPosition();
		this.setChanged();
		this.notifyObservers(false);
		
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
	}

	/**
	 * @return (Integer) Returns the position of the train in the canton
	 * @throws TrainIsNotInThisCanton 
	 */
	public int positionInCanton() throws TrainIsNotInThisCanton{
		return positionInCanton(currentCanton);
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
	 * Add a train on the line with a road map
	 * @param road
	 * @throws BadControlInformationException If line is not correctly set
	 * @throws BadRoadMapException If roadmaps don't exist or its informations are not correctly set
	 * @throws RailWayNotExistException If Railway don't exist
	 */
	public static void launchTrain(String road)	throws BadControlInformationException, BadRoadMapException, RailWayNotExistException {
		verifyInformation();
		HashMap<String, RoadMap> roads = Line.getRoadMaps();
		if (!roads.containsKey(road)) {
			throw new RoadMapNameNotExistException("Impossible to launch the train - the road don't exist");
		}
		ArrayList<Integer> rails = roads.get(road).getRailwaysIDs();
		if (rails.size() == 0) {
			throw new EmptyRoadMapException("Impossible to launch the train - the road is empty");
		}
		if (Line.getRailWays().get(rails.get(0)) == null) {
			throw new RailWayNotExistException("Impossible to launch the train - the rail way don't exist");
		}
		Train t = new Train(Line.getRailWays().get(rails.get(0)).getFirstCanton(), roads.get(road));
		Thread tThread = new Thread(t);
		tThread.start();
	}
	
	/**
	 * Verify if the line is correctly set
	 * @throws BadControlInformationException If line is not correctly set
	 */
	private static void verifyInformation() throws BadControlInformationException {
		if (Line.getRailWays().size() == 0) {
			throw new BadControlInformationException("No RailWay");
		}
		if (Line.getCantons().size() == 0) {
			throw new BadControlInformationException("No Canton");
		}
		if (Line.getRoadMaps().size() == 0) {
			throw new BadControlInformationException("No road");
		}
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
