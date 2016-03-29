package ucp.greves.model.line.station;

import ucp.greves.data.exceptions.station.StationNotFoundException;
import ucp.greves.data.line.station.Station;
import ucp.greves.data.train.Train;

/**
 * Decorator pattern to know if there is a station or not
 *
 */
public interface StationDecorator {
	
	/**
	 * Makes the train enter the station
	 * @param train
	 * 		(Train) The train to enter the station
	 */
	public void enter(Train train) ;
	
	/**
	 * @return (boolean) Checks if there is a station or not
	 */
	public boolean hasStation();
	
	/**
	 * @return (Station) The station
	 * @throws StationNotFoundException if the station doesn't exist
	 */
	public Station getStation() throws StationNotFoundException;

}
