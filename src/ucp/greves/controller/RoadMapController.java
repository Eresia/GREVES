package ucp.greves.controller;

import java.util.ArrayList;

import ucp.greves.data.line.roadMap.RoadMap;
import ucp.greves.data.line.station.Station;
import ucp.greves.model.line.Line;

public class RoadMapController {

	public static ArrayList<String> StringlistOfRoadMapNames(){
		return new ArrayList<String>(Line.getRoadMaps().keySet());
	}
	
	public static RoadMap getRoadMapByName(String name){
		return Line.getRoadMaps().get(name);
	}
	
	public static int getLastStation(String roadMap){
		return Line.getRoadMaps().get(roadMap).getLastStation();
	}
}
