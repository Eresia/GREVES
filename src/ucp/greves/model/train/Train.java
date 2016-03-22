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

	public Train(Canton startCanton, RoadMap map, int speed) {

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
		isRemoved = true;
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

	/*Deprecated*/
	/*public Station getNextStation() throws StationNotFoundException{
		Station result = null;
		if (currentCanton.hasStation() && currentCanton.getStationPosition() > positionInCanton()) {
			result = currentCanton.getStation();
		}
		else{
			try{
				Canton actual = currentCanton.getNextCanton(roadMap);
				boolean stationFound = false;
				while(!stationFound){
					if(actual.hasStation()){
						result = actual.getStation();
						stationFound = true;
					}
					else{
						actual = actual.getNextCanton(roadMap);
					}
				}
			} catch(TerminusException e){
				throw new StationNotFoundException();
			}
		}

		return result;
	}
	
	public ArrayList<Station> getNextStations(){
		ArrayList<Station> result = new ArrayList<Station>();
		
		try{
			if (currentCanton.hasStation() && currentCanton.getStationPosition() > positionInCanton()) {
				result.add(currentCanton.getStation());
			}
	
			try{
				Canton actual = currentCanton.getNextCanton(roadMap);
				while(true){
					if(actual.hasStation()){
						result.add(actual.getStation());
					}
					actual = actual.getNextCanton(roadMap);
				}
			} catch(TerminusException e){
				
			}
		} catch(StationNotFoundException e){
			e.printStackTrace();
		}
		
		return result;
	}*/

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

	@Override
	public String toString() {
		return "Train [speed=" + currentCanton.getTrainSpeed(position) + "]";
	}

	public void updatePosition() {
		ModifiedTrainInformation informations;
		try {
			informations = currentCanton.updatedTrainPosition(position,	roadMap.cross(currentCanton.getStation().getName()));
		} catch (StationNotFoundException e) {
			informations = currentCanton.updatedTrainPosition(position, false);
		}
		position -= informations.getUpdatedPosition();
		
		if(informations.getStationCrossed()){
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

}
