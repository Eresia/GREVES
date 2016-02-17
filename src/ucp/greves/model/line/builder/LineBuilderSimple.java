package ucp.greves.model.line.builder;

import java.util.Random;

import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.exceptions.PropertyNotFoundException;
import ucp.greves.model.exceptions.railway.DoubledRailwayException;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.RailWay;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.station.Station;

public class LineBuilderSimple {
	
	static public Line BuildLine() throws DoubledRailwayException{
		Line ln = new Line();
		RailWay railWay = new RailWay();
		RailWay railWay2 = new RailWay();
		Random rn = new Random();
		while(railWay.getLength() < 1000){
			addCanton(railWay, rn);
		}
		while(railWay2.getLength() < 1000){
			addCanton(railWay2, rn);
		}
		railWay.connectTo(railWay2);
		railWay2.connectTo(railWay);
		
		ln.addRailWay(railWay);
		ln.addRailWay(railWay2);
		
		
		
		
		return ln;
	}
	
	private static void addCanton(RailWay rw, Random rn){
		rw.addCanton(rn.nextInt(100)+20);
		Canton canton = rw.getFirstCanton();
		int hasGare = rn.nextInt(5);
		if(hasGare == 3){
			int waitTime;
			try {
				waitTime = (int) ConfigurationEnvironment.getInstance().getProperty("station_wait_time").getValue();
			} catch (PropertyNotFoundException e) {
				e.printStackTrace();
				waitTime = 500;
			}
			canton.setStation(new Station("TestSTation" + canton.getId(), waitTime), (canton.getLength()/2));
		}
	}
	

}
