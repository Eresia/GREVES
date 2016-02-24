package ucp.greves.view;

import java.util.Observable;
import java.util.Observer;

import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

import ucp.greves.controller.CantonController;
import ucp.greves.model.line.canton.Canton;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class CantonView extends Parent implements Observer {
	
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
		Circle startLine = new Circle();
		startLine.setCenterX(posXA.get());
		startLine.setCenterY(posYA.get());
		startLine.setRadius(3);
		Circle endLine = new Circle();
		endLine.setCenterX(posXB.get());
		endLine.setCenterY(posYB.get());
		endLine.setRadius(3);
		this.getChildren().addAll(startLine , endLine);
		
	this.getChildren().add(lineofCanton);
	}
	@Override
	public void update(Observable o, Object arg) {
		Canton c = (Canton)o;
		if( c.isFree()){
			lineofCanton.setStroke(Color.GREEN);
		}else{
			lineofCanton.setStroke(Color.RED);
		}
			
	}
	public IntegerProperty getEndX(){
		return this.posXB;
	}
	public IntegerProperty getEndY(){
		return this.posYB;
	}
	
}
