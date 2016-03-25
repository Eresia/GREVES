package ucp.greves.model.line.station;

import ucp.greves.model.exceptions.station.StationNotFoundException;
import ucp.greves.model.train.Train;

public interface StationDecorator {
	
	public void enter(Train train) ;
	public boolean hasStation();
	public Station getStation() throws StationNotFoundException;

}
