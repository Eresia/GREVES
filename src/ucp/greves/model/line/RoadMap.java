package ucp.greves.model.line;

import java.util.ArrayList;

import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.exceptions.railway.DoubledRailwayException;
import ucp.greves.model.exceptions.roadmap.RoadMapAlreadyExistException;
import ucp.greves.model.exceptions.station.StationNotFoundException;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.station.Station;

public class RoadMap {
	private ArrayList<Integer> railwaysIDs;
	private String name;
	private ArrayList<Integer> stations;
	/**
	 * departures is the timetable of departures (in minutes)
	 */
	private ArrayList<Integer> departures;
	
	
	public RoadMap(String name) throws RoadMapAlreadyExistException{
		Line.register_roadMap(name, this);
		this.name = name;
		railwaysIDs = new ArrayList<Integer>();
		stations = new ArrayList<Integer>();
	}

	public ArrayList<Integer> getRailwaysIDs() {
		return railwaysIDs;
	}
	
	public void addRailWay(int id) throws DoubledRailwayException {
		addRailWay(id, null);
	}

	public void addRailWay(int id, ArrayList<String> dontPassStation) throws DoubledRailwayException {
		if(railwaysIDs.contains(id)) {
			throw new DoubledRailwayException();
		}
		railwaysIDs.add(id);
		
		try{
			Canton canton = Line.getRailWays().get(id).getFirstCanton();
			while(canton != null){
				if(canton.hasStation()){
					try {
						Station station = canton.getStation();
						if(dontPassStation != null){
							boolean isInArray = false;
							
							for(String s : dontPassStation){
								if(s.equals(station.getName())){
									isInArray = true;
								}
							}
							if(!isInArray){
								addStation(canton.getId());
							}
						}
					} catch (StationNotFoundException e) {
						e.printStackTrace();
					}
				}
				canton = canton.getNextCanton(null);
			}
			
		} catch(TerminusException e){
			
		}
		
	}
	
	private void addStation(int station){
		stations.add(station);
	}
	
	public boolean cross(int stationCantonId){
		return stations.contains(stationCantonId);
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
	
	public ArrayList<Integer> getStations() {
		return stations;
	}
}
