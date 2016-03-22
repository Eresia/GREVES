package ucp.greves.view;

import java.util.Observable;
import java.util.Observer;

import javax.management.RuntimeErrorException;

import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;
import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

import ucp.greves.controller.CantonController;
import ucp.greves.model.exceptions.canton.CantonIsEmptyException;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.train.Train;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import jdk.nashorn.internal.runtime.regexp.JoniRegExp.Factory;

public class CantonView extends Parent implements Observer {
	private Train innerTrain;
	private double scaleFactor;
	private Circle trainPosition;
	IntegerProperty  posXA, posYA, posXB, posYB;
	Line lineofCanton;
	CantonController controller;
	public CantonView(IntegerProperty posXA ,IntegerProperty posYA, double factor, Canton canton , CantonController controller){
		canton.addObserver(this);
		  this.posXA =new SimpleIntegerProperty(); 
		  this.posXB =new SimpleIntegerProperty(); 
		  this.posYA =new SimpleIntegerProperty(); 
		  this.posYB =new SimpleIntegerProperty(); 
		this.posXA.bindBidirectional(posXA);
		this.posYA.bindBidirectional(posYA);
		this.scaleFactor = factor;		
		posXB.set((int) (posXA.get() ));
		posYB.set((int) (posYA.get() + (factor * canton.getLength())));
		this.controller = controller;
		this.lineofCanton = new Line(this.posXA.get(), this.posYA.get(), this.posXB.get(), this.posYB.get());
		this.lineofCanton.setFill(Color.GREEN);
		this.lineofCanton.setStrokeWidth(3);
		if( canton.isFree()){
			lineofCanton.setStroke(Color.GREEN);
		}else{
			lineofCanton.setStroke(Color.RED);
		}
		
		this.trainPosition = new Circle();
		this.trainPosition.setRadius(3);
//		Circle startLine = new Circle();
//		startLine.setCenterX(posXA.get());
//		startLine.setCenterY(posYA.get());
//		startLine.setRadius(3);
//		Circle endLine = new Circle();
//		endLine.setCenterX(posXB.get());
//		endLine.setCenterY(posYB.get());
//		endLine.setRadius(3);
//		this.getChildren().addAll(startLine , endLine);
		
	this.getChildren().add(lineofCanton);
	}

	
	@Override
	public void update(Observable o, Object arg) {
		if( o instanceof  Canton){
		
		Canton c = (Canton)o;
		if( c.isFree()){
			lineofCanton.setStroke(Color.GREEN);
			if(this.innerTrain != null){
				this.innerTrain.deleteObserver(this);
				this.innerTrain = null;
				Platform.runLater(()->  this.getChildren().remove(trainPosition));
			}
		}else{
			try {
				this.innerTrain = c.getOccupyingTrain();
				this.innerTrain.addObserver(this);
				trainPosition.setCenterX( posXB.get() -  ( this.scaleFactor *   innerTrain.positionInCanton() ));
				this.trainPosition.setCenterY(posYA.get());
				
				Platform.runLater(()->  this.getChildren().add(trainPosition));
				
			} catch (CantonIsEmptyException e) {
				
				
				throw new RuntimeErrorException(new Error("Appel a un train inexistant") );
			}
			
			lineofCanton.setStroke(Color.RED);
		}
		}else if (o instanceof Train) {
			Train t = (Train)o;
			this.trainPosition.setCenterX( posXB.get());
			this.trainPosition.setCenterY(posYA.get() +  ( this.scaleFactor *   t.positionInCanton() ));
			
			

			
		}
			
	}
	public IntegerProperty getEndX(){
		return this.posXB;
	}
	public IntegerProperty getEndY(){
		return this.posYB;
	}
	
}
