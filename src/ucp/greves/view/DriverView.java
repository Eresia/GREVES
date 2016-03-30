package ucp.greves.view;

import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ucp.greves.controller.StationController;
import ucp.greves.controller.TrainController;
import ucp.greves.data.exceptions.station.StationNotFoundException;
import ucp.greves.data.train.Train;

public class DriverView extends Application implements Observer{

	private Train train;
	
	
	
	private Label finalStation;
	private Label nextStationName;
	private Label nextStationTime;
	private Label timeState;
	
	
	public DriverView(int trainId) {
		Stage stage = new Stage();
		try {
			this.train = TrainController.getRunningTrainById(trainId);
			
			start(stage);
		} catch (Exception e) {
			stage.close();
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("driver_view.fxml"));
		Scene scene = new Scene(root,600,400);
		primaryStage.setTitle("G.R.E.V.E.S. - Vue conducteur");
		
		
		
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
	}

	@Override
	public void update(Observable o, Object arg) {
		Platform.runLater(() -> this.finalStation.textProperty().set(StationController.getStationByCantonId(train.getRoadMap().getLastStation()).getName()));
		try {
			
			String StationName = StationController.getStationByCantonId(train.nextStations().get(0)).getName();
			Platform.runLater(() -> this.nextStationName.textProperty().set(StationName));
		} catch (StationNotFoundException e) {
			Platform.runLater(() -> this.nextStationName.textProperty().set(" "));
		}
	
		
	}
}
