package ucp.greves.controller;

import java.util.ArrayList;

import ucp.greves.model.line.Line;
import ucp.greves.model.line.station.Station;

public class StationController {

	public static ArrayList<Integer> IntegerlistOfStationsID(){
		return new ArrayList<Integer>(Line.getStations().keySet());
	}
	
	public static Station getStationById(int id){
		return Line.getStations().get(id);
	}
}
