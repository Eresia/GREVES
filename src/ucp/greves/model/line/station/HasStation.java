package ucp.greves.model.line.station;

import ucp.greves.model.exceptions.station.StationNotFoundException;
import ucp.greves.model.train.Train;

public class HasStation implements StationDecorator{
	
	private Station station;
	
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
