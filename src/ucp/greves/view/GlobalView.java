package ucp.greves.view;

import java.awt.Dimension;
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
import ucp.greves.controller.ConfigurationController;
import ucp.greves.controller.GodModeController;
import ucp.greves.controller.StationController;
import ucp.greves.controller.TrainController;
import ucp.greves.data.exceptions.canton.CantonNotExistException;
import ucp.greves.data.exceptions.train.TrainNotExistException;
import ucp.greves.data.line.canton.Canton;
import ucp.greves.data.line.station.GlobalStation;

public class GlobalView extends Application{
	
	IntegerProperty paneWidth, paneHeight;
	
	private TableView<GlobalStation> stationList;
	private volatile static CantonView selectedCanton = null;
	private static Label selectedCantonState = null;
	private ComboBox<Integer> trainIDListComboBox;
	private volatile static ArrayList<LineView> lines;
	
	public static void main(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("global_view.fxml"));
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int width;
		int height;
		if(dimension.getWidth() < 1024 || dimension.getHeight() < 768){
			width = (int) dimension.getWidth() - 20;
			height = (int) dimension.getHeight() - 20;
		}
		else{
			width = 1024;
			height = 768;
		}
		Scene scene = new Scene(root,width,height);
		primaryStage.setTitle("G.R.E.V.E.S. - Vue globale");
		
		lines = new ArrayList<LineView>();

		this.paneHeight = new SimpleIntegerProperty();
		this.paneWidth = new SimpleIntegerProperty();

		setButton(root);
		setTime(root);
		setSelectedCantonState(root);
	      
		primaryStage.setScene(scene);
		primaryStage.show();  

		ConfigurationController.buildConfiguration();
		
		//Launch Time witch this method
		GodModeController.startStimulation();
  
		ScrollPane lineDraw = (ScrollPane) root.lookup("#LineDraw"); //Get the borderPane from the root
		LineView lineView = new LineView(false);
		lines.add(lineView);
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
				CantonView selectedCanton = getSelectedCanton();
				if(selectedCanton != null) {
					try {
						CantonController.blockCanton(selectedCanton.getCanton().getId());
						changeStateCanton(selectedCanton.getCanton().getId());
						selectedCantonState.setTextFill(selectedCanton.getStateColor());
						selectedCantonState.setText(selectedCanton.getStateText());
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
				CantonView selectedCanton = getSelectedCanton();
				if(selectedCanton != null) {
					try {
						CantonController.createSlowDown(selectedCanton.getCanton().getId());
						selectedCanton.changeState();
						selectedCantonState.setTextFill(selectedCanton.getStateColor());
						selectedCantonState.setText(selectedCanton.getStateText());
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
				CantonView selectedCanton = getSelectedCanton();
				if(selectedCanton != null) {
					try {
						CantonController.removeCantonProblem(selectedCanton.getCanton().getId());
						selectedCanton.changeState();
						selectedCantonState.setTextFill(selectedCanton.getStateColor());
						selectedCantonState.setText(selectedCanton.getStateText());
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
				int trainId ;
				
				if(!trainIDListComboBox.getSelectionModel().isEmpty()){
					trainId = trainIDListComboBox.getSelectionModel().getSelectedItem();
					new DriverView(trainId);
				}
				
			}
		});
		
		Button globalMap = (Button) root.lookup("#GlobalMap");
		globalMap.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				new GlobalMap(lines);				
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
		new ClockView((Label) root.lookup("#TimeLabel"));
	}
	
	public void setSelectedCantonState(Parent root){
		selectedCantonState = (Label) root.lookup("#CantonState");
		selectedCantonState.setText("");
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
	
	public static void setSelectedCanton(CantonView cv){
		Canton canton = cv.getCanton();
		if(selectedCanton != null){
			unSelectCanton(selectedCanton.getCanton().getId());
		}
		if((selectedCanton == null) || (cv.getCanton() != selectedCanton.getCanton())){
			selectedCanton = cv;
			selectCanton(canton.getId());
			selectedCantonState.setTextFill(cv.getStateColor());
			selectedCantonState.setText(cv.getStateText());
		}
		else{
			selectedCantonState.setText("");
			selectedCanton = null;
		}
	}
	
	public static CantonView getSelectedCanton(){
		return selectedCanton;
	}
	
	public static void selectCanton(Integer canton){
		ArrayList<LineView> line = (ArrayList<LineView>) lines.clone();
		for(LineView l : line){
			l.selectCanton(canton);
		}
	}
	
	public static void unSelectCanton(Integer canton){
		ArrayList<LineView> line = (ArrayList<LineView>) lines.clone();
		for(LineView l : line){
			l.unSelectCanton(canton);
		}
	}
	
	public static void changeStateCanton(Integer canton){
		ArrayList<LineView> line = (ArrayList<LineView>) lines.clone();
		for(LineView l : line){
			l.changeStateCanton(canton);
		}
	}

}
