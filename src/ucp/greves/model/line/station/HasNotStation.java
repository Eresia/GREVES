package ucp.greves.model.line.station;

import ucp.greves.data.line.station.Station;
import ucp.greves.data.train.Train;
import ucp.greves.model.exceptions.station.StationNotFoundException;

public class HasNotStation implements StationDecorator{
	
	public HasNotStation() {}

	@Override
	public void enter(Train train) {}

	@Override
	public boolean hasStation() {
		return false;
	}

	@Override
	public Station getStation() throws StationNotFoundException {
		throw new StationNotFoundException();
	}

}
