package ucp.greves.model;

import java.lang.Runnable;

class Train implements Runnable {
	private RoadMap roadMap;
	private int position = 0;
	private Canton currentCanton;

	private int trainID;
	
	/**
	 * Distance per time unit.
	 */
	private int speed;
	private boolean hasArrived = false;

	public Train(Canton startCanton, RoadMap map, int speed) {
		this.roadMap = map;
		currentCanton = startCanton;
		currentCanton.enter(this);
		this.speed = speed;
	}

	public RoadMap getRoadMap() {
		return this.roadMap;
	}

	public void setRoadMap(RoadMap value) {
		this.roadMap = value;
	}

	public int getTrainID() {
		return this.trainID;
	}

	public void setTrainID(int value) {
		this.trainID = value;
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
			// try {
			// sleep(SimulationGUI.TIME_UNIT);
			// } catch (InterruptedException e) {
			// System.err.println(e.getMessage());
			// }
			// if (position + speed >= currentCanton.getEndPoint()) {
			// try {
			// Canton nextCanton = line.getCantonByPosition(position + speed);
			// nextCanton.enter(this);
			// } catch (TerminusException e) {
			// hasArrived = true;
			// position = line.getTotalLenght();
			// }
			// } else {
			// updatePosition();
			// }
		}
		currentCanton.exit();
	}

	@Override
	public String toString() {
		return "Train [speed=" + speed + "]";
	}

	public void updatePosition() {
		position += speed;
	}

}
