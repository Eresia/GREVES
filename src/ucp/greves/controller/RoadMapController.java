package ucp.greves.controller;

import java.util.ArrayList;

import ucp.greves.model.line.Line;
import ucp.greves.model.line.RoadMap;

public class RoadMapController {

	public static ArrayList<String> StringlistOfRoadMapNames(){
		return new ArrayList<String>(Line.getRoadMaps().keySet());
	}
	
	public static RoadMap getRoadMapByName(String name){
		return Line.getRoadMaps().get(name);
	}
}
