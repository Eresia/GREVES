package ucp.greves.view;

import java.util.ArrayList;

import ucp.greves.controller.GodModeController;
import ucp.greves.controller.RailWayController;
import ucp.greves.model.train.Train;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Parent;

public class LineView extends Parent{
	
	public ArrayList<RailWayView> RailWays;
	
	public LineView(IntegerProperty paneWidth, IntegerProperty paneHeight){

		RailWays = new ArrayList<RailWayView>();
		RailWayController Rcontroller = new RailWayController();
		 ArrayList<Integer> RailsWayIds = Rcontroller.IntegerlistOfRailWaysID();
		 int numberOfRailWays = Rcontroller.IntegerlistOfRailWaysID().size();
		 for(int rid : RailsWayIds){
			 RailWayView RWV = new RailWayView(Rcontroller.getRailWaysById(rid), paneWidth, paneHeight, numberOfRailWays);
			 this.RailWays.add(RWV);
			 this.getChildren().add(RWV);
		 } 
	}
	
}
