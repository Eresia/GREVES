package ucp.greves.model.line.station;

import java.util.ArrayList;

/**
 * Contains all stations with the same name 
 * TODO: Complete doc
 * @author Bastien
 *
 */
public class GlobalStation {
	
	public String name;
	public ArrayList<Integer> stations;
	
	public GlobalStation(String name){
		this.name = name;
		stations = new ArrayList<Integer>();
	}
	
	public void addStation(int station){
		stations.add(station);
	}
	
	public ArrayList<Integer> getStations(){
		return stations;
	}
	
	public String getName(){
		return name;
	}

}
