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
import javafx.scene.text.Text;
import ucp.greves.controller.CantonController;
import ucp.greves.data.exceptions.canton.CantonIsEmptyException;
import ucp.greves.data.exceptions.station.StationNotFoundException;
import ucp.greves.data.exceptions.train.TrainIsNotInACanton;
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
	
	private final static Paint colorSelected = Color.BLUE;
	private final static Paint colorBlocked = Color.RED;
	private final static Paint colorSlow = Color.YELLOW;

	public CantonView(IntegerProperty posXA, IntegerProperty posYA,
			double factor, Canton canton, CantonController controller) {
		this.canton = canton;
		this.isSelected = false;
		SystemProperties.setFXProperty("javafx.debug", "true");
		this.posXA = new SimpleIntegerProperty();
		this.posXB = new SimpleIntegerProperty();
		this.posYA = new SimpleIntegerProperty();
		this.posYB = new SimpleIntegerProperty();
		this.posXA.bindBidirectional(posXA);
		this.posYA.bindBidirectional(posYA);
		this.scaleFactor = factor;
		posXB.set((int) (posXA.get()));
		posYB.set((int) (posYA.get() + (factor * canton.getLength())));
		this.lineofCanton = new Line(this.posXA.get(), this.posYA.get(),
				this.posXB.get(), this.posYB.get());
		this.lineofCanton.setFill(Color.GREEN);
		this.lineofCanton.setStrokeWidth(3);
		if (canton.isFree()) {
			lineofCanton.setStroke(Color.GREEN);
		} else {
			lineofCanton.setStroke(Color.RED);
		}
		Circle stationPosition;
		Text stationText;
		try {
			Station station = canton.getStation();
			int stationPos = canton.getStationPosition();
			stationPosition = new Circle();
			stationPosition.setRadius(4);
			stationPosition.setStroke(Color.BLUE);
			stationPosition.setCenterX(this.posXA.get());
			stationPosition.setCenterY(posYA.get()
					+ (this.scaleFactor * stationPos));
			stationText = new Text();
			stationText.xProperty().set(this.posXA.get() + 4);
			stationText.yProperty().set(
					posYA.get() + (this.scaleFactor * stationPos));
			stationText.textProperty().set(station.getName());
			this.getChildren().add(stationPosition);
			this.getChildren().add(stationText);

		} catch (StationNotFoundException e) {
			// TODO: handle exception
		}

		/*
		 * PARTIE AFFICHAGE DE LA POSTION DU TRAIN
		 */

		this.trainPosition = new Polygon();
		this.trainPosition.setStroke(Color.DARKGRAY);
		this.trainText = new Text();

		canton.addObserver(this);
		this.getChildren().add(lineofCanton);
		
		setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				GlobalView.setSelectedCanton(CantonView.this);				
			}
			
		});
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
					int trainPositionOnCanton = this.innerTrain.positionInCanton(c);
					Platform.runLater(()-> trainPosition.getPoints().setAll(
							(double) posXA.get(),
							(double) posYA.get(),
							(double) posXA.get() - 5,
							(double) posYA.get() - 5,
							(double) posXA.get() + 5,
							(double) posYA.get() - 5

					));
					Platform.runLater(() -> this.trainText.yProperty().set(posYA.get() + 2));
					Platform.runLater(() -> this.trainText.xProperty().set((double) posXA.get() + 6));
					Platform.runLater(() -> this.trainText.textProperty().set(trainId));
					 
					Platform.runLater(() -> this.getChildren().add(trainPosition));
					Platform.runLater(() -> this.getChildren().add(trainText));
					this.innerTrain.addObserver(this);
					Platform.runLater(() ->setColor(Color.RED));
				} catch (CantonIsEmptyException e) {
					actionOnFree();
				} catch (TrainIsNotInThisCanton e) {
					actionOnFree();
				}
			}
		} else if (o instanceof Train) {
			Train t = (Train) o;			
			
			try {
				int trainPositionInCanton = t.positionInCanton();
				Platform.runLater(() -> this.trainPosition.setLayoutY(scaleFactor * trainPositionInCanton));
				Platform.runLater(() -> this.trainText.setLayoutY(scaleFactor * trainPositionInCanton ));
			} catch (TrainIsNotInThisCanton e) {
				//e.printStackTrace();
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
		Platform.runLater(() ->setColor(Color.GREEN));
	}
	
	private void setColor(Paint color){
		if(isSelected){
			lineofCanton.setStroke(colorSelected);
		}
		else{
			switch(canton.getState()){
				case BLOCKED:
					lineofCanton.setStroke(colorBlocked);
					break;
				case SLOWSDOWN:
					lineofCanton.setStroke(colorSlow);
					break;
				case NO_PROBLEM:
					lineofCanton.setStroke(color);
					break;
			}
		}
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
		setColor(colorSelected);
	}
	
	public void unSelect(){
		isSelected = false;
		if(canton.isFree()){
			setColor(Color.GREEN);
		}
		else{
			setColor(Color.RED);
		}
	}
	
	public void changeState(){
		if(canton.isFree()){
			setColor(Color.GREEN);
		}
		else{
			setColor(Color.RED);
		}
	}


}
