package ucp.greves.view;

import java.util.Observable;
import java.util.Observer;

import ucp.greves.controller.TrainController;
import ucp.greves.model.train.Train;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Parent;

public class TrainView extends Parent implements Observer{

	private IntegerProperty StartPosX,StartPosY,EndPosX,EndPosY;
	public TrainView(IntegerProperty StartPosX , IntegerProperty StartPosY , IntegerProperty EndPosX, IntegerProperty EndPosY , Train tr){
		TrainController trcontroller  = new TrainController();
		this.StartPosX.bind(StartPosX);
		this.StartPosY.bind(StartPosY);
		this.EndPosX.bind(EndPosX);
		this.EndPosY.bind(EndPosY);
		
		tr.getPosition();
		
		
	}
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}
	
}
