package ucp.greves.model.line.canton;

import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.configuration.Registry;
import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.exceptions.gare.GareNotFoundException;
import ucp.greves.model.line.RoadMap;
import ucp.greves.model.line.gare.Gare;
import ucp.greves.model.line.gare.GareDecorator;
import ucp.greves.model.line.gare.HasGare;
import ucp.greves.model.line.gare.HasNotGare;
import ucp.greves.model.train.Train;

public class Canton {

	protected int id;
	protected int length;
	protected Train occupyingTrain = null;

	private Canton nextCanton;

	private GareDecorator gare;
	private int positionGare;

	public Canton(Canton nextCanton, int length) {
		this.id = Registry.register_canton(this);
		this.nextCanton = nextCanton;
		buildCanton(length);
	}

	protected Canton(int length) {
		this.id = Registry.register_canton(this);
		buildCanton(length);
	}
	
	private void buildCanton(int length){
		this.length = length;
		gare = new HasNotGare();
		positionGare = -1;
	}

	public Canton getNextCanton(RoadMap road) throws TerminusException {
		return nextCanton;
	}

	public int getStartPoint() {
		return nextCanton.getStartPoint() + length;
	}

	public int getLength() {
		return length;
	}

	public synchronized void enter(Train train) {
		if (occupyingTrain != null) {
			if (ConfigurationEnvironment.inDebug()) {
				System.out.println(toString() + " occupied !");
			}
			// Train stopped just before canton start point !
			train.setPosition(getStartPoint() - 1);
			try {
				wait();
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
			}
		}

		int trainPosition = train.getPosition();
		if (trainPosition < 0) {
			train.setPosition(trainPosition + getStartPoint());
		}

		if (ConfigurationEnvironment.inDebug()) {
			System.out.println("Canton changed successfully");
		}
		Canton oldCanton = train.getCurrentCanton();
		train.setCurrentCanton(this);
		train.updatePosition();

		oldCanton.exit();
		occupyingTrain = train;

	}

	public synchronized void exit() {
		occupyingTrain = null;
		notify();
		if (ConfigurationEnvironment.inDebug()) {
			System.out.println("Canton freed !");
		}
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
	
	public void enterInGare(){
		try {
			gare.waitInGare();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public int getGarePosition(){
		return positionGare;
	}

	public void setGare(Gare gare, int position) {
		this.gare = new HasGare(gare);
	}

	public boolean hasGare() {
		return gare.hasGare();
	}
	
	public Gare getGare() throws GareNotFoundException{
		return gare.getGare();
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
