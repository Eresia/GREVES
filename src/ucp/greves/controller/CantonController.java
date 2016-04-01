package ucp.greves.controller;
import java.util.ArrayList;

import ucp.greves.data.exceptions.canton.CantonNotExistException;
import ucp.greves.data.line.canton.Canton;
import ucp.greves.model.line.Line;

/**
 * Controller for cantons actions
 *
 */
public class CantonController {
	
	/**
	 * Return Returns the list of cantons
	 * @return 
	 * 		(ArrayList<Integer>) the list of cantons
	 */
	public static ArrayList<Integer> integerlistOfCantonID(){
		return new ArrayList<Integer>(Line.getCantons().keySet());
	}

	/**
	 * Return a canton by its id
	 * @param id 
	 * 		(Integer)The canton id
	 * @return
	 * 		(Canton) The canton with the id given
	 */
	public static Canton getCantonById(int id){
		return Line.getCantons().get(id);
	}
	
	/**
	 * Create a slow down in a canton
	 * @param canton
	 * 		(Integer) Id of the canton slow down
	 * @throws CantonNotExistException If the canton not exist
	 */
	public static void createSlowDown(int canton) throws CantonNotExistException{
		if(!Line.getCantons().containsKey(canton)){
			throw new CantonNotExistException("Canton " + canton + " not exist");
		}
		Line.getCantons().get(canton).createSlowDown();
	}
	
	/**
	 * Create a slow down in a canton with a defined speed
	 * @param canton
	 * 		(Integer) Id of the canton slow down
	 * @param newSpeed
	 * 		(Integer) The new speed of the canton
	 * @throws CantonNotExistException If the canton not exist
	 */
	public static void createSlowDown(int canton, int newSpeed) throws CantonNotExistException{
		if(!Line.getCantons().containsKey(canton)){
			throw new CantonNotExistException("Canton " + canton + " not exist");
		}
		Line.getCantons().get(canton).createSlowDown(newSpeed);
	}
	
	/**
	 * Block a canton
	 * @param canton 
	 * 		(Integer) Id of the canton blocked
	 * @throws CantonNotExistException If the canton not exist
	 */
	public static void blockCanton(int canton) throws CantonNotExistException{
		if(!Line.getCantons().containsKey(canton)){
			throw new CantonNotExistException("Canton " + canton + " not exist");
		}
		Line.getCantons().get(canton).blockCanton();
	}
	
	/**
	 * Normalize a canton
	 * @param canton
	 * 		(Integer) Id of the canton normalize
	 * @throws CantonNotExistException If the canton not exist
	 */
	public static void normalizeCanton(int canton) throws CantonNotExistException{
		if(!Line.getCantons().containsKey(canton)){
			throw new CantonNotExistException("Canton " + canton + " not exist");
		}
		Line.getCantons().get(canton).removeProblem();
	}
}
