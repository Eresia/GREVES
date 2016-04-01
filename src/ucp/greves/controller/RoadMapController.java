package ucp.greves.controller;

import java.util.ArrayList;

import ucp.greves.data.line.roadMap.RoadMap;
import ucp.greves.model.line.Line;

/**
 * Controller of the road maps
 *
 */
public class RoadMapController {

	/**
	 * Return Returns the list of road map names
	 * @return 
	 * 		(ArrayList<String>) the list of road map names
	 */
	public static ArrayList<String> StringlistOfRoadMapNames(){
		return new ArrayList<String>(Line.getRoadMaps().keySet());
	}
	
	/**
	 * Return a road map by its name
	 * @param name
	 * 		(String)The road map name
	 * @return
	 * 		(RoadMap) The road map with the id given
	 */
	public static RoadMap getRoadMapByName(String name){
		return Line.getRoadMaps().get(name);
	}
	
	/**
	 * Return the las station of the road map given
	 * @param roadMap
	 * 		(String) The name of the road map
	 * @return
	 * 		(Integer) The las station of the road map
	 */
	public static int getLastStation(String roadMap){
		return Line.getRoadMaps().get(roadMap).getLastStation();
	}
}
