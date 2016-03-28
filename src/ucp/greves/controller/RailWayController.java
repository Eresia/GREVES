package ucp.greves.controller;

import java.util.ArrayList;

import ucp.greves.data.line.railWay.RailWay;
import ucp.greves.model.line.Line;

public class RailWayController {
	
	public static ArrayList<Integer> integerlistOfRailWaysID(){
		return new ArrayList<Integer>(Line.getRailWays().keySet());
	}
	
	public static RailWay getRailWayById(int id){
		return Line.getRailWays().get(id);
	}
}
