package ucp.greves.view;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ucp.greves.controller.CantonController;
import ucp.greves.controller.GodModeController;
import ucp.greves.controller.StationController;
import ucp.greves.controller.TrainController;
import ucp.greves.data.line.canton.Canton;
import ucp.greves.data.line.station.GlobalStation;
import ucp.greves.data.line.station.Station;
import ucp.greves.data.train.Train;
import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.exceptions.BadControlInformationException;
import ucp.greves.model.exceptions.canton.CantonNotExistException;
import ucp.greves.model.exceptions.railway.RailWayNotExistException;
import ucp.greves.model.exceptions.roadmap.BadRoadMapException;
import ucp.greves.model.exceptions.train.TrainNotExistException;
import ucp.greves.model.line.Line;

public class GlobalView extends Application{
	
	IntegerProperty paneWidth, paneHeight;
	
	private TableView<GlobalStation> stationList;
	private static Canton selectedCanton = null;
	private ComboBox<Integer> trainIDListComboBox;
	
	public static void main(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("global_view.fxml"));
		Scene scene = new Scene(root,1024,768);
		primaryStage.setTitle("G.R.E.V.E.S. - Vue globale");

		this.paneHeight = new SimpleIntegerProperty();
		this.paneWidth = new SimpleIntegerProperty();

		setButton(root);
		setTime(root);
	      
		primaryStage.setScene(scene);
		primaryStage.show();  

		//ConfigurationEnvironment.getInstance().setProperty("BUILD_CONFIGURATION", "JSON");
		ConfigurationEnvironment.getInstance().setProperty("BUILD_CONFIGURATION", "XML");
		Line.getInstance();
		
		//Launch Time witch this method
		GodModeController.startStimulation();
  
		ScrollPane lineDraw = (ScrollPane) root.lookup("#LineDraw"); //Get the borderPane from the root
		LineView lineView = new LineView();
		lineDraw.setContent(lineView);
		setTrainIDsList(root);
		setStationList(root);
		
		//make all the window close and stop the program when this window is closed
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				Platform.exit();
				GodModeController.stopSimulation();			
			}
		});
	}
	
	public void setButton(Parent root){
		
		/****************** Trains Management (Gestion des Trains) *******************/
		Button buttonAdd = (Button) root.lookup("#AddTrainViewButton");
		buttonAdd.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				new addTrainView();
			}
		});
		
		Button buttonDelete = (Button) root.lookup("#DeleteTrain");
		buttonDelete.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				Integer selectedTrain = trainIDListComboBox.getValue();
				
				if(selectedTrain != null) {
					TrainController.removeTrain(selectedTrain);
				}
			}
		});
		
		Button buttonStart = (Button) root.lookup("#StartTrain");
		buttonStart.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				Integer selectedTrain = trainIDListComboBox.getValue();
				
				if(selectedTrain != null) {
					try {
						TrainController.unblockTrain(selectedTrain);
					} catch (TrainNotExistException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		Button buttonStop = (Button) root.lookup("#StopTrain");
		buttonStop.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				Integer selectedTrain = trainIDListComboBox.getValue();
				
				if(selectedTrain != null) {
					try {
						TrainController.blockTrain(selectedTrain);
					} catch (TrainNotExistException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		/****************** Cantons Management (Gestion Cantons) *******************/
		//Button "Arrêt"
		Button stopCantonButton = (Button) root.lookup("#StopCanton");
		stopCantonButton.setOnAction(new EventHandler<ActionEvent>() {
		
			@Override
			public void handle(ActionEvent event) {
				Canton selectedCanton = getSelectedCanton();
				if(selectedCanton != null) {
					try {
						CantonController.blockCanton(selectedCanton.getId());
					} catch (CantonNotExistException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		//Button "Ralentir"
		Button slowDownCantonButton = (Button) root.lookup("#SlowCanton");
		slowDownCantonButton.setOnAction(new EventHandler<ActionEvent>() {
		
			@Override
			public void handle(ActionEvent event) {
				Canton selectedCanton = getSelectedCanton();
				if(selectedCanton != null) {
					try {
						CantonController.createSlowDown(selectedCanton.getId());
					} catch (CantonNotExistException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		//Button "Normaliser"
		Button normaliseCantonButton = (Button) root.lookup("#NormalCanton");
		normaliseCantonButton.setOnAction(new EventHandler<ActionEvent>() {
		
			@Override
			public void handle(ActionEvent event) {
				Canton selectedCanton = getSelectedCanton();
				if(selectedCanton != null) {
					try {
						CantonController.removeCantonProblem(selectedCanton.getId());
					} catch (CantonNotExistException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		/****************** Secondary Views Management *******************/
		//Button "Horaires" to create a new view with the Station's display.
		Button stationViewButton = (Button) root.lookup("#StationViewButton");
		stationViewButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				GlobalStation selectedStation = stationList.getSelectionModel().getSelectedItem();
				if(selectedStation != null) {
					new StationView(selectedStation);
				}
			}
		});
		
		//Button "Vue conducteur" to create a new view with the Train Driver's display.
		Button DriverViewButton = (Button) root.lookup("#DriverViewButton");
		DriverViewButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				new DriverView();
			}
		});
		
		//Slider to change the simulation's speed.
		Slider changeSpeed = (Slider) root.lookup("#ChangeSpeed");
		changeSpeed.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				GodModeController.changeSimulationSpeed((int) changeSpeed.getValue());
			}
		});
		changeSpeed.setValue(0);
	}
	
	public void setTime(Parent root){
		Label label  = (Label) root.lookup("#TimeLabel");
		//TODO : Print hour ( TimeController.getClockString() )
		//label.textProperty().bind(r);
	}
	
	/*public void addStation(Parent root){
		TableView<String> table  = (TableView<String>) root.lookup("#table");
		table.getColumns().get(0);
		System.out.println(table.getColumns().get(0).);
	}*/
	
	/**
	 * Get the ComboBox which is supposed to contain the list of the trains and fill it
	 * 
	 * @param root
	 * 		 (Parent) The root of the scene where the list is placed
	 */
	public void setTrainIDsList(Parent root){
		trainIDListComboBox = (ComboBox<Integer>) root.lookup("#TrainList");
		
		trainIDListComboBox.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				trainIDListComboBox.getItems().clear();
				
				ArrayList<Integer> trainIDList = TrainController.integerListOfRunningTrainsID();
				
				for(int i = 0; i < trainIDList.size(); i++) {
					trainIDListComboBox.getItems().add(trainIDList.get(i));
				}
				
			}
		});
	}
	
	
	/**
	 * Get the TableView which is supposed to contain the list of the Station and fill it
	 * 
	 * @param root
	 * 		 (Parent) The root of the scene where the list is placed
	 */
	public void setStationList(Parent root){
		stationList = (TableView<GlobalStation>) root.lookup("#StationList");
		TableColumn<GlobalStation, String> stationNames = (TableColumn<GlobalStation, String>) stationList.getColumns().get(0);
		stationNames.setCellValueFactory(new PropertyValueFactory<GlobalStation,String>("name"));
		ObservableList<GlobalStation> stationListObs = FXCollections.observableArrayList();
		GlobalStation currentStation;
		for(String currentStationId : StationController.StringlistOfGlobalStationsName()){
			currentStation = StationController.getGlobalStationByName(currentStationId);
			stationListObs.add(currentStation);
		}
		stationList.setItems(stationListObs);
	}
	
	public static void setSelectedCanton(Canton canton){
		selectedCanton = canton;
	}
	
	public static Canton getSelectedCanton(){
		return selectedCanton;
	}

}
