package ucp.greves.model;

import java.util.HashSet;
import java.util.Set;

public class Line {

	private int totalLenght;
	private int usedLength = 0;

	private Set<RailWay> railWay;

	public Line(int totalLenght) {
		this.totalLenght = totalLenght;
	}

	public Set<RailWay> getRailWay() {
		if (this.railWay == null) {
			this.railWay = new HashSet<RailWay>();
		}
		return this.railWay;
	}

	public boolean isFull() {
		return usedLength == totalLenght;
	}

	public int getTotalLenght() {
		return totalLenght;
	}

	public int getUsedLength() {
		return usedLength;
	}

}
