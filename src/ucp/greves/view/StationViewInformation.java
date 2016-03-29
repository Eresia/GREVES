package ucp.greves.view;

import ucp.greves.controller.StationController;
import ucp.greves.controller.TrainController;
import ucp.greves.data.exceptions.time.UndefinedTimeException;
import ucp.greves.data.exceptions.train.TrainNotExistException;
import ucp.greves.data.line.station.NextTrainInformations;

public class StationViewInformation {
	
	private NextTrainInformations informations;
	
	public StationViewInformation(NextTrainInformations informations){
		this.informations = informations;
	}
	
	public String getId(){
		return String.valueOf(informations.getId());
	}
	
	public String getDestination(){
		String destination = "";
		try {
			int idStation =TrainController.getRunningTrainById(informations.getId()).getRoadMap().getLastStation();
			destination = StationController.getStationByCantonId(idStation).getName();
		} catch (TrainNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return  destination;
	}
	
	public String getTime(){
		try{
			return informations.getTime().getString();
		} catch(UndefinedTimeException e){
			return "Undefined Time";
		}
	}
	
	public NextTrainInformations getTrainInformations(){
		return informations;
	}

}
