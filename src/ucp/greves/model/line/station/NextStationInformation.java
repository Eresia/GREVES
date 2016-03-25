package ucp.greves.model.line.station;

import ucp.greves.model.schedule.Time;

public class NextStationInformation {
	
	private int station;
	private Time timeToTravel;
	
	public NextStationInformation(int station, Time timeToTravel){
		this.station = station;
		this.timeToTravel = timeToTravel;
	}

	public int getStation() {
		return station;
	}

	public Time getTimeToTravel() {
		return timeToTravel;
	}
	
	

}
