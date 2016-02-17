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
	
	static public void BuildLine() throws DoubledRailwayException{
		RailWay railWay = new RailWay(0);
		RailWay railWay2 = new RailWay(1);
		Random rn = new Random();
		while(railWay.getLength() < 1000){
			addCanton(railWay, rn);
		}
		while(railWay2.getLength() < 1000){
			addCanton(railWay2, rn);
		}
		railWay.connectTo(railWay2);
		railWay2.connectTo(railWay);
		
		Line.register_railway(railWay);
		Line.register_railway(railWay2);
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
