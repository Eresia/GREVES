package ucp.greves.data.line.station;

import java.util.ArrayList;

/**
 * Contains a list with all stations with the same name
 * 
 * @author Bastien
 *
 * @see Station
 * @see DepositeryStation
 */
public class GlobalStation {
	
	private String name;
	private ArrayList<Integer> stations;
	
	/**
	 * Creates a GlobalStation and initializes the list
	 * @param name
	 */
	public GlobalStation(String name){
		this.name = name;
		stations = new ArrayList<Integer>();
	}
	
	/**
	 * Adds a station to the list
	 * @param station
	 */
	public void addStation(int station){
		stations.add(station);
	}
	
	/**
	 * @return (ArrayList<Integer>) Returns the list of stations
	 */
	public ArrayList<Integer> getStations(){
		return stations;
	}
	
	/**
	 * @return (String) Returns the name the stations share
	 */
	public String getName(){
		return name;
	}

}
