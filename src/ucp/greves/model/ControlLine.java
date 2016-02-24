package ucp.greves.model;

import java.util.ArrayList;
import java.util.HashMap;

import ucp.greves.model.exceptions.BadControlInformationException;
import ucp.greves.model.exceptions.railway.RailWayNotExistException;
import ucp.greves.model.exceptions.roadmap.BadRoadMapException;
import ucp.greves.model.exceptions.roadmap.EmptyRoadMapException;
import ucp.greves.model.exceptions.roadmap.RoadMapNameNotExistException;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.RoadMap;
import ucp.greves.model.line.station.DepositeryStation;
import ucp.greves.model.train.Train;

public class ControlLine {
	
	private static ControlLine instance = new ControlLine();
	private HashMap<String, RoadMap> roads;
	private DepositeryStation stockRemoveTrain;
	
	private ControlLine(){
		roads = new HashMap<String, RoadMap>();
		stockRemoveTrain = new DepositeryStation("Removed Train Stockage");
	}
	
	public static ControlLine getInstance(){
		return instance;
	}
	
	public void changeSimulationSpeed(int duration){
		Train.changeSimulationSpeed(duration);
	}
	
	public void launchTrain(String road, int speed) throws BadControlInformationException, BadRoadMapException, RailWayNotExistException{
		verifyInformation();
		if(!roads.containsKey(road)){
			throw new RoadMapNameNotExistException("Impossible to launch the train - the road don't exist");
		}
		ArrayList<Integer> rails = roads.get(road).getRailwaysIDs();
		if(rails.size() == 0){
			throw new EmptyRoadMapException("Impossible to launch the train - the road is empty");
		}
		if(Line.getRailWays().get(rails.get(0)) == null){
			throw new RailWayNotExistException("Impossible to launch the train - the rail way don't exist");
		}
		Train t = new Train(Line.getRailWays().get(rails.get(0)).getFirstCanton(), roads.get(road), speed);
		Thread tThread = new Thread(t);
		tThread.start();
	}
	
	public void removeTrain(int train){
		Line.getTrains().get(train).remove(stockRemoveTrain);
	}
	
	public void addRoad(String name, RoadMap road){
		roads.put(name, road);
	}
	
	public void removeRoad(String name){
		roads.remove(name);
	}
	
	public RoadMap getRoad(String name){
		return roads.get(name);
	}
	
	private void verifyInformation() throws BadControlInformationException{
		if(Line.getRailWays().size() == 0){
			throw new BadControlInformationException("No RailWay");
		}
		if(Line.getCantons().size() == 0){
			throw new BadControlInformationException("No Canton");
		}
		if(roads.size() == 0){
			throw new BadControlInformationException("No road");
		}
	}

}
