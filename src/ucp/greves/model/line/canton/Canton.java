package ucp.greves.model.line.canton;

import ucp.greves.model.configuration.Registry;
import ucp.greves.model.exceptions.TerminusException;
import ucp.greves.model.line.RoadMap;
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
		this.id = Registry.register_canton(this);
		this.length = length;
	}
	
	public Canton getNextCanton(RoadMap road) throws TerminusException{
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
			train.setPosition(getStartPoint() - 1);
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
		}
		
		int trainPosition = train.getPosition();
		if(trainPosition < 0){
			train.setPosition(trainPosition + getStartPoint());
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Canton other = (Canton) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	

}
