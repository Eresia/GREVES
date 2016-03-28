package ucp.greves.data.line.roadMap;

import java.util.ArrayList;

import ucp.greves.data.line.canton.Canton;
import ucp.greves.data.line.station.Station;
import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.exceptions.railway.DoubledRailwayException;
import ucp.greves.model.exceptions.roadmap.RoadMapAlreadyExistException;
import ucp.greves.model.exceptions.station.StationNotFoundException;
import ucp.greves.model.line.Line;

/**
 * RoadMap is the list of IDs of the railways and stations the train follows
 * 
 * The IDs of the station are the IDs of their cantons.
 * 
 * @see ucp.greves.data.line.railWay.RailWay
 * @see ucp.greves.data.line.station.Station
 */
public class RoadMap {
	private ArrayList<Integer> railwaysIDs;
	private String name;
	private ArrayList<Integer> stations;	
	
	/**
	 * Creates a roadmap and registers it
	 * @param name
	 * 		(String) The name of the roadmap
	 * @throws RoadMapAlreadyExistException if the roadmap is already registered
	 * 
	 * @see Line#register_roadMap
	 */
	public RoadMap(String name) throws RoadMapAlreadyExistException{
		Line.register_roadMap(name, this);
		this.name = name;
		railwaysIDs = new ArrayList<Integer>();
		stations = new ArrayList<Integer>();
	}

	/**
	 * @return (ArrayList<Integer>) Returns the list of the railways' IDs the roadmap use
	 */
	public ArrayList<Integer> getRailwaysIDs() {
		return railwaysIDs;
	}
	
	/**
	 * Add a railway to the roadmap
	 * Calls {@link RoadMap#addRailWay(int, ArrayList)} with (id, null)
	 * 
	 * @param id 
	 * 		(Integer) The id of the railway to add
	 * @throws DoubledRailwayException if the railway is already used by the roadmap
	 */
	public void addRailWay(int id) throws DoubledRailwayException {
		addRailWay(id, null);
	}

	/**
	 * Adds a railway to be used by the roadmap
	 * 
	 * @param id
	 * 		(Integer) The id of the railway to add
	 * @param dontPassStation
	 * 		(ArrayList<String>) The names of the stations to avoid (not stopping at them)
	 * @throws DoubledRailwayException if the railway is already used by the roadmap
	 */
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
	
	/**
	 * Adds a station to the roadmap
	 * @param station
	 * 		(Integer) The id of the station to add
	 * 
	 * @see Station
	 */
	private void addStation(int station){
		stations.add(station);
	}
	
	/**
	 * Checks if the roadmap stops at the station
	 * 
	 * @param stationCantonId
	 * 		(Integer) The id of the station to check
	 * @return
	 * 		(boolean) Tells if the roadmap stops at the station or not
	 * 
	 * @see Station
	 */
	public boolean cross(int stationCantonId){
		return stations.contains(stationCantonId);
	}
	
	/**
	 * @return (Integer) Returns the id of the first railway, where the train starts
	 */
	public Integer getFirstRailWay(){
		return railwaysIDs.get(0);
	}

	
	/**
	 * @return (Integer) Returns the id of the last railway, where the train stops
	 */
	public Integer getLastRailWay(){
		return railwaysIDs.get(railwaysIDs.size() - 1);
	}
	
	/**
	 * @return (String) Returns the name of the roadmap
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @return (ArrayList<Integer>) Returns the list of stations where the train stops
	 */
	public ArrayList<Integer> getStations() {
		return stations;
	}
}
