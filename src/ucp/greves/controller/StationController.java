package ucp.greves.controller;

import java.util.ArrayList;
import java.util.HashMap;

import ucp.greves.data.line.station.GlobalStation;
import ucp.greves.data.line.station.Station;
import ucp.greves.data.time.TimeDecorator;
import ucp.greves.model.line.Line;

public class StationController {

	public static ArrayList<Integer> IntegerlistOfStationsID(){
		return new ArrayList<Integer>(Line.getStations().keySet());
	}
	
	public static Station getStationById(int id){
		return Line.getStations().get(id);
	}
	
	public static HashMap<Integer, TimeDecorator> getNextTrainsInStation(int station){
		return Line.getStations().get(station).getNextTrains();
	}
	
	public static ArrayList<String> StringlistOfGlobalStationsName(){
		return new ArrayList<String>(Line.getGlobalStations().keySet());
	}
	
	public static GlobalStation getGlobalStationByName(String name){
		return Line.getGlobalStations().get(name);
	}
}
