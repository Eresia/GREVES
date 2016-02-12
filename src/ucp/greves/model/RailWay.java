package ucp.greves.model;

import java.util.ArrayList;

public class RailWay {
	private Terminus terminus;
	private int id;
	private ArrayList<Canton> canton_list;

	public RailWay(){
		this.terminus = new Terminus();
		this.id = Registry.register_railway();
		this.canton_list = new ArrayList<Canton>();
	}
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}


	public Terminus getTerminus() {
		return this.terminus;
	}


	public int getLength(){
		return this.getFirstCanton().getStartPoint();
	}


	public void addCanton(int lenght) {
		Canton cn;
		if(this.canton_list.size() == 0){
			 cn  = new Canton(terminus, lenght);
		}else{
			 cn  = new Canton(this.getFirstCanton(), lenght);
		}
		this.canton_list.add(cn);
	}
	
	public Canton getFirstCanton(){
		if(this.canton_list.size() == 0){
			return this.terminus;
		}else{
			return canton_list.get(canton_list.size() -1 );
		}
	}
	public void connectTo(RailWay r){
		this.terminus.AddNextRailWay(r);
		this.terminus.selectNextRailWay(r.id);
	}
	
	
	// public Canton getCantonByPosition(int position)throws TerminusException {
	// //return new Canton(id, length, startPoint);
	// }
}
