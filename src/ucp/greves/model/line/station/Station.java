package ucp.greves.model.line.station;

import ucp.greves.model.exceptions.canton.CantonHasAlreadyStationException;
import ucp.greves.model.exceptions.canton.CantonNotExistException;
import ucp.greves.model.line.Line;

public class Station {
	
	private String name;
	private int waitTime;
	
	public Station(int canton, String name, int waitTime) throws CantonHasAlreadyStationException, CantonNotExistException{
		this(name);
		Line.register_station(canton, this);
		this.waitTime = waitTime;
	}
	
	protected Station(int canton, String name) throws CantonHasAlreadyStationException, CantonNotExistException{
		this(canton, name, -1);
	}
	
	protected Station(String name){
		this.name = name;
		this.waitTime = -1;
	}
	
	public String getName(){
		return name;
	}
	
	public int getWaitTime(){
		return waitTime;
	}
	
	public void waitInStation() throws InterruptedException{
		waitInStation(waitTime);
	}
	
	public void waitInStation(int specialTime) throws InterruptedException{
		for(int i = 0; i < specialTime; i++){
			Thread.sleep(specialTime);
		}
	}

}
