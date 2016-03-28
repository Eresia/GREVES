package ucp.greves.controller;
import java.util.ArrayList;

import ucp.greves.data.line.canton.Canton;
import ucp.greves.model.exceptions.canton.CantonNotExistException;
import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.exceptions.railway.RailWayNotExistException;
import ucp.greves.model.line.Line;
public class CantonController {
	
	public static ArrayList<Integer> integerlistOfCantonID(){
		return new ArrayList<Integer>(Line.getCantons().keySet());
	}

	public static Canton getCantonById(int id){
		return Line.getCantons().get(id);
	}
	
	public static int findRailWay(int canton) throws RailWayNotExistException {
		int railway = -1;

		for (Integer i : Line.getRailWays().keySet()) {
			Canton c = Line.getRailWays().get(i).getFirstCanton();
			while ((c != null) && (railway == -1)) {
				try {
					if (c.getId() == canton) {
						railway = i;
						break;
					} else {
						c = c.getNextCanton(null);
					}
				} catch (TerminusException e) {
					break;
				}
			}
			if (railway != -1) {
				break;
			}
		}
		if (railway == -1) {
			throw new RailWayNotExistException("Canton " + canton + "is not in a railway");
		}
		return railway;
	}
	
	public static void createSlowDown(int canton) throws CantonNotExistException{
		if(!Line.getCantons().containsKey(canton)){
			throw new CantonNotExistException("Canton " + canton + " not exist");
		}
		Line.getCantons().get(canton).createSlowDown();
	}
	
	public static void createSlowDown(int canton, int newSpeed) throws CantonNotExistException{
		if(!Line.getCantons().containsKey(canton)){
			throw new CantonNotExistException("Canton " + canton + " not exist");
		}
		Line.getCantons().get(canton).createSlowDown(newSpeed);
	}
	
	public static void blockCanton(int canton) throws CantonNotExistException{
		if(!Line.getCantons().containsKey(canton)){
			throw new CantonNotExistException("Canton " + canton + " not exist");
		}
		Line.getCantons().get(canton).blockCanton();
	}
	
	public static void removeCantonProblem(int canton) throws CantonNotExistException{
		if(!Line.getCantons().containsKey(canton)){
			throw new CantonNotExistException("Canton " + canton + " not exist");
		}
		Line.getCantons().get(canton).removeProblem();
	}
}
