package ucp.greves.model.line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Line {

	private HashMap<Integer ,RailWay> railWay;

	public Line() {
		this.railWay = new HashMap<Integer, RailWay>();
	}

	public HashMap<Integer, RailWay> getRailWay() {

		return this.railWay;
	}


	public int getTotalLenght() {
		int length = 0;
		for(Integer rkey : railWay.keySet()){
			length += railWay.get(rkey).getLength();
		}
		return length;
	}
	
	public void addRailWay( RailWay r ){
		if(! this.railWay.containsKey(r.getId())){
			this.railWay.put(r.getId(), r);
		}
		
	}


}
