package ucp.greves.model;

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
		int lenght = 0;
		for(Integer rkey : railWay.keySet()){
			lenght += railWay.get(rkey).getLenght();
		}
		return lenght;
	}
	
	public void addRailWay( RailWay r ){
		if(! this.railWay.containsKey(r.getId())){
			this.railWay.put(r.getId(), r);
		}
		
	}


}
