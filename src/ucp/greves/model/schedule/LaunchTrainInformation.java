package ucp.greves.model.schedule;

import ucp.greves.data.time.Time;

/**
 * This class is used to store informations about launching a train.
 * It has a time and the name of a roadmap.
 * 
 * @see Time
 * @see RoadMap
 */
public class LaunchTrainInformation {
	
	private Time time;
	private String roadMap;
	
	/**
	 * Initializes the departure of a train
	 * @param time
	 * 		(Time) The time when a train will be launched
	 * @param roadMap
	 * 		(String) The name of the roadmap to follow
	 */
	public LaunchTrainInformation(Time time, String roadMap){
		this.time = time;
		this.roadMap = roadMap;
	}

	/**
	 * @return (Time) Returns the time of departure
	 */
	public Time getTime() {
		return time;
	}

	/**
	 * Sets the time of departure
	 * @param time
	 * 		(Time) The new time of departure
	 */
	public void setTime(Time time) {
		this.time = time;
	}

	/**
	 * @return (String) Returns the name of the roadmap
	 */
	public String getRoadMap() {
		return roadMap;
	}

	/**
	 * Sets the roadmap
	 * @param roadMap
	 * 		(String) The name of the roadmap for the train to follow
	 */
	public void setRoadMap(String roadMap) {
		this.roadMap = roadMap;
	}
	
	public String toString(){
		return "Information - Time : " + time + ", RoadMap : " + roadMap;
	}

}
