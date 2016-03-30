package ucp.greves.controller;

import ucp.greves.data.exceptions.canton.CantonIsBlockedException;
import ucp.greves.data.exceptions.canton.CantonNotExistException;
import ucp.greves.data.exceptions.station.StationNotFoundException;
import ucp.greves.data.line.station.Station;
import ucp.greves.data.time.Time;
import ucp.greves.model.line.Line;
import ucp.greves.model.schedule.Clock;
import ucp.greves.model.simulation.SimulationInfo;
import ucp.greves.network.Server;

public class GodModeController {

	public static void changeSimulationSpeed(int duration) {
		SimulationInfo.changeSimulationSpeed(duration);
	}
	
	public static void startStimulation(){
		Clock.getInstance().start();
		Line.getInstance().getSchedule().start();
		new Server(8888).start();
	}
	
	public static void stopSimulation(){
		SimulationInfo.stopSimulation();
		for(Integer i : TrainController.integerListOfRunningTrainsID()){
			TrainController.removeTrain(i);
		}
	}
	
	public static boolean simulationStopped(){
		return SimulationInfo.stopped();
	}
	
	public static void waitFrameTime() throws InterruptedException{
		SimulationInfo.waitFrameTime();
	}
	
	public static Time timeToNextStation(int canton, int positionOnRailWay, int nextStation, int nextStationRailWay) throws CantonNotExistException, StationNotFoundException, CantonIsBlockedException {
		return Station.timeToNextStation(canton, positionOnRailWay, nextStation, nextStationRailWay);
	}
}
