package ucp.greves.controller;

import java.util.ArrayList;
import java.util.HashMap;

import ucp.greves.model.line.Line;
import ucp.greves.model.line.station.Station;
import ucp.greves.model.schedule.Time;

public class StationController {

	public static ArrayList<Integer> IntegerlistOfStationsID(){
		return new ArrayList<Integer>(Line.getStations().keySet());
	}
	
	public static Station getStationById(int id){
		return Line.getStations().get(id);
	}
	
	public static HashMap<Integer, Time> getNextTrains(int station){
		return Line.getStations().get(station).getNextTrains();
	}
}
