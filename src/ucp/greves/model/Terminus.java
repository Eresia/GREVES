package ucp.greves.model;

import java.util.HashSet;
import java.util.Set;

public class Terminus extends Canton {
	private Set<RailWay> railWayAvailable;
	private RailWay railWay;

	public Terminus(int id, int length, int startPoint) {
		super(id, length, startPoint);
		// TODO Auto-generated constructor stub
	}

	public Set<RailWay> getRailWayAvailable() {
		if (this.railWayAvailable == null) {
			this.railWayAvailable = new HashSet<RailWay>();
		}
		return this.railWayAvailable;
	}

	public void setRailWay(RailWay value) {
		this.railWay = value;
	}

	public RailWay getRailWay() {
		return this.railWay;
	}

}
