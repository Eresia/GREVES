package ucp.greves.data.line.canton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import ucp.greves.data.line.railWay.RailWay;
import ucp.greves.data.line.roadMap.RoadMap;
import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.exceptions.railway.RailWayNotDefinedException;
import ucp.greves.model.line.Line;

public class Terminus extends Canton {
	private ArrayList<Integer> railWayAvailable;
	
	public Terminus(int railWay, int length) {
		this(length);
		setRailWay(railWay);
	}

	public Terminus(int length) {
		super(length);
		this.railWayAvailable = new ArrayList<Integer>();
	}

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
	
	public ArrayList<Integer> getNextRailWays(){
		return railWayAvailable;
	}

	public void AddNextRailWay(RailWay r) {
		if (!this.railWayAvailable.contains(r.getId())) {
			this.railWayAvailable.add(r.getId());
		}
	}

}
