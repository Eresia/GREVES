package ucp.greves.model.train;

import java.util.ArrayList;
import java.util.Observable;

import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.exceptions.station.StationNotFoundException;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.RoadMap;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.station.DepositeryStation;
import ucp.greves.model.simulation.SimulationSpeed;

public class Train extends Observable implements Runnable {
	private int trainID;
	private RoadMap roadMap;
	private volatile int position = 0;
	private Canton currentCanton;
	private int nextStation;

	private volatile boolean hasArrived;

	private volatile boolean isRemoved;
	private volatile DepositeryStation removeStation;

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

	public int getTrainID() {
		return this.trainID;
	}

	public void setTrainID(int value) {
		this.trainID = value;
	}

	public RoadMap getRoadMap() {
		return this.roadMap;
	}

	public void setRoadMap(RoadMap value) {
		this.roadMap = value;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Canton getCurrentCanton() {
		return currentCanton;
	}

	public void setCurrentCanton(Canton currentCanton) {
		this.currentCanton = currentCanton;
	}

	@Override
	public void run() {
		currentCanton.enter(this);
		while (!hasArrived() && !isRemoved()) {
			try {
				SimulationSpeed.waitFrameTime();
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
		if (isRemoved()) {
			removeStation.stockTrain(this);
		}
	}

	public void remove(DepositeryStation station) {
		removeStation = station;
		station.stockTrain(this);
		isRemoved = true;
		hasArrived = true;
		Line.register_arrived_train(trainID);
	}
	
	public int nextStation() throws StationNotFoundException{
		if(nextStation == -1){
			throw new StationNotFoundException();
		}
		
		return nextStation;
	}
	
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

	public void blockTrain() {
		currentCanton.blockCanton();
	}

	public void unblockTrain() {
		currentCanton.removeProblem();
	}

	public boolean hasArrived() {
		return hasArrived;
	}

	public boolean isRemoved() {
		return isRemoved;
	}

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

	public int positionInCanton() {
		return currentCanton.getStartPoint() - position;
	}
	
	public int getSpeed(){
		return currentCanton.getTrainSpeed(position);
	}
	
	@Override
	public String toString() {
		return "Train [speed=" + currentCanton.getTrainSpeed(position) + "]";
	}

}
