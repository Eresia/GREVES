/*
 * TODO : JavaDoc
 */
package ucp.greves.view;

import java.util.ArrayList;

import javafx.scene.Parent;
import javafx.scene.shape.Circle;
import ucp.greves.controller.RailWayController;

public class LineView extends Parent{
	
	public ArrayList<RailWayView> RailWays;
	/**
	 * Construct the view line with width and height
	 * 
	 */
	public LineView(Boolean global){
		
		Circle circle = new Circle();
		circle.setRadius(1);
		circle.setCenterX(0);
		circle.setCenterY(0);
		this.getChildren().add(circle);

		RailWays = new ArrayList<RailWayView>();
		RailWayController Rcontroller = new RailWayController();
		 ArrayList<Integer> RailsWayIds = Rcontroller.integerlistOfRailWaysID();
		 int numberOfRailWays = Rcontroller.integerlistOfRailWaysID().size();
		 for(int rid : RailsWayIds){
			 RailWayView RWV = new RailWayView(Rcontroller.getRailWayById(rid), numberOfRailWays, global);
			 this.RailWays.add(RWV);
			 this.getChildren().add(RWV);
		 } 
	}

	
}
