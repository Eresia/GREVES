package ucp.greves.controller;

import java.util.ArrayList;
import java.util.HashMap;

import ucp.greves.model.exceptions.BadControlInformationException;
import ucp.greves.model.exceptions.canton.CantonNotExistException;
import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.exceptions.railway.RailWayNotExistException;
import ucp.greves.model.exceptions.roadmap.BadRoadMapException;
import ucp.greves.model.exceptions.roadmap.EmptyRoadMapException;
import ucp.greves.model.exceptions.roadmap.RoadMapNameNotExistException;
import ucp.greves.model.exceptions.train.TrainNotExistException;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.RoadMap;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.station.DepositeryStation;
import ucp.greves.model.line.station.Station;
import ucp.greves.model.schedule.Clock;
import ucp.greves.model.schedule.LaunchTrainInformation;
import ucp.greves.model.schedule.Time;
import ucp.greves.model.simulation.SimulationInfo;
import ucp.greves.model.train.Train;

public class GodModeController {

	private static GodModeController instance = new GodModeController();
	private DepositeryStation stockRemoveTrain;

	private GodModeController() {
		stockRemoveTrain = new DepositeryStation("Removed Train Stockage");
	}

	public static GodModeController getInstance() {
		return instance;
	}

	public void changeSimulationSpeed(int duration) {
		SimulationInfo.changeSimulationSpeed(duration);
	}
	
	public void startStimulation(){
		Clock.getInstance().start();
		Line.getInstance().getSchedule().start();
	}
	
	public void stopSimulation(){
		SimulationInfo.stopSimulation();
		for(Integer i : TrainController.integerListOfRunningTrainsID()){
			TrainController.getRunningTrainById(i).remove(stockRemoveTrain);
		}
	}

	public void launchTrain(String road)	throws BadControlInformationException, BadRoadMapException, RailWayNotExistException {
		verifyInformation();
		HashMap<String, RoadMap> roads = Line.getRoadMaps();
		if (!roads.containsKey(road)) {
			throw new RoadMapNameNotExistException("Impossible to launch the train - the road don't exist");
		}
		ArrayList<Integer> rails = roads.get(road).getRailwaysIDs();
		if (rails.size() == 0) {
			throw new EmptyRoadMapException("Impossible to launch the train - the road is empty");
		}
		if (Line.getRailWays().get(rails.get(0)) == null) {
			throw new RailWayNotExistException("Impossible to launch the train - the rail way don't exist");
		}
		Train t = new Train(Line.getRailWays().get(rails.get(0)).getFirstCanton(), roads.get(road));
		Thread tThread = new Thread(t);
		tThread.start();
	}
	
	public void addLaunch(String roadMap, Time time){
		Line.getInstance().getSchedule().addInformation(new LaunchTrainInformation(time, roadMap));
	}

	public void removeTrain(int train) {
		if(Line.getTrains().containsKey(train)) {
			Line.getTrains().get(train).remove(stockRemoveTrain);
		}
	}
	
	public void createSlowDown(int canton) throws CantonNotExistException{
		if(!Line.getCantons().containsKey(canton)){
			throw new CantonNotExistException("Canton " + canton + " not exist");
		}
		Line.getCantons().get(canton).createSlowDown();
	}
	
	public void createSlowDown(int canton, int newSpeed) throws CantonNotExistException{
		if(!Line.getCantons().containsKey(canton)){
			throw new CantonNotExistException("Canton " + canton + " not exist");
		}
		Line.getCantons().get(canton).createSlowDown(newSpeed);
	}
	
	public void blockCanton(int canton) throws CantonNotExistException{
		if(!Line.getCantons().containsKey(canton)){
			throw new CantonNotExistException("Canton " + canton + " not exist");
		}
		Line.getCantons().get(canton).blockCanton();
	}
	
	public void removeCantonProblem(int canton) throws CantonNotExistException{
		if(!Line.getCantons().containsKey(canton)){
			throw new CantonNotExistException("Canton " + canton + " not exist");
		}
		Line.getCantons().get(canton).removeProblem();
	}
	
	public void blockTrain(int train) throws TrainNotExistException{
		if(!Line.getTrains().containsKey(train)){
			throw new TrainNotExistException("Train " + train + " not exist");
		}
		Line.getTrains().get(train).blockTrain();
	}
	
	public void unblockTrain(int train) throws TrainNotExistException{
		if(!Line.getTrains().containsKey(train)){
			throw new TrainNotExistException("Train " + train + " not exist");
		}
		Line.getTrains().get(train).unblockTrain();
	}

	public void removeRoad(String name) {
		Line.getRoadMaps().remove(name);
	}

	public RoadMap getRoad(String name) throws RoadMapNameNotExistException {
		if(Line.getRoadMaps().containsKey(name)){
			throw new RoadMapNameNotExistException("Road map " + name + " not exist");
		}
		return Line.getRoadMaps().get(name);
	}

	public int findRailWay(int canton) throws RailWayNotExistException {
		int railway = -1;

		for (Integer i : Line.getRailWays().keySet()) {
			Canton c = Line.getRailWays().get(i).getFirstCanton();
			while ((c != null) && (railway == -1)) {
				try {
					if (c.getId() == canton) {
						railway = i;
						break;
					} else {
						c = c.getNextCanton(null);
					}
				} catch (TerminusException e) {
					break;
				}
			}
			if (railway != -1) {
				break;
			}
		}
		if (railway == -1) {
			throw new RailWayNotExistException("Canton " + canton + "is not in a railway");
		}
		return railway;
	}

	public int findRailWay(Station station) throws RailWayNotExistException, CantonNotExistException {
		return findRailWay(findCanton(station));
	}

	public int findCanton(Station station) throws CantonNotExistException {
		int canton = -1;
		if (!Line.getStations().containsValue(station)) {
			throw new CantonNotExistException("Any canton contains the station " + station.getName());
		}
		for (Integer i : Line.getStations().keySet()) {
			if (Line.getStations().get(i) == station) {
				canton = i;
				break;
			}
		}
		return canton;
	}

	private void verifyInformation() throws BadControlInformationException {
		if (Line.getRailWays().size() == 0) {
			throw new BadControlInformationException("No RailWay");
		}
		if (Line.getCantons().size() == 0) {
			throw new BadControlInformationException("No Canton");
		}
		if (Line.getRoadMaps().size() == 0) {
			throw new BadControlInformationException("No road");
		}
	}

}
