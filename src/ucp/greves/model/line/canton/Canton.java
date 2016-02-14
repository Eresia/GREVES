package ucp.greves.model.line.canton;

import ucp.greves.model.configuration.Registry;
import ucp.greves.model.exceptions.TerminusException;
import ucp.greves.model.train.Train;

public class Canton {

	protected int id;
	protected int length;
	protected Train occupyingTrain = null;
	
	private Canton nextCanton;
	
	
	
	public Canton(Canton nextCanton , int length) {
		this.id = Registry.register_canton(this);
		this.length = length;
		this.nextCanton = nextCanton;
	}
	
	protected Canton(int length){
		
	}
	
	public Canton getNextCanton() throws TerminusException{
		return nextCanton;
	}
	public int getStartPoint() {
		return nextCanton.getStartPoint() + length ;
	}

	public int getLength() {
		return length;
	}

	public synchronized void enter(Train train) {
		if (occupyingTrain != null) {
			System.out.println(toString() + " occupied !");
			// Train stopped just before canton start point !
			train.setPosition(getStartPoint() + 1);
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
		}

		System.out.println("Canton changed successfully");
		Canton oldCanton = train.getCurrentCanton();
		train.setCurrentCanton(this);
		train.updatePosition();

		oldCanton.exit();
		occupyingTrain = train;

	}

	public synchronized void exit() {
		occupyingTrain = null;
		notify();
		System.out.println("Canton freed !");
	}

	public boolean isFree() {
		return occupyingTrain == null;
	}

	@Override
	public String toString() {
		return "Canton [id=" + id + "]";
	}

	public int getId() {
		return id;
	}
	public int getEndPoint() {
		return nextCanton.getStartPoint() + 1;

	}

}
