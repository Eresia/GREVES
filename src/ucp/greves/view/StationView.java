/*
 * TODO : See javadoc
 */
package ucp.greves.view;

import java.util.Observable;
import java.util.Observer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ucp.greves.controller.ClockController;
import ucp.greves.controller.StationController;
import ucp.greves.controller.TrainController;
import ucp.greves.data.exceptions.station.StationNotFoundException;
import ucp.greves.data.line.station.GlobalStation;
import ucp.greves.data.line.station.NextTrainInformations;
import ucp.greves.data.line.station.Station;
import ucp.greves.data.time.Time;

public class StationView extends Application implements Observer{
	
	private GlobalStation station;
	private Station firstStation;
	private Station secondStation;
	
	private TableView<NextTrainInformations> nextTrainFirstTable;
	private ObservableList<NextTrainInformations> nextTrainListOne;
	
	private TableView<Station> fisrtNextStation;
	private ObservableList<Station> stationListOne;
	
	private TableView<NextTrainInformations> nextTrainSecondTable;
	private ObservableList<NextTrainInformations> nextTrainListTwo;
	
	private TableView<Station> secondNextStation;
	private ObservableList<Station> stationListTwo;


	public StationView(GlobalStation station) {
		this.station = station;
		firstStation = StationController.getStationByCantonId(station.getStations().get(0));
		secondStation = StationController.getStationByCantonId(station.getStations().get(1));
		Stage stage = new Stage();
		ClockController.getCurrentTime().addObserver(this);
		try {
			start(stage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	/**
	 * @param primaryStage
	 * 	(Stage) 
	 * @throws 
	 * 	Exception
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("station_view.fxml"));
		Scene scene = new Scene(root,800,600);
		primaryStage.setTitle("G.R.E.V.E.S. - Vue de la gare : " + station.getName());
		setTableView(root);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	/**
	 * Get the Tableview which are supposed to contain the usefull informations and fill them
	 * 
	 * @param root
	 * 		 (Parent) The root of the scene where the TableView are placed
	 */
	public void setTableView(Parent root){
		//first TableView of next trains
		nextTrainFirstTable = (TableView<NextTrainInformations>) root.lookup("#NextTrainFirstTable");
		TableColumn<NextTrainInformations, Integer> firstNextTrainColumnOne = (TableColumn<NextTrainInformations, Integer>) nextTrainFirstTable.getColumns().get(0);
		firstNextTrainColumnOne.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumn<NextTrainInformations, Integer> firstNextTrainColumnTwo = (TableColumn<NextTrainInformations, Integer>) nextTrainFirstTable.getColumns().get(1);
		firstNextTrainColumnTwo.setCellValueFactory(new PropertyValueFactory<>("destination"));
		TableColumn<NextTrainInformations, Integer> firstNextTrainColumnThree = (TableColumn<NextTrainInformations, Integer>) nextTrainFirstTable.getColumns().get(2);
		firstNextTrainColumnThree.setCellValueFactory(new PropertyValueFactory<>("time"));
		nextTrainListOne = FXCollections.observableArrayList();
		for(NextTrainInformations currentTrain : StationController.getNextTrainsInStation(firstStation.getCanton())){
			nextTrainListOne.add(currentTrain);
		}
		nextTrainFirstTable.setItems(nextTrainListOne);
		//nextTrainFirstTable.getItems().get(0);
		
		//first TableView of next stations
		fisrtNextStation = (TableView<Station>) root.lookup("#NextStationFirstTable");
		TableColumn<Station, String> fisrtNextStationColumnOne = (TableColumn<Station, String>) fisrtNextStation.getColumns().get(0);
		fisrtNextStationColumnOne.setCellValueFactory(new PropertyValueFactory<Station,String>("name"));
		stationListOne = FXCollections.observableArrayList();
		fisrtNextStation.setItems(stationListOne);
		
		//Second TableView of next trains
		nextTrainSecondTable = (TableView<NextTrainInformations>) root.lookup("#NextTrainSecondTable");
		TableColumn<NextTrainInformations, Integer> secondNextTrainColumnOne = (TableColumn<NextTrainInformations, Integer>) nextTrainSecondTable.getColumns().get(0);
		secondNextTrainColumnOne.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumn<NextTrainInformations, Integer>secondNextTrainColumnTwo = (TableColumn<NextTrainInformations, Integer>) nextTrainSecondTable.getColumns().get(1);
		secondNextTrainColumnTwo.setCellValueFactory(new PropertyValueFactory<>("destination"));
		TableColumn<NextTrainInformations, Integer> secondNextTrainColumnThree = (TableColumn<NextTrainInformations, Integer>) nextTrainSecondTable.getColumns().get(2);
		secondNextTrainColumnThree.setCellValueFactory(new PropertyValueFactory<>("time"));
		nextTrainListTwo = FXCollections.observableArrayList();
		for(NextTrainInformations currentTrain : StationController.getNextTrainsInStation(secondStation.getCanton())){
			nextTrainListTwo.add(currentTrain);
		}
		nextTrainSecondTable.setItems(nextTrainListTwo);
		
		//second TableView of next stations
		secondNextStation = (TableView<Station>) root.lookup("#NextStationSecondTable");
		TableColumn<Station, String> secondNextStationColumnOne = (TableColumn<Station, String>) secondNextStation.getColumns().get(0);
		secondNextStationColumnOne.setCellValueFactory(new PropertyValueFactory<Station,String>("name"));
		stationListTwo = FXCollections.observableArrayList();
		secondNextStation.setItems(stationListTwo);
		
	}

	@Override
	public synchronized void update(Observable o, Object arg) {
		if(o instanceof Time){
			nextTrainListOne = FXCollections.observableArrayList();
			for(NextTrainInformations currentTrain : StationController.getNextTrainsInStation(firstStation.getCanton())){
				nextTrainListOne.add(currentTrain);
			}
			if(nextTrainListOne != null && nextTrainFirstTable != null){
				nextTrainFirstTable.setItems(nextTrainListOne);
			}
			
			
			stationListOne = FXCollections.observableArrayList();
			if(!nextTrainListOne.isEmpty()){
				NextTrainInformations train = nextTrainListOne.get(0);
				try {
					for(Integer currentStaion : TrainController.getRunningTrainById(train.getId()).nextStations()){
						stationListOne.add(StationController.getStationByCantonId(currentStaion));
					}
				} catch (StationNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(stationListOne != null && fisrtNextStation != null){
				fisrtNextStation.setItems(stationListOne);
			}

			
			nextTrainListTwo = FXCollections.observableArrayList();
			for(NextTrainInformations currentTrain : StationController.getNextTrainsInStation(secondStation.getCanton())){
				nextTrainListTwo.add(currentTrain);
			}
			if(nextTrainListTwo != null && nextTrainSecondTable != null){
				nextTrainSecondTable.setItems(nextTrainListTwo);
			}
			
			stationListTwo = FXCollections.observableArrayList();
			if(!nextTrainListTwo.isEmpty()){
				NextTrainInformations train = nextTrainListTwo.get(0);
				try {
					for(Integer currentStaion : TrainController.getRunningTrainById(train.getId()).nextStations()){
						stationListTwo.add(StationController.getStationByCantonId(currentStaion));
					}
				} catch (StationNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(stationListTwo != null && secondNextStation != null){
				secondNextStation.setItems(stationListTwo);
			}

		}
		
	}
}
