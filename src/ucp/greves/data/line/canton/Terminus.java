package ucp.greves.data.line.canton;

import java.util.ArrayList;

import ucp.greves.data.exceptions.canton.TerminusException;
import ucp.greves.data.exceptions.railway.RailWayNotDefinedException;
import ucp.greves.data.line.railWay.RailWay;
import ucp.greves.data.line.roadMap.RoadMap;
import ucp.greves.model.line.Line;

/**
 * The Terminus is a {@link Canton} used to "declare" the end of a {@link RailWay}
 *
 * @see Canton
 * @see RailWay
 */
public class Terminus extends Canton {
	private ArrayList<Integer> railWayAvailable;
	
	/**
	 * Creates a terminus
	 * 
	 * @param railWay
	 * 		(Integer) The id of the railway following this terminus
	 * @param length
	 * 		(Integer) The length of this terminus
	 */
	public Terminus(int railWay, int length) {
		this(length);
		setRailWay(railWay);
	}

	/**
	 * Creates a terminus
	 * 
	 * @param length
	 * 		(Integer) The length of this terminus
	 */
	public Terminus(int length) {
		super(length);
		this.railWayAvailable = new ArrayList<Integer>();
	}

	/**
	 * Gets the list of railways following the one this terminus is on
	 * 
	 * @return
	 * 		(ArrayList<Integer>) Returns the list of available railways
	 */
	public ArrayList<Integer> getRailWayAvailable() {

		return this.railWayAvailable;
	}

	@Override
	public int getStartPoint() {
		return length;
	}
	
	@Override
	public int getEndPoint() {
		return 0;
	}

	@Override
	public Canton getNextCanton(RoadMap road) throws TerminusException {
		return getNextRailWay(road).getFirstCanton();
	}
	
	@Override
	public Canton getNextCanton(int rw) throws TerminusException {
		if(railWayAvailable.contains(rw)){
			return Line.getRailWays().get(rw).getFirstCanton();
		}
		else{
			throw new TerminusException();
		}
	}

	/**
	 * Gets the railway following this one
	 * 
	 * @param road
	 * 		(RoadMap) The roadmap the train follows
	 * @return
	 * 		(RailWay) Returns the railway the train will go on next
	 * @throws TerminusException if the next railway is not defined
	 */
	public RailWay getNextRailWay(RoadMap road) throws TerminusException {
		int actualPos;
		ArrayList<Integer> rails;
		
		int railWay;
		try {
			railWay = getRailWay();
		} catch (RailWayNotDefinedException e) {
			throw new TerminusException();
		}
		
		if (this.railWayAvailable.size() == 0 || road == null) {
			throw new TerminusException();
		}
		
		rails = road.getRailwaysIDs();
		actualPos = rails.indexOf(railWay);
		if(actualPos == (rails.size()-1)){
			throw new TerminusException();
		}
		
		int next = rails.get(actualPos+1);
		if(!railWayAvailable.contains(next)){
			throw new TerminusException();
		}
		
		return Line.getRailWays().get(next);
	}
	
	/**
	 * @return
	 * 		(ArrayList<Integer>) Returns the list of the railways following the terminus
	 */
	public ArrayList<Integer> getNextRailWays(){
		return railWayAvailable;
	}

	/**
	 * Adds a railway to the list of railways following the terminus
	 * @param r
	 * 		(RailWay) The railway to add to the list
	 */
	public void AddNextRailWay(RailWay r) {
		if (!this.railWayAvailable.contains(r.getId())) {
			this.railWayAvailable.add(r.getId());
		}
	}

}
