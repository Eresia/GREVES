package ucp.greves.model;

import java.util.ArrayList;

public class RoadMap {
	private ArrayList<Integer> railwaysIDs;
	private String name;
	/**
	 * departures is the timetable of departures (in minutes)
	 */
	private ArrayList<Integer> departures;

	public ArrayList<Integer> getRailwaysIDs() {
		return railwaysIDs;
	}

	public void addRailWay(int id) {
		railwaysIDs.add(id);
	}
}
