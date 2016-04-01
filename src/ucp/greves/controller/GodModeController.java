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

/**
 * The controller for the God Mode View
 *
 */
public class GodModeController {

	/**
	 * Change the simulation speed
	 * @param duration
	 * 		(Integer) The new simulation speed
	 */
	public static void changeSimulationSpeed(int duration) {
		SimulationInfo.changeSimulationSpeed(duration);
	}
	
	/**
	 * Start the simulation
	 */
	public static void startStimulation(){
		Clock.getInstance().start();
		Line.getInstance().getSchedule().start();
		new Server(8888).start();
	}
	
	/**
	 * Stop the simulation
	 */
	public static void stopSimulation(){
		SimulationInfo.stopSimulation();
		for(Integer i : TrainController.integerListOfRunningTrainsID()){
			TrainController.removeTrain(i);
		}
	}
	
	/**
	 * Return if the simulation is stopped
	 * @return
	 * 		(Boolean) if the simulation is stopped
	 */
	public static boolean simulationStopped(){
		return SimulationInfo.stopped();
	}
	
	/**
	 * Wait a frame
	 * @throws InterruptedException If an error in threads
	 */
	public static void waitFrameTime() throws InterruptedException{
		SimulationInfo.waitFrameTime();
	}
	
	/**
	 * Give the time to the next station for a position given
	 * @param canton
	 * 		(Integer) the actual canton 
	 * @param positionOnRailWay
	 * 		(Integer) the actual position on the railway
	 * @param nextStation
	 * 		(Integer) the next station canton id
	 * @param nextStationRailWay
	 * 		(Integer) the next station railway
	 * @return
	 * @throws CantonNotExistException If the canton not exist
	 * @throws StationNotFoundException If the station not exist
	 * @throws CantonIsBlockedException If a canton is blocked between the position and the next station
	 */
	public static Time timeToNextStation(int canton, int positionOnRailWay, int nextStation, int nextStationRailWay) throws CantonNotExistException, StationNotFoundException, CantonIsBlockedException {
		return Station.timeToNextStation(canton, positionOnRailWay, nextStation, nextStationRailWay);
	}
}
