package ucp.greves.data.line.station;

import ucp.greves.controller.StationController;
import ucp.greves.controller.TrainController;
import ucp.greves.data.exceptions.train.TrainNotExistException;
import ucp.greves.data.time.TimeDecorator;

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
