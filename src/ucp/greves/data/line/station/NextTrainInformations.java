package ucp.greves.data.line.station;

import ucp.greves.controller.StationController;
import ucp.greves.controller.TrainController;
import ucp.greves.data.exceptions.station.StationNotFoundException;
import ucp.greves.data.exceptions.train.TrainNotExistException;
import ucp.greves.data.time.TimeDecorator;
import ucp.greves.data.train.Train;

public class NextTrainInformations {
	
	private final int id;
	private final TimeDecorator time;
	private String destination;
	
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
	
	public String getDestination(){
		try {
			int lastIndex =TrainController.getRunningTrainById(id).nextStations().size() -1;
			int idStation = TrainController.getRunningTrainById(id).nextStations().get(lastIndex);
			destination = StationController.getStationByCantonId(idStation).getName();
		} catch (StationNotFoundException | TrainNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  destination;
	}

}
