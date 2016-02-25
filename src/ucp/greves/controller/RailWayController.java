package ucp.greves.controller;

import java.util.ArrayList;
import java.util.HashMap;

import ucp.greves.model.line.Line;
import ucp.greves.model.line.RailWay;

public class RailWayController {

	private Line model;
	
	public RailWayController(){
		this.model = Line.getInstance();
	}
	public ArrayList<Integer> IntegerlistOfRailWaysID(){
		return new ArrayList<Integer>(Line.getRailWays().keySet());
	}
	public RailWay getRailWaysById(int id){
		return Line.getRailWays().get(id);
	}
}
