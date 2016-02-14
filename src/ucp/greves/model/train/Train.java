package ucp.greves.model.train;

import java.lang.Runnable;

import ucp.greves.model.configuration.Registry;
import ucp.greves.model.exceptions.TerminusException;
import ucp.greves.model.line.RoadMap;
import ucp.greves.model.line.canton.Canton;

public class Train implements Runnable {
	private int trainID;
	private RoadMap roadMap;
	private Canton currCanton;
	private volatile int position = 0;
	private Canton currentCanton;

	private int speed;
	private boolean hasArrived = false;

	public Train(Canton startCanton, RoadMap map, int speed) {
		this.trainID = Registry.register_train(this);
		currentCanton = startCanton;
		currentCanton.enter(this);
		this.speed = speed;
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
		while (!hasArrived) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException ie) {
				System.err.println(ie.getMessage());
			}
			
			if (position + speed >= currentCanton.getEndPoint()) {
				try {
					Canton nextCanton = currCanton.getNextCanton();
					nextCanton.enter(this);
				} catch (TerminusException e) {
					hasArrived = true;
					//position = line.getTotalLenght();
				}
			} else {
				updatePosition();
			}
		}
		currentCanton.exit();
	}

	@Override
	public String toString() {
		return "Train [speed=" + speed + "]";
	}

	public void updatePosition() {
		position -= speed;
	}

}