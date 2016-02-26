package ucp.greves.model.line;

import java.util.ArrayList;

import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.canton.Terminus;

public class RailWay {
	private Terminus terminus;
	private int id;
	private ArrayList<Canton> canton_list;

	public RailWay(int id){
		//this.terminus = new Terminus(1);
		//this.id = Registry.register_railway();
		this.id = id;
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
		if(terminus == null){
			terminus = new Terminus(getId(), lenght);
		}
		else{
			Canton cn;
			if(this.canton_list.size() == 0){
				 cn  = new Canton(terminus, getId(), lenght);
			}else{
				 cn  = new Canton(this.getFirstCanton(), getId(), lenght);
			}
			this.canton_list.add(cn);
		}
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
	public ArrayList<Integer> getStartsPoints(){
		ArrayList<Integer>	temp= new ArrayList<Integer>();

		for(Canton c : canton_list){
			temp.add(c.getStartPoint());
		}
		return temp;
	}
	public ArrayList<Integer> getIdsCantonsStart2End(){
		ArrayList<Integer>  temp = new ArrayList<Integer>();
		for(int i = canton_list.size() - 1 ; i >=0 ; i--){
			temp.add(canton_list.get(i).getId());
		}
		temp.add(this.terminus.getId());
		return temp;
	}
	
	
	// public Canton getCantonByPosition(int position)throws TerminusException {
	// //return new Canton(id, length, startPoint);
	// }
}
