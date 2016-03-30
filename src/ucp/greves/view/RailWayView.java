/*
 * TODO : See javadoc
 */
package ucp.greves.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Parent;
import ucp.greves.controller.CantonController;
import ucp.greves.controller.RailWayController;
import ucp.greves.data.line.railWay.RailWay;

public class RailWayView extends Parent  {
	
	private HashMap<Integer, CantonView> Cantons;
	IntegerProperty startXpos, startYpos;
	private RailWay railway;
	
	/**
	 * 
	 * @param railway
	 * 	(Railway) The railway that will be print in the pane
	 * @param numberOfRailWays
	 * 	(int) number of railways for know the start position of them
	 * 
	 * @see Railway
	 */
	public RailWayView(RailWay railway, int numberOfRailWays, Boolean global){
		this.Cantons = new HashMap<Integer, CantonView>();
		this.railway = railway;
		startXpos = new SimpleIntegerProperty();
		startYpos = new SimpleIntegerProperty();
		RailWayController controller = new RailWayController();
		CantonController cantonController = new CantonController();
		if(global){
			this.globalOrder();
		}
		else{
			startXpos.setValue(((railway.getId())*150) + 50);
			startYpos.set(10);
		}
		IntegerProperty xpos =new SimpleIntegerProperty();
		IntegerProperty ypos = new SimpleIntegerProperty();
		xpos.bind(startXpos);
		ypos.bind(startYpos);
		ArrayList<Integer> inverseList = new ArrayList<>();
		for(int c : railway.getIdsCantonsStart2End()){
			if(global && ((railway.getId() % 2) == 1)){
				inverseList.add(c);
			}
			else{
				CantonView cv = new CantonView(xpos, ypos,0.0037, cantonController.getCantonById(c), cantonController, global, true);
				this.Cantons.put(c, cv);
				this.getChildren().add(cv);
				xpos = cv.getEndX();
				ypos = cv.getEndY();
			}
		}
		if(global && ((railway.getId() % 2) == 1)){
			Collections.reverse(inverseList);
			for(int c : inverseList){
				CantonView cv = new CantonView(xpos, ypos,0.0037, cantonController.getCantonById(c), cantonController, global, false);
				this.Cantons.put(c, cv);
				this.getChildren().add(cv);
				xpos = cv.getEndX();
				ypos = cv.getEndY();
			}
		}
		
	}
	
	public void globalOrder(){
		switch (railway.getId()) {
		case 0:
			startYpos.setValue(50);
			startXpos.setValue(25);
			break;
			
		case 1:
			startYpos.setValue(100);
			startXpos.setValue(25);
			break;
			
		case 2:
			startYpos.setValue(200);
			startXpos.setValue(425);
			break;
			
		case 3:
			startYpos.setValue(250);
			startXpos.setValue(425);
			break;
			
		case 4:
			startYpos.setValue(125);
			startXpos.setValue(625);
			break;
			
		case 5:
			startYpos.setValue(175);
			startXpos.setValue(625);
			break;
			
		case 6:
			startYpos.setValue(350);
			startXpos.setValue(300);
			break;
			
		case 7:
			startYpos.setValue(400);
			startXpos.setValue(300);
			break;
			
		case 8:
			startYpos.setValue(225);
			startXpos.setValue(1000);
			break;
			
		case 9:
			startYpos.setValue(275);
			startXpos.setValue(1000);
			break;
			
		case 10:
			startYpos.setValue(150);
			startXpos.setValue(1825);
			break;
			
		case 11:
			startYpos.setValue(200);
			startXpos.setValue(1825);
			break;
			
		case 12:
			startYpos.setValue(300);
			startXpos.setValue(1825);
			break;
			
		case 13:
			startYpos.setValue(350);
			startXpos.setValue(1825);
			break;

		default:
			startYpos.setValue(((this.railway.getId()+1)*150));
			startXpos.setValue(0);
			break;
		}
	}


}
