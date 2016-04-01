package ucp.greves.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ucp.greves.controller.CantonController;
import ucp.greves.controller.StationController;
import ucp.greves.controller.TrainController;
import ucp.greves.data.exceptions.canton.TerminusException;
import ucp.greves.data.exceptions.station.StationNotFoundException;
import ucp.greves.data.exceptions.train.TrainNotExistException;
import ucp.greves.data.line.canton.Canton;
import ucp.greves.data.train.Train;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DriverView extends Application implements Observer{

	private Train train;
	
	
	
	private Label finalStation;
	private Label nextStationName;
	private Label nextStationTime;
	private Label timeState;
	private Pane driverLineDraw;

	private Stage stage;

	private SimpleIntegerProperty startXpos , startYpos ;
	
	
	public DriverView(int trainId) {
		stage = new Stage();
		try {
			this.train = TrainController.getRunningTrainById(trainId);
			
			start(stage);
		} catch (TrainNotExistException e) {
			stage.close();
		}
	}
	
	@Override
	public void start(Stage primaryStage)   {
		Parent root;
		
		try {
			root = FXMLLoader.load(getClass().getResource("driver_view.fxml"));

		Scene scene = new Scene(root,600,400);
		primaryStage.setTitle("G.R.E.V.E.S. - Vue conducteur");
		
		this.startXpos = new SimpleIntegerProperty();
		this.startYpos = new SimpleIntegerProperty();

		 driverLineDraw = (Pane) root.lookup("#DriverLineDraw");
		 startYpos.set((int)driverLineDraw.heightProperty().divide(2).get());
		 startXpos.setValue(10);
		 finalStation =  (Label) root.lookup("#FinalStation");
		 nextStationName = (Label) root.lookup("#NextStationName");
		 nextStationTime = (Label) root.lookup("#NextStationTime");
		 timeState = (Label) root.lookup("#TimeState");
		 
		 this.finalStation.textProperty().set(StationController.getStationByCantonId(train.getRoadMap().getLastStation()).getName());
		 try {
			this.nextStationName.textProperty().set(StationController.getStationByCantonId(train.nextStations().get(0)).getName());
		} catch (StationNotFoundException e) {
			this.nextStationName.textProperty().set(" ");
		}
		 this.train.addObserver(this);
		 primaryStage.setScene(scene);
		 
		primaryStage.show();
		} catch (IOException e1) {

			e1.printStackTrace();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		if(train.hasArrived()){
			this.train.deleteObserver(this);
			Platform.runLater(() ->stage.close());
		}
		else{
			Platform.runLater(() -> this.finalStation.textProperty().set(StationController.getStationByCantonId(train.getRoadMap().getLastStation()).getName()));
			try {
				
				String StationName = StationController.getStationByCantonId(train.nextStations().get(0)).getName();
				Platform.runLater(() -> this.nextStationName.textProperty().set(StationName));
			} catch (StationNotFoundException e) {
				Platform.runLater(() -> this.nextStationName.textProperty().set(" "));
			}
			
			ArrayList<CantonView> cantonlist = new ArrayList<CantonView>();
			IntegerProperty xpos = new SimpleIntegerProperty();
			IntegerProperty ypos = new SimpleIntegerProperty();
			xpos.bind(startXpos);
			ypos.bind(startYpos);
			Platform.runLater(() -> this.driverLineDraw.getChildren().clear());
			Canton tempc = this.train.getCurrentCanton();
			
			for(int i = 0 ; i < 5 ; i++ ){
				CantonView cv = new CantonView(xpos, ypos, 0.018, tempc, true , false , true);
				cantonlist.add(cv);
				 xpos = cv.getEndX();
				 ypos = cv.getEndY();
				 try {
					tempc = tempc.getNextCanton(0);
				} catch (TerminusException e) {
					continue;
				}
				
			}
			Platform.runLater(() -> this.driverLineDraw.getChildren().addAll(cantonlist));
		}
		
		
	}
}
