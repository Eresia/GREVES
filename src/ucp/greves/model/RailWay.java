package ucp.greves.model;

public class RailWay {
	private Terminus terminus;
	private Terminus nextTerminus;

	public Terminus getTerminus() {
		return this.terminus;
	}

	public void setTerminus(Terminus value) {
		this.terminus = value;
	}

	public void setNextTerminus(Terminus value) {
		this.nextTerminus = value;
	}

	public Terminus getNextTerminus() {
		return this.nextTerminus;
	}

	public void addCanton(int id, int cantonLength) {
	
	}

	// public Canton getCantonByPosition(int position)throws TerminusException {
	// //return new Canton(id, length, startPoint);
	// }
}
