package ucp.greves.model.line.station;

import ucp.greves.model.exceptions.canton.CantonHasAlreadyStationException;
import ucp.greves.model.exceptions.canton.CantonNotExistException;
import ucp.greves.model.line.Line;

public class Station {
	
	//private int id;
	private String name;
	private int waitTime;
	
	public Station(int canton, String name, int waitTime) throws CantonHasAlreadyStationException, CantonNotExistException{
		Line.register_station(canton, this);
		this.name = name;
		this.waitTime = waitTime;
	}
	
	/*public int getId(){
		return id;
	}*/
	
	public String getName(){
		return name;
	}
	
	public int getWaitTime(){
		return waitTime;
	}

}
