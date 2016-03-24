package ucp.greves.model.schedule;

import java.util.ArrayList;

import ucp.greves.controller.GodModeController;
import ucp.greves.model.exceptions.BadControlInformationException;
import ucp.greves.model.exceptions.railway.RailWayNotExistException;
import ucp.greves.model.exceptions.roadmap.BadRoadMapException;
import ucp.greves.model.simulation.SimulationSpeed;

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
			while(!Clock.getInstance().stopped()){
				for(LaunchTrainInformation info : informations){
					System.out.println(info.getTime().isInferior(ancientTime, true));
					System.out.println(info.getTime().isSuperiorOrEquals(newTime, true));
					if(info.getTime().isInferior(ancientTime, true) && info.getTime().isSuperiorOrEquals(newTime, true)){
						GodModeController.getInstance().launchTrain(info.getRoadMap());
					}
				}
				ancientTime = newTime.clone();
				SimulationSpeed.waitFrameTime();
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
