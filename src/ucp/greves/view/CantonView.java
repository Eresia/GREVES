package ucp.greves.view;

import java.util.Observable;
import java.util.Observer;

import com.sun.javafx.runtime.SystemProperties;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import ucp.greves.controller.CantonController;
import ucp.greves.data.exceptions.canton.CantonIsEmptyException;
import ucp.greves.data.exceptions.station.StationNotFoundException;
import ucp.greves.data.exceptions.train.TrainIsNotInThisCanton;
import ucp.greves.data.line.canton.Canton;
import ucp.greves.data.line.station.Station;
import ucp.greves.data.train.Train;

public class CantonView extends Parent implements Observer {
	private Train innerTrain;
	private double scaleFactor;
	private Polygon trainPosition;
	private Text trainText;
	private IntegerProperty posXA, posYA, posXB, posYB;
	private Line lineofCanton;
	private Canton canton;
	private volatile boolean isSelected;
	private Boolean global;
	private Boolean drivers;
	private Boolean direction;
	private Boolean horizontal;
	
	private final static Paint colorTrain = Color.DARKGRAY;
	private final static Paint colorStation = Color.BLUE;
	private final static Paint colorCantonOccuped = Color.RED;
	private final static Paint colorCantonBlocked = Color.BLACK;
	private final static Paint colorCantonSlow = Color.YELLOW;
	private final static Paint colorCantonNormal = new Color(0.45, 0.7, 0, 1);
	
	public CantonView(IntegerProperty posXA, IntegerProperty posYA,
			double factor, Canton canton,  Boolean global, Boolean direction, Boolean horizontal) {
		this(posXA, posYA, factor, canton, global, direction, horizontal, false);
	}

