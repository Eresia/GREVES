package ucp.greves.model.line.gare;

import ucp.greves.model.exceptions.gare.GareNotFoundException;

public interface GareDecorator {
	
	public void waitInGare() throws InterruptedException;
	public boolean hasGare();
	public Gare getGare() throws GareNotFoundException;

}
