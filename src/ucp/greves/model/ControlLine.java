package ucp.greves.model;

import java.util.ArrayList;
import java.util.HashMap;

import ucp.greves.model.exceptions.BadControlInformationException;
import ucp.greves.model.exceptions.railway.RailWayNotExistException;
import ucp.greves.model.exceptions.roadmap.BadRoadmapException;
import ucp.greves.model.exceptions.roadmap.EmptyRoadMapException;
import ucp.greves.model.exceptions.roadmap.RoadMapNameNotExistException;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.RoadMap;
import ucp.greves.model.train.Train;

public class ControlLine {
	
	private static ControlLine instance = new ControlLine();
	private Line line;
	private HashMap<String, RoadMap> roads;
	private ArrayList<Train> trains;
	
	private ControlLine(){
		roads = new HashMap<String, RoadMap>();
		line = null;
		trains = new ArrayList<Train>();
	}
	
	public static ControlLine getInstance(){
		return instance;
	}
	
	public void launchTrain(String road, int speed) throws BadControlInformationException, BadRoadmapException, RailWayNotExistException{
		verifyInformation();
		if(!roads.containsKey(road)){
			throw new RoadMapNameNotExistException("Impossible to launch the train - the road don't exist");
		}
		ArrayList<Integer> rails = roads.get(road).getRailwaysIDs();
		if(rails.size() == 0){
			throw new EmptyRoadMapException("Impossible to launch the train - the road is empty");
		}
		if(line.getRailWay(rails.get(0)) == null){
			throw new RailWayNotExistException("Impossible to launch the train - the rail way don't exist");
		}
		System.out.println(line.getRailWay(rails.get(0)));
		Train t = new Train(line.getRailWay(rails.get(0)).getFirstCanton(), roads.get(road), speed);
		trains.add(t);
		Thread tThread = new Thread(t);
		tThread.start();
	}
	
	public Line getLine(){
		return line;
	}
	
	public void setLine(Line line){
		this.line = line;
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
	
	public ArrayList<Train> getTrains(){
		return trains;
	}
	
	private void verifyInformation() throws BadControlInformationException{
		if(line == null){
			throw new BadControlInformationException("Line is not set");
		}
		if(roads.size() == 0){
			throw new BadControlInformationException("No road");
		}
	}

}
