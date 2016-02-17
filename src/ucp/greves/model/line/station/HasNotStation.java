package ucp.greves.model.line.station;

import ucp.greves.model.exceptions.station.StationNotFoundException;

public class HasNotStation implements StationDecorator{
	
	public HasNotStation() {}

	@Override
	public void waitInStation() throws InterruptedException {}

	@Override
	public boolean hasStation() {
		return false;
	}

	@Override
	public Station getStation() throws StationNotFoundException {
		throw new StationNotFoundException();
	}

}
