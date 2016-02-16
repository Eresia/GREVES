package ucp.greves.model.line.canton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import ucp.greves.model.ControlLine;
import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.line.RailWay;
import ucp.greves.model.line.RoadMap;

public class Terminus extends Canton {
	private HashMap<Integer, RailWay> railWayAvailable;
	private RailWay nextRailWay;

	public Terminus(int length) {
		super(length);
		this.railWayAvailable = new HashMap<Integer, RailWay>();
		nextRailWay = null;
	}

	public Set<Integer> getRailWayAvailable() {

		return this.railWayAvailable.keySet();
	}

	public void setRailWay(RailWay value) {
		this.nextRailWay = value;
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
		if (this.nextRailWay == null || ControlLine.getInstance().getLine().getRailWay(road.getLastRailWay()).getTerminus() == this) {
			throw new TerminusException();
		} else {
			return this.nextRailWay.getFirstCanton();
		}
	}

	public RailWay getNextRailWay() throws TerminusException {
		if (this.nextRailWay == null) {
			throw new TerminusException();
		}
		return this.nextRailWay;
	}

	public void AddNextRailWay(RailWay r) {
		if (!this.railWayAvailable.containsKey(r.getId())) {
			this.railWayAvailable.put(r.getId(), r);
		}
	}

	public void selectNextRailWay(Integer rId) {
		if (this.railWayAvailable.containsKey(rId)) {
			this.nextRailWay = this.railWayAvailable.get(rId);
		}
	}

}
