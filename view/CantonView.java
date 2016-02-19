package ucp.greves.view;

import java.util.Observable;
import java.util.Observer;

import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;

import ucp.greves.controller.CantonController;
import ucp.greves.model.line.canton.Canton;
import javafx.beans.property.IntegerProperty;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class CantonView extends Parent implements Observer {
	
	IntegerProperty  posXA, posYA, posXB, posYB;
	Line lineofCanton;
	CantonController controller;
	public CantonView(IntegerProperty posXA ,IntegerProperty posYA, double factor, Canton canton , CantonController controller){
		canton.addObserver(this);
		  
		this.posXA.bindBidirectional(posXA);
		this.posYA.bindBidirectional(posYA);
		posXB.set((int) (posXA.get() + (factor * canton.getLength())));
		posYB.set((int) (posXA.get()));
		this.controller = controller;
		this.lineofCanton = new Line(this.posXA.get(), this.posXB.get(), this.posYB.get(), this.posXB.get());
	this.getChildren().add(lineofCanton);
	}
	@Override
	public void update(Observable o, Object arg) {
		Canton c = (Canton)o;
		if( c.isFree()){
			lineofCanton.setStroke(Color.RED);
		}else{
			lineofCanton.setStroke(Color.GREEN);
		}
			
	}
	public IntegerProperty getEndX(){
		return this.posXB;
	}
	public IntegerProperty getEndY(){
		return this.posYB;
	}
	
}
