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

public class Train extends Observable implements Runnable {
	private int trainID;
	private RoadMap roadMap;
	private volatile int position = 0;
	private Canton currentCanton;

	private int speed;
	private final int speedDefault;

	private final static int SPEED_MAX_DEFAULT = 100;
	private final static int SPEED_STATION_DEFAULT = 15;
	private final static int DISTANCE_TO_STATION_DEFAULT = 100;
	private final static int FRAME_DURATION_DEFAULT = 50;

	private final static int SPEED_MAX = setSpeedMax();
	private final static int SPEED_STATION = setSpeedStation();
	private final static int DISTANCE_TO_STATION = setDistanceToStation();
	private final static int FRAME_DURATION = setFrameDuration();
	private volatile static int simulationSpeed = 1;

	private volatile boolean hasArrived;
	
	private volatile boolean isRemoved;
	private volatile DepositeryStation removeStation;

	public Train(Canton startCanton, RoadMap map, int speed) {
		if (speed > SPEED_MAX) {
			this.speed = SPEED_MAX;
			this.speedDefault = SPEED_MAX;
		} else {
			this.speed = speed;
			this.speedDefault = speed;
		}

		this.trainID = Line.register_train(this);
		currentCanton = startCanton;
		this.roadMap = map;
		hasArrived = false;
		isRemoved = false;
		removeStation = null;
		position = Line.getRailWays().get(map.getRailwaysIDs().get(0)).getLength();
		currentCanton.enter(this);
	}
	
	public static void changeSimulationSpeed(int duration){
		simulationSpeed = duration;
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
		while (!hasArrived() && !isRemoved()) {
			try {
				Thread.sleep(FRAME_DURATION);
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
			} else {
				updatePosition();
			}
			this.setChanged();
			this.notifyObservers();
		}
		currentCanton.exit();
		if(!isRemoved()){
			removeStation.stockTrain(this);
		}
	}
	
	public void waitFrameTime() throws InterruptedException{
		int waitVar = 0;
		while(waitVar < FRAME_DURATION){
			Thread.sleep(1);
			waitVar += simulationSpeed;
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
		return "Train [speed=" + speed + "]";
	}

	public void updatePosition() {
		int startPoint = currentCanton.getStartPoint();
		int positionStation = currentCanton.getStationPosition();
		int positionOnCanton = startPoint - position;

		try {
			boolean crossStation = roadMap.cross(currentCanton.getStation().getName());
			if (positionOnCanton < positionStation && (positionOnCanton + DISTANCE_TO_STATION) >= positionStation) {
				speed = SPEED_STATION;
			} else {
				speed = speedDefault;
			}
			if (crossStation) {
				if (positionOnCanton < positionStation && (positionOnCanton + speed) >= positionStation) {
					currentCanton.enterInStation();
				}
			}
		} catch (StationNotFoundException e) {}

		position -= speed;
		this.setChanged();
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
	
	private static int setFrameDuration() {
		int fD = FRAME_DURATION_DEFAULT;
		try {
			ConfigurationEnvironmentElement frameElement = ConfigurationEnvironment.getInstance()
					.getProperty("frame_simulation_duration");
			if (!frameElement.getType().equals(Integer.class)) {
				System.err.println(
						"Frame duration has not the right type, default value " + SPEED_STATION_DEFAULT + " used");
			} else {
				fD = (Integer) frameElement.getValue();
			}
		} catch (PropertyNotFoundException e) {
			System.err.println("Frame duration not defined, default value " + SPEED_STATION_DEFAULT + " used");
		}
		return fD;
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

}
