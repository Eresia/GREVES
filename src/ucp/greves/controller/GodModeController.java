package ucp.greves.controller;

import ucp.greves.model.line.Line;
import ucp.greves.model.schedule.Clock;
import ucp.greves.model.simulation.SimulationInfo;

public class GodModeController {

	public static void changeSimulationSpeed(int duration) {
		SimulationInfo.changeSimulationSpeed(duration);
	}
	
	public static void startStimulation(){
		Clock.getInstance().start();
		Line.getInstance().getSchedule().start();
	}
	
	public static void stopSimulation(){
		SimulationInfo.stopSimulation();
		for(Integer i : TrainController.integerListOfRunningTrainsID()){
			TrainController.removeTrain(i);
		}
	}
}
