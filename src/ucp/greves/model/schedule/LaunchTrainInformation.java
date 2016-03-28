package ucp.greves.model.schedule;

import ucp.greves.data.time.Time;

public class LaunchTrainInformation {
	
	private Time time;
	private String roadMap;
	
	public LaunchTrainInformation(Time time, String roadMap){
		this.time = time;
		this.roadMap = roadMap;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public String getRoadMap() {
		return roadMap;
	}

	public void setRoadMap(String roadMap) {
		this.roadMap = roadMap;
	}
	
	public String toString(){
		return "Information - Time : " + time + ", RoadMap : " + roadMap;
	}

}
