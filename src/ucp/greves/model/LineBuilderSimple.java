package ucp.greves.model;

import java.util.Random;

public class LineBuilderSimple {
	
	static public Line BuildLine(){
		Line ln = new Line();
		RailWay railWay = new RailWay();
		RailWay railWay2 = new RailWay();
		Random rn = new Random();
		while(railWay.getLength() < 1000){
			railWay.addCanton(rn.nextInt(100)+20);
		}
		while(railWay2.getLength() < 1000){
			railWay2.addCanton(rn.nextInt(100)+20);
		}
		railWay.connectTo(railWay2);
		railWay.connectTo(railWay);
		
		ln.addRailWay(railWay);
		ln.addRailWay(railWay2);
		
		
		
		
		return ln;
	}
	

}
