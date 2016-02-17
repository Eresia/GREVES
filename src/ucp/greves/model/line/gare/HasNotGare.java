package ucp.greves.model.line.gare;

import ucp.greves.model.exceptions.gare.GareNotFoundException;

public class HasNotGare implements GareDecorator{
	
	public HasNotGare() {}

	@Override
	public void waitInGare() throws InterruptedException {}

	@Override
	public boolean hasGare() {
		return false;
	}

	@Override
	public Gare getGare() throws GareNotFoundException {
		throw new GareNotFoundException();
	}

}
