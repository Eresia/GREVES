package ucp.greves.controller;

import ucp.greves.data.exceptions.canton.CantonIsBlockedException;
import ucp.greves.data.exceptions.canton.CantonNotExistException;
import ucp.greves.data.exceptions.canton.TerminusException;
import ucp.greves.data.exceptions.station.StationNotFoundException;
import ucp.greves.data.line.canton.Canton;
import ucp.greves.data.line.station.Station;
import ucp.greves.data.time.Time;
import ucp.greves.model.line.Line;
import ucp.greves.model.schedule.Clock;
import ucp.greves.model.simulation.SimulationInfo;
import ucp.greves.model.train.ModifiedTrainInformation;
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
	
	public static void waitFrameTime() throws InterruptedException{
		SimulationInfo.waitFrameTime();
	}
	
	public static Time timeToNextStation(int canton, int positionOnRailWay, int nextStation, int nextStationRailWay) throws CantonNotExistException, StationNotFoundException, CantonIsBlockedException{
		Time result = null;
		Canton c = Line.getCantons().get(canton);
		if(c == null){
			throw new CantonNotExistException();
		}
		
		Station s = Line.getStations().get(nextStation);
		if(s == null){
			throw new StationNotFoundException();
		}
		
		int nbFrame = 0;
		int position = positionOnRailWay;
		
		try {
		
			while(true){
				if (position - c.getTrainSpeed(position) <= c.getEndPoint()) {
					c = c.getNextCanton(nextStationRailWay);
					position = c.simulateEnter(position);
				} 
				
				boolean crossStation = false;
				if(c.hasStation()){
					if(c.getStation().getCanton() == nextStation){
						crossStation = true;
					}
				}
				ModifiedTrainInformation info = c.updatedTrainPosition(position, crossStation);
				nbFrame++;
				
				if(info.getStationCrossed()){
					break;
				}
				position -= info.getUpdatedPosition();
			}
		} catch (TerminusException e) {
			return new Time();
		}
		
		result = new Time(0, 0, Clock.nbSecondByFrame());
		result.multTime(nbFrame);
		return result;
	}
}
