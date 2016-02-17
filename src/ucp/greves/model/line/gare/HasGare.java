package ucp.greves.model.line.gare;

import ucp.greves.model.exceptions.gare.GareNotFoundException;

public class HasGare implements GareDecorator{
	
	private Gare gare;
	
	public HasGare(Gare gare){
		this.gare = gare;
	}

	@Override
	public void waitInGare() throws InterruptedException {
		Thread.sleep(gare.getWaitTime());
	}

	@Override
	public boolean hasGare() {
		return true;
	}

	@Override
	public Gare getGare() throws GareNotFoundException {
		return gare;
	}

}