	public CantonView(IntegerProperty posXA, IntegerProperty posYA,
			double factor, Canton canton,  Boolean global, Boolean direction, Boolean horizontal, Boolean drivers) {
		this.canton = canton;
		this.isSelected = false;
		this.global = global;
		this.drivers = drivers;
		this.direction = direction;
		this.horizontal = horizontal;
		SystemProperties.setFXProperty("javafx.debug", "true");
		this.posXA = new SimpleIntegerProperty();
		this.posXB = new SimpleIntegerProperty();
		this.posYA = new SimpleIntegerProperty();
		this.posYB = new SimpleIntegerProperty();
		this.posXA.bindBidirectional(posXA);
		this.posYA.bindBidirectional(posYA);
		this.scaleFactor = factor;
		if(global || drivers){
			scaleFactor = scaleFactor * 1.5;
			posXB.set((int) (posXA.get() + (scaleFactor * canton.getLength())));
			posYB.set((int) (posYA.get()));
		}
		else{
			if(horizontal){
				
				posXB.set((int) (posXA.get() + (scaleFactor * canton.getLength())));
				posYB.set((int) (posYA.get()));
			
			}else{
				posXB.set((int) (posXA.get()));
				posYB.set((int) (posYA.get() + (scaleFactor * canton.getLength())));
			}
			}
		this.lineofCanton = new Line(this.posXA.get(), this.posYA.get(),
				this.posXB.get(), this.posYB.get());
		this.lineofCanton.setFill(colorCantonNormal);
		this.lineofCanton.setStrokeWidth(3);
		CantonView selectedCanton = GlobalView.getSelectedCanton();
		if(selectedCanton != null && selectedCanton.getCanton() == canton){
			isSelected = true;
		}
		if (canton.isFree()) {
			setColor(colorCantonNormal);
		} else {
			setColor(colorCantonOccuped);
		}
		Circle stationPosition;
		Text stationText;
		try {
			Station station = canton.getStation();
			int stationPos = canton.getStationPosition();
			stationPosition = new Circle();
			stationPosition.setRadius(4);
			stationPosition.setStroke(colorStation);
			stationText = new Text();
			if(global || drivers){
				stationPosition.setCenterX(this.posXA.get() + (this.scaleFactor * stationPos));
				stationPosition.setCenterY(this.posYA.get());
				if((this.canton.getId() %2) == 0){
					stationText.yProperty().set(posYA.get() - 5);
				}
				else{
					stationText.yProperty().set(posYA.get() + 10);
				}
				stationText.xProperty().set(this.posXA.get());
				
				stationText.setFont(new Font(8));
			}
			else{
				
				stationPosition.setCenterX(this.posXA.get());
				stationPosition.setCenterY(this.posYA.get()
						+ (this.scaleFactor * stationPos));
				stationText.xProperty().set(this.posXA.get() + 4);
				stationText.yProperty().set(
						posYA.get() + (this.scaleFactor * stationPos));
			}
			stationText.textProperty().set(station.getName());
			
			this.getChildren().add(stationPosition);
			this.getChildren().add(stationText);

		} catch (StationNotFoundException e) {
			
		}

		/*
		 * PARTIE AFFICHAGE DE LA POSTION DU TRAIN
		 */

		this.trainPosition = new Polygon();
		this.trainPosition.setStroke(colorTrain);
		this.trainText = new Text();

		canton.addObserver(this);
		this.getChildren().add(lineofCanton);
		
		setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				GlobalView.setSelectedCanton(CantonView.this);				
			}
			
		});
		
		if(drivers){
			update(canton, null);
		}
	}

	@Override
	public synchronized void   update(Observable o, Object arg) {
		if (o instanceof Canton) {

			Canton c = (Canton) o;
			boolean isFree = c.isFree();
			
			actionOnFree();
			
			if (!isFree) {
				try {
					this.innerTrain = c.getOccupyingTrain();
					String trainId = String.valueOf(innerTrain.getTrainID());
					this.innerTrain.addObserver(this);
					//int trainPositionOnCanton = this.innerTrain.positionInCanton(c);
					if(global || drivers){
						if(direction){
							if(!drivers){
								Platform.runLater(()-> trainPosition.getPoints().setAll(
										(double) posXA.get() + 6,
										(double) posYA.get(),
										(double) posXA.get() - 5 + 6,
										(double) posYA.get() - 5,
										(double) posXA.get() - 5 + 6,
										(double) posYA.get() + 5
											));
							}
							else{
								Platform.runLater(()-> trainPosition.getPoints().setAll(
										(double) posXA.get() + 11,
										(double) posYA.get(),
										(double) posXA.get() - 5 + 11,
										(double) posYA.get() - 5,
										(double) posXA.get() - 5 + 11,
										(double) posYA.get() + 5
											));
							}
							
						}
						else{
							Platform.runLater(()-> trainPosition.getPoints().setAll(
									(double) posXA.get() - 6,
									(double) posYA.get(),
									(double) posXA.get() + 5 - 6,
									(double) posYA.get() - 5,
									(double) posXA.get() + 5 - 6,
									(double) posYA.get() + 5
										));
						}
						Platform.runLater(() -> this.trainText.yProperty().set((double) posYA.get() + 15));
						Platform.runLater(() -> this.trainText.xProperty().set((double) posXA.get() + 2));
					}
					else{
						if(! horizontal){
						Platform.runLater(()-> trainPosition.getPoints().setAll(
							(double) posXA.get(),
							(double) posYA.get() + 5,
							(double) posXA.get() - 5,
							(double) posYA.get() - 5 + 5,
							(double) posXA.get() + 5,
							(double) posYA.get() - 5 + 5
								));
						Platform.runLater(() -> this.trainText.yProperty().set(posYA.get() + 2));
						Platform.runLater(() -> this.trainText.xProperty().set((double) posXA.get() + 6));
						}else{
							Platform.runLater(()-> trainPosition.getPoints().setAll(
									(double) posXA.get() + 5 ,
									(double) posYA.get() + 5,
									(double) posXA.get() ,
									(double) posYA.get() ,
									(double) posXA.get() - 5,
									(double) posYA.get() + 5
										));
								Platform.runLater(() -> this.trainText.yProperty().set(posYA.get() + 2));
								Platform.runLater(() -> this.trainText.xProperty().set((double) posXA.get() + 6));
						}
						}
					
					Platform.runLater(() -> this.trainText.textProperty().set(trainId));
					 
					Platform.runLater(() -> this.getChildren().add(trainPosition));
					Platform.runLater(() -> this.getChildren().add(trainText));
					this.innerTrain.addObserver(this);
					Platform.runLater(() ->setColor(colorCantonOccuped));
				} catch (CantonIsEmptyException e) {
					actionOnFree();
				}/* catch (TrainIsNotInThisCanton e) {
					actionOnFree();
				}*/
			}
		} else if (o instanceof Train) {
			if(!((Boolean) arg)){
				Train t = (Train) o;
				
				try {
					int trainPositionInCanton = t.positionInCanton();
					if(global || drivers){
						if(direction){
							Platform.runLater(() -> this.trainPosition.setLayoutX(scaleFactor * trainPositionInCanton));
							Platform.runLater(() -> this.trainText.setLayoutX(scaleFactor * trainPositionInCanton ));
						}
						else{
							Platform.runLater(() -> this.trainPosition.setLayoutX((scaleFactor * trainPositionInCanton * -1) + (this.posXB.get() - this.posXA.get())));
							Platform.runLater(() -> this.trainText.setLayoutX((scaleFactor * trainPositionInCanton * -1) + (this.posXB.get() - this.posXA.get())));
						}
					}
					else{
						if (horizontal) {
							Platform.runLater(() -> this.trainPosition.setLayoutX(scaleFactor * trainPositionInCanton));
							Platform.runLater(() -> this.trainText.setLayoutX(scaleFactor * trainPositionInCanton ));
						}else{
							Platform.runLater(() -> this.trainPosition.setLayoutY(scaleFactor * trainPositionInCanton));
							Platform.runLater(() -> this.trainText.setLayoutY(scaleFactor * trainPositionInCanton ));
						}

					}
				} catch (TrainIsNotInThisCanton e) {
					//e.printStackTrace();
				}
			}

		}
	
	}
	
	private void actionOnFree(){
		if (this.innerTrain != null) {
			this.innerTrain.deleteObserver(this);
			this.innerTrain = null;
			Platform.runLater(() -> this.getChildren().remove(
					trainPosition));
			Platform.runLater(() -> this.getChildren()
					.remove(trainText));
		}
		Platform.runLater(() ->setColor(colorCantonNormal));
	}
	
	private void setColor(Paint color){
		if(isSelected){
			if(global || drivers){
				lineofCanton.setScaleY(2);
			}
			else{
				lineofCanton.setScaleX(2);
			}
			
		}
		else{
			if(global || drivers){
				lineofCanton.setScaleY(1);
			}
			else{
				lineofCanton.setScaleX(1);
			}
		}
		lineofCanton.setStroke(getStateColor(color));
	}
	
	public Paint getStateColor(){
		return getStateColor(colorCantonNormal);
	}
	
	private Paint getStateColor(Paint defaultColor){
		Paint color = null;
		switch(canton.getState()){
			case BLOCKED:
				color = colorCantonBlocked;
				break;
			case SLOWSDOWN:
				color = colorCantonSlow;
				break;
			case NO_PROBLEM:
				color = defaultColor;
				break;
		}
		return color;
	}
	
	public String getStateText(){
		String text = null;
		switch(canton.getState()){
			case BLOCKED:
				text = "Bloqué";
				break;
			case SLOWSDOWN:
				text = "Ralenti";
				break;
			case NO_PROBLEM:
				text = "Normal";
				break;
		}
		return text;
	}

	public IntegerProperty getEndX() {
		return this.posXB;
	}

	public IntegerProperty getEndY() {
		return this.posYB;
	}
	
	public Canton getCanton(){
		return canton;
	}
	
	public void select(){
		isSelected = true;
		setColor(colorCantonNormal);
	}
	
	public void unSelect(){
		isSelected = false;
		if(canton.isFree()){
			setColor(colorCantonNormal);
		}
		else{
			setColor(colorCantonOccuped);
		}
	}
	
	public void changeState(){
		if(canton.isFree()){
			setColor(colorCantonNormal);
		}
		else{
			setColor(colorCantonOccuped);
		}
	}


}
