package ucp.greves.model.line;

import java.util.HashMap;

import ucp.greves.model.exceptions.railway.DoubledRailwayException;

public class Line {

	private HashMap<Integer ,RailWay> railWay;

	public Line() {
		this.railWay = new HashMap<Integer, RailWay>();
	}

	public HashMap<Integer, RailWay> getRailWay() {
		return this.railWay;
	}
	
	public RailWay getRailWay(int way){
		return this.railWay.get(way);
	}


	public int getTotalLenght() {
		int length = 0;
		for(Integer rkey : railWay.keySet()){
			length += railWay.get(rkey).getLength();
		}
		return length;
	}
	
	public void addRailWay( RailWay r ) throws DoubledRailwayException{
		if(this.railWay.containsKey(r.getId())){
			throw new DoubledRailwayException();
		}
		this.railWay.put(r.getId(), r);
		
	}


}
