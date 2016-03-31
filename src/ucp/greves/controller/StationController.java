package ucp.greves.controller;

import java.util.ArrayList;
import java.util.Collections;

import ucp.greves.data.line.station.GlobalStation;
import ucp.greves.data.line.station.NextTrainInformations;
import ucp.greves.data.line.station.Station;
import ucp.greves.model.line.Line;

public class StationController {

	public static ArrayList<Integer> integerlistOfCantonStationsID(){
		return new ArrayList<Integer>(Line.getStations().keySet());
	}
	
	public static Station getStationByCantonId(int id){
		return Line.getStations().get(id);
	}
	
	public static ArrayList<NextTrainInformations> getNextTrainsInStation(int station){
		return Line.getStations().get(station).getNextTrains();
	}
	
	public static ArrayList<NextTrainInformations> getNextTrainsInStation(int station, int nb){
		return Line.getStations().get(station).getNextTrains(nb);
	}
	
	public static ArrayList<String> StringlistOfGlobalStationsName(){
		ArrayList<String> stations = new ArrayList<String>(Line.getGlobalStations().keySet());
		Collections.sort(stations);
		return stations;
	}
	
	public static GlobalStation getGlobalStationByName(String name){
		return Line.getGlobalStations().get(name);
	}
}
