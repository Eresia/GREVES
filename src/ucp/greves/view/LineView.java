/*
 * TODO : JavaDoc
 */
package ucp.greves.view;

import java.util.ArrayList;

import ucp.greves.controller.RailWayController;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Parent;

public class LineView extends Parent{
	
	public ArrayList<RailWayView> RailWays;
	/**
	 * Construct the view line with width and height
	 * @param paneWidth
	 * 	(IntegerProperty) the width of the pane
	 * @param paneHeight
	 * 	(IntegerProperty) The Height of the pane
	 */
	public LineView(IntegerProperty paneWidth, IntegerProperty paneHeight){

		RailWays = new ArrayList<RailWayView>();
		RailWayController Rcontroller = new RailWayController();
		 ArrayList<Integer> RailsWayIds = Rcontroller.IntegerlistOfRailWaysID();
		 int numberOfRailWays = Rcontroller.IntegerlistOfRailWaysID().size();
		 for(int rid : RailsWayIds){
			 RailWayView RWV = new RailWayView(Rcontroller.getRailWayById(rid), paneWidth, paneHeight, numberOfRailWays);
			 this.RailWays.add(RWV);
			 this.getChildren().add(RWV);
		 } 
	}
	
}
