package ucp.greves.model.line.station;

import ucp.greves.model.exceptions.station.StationNotFoundException;

public interface StationDecorator {
	
	public void waitInStation() throws InterruptedException;
	public boolean hasStation();
	public Station getStation() throws StationNotFoundException;

}