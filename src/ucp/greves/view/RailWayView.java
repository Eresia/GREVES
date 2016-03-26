/*
 * TODO : See javadoc
 */
package ucp.greves.view;

import java.util.HashMap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Parent;
import ucp.greves.controller.CantonController;
import ucp.greves.controller.RailWayController;
import ucp.greves.model.line.RailWay;
import ucp.greves.model.line.canton.Canton;

public class RailWayView extends Parent  {
	
	private HashMap<Integer, CantonView> Cantons;
	IntegerProperty startXpos, startYpos, test;
	
	/**
	 * 
	 * @param railway
	 * 	(Railway) The railway that will be print in the pane
	 * @param numberOfRailWays
	 * 	(int) number of railways for know the start position of them
	 * 
	 * @see Railway
	 */
	public RailWayView(RailWay railway, int numberOfRailWays){
		this.Cantons = new HashMap<Integer, CantonView>();
		startXpos = new SimpleIntegerProperty();
		startYpos = new SimpleIntegerProperty();
		test = new SimpleIntegerProperty();
		RailWayController controller = new RailWayController();
		CantonController cantonController = new CantonController();
		startXpos.setValue(((railway.getId()+1)*150));
		startYpos.set(10);
		IntegerProperty xpos =new SimpleIntegerProperty();
		IntegerProperty ypos = new SimpleIntegerProperty();
		xpos.bind(startXpos);
		ypos.bind(startYpos);
		for(int c : railway.getIdsCantonsStart2End()){
			CantonView cv = new CantonView(xpos, ypos,0.02, cantonController.getCantonById(c), cantonController);
			this.Cantons.put(c, cv);
			this.getChildren().add(cv);
			 xpos = cv.getEndX();
			 ypos = cv.getEndY();
		}
		
	}

}
