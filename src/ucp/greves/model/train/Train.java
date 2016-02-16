package ucp.greves.model.train;

import ucp.greves.model.ControlLine;
import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.configuration.ConfigurationEnvironmentElement;
import ucp.greves.model.configuration.Registry;
import ucp.greves.model.exceptions.PropertyNotFoundException;
import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.line.RoadMap;
import ucp.greves.model.line.canton.Canton;

public class Train implements Runnable {
	private int trainID;
	private RoadMap roadMap;
	private volatile int position = 0;
	private Canton currentCanton;

	private int speed;
	public final static int SPEED_MAX_DEFAULT = 100;
	
	private volatile boolean hasArrived;

	public Train(Canton startCanton, RoadMap map, int speed) {
		int speedMax = SPEED_MAX_DEFAULT;
		try {
			ConfigurationEnvironmentElement speedElement = ConfigurationEnvironment.getInstance().getProperty("train_speed_max");
			if(!speedElement.getType().equals(Integer.class)){
				System.err.println("Train speed max has not the right type, default value " + SPEED_MAX_DEFAULT + " used");
			}
			else{
				speedMax = (Integer) speedElement.getValue();
			}
		} catch (PropertyNotFoundException e) {
			System.err.println("Train speed max not defined, default value " + SPEED_MAX_DEFAULT + " used");
		}
		
		if(speed > speedMax){
			this.speed = speedMax;
		}
		else{
			this.speed = speed;
		}
		
		this.trainID = Registry.register_train(this);
		currentCanton = startCanton;
		currentCanton.enter(this);
		this.roadMap = map;
		hasArrived = false;
		position = ControlLine.getInstance().getLine().getRailWay(map.getRailwaysIDs().get(0)).getLength();
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
		while (!hasArrived()) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException ie) {
				System.err.println(ie.getMessage());
			}
			
			if (position - speed <= currentCanton.getEndPoint()) {
				try {
					Canton nextCanton = currentCanton.getNextCanton(roadMap);
					nextCanton.enter(this);
				} catch (TerminusException e) {
					speed = 0;
					hasArrived = true;
					position = 0;
				}
			} 
			else{
				updatePosition();
			}
		}
		currentCanton.exit();
	}
	
	public boolean hasArrived(){
		return hasArrived;
	}

	@Override
	public String toString() {
		return "Train [speed=" + speed + "]";
	}

	public void updatePosition() {
		position -= speed;
	}

}
