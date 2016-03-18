package ucp.greves.model.train;

import java.util.Observable;

import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.configuration.ConfigurationEnvironmentElement;
import ucp.greves.model.exceptions.PropertyNotFoundException;
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
		if(isRemoved()){
			removeStation.stockTrain(this);
		}
	}
	
	public void remove(DepositeryStation station){
		removeStation = station;
		isRemoved = true;
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
		
		try {
			position -= currentCanton.updateTrainPosition(position, roadMap.cross(currentCanton.getStation().getName()));
		} catch (StationNotFoundException e) {
			position -= currentCanton.updateTrainPosition(position, false);
		}
		this.setChanged();
	}

}
