package ucp.greves.model.line.station;

import ucp.greves.model.exceptions.station.StationNotFoundException;

public class HasStation implements StationDecorator{
	
	private Station station;
	
	public HasStation(Station station){
		this.station = station;
	}

	@Override
	public void waitInStation() throws InterruptedException {
		station.waitInStation();
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
