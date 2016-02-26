package ucp.greves.model.line;

import java.util.ArrayList;

import ucp.greves.model.exceptions.railway.DoubledRailwayException;
import ucp.greves.model.exceptions.roadmap.StationAlreadyExistInRoadMapException;

public class RoadMap {
	private ArrayList<Integer> railwaysIDs;
	private String name;
	private ArrayList<Integer> stations;
	/**
	 * departures is the timetable of departures (in minutes)
	 */
	private ArrayList<Integer> departures;
	
	
	public RoadMap(String name){
		this.name = name;
		railwaysIDs = new ArrayList<Integer>();
		stations = new ArrayList<Integer>();
	}

	public ArrayList<Integer> getRailwaysIDs() {
		return railwaysIDs;
	}

	public void addRailWay(int id) throws DoubledRailwayException {
		if(railwaysIDs.contains(id)) {
			throw new DoubledRailwayException();
		}
		railwaysIDs.add(id);
	}
	
	public void addStation(int station) throws StationAlreadyExistInRoadMapException{
		if(stations.contains(station)){
			throw new StationAlreadyExistInRoadMapException("Station " + station + "alreday crossed");
		}
		stations.add(station);
	}
	
	public boolean cross(String stationName){
		return stations.contains(stationName);
	}
	
	public Integer getFirstRailWay(){
		return railwaysIDs.get(0);
	}
	
	public Integer getLastRailWay(){
		return railwaysIDs.get(railwaysIDs.size() - 1);
	}
	
	public String getName(){
		return name;
	}
}
