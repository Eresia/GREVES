package ucp.greves.model.line.station;

import ucp.greves.data.line.station.Station;
import ucp.greves.data.train.Train;
import ucp.greves.model.exceptions.station.StationNotFoundException;

public interface StationDecorator {
	
	public void enter(Train train) ;
	public boolean hasStation();
	public Station getStation() throws StationNotFoundException;

}
