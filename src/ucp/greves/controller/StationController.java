package ucp.greves.controller;

import java.util.ArrayList;
import java.util.Collections;

import ucp.greves.data.line.station.GlobalStation;
import ucp.greves.data.line.station.NextTrainInformations;
import ucp.greves.data.line.station.Station;
import ucp.greves.model.line.Line;

/**
 * Controller of the stations
 *
 */
public class StationController {

	/**
	 * Return Returns the list of station canton id
	 * @return 
	 * 		(ArrayList<Integer>) the list of stations canton id
	 */
	public static ArrayList<Integer> integerlistOfCantonStationsID(){
		return new ArrayList<Integer>(Line.getStations().keySet());
	}
	
	/**
	 * Return a station by its canton id
	 * @param id 
	 * 		(Integer)The canton id
	 * @return
	 * 		(Station) The station with the canton id given
	 */
	public static Station getStationByCantonId(int id){
		return Line.getStations().get(id);
	}
	
	/**
	 * Give the next trains which arrived on a station given
	 * @param station
	 * 		(Integer) the station given
	 * @return
	 * 		(ArrayList<NextTrainInformations>) Informations of next trains
	 */
	public static ArrayList<NextTrainInformations> getNextTrainsInStation(int station){
		return Line.getStations().get(station).getNextTrains();
	}
	
	/**
	 * Give a number of the next trains which arrived on a station given
	 * @param station
	 * 		(Integer) The station given
	 * @param nb
	 * 		(Integer) The number of trains wished
	 * @return
	 * 		(ArrayList<NextTrainInformations>) Informations of next trains
	 */
	public static ArrayList<NextTrainInformations> getNextTrainsInStation(int station, int nb){
		return Line.getStations().get(station).getNextTrains(nb);
	}
	
	/**
	 * Return Returns the list of golbal station names
	 * @return 
	 * 		(ArrayList<String>) the list of global stations names
	 */
	public static ArrayList<String> StringlistOfGlobalStationsName(){
		ArrayList<String> stations = new ArrayList<String>(Line.getGlobalStations().keySet());
		Collections.sort(stations);
		return stations;
	}
	
	public static GlobalStation getGlobalStationByName(String name){
		return Line.getGlobalStations().get(name);
	}
}
