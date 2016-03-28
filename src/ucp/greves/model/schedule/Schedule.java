package ucp.greves.model.schedule;

import java.util.ArrayList;

import ucp.greves.controller.GodModeController;
import ucp.greves.controller.TrainController;
import ucp.greves.data.time.Time;
import ucp.greves.model.exceptions.BadControlInformationException;
import ucp.greves.model.exceptions.railway.RailWayNotExistException;
import ucp.greves.model.exceptions.roadmap.BadRoadMapException;
import ucp.greves.model.simulation.SimulationInfo;

public class Schedule extends Thread{
	
	private ArrayList<LaunchTrainInformation> informations;
	
	public Schedule(){
		informations = new ArrayList<LaunchTrainInformation>();
	}
	
	public void addInformation(LaunchTrainInformation info){
		informations.add(info);
	}
	
	public void run(){
		try {
			Time ancientTime = Clock.getTime().clone();
			Time newTime = ancientTime;
			while(!SimulationInfo.stopped()){
				for(LaunchTrainInformation info : informations){
					if(info.getTime().isSuperior(ancientTime) && info.getTime().isInferiorOrEquals(newTime)){
						TrainController.launchTrain(info.getRoadMap());
					}
				}
				ancientTime = newTime.clone();
				SimulationInfo.waitFrameTime();
				newTime = Clock.getTime().clone();
			}
		} catch (InterruptedException | BadControlInformationException | BadRoadMapException | RailWayNotExistException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<LaunchTrainInformation> getInformations(){
		return informations;
	}

}
