package ucp.greves.view;

import java.util.ArrayList;

import ucp.greves.controller.RailWayController;
import javafx.scene.Parent;

public class LineView extends Parent{
	
	public ArrayList<RailWayView> RailWays;
	
	public LineView(){
		RailWays = new ArrayList<RailWayView>();
		RailWayController Rcontroller = new RailWayController();
		 ArrayList<Integer> RailsWayIds = Rcontroller.IntegerlistOfRailWaysID();
		 for(int rid : RailsWayIds){
			 RailWayView RWV = new RailWayView(Rcontroller.getRaiWailById(rid));
			 this.RailWays.add(RWV);
			 this.getChildren().add(RWV);
		 }
	}
	
}
