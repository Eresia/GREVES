package ucp.greves.view;

import java.util.HashMap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Parent;
import ucp.greves.controller.CantonController;
import ucp.greves.controller.RailWayController;
import ucp.greves.model.line.RailWay;

public class RailWayView extends Parent  {
	
	private HashMap<Integer, CantonView> Cantons;
	IntegerProperty startXpos, startYpos;
	public RailWayView(RailWay railway){
		this.Cantons = new HashMap<Integer, CantonView>();
		startXpos = new SimpleIntegerProperty();
		startYpos = new SimpleIntegerProperty();
		RailWayController controller = new RailWayController();
		CantonController cantonController = new CantonController();
		startXpos.set(10 + railway.getId() * 50);
		startYpos.set(10 );
		IntegerProperty xpos =new SimpleIntegerProperty();
		IntegerProperty ypos = new SimpleIntegerProperty();
		xpos.bind(startXpos);
		ypos.bind(startYpos);
		for(int c : railway.getIdsCantonsStart2End()){
			CantonView cv = new CantonView(xpos, ypos,0.4, cantonController.getCantonById(c), cantonController);
			this.Cantons.put(c, cv);
			this.getChildren().add(cv);
			 xpos = cv.getEndX();
			 ypos = cv.getEndY();
		}
	}

}
