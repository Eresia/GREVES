package ucp.greves.controller;

import java.util.ArrayList;

import ucp.greves.data.line.railWay.RailWay;
import ucp.greves.model.line.Line;

/**
 * The controller of the railway
 */
public class RailWayController {
	
	/**
	 * Return Returns the list of railways
	 * @return 
	 * 		(ArrayList<Integer>) the list of railways
	 */
	public static ArrayList<Integer> integerlistOfRailWaysID(){
		return new ArrayList<Integer>(Line.getRailWays().keySet());
	}
	
	/**
	 * Return a railway by its id
	 * @param id 
	 * 		(Integer)The railway id
	 * @return
	 * 		(Railway) The railway with the id given
	 */
	public static RailWay getRailWayById(int id){
		return Line.getRailWays().get(id);
	}
}
