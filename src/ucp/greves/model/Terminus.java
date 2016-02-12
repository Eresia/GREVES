package ucp.greves.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Terminus extends Canton {
	private HashMap<Integer, RailWay> railWayAvailable;
	private RailWay nextRailWay;

	public Terminus() {
		super(1);
		this.railWayAvailable = new HashMap<Integer, RailWay>();
	}

	public Set<Integer> getRailWayAvailable() {

		return this.railWayAvailable.keySet();
	}

	public void setRailWay(RailWay value) {
		this.nextRailWay = value;
	}

	@Override
	public int getStartPoint() {

		return this.length;
	}

	@Override
	public Canton getNextCanton() throws TerminusException {
		if (this.nextRailWay == null) {
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
