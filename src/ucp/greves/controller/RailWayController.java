package ucp.greves.controller;

import java.util.ArrayList;

import ucp.greves.model.line.Line;
import ucp.greves.model.line.RailWay;

public class RailWayController {
	
	public static ArrayList<Integer> IntegerlistOfRailWaysID(){
		return new ArrayList<Integer>(Line.getRailWays().keySet());
	}
	
	public static RailWay getRailWayById(int id){
		return Line.getRailWays().get(id);
	}
}
