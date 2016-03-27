package ucp.greves.model.line;

import java.util.ArrayList;

import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.exceptions.railway.DoubledRailwayException;
import ucp.greves.model.exceptions.station.StationNotFoundException;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.canton.Terminus;
import ucp.greves.model.line.station.Station;

public class RailWay {
	private Terminus terminus;
	private int id;
	private ArrayList<Canton> canton_list;

	public RailWay(int id) throws DoubledRailwayException{
		this.id = id;
		this.canton_list = new ArrayList<Canton>();
		Line.register_railway(this);
		terminus = null;
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
	
	/**
	 * Give the name of the RailWay : "FirstStation : LastStation"
	 * @return The name of the Rail : "FirstStation : LastStation"
	 */
	public String getName(){
		Canton canton = getFirstCanton();
		String firstStation = null, lastStation = null;
		try{
			while(canton != null){
				try{
					Station s = canton.getStation();
					if(firstStation == null){
						firstStation = s.getName();
					}
					lastStation = s.getName();
				} catch(StationNotFoundException e){
					
				}
				canton = canton.getNextCanton(null);
			}
		} catch(TerminusException e){
			
		}
		
		return firstStation + " : " + lastStation;
	}
}
