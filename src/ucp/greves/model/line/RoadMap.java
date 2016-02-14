package ucp.greves.model.line;

import java.util.ArrayList;

import ucp.greves.model.exceptions.railway.DoubledRailwayException;

public class RoadMap {
	private ArrayList<Integer> railwaysIDs;
	private String name;
	/**
	 * departures is the timetable of departures (in minutes)
	 */
	private ArrayList<Integer> departures;
	
	
	public RoadMap(String name){
		this.name = name;
		railwaysIDs = new ArrayList<Integer>();
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
