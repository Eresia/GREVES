package ucp.greves.model.line.station;

import ucp.greves.data.line.station.Station;
import ucp.greves.data.train.Train;
import ucp.greves.model.exceptions.station.StationNotFoundException;

/**
 * HasStation (following {@link StationDecorator}) if there is a station
 * If not, see {@link HasNotStation}
 * 
 * @see Station
 */
public class HasStation implements StationDecorator{
	
	private Station station;
	
	/**
	 * Creates a station
	 * @param station
	 * 		(Station) The station to associate
	 */
	public HasStation(Station station){
		this.station = station;
	}

	@Override
	public void enter(Train train) {
		station.enter(train);
	}

	@Override
	public boolean hasStation() {
		return true;
	}

	@Override
	public Station getStation() throws StationNotFoundException {
		return station;
	}

}
