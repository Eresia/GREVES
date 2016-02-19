package ucp.greves.view;

import java.util.HashMap;

import javafx.beans.property.IntegerProperty;
import ucp.greves.controller.CantonController;
import ucp.greves.controller.RailWayController;
import ucp.greves.model.line.RailWay;

public class RailWayView {
	
	private HashMap<Integer, CantonView> Cantons;
	IntegerProperty startXpos, startYpos;
	public RailWayView(RailWay railway){
		startXpos = new IntegerProperty(1);
		RailWayController controller = new RailWayController();
		CantonController cantonController = new CantonController();
		startXpos.set(10);
		startYpos.set(10);
		IntegerProperty xpos ,ypos;
		xpos.bind(startXpos);
		ypos.bind(startYpos);
		for(int c : railway.getIdsCantonsStart2End()){
			
			CantonView cv = new CantonView(xpos, ypos,0.1, cantonController.getCantonById(c), cantonController);
			this.Cantons.put(c, cv);
		}
	}

}
