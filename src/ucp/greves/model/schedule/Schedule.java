package ucp.greves.model.schedule;

import java.util.ArrayList;

import ucp.greves.controller.TrainController;
import ucp.greves.data.time.Time;
import ucp.greves.model.exceptions.BadControlInformationException;
import ucp.greves.model.exceptions.railway.RailWayNotExistException;
import ucp.greves.model.exceptions.roadmap.BadRoadMapException;
import ucp.greves.model.simulation.SimulationInfo;

/**
 * This class is the schedule used to launch trains
 * 
 * @see LaunchTrainInformation
 */
public class Schedule extends Thread{
	
	private ArrayList<LaunchTrainInformation> informations;
	
	/**
	 * Creates a schedule of departures
	 */
	public Schedule(){
		informations = new ArrayList<LaunchTrainInformation>();
	}
	
	/**
	 * Adds a departure plan to the schedule
	 * @param info
	 * 		(LaunchTrainInformation) The info needed to launch a new train
	 */
	public void addInformation(LaunchTrainInformation info){
		informations.add(info);
	}
	
	@Override
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
	
	/**
	 * @return (ArrayList<LaunchTrainInformation>) Returns the list of departures informations
	 */
	public ArrayList<LaunchTrainInformation> getInformations(){
		return informations;
	}

}
