package ucp.greves.data.line.railWay;

import java.util.ArrayList;

import ucp.greves.data.exceptions.canton.TerminusException;
import ucp.greves.data.exceptions.railway.DoubledRailwayException;
import ucp.greves.data.exceptions.station.StationNotFoundException;
import ucp.greves.data.line.canton.Canton;
import ucp.greves.data.line.canton.Terminus;
import ucp.greves.data.line.station.Station;
import ucp.greves.model.line.Line;

/**
 * RailWay is defined by a list of cantons and a terminus
 *
 */
public class RailWay {
	private Terminus terminus;
	private int id;
	private ArrayList<Canton> canton_list;

	/**
	 * Creates a railway
	 * 
	 * @param id
	 * 		(Integer) The id of the railway
	 * @throws DoubledRailwayException
	 *             if the id is already used
	 */
	public RailWay(int id) throws DoubledRailwayException {
		this.id = id;
		this.canton_list = new ArrayList<Canton>();
		Line.register_railway(this);
		terminus = null;
	}

	/**
	 * @return (Integer) Returns the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return (Terminus) Returns the terminus of the railway
	 */
	public Terminus getTerminus() {
		return this.terminus;
	}

	/**
	 * @return (Integer) The total length of the railway
	 */
	public int getLength() {
		return this.getFirstCanton().getStartPoint();
	}

	/**
	 * Adds a canton at the beginning of the railway
	 * 
	 * @param length
	 * 		(Integer) The length of the canton
	 */
	public void addCanton(int length) {
		if (terminus == null) {
			terminus = new Terminus(getId(), length);
		} else {
			Canton cn;
			if (this.canton_list.size() == 0) {
				cn = new Canton(terminus, getId(), length);
			} else {
				cn = new Canton(this.getFirstCanton(), getId(), length);
			}
			this.canton_list.add(cn);
		}
	}

	/**
	 * @return (Canton) Returns the first canton of the railway
	 */
	public Canton getFirstCanton() {
		if (this.canton_list.size() == 0) {
			return this.terminus;
		} else {
			return canton_list.get(canton_list.size() - 1);
		}
	}
	
	/**
	 * Connects a railway at the end of this on
	 * @param r
	 * 		(RailWay) The railway to connect at the end
	 */
	public void connectTo(RailWay r) {
		this.terminus.AddNextRailWay(r);
	}

	/**
	 * @return (ArrayList<Integer>) Returns a list of the starting point of the cantons of this railway
	 */
	public ArrayList<Integer> getStartsPoints() {
		ArrayList<Integer> temp = new ArrayList<Integer>();

		for (Canton c : canton_list) {
			temp.add(c.getStartPoint());
		}
		return temp;
	}

	/**
	 * @return (ArrayList<Integer>) Returns the list of the cantons' IDs from the beginning of the railway to the end
	 */
	public ArrayList<Integer> getIdsCantonsStart2End() {
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (int i = canton_list.size() - 1; i >= 0; i--) {
			temp.add(canton_list.get(i).getId());
		}
		temp.add(this.terminus.getId());
		return temp;
	}

	/**
	 * Give the name of the RailWay : "FirstStation : LastStation"
	 * 
	 * @return The name of the Rail : "FirstStation : LastStation"
	 */
	public String getName() {
		Canton canton = getFirstCanton();
		String firstStation = null, lastStation = null;
		try {
			while (canton != null) {
				try {
					Station s = canton.getStation();
					if (firstStation == null) {
						firstStation = s.getName();
					}
					lastStation = s.getName();
				} catch (StationNotFoundException e) {

				}
				canton = canton.getNextCanton(null);
			}
		} catch (TerminusException e) {

		}

		return firstStation + " : " + lastStation;
	}
}
