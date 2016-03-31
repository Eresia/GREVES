package ucp.greves.data.line.station;

import ucp.greves.data.time.TimeDecorator;

/**
 * This class represents the informations of the next trains for a station
 */
public class NextTrainInformations {
	
	private final int id;
	private final TimeDecorator time;
	
	public NextTrainInformations(int id, TimeDecorator time){
		this.id = id;
		this.time = time;
	}
	
	public int getId(){
		return id;
	}
	
	public TimeDecorator getTime(){
		return time;
	}

}
