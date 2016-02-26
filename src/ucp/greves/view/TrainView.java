package ucp.greves.view;

import java.util.Observable;
import java.util.Observer;

import ucp.greves.controller.TrainController;
import ucp.greves.model.train.Train;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Parent;
import javafx.scene.shape.Rectangle;

public class TrainView extends Parent implements Observer{
	
	Rectangle rect;
	
	public TrainView(Train tr){
		TrainController trcontroller  = new TrainController();

		this.rect = new Rectangle(10, 10); //Rectnagle qui représente le train
		
		tr.addObserver(this);
		this.getChildren().add(rect);
		
	}
	@Override
	public void update(Observable o, Object arg) {
		// Mis a jour de la position du train
		
	}
	
}
