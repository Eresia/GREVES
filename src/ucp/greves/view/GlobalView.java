package ucp.greves.view;

import javafx.application.Application;
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
import javafx.stage.Stage;
import ucp.greves.controller.GodModeController;
import ucp.greves.controller.StationController;
import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.exceptions.BadControlInformationException;
import ucp.greves.model.exceptions.canton.CantonNotExistException;
import ucp.greves.model.exceptions.railway.RailWayNotExistException;
import ucp.greves.model.exceptions.roadmap.BadRoadMapException;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.station.Station;
import ucp.greves.model.train.Train;

public class GlobalView extends Application{
	
	IntegerProperty paneWidth, paneHeight;
	
	private TableView<Station> stationList;
	private static Canton selectedCanton = null;
	
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
		GodModeController.getInstance().startStimulation();
  
		ScrollPane lineDraw = (ScrollPane) root.lookup("#LineDraw"); //Get the borderPane from the root
		LineView lineView = new LineView();
		lineDraw.setContent(lineView);
		//addStation(root);
		setStationList(root);
	}
	
	public void setButton(Parent root){
		Button buttonAdd = (Button) root.lookup("#AddTrainViewButton");
		buttonAdd.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
								
				/*TODO : fenêtre popup demandant à l'utilisateur le parcours et vitesse qu'il veut pour le train.
				 * 	(=> besoin de la liste des roadmap pour les proposer).
				 */
				
				/*int speed = 100;
				try {
					speed = (int) ConfigurationEnvironment.getInstance().getProperty("train_speed_max").getValue();
				} catch (PropertyNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
				String roadmapName = "Cergy-Marne";
				
				try {
					GodModeController.getInstance().launchTrain(roadmapName);
				} catch (BadControlInformationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadRoadMapException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RailWayNotExistException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
						GodModeController.getInstance().blockCanton(selectedCanton.getId());
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
						GodModeController.getInstance().createSlowDown(selectedCanton.getId());
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
						GodModeController.getInstance().removeCantonProblem(selectedCanton.getId());
					} catch (CantonNotExistException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		//Button "Horaires" to create a new view with the Station's display.
		Button stationViewButton = (Button) root.lookup("#StationViewButton");
		stationViewButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				Station selectedStation = stationList.getSelectionModel().getSelectedItem();
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
				GodModeController.getInstance().changeSimulationSpeed((int) changeSpeed.getValue());
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
	public void addTrains(Parent root){
		ComboBox<Train> trainList = (ComboBox<Train>) root.lookup("#TrainList"); //Pas sur du type que contient la combobox 
	}
	
	
	/**
	 * Get the TableView which is supposed to contain the list of the Station and fill it
	 * 
	 * @param root
	 * 		 (Parent) The root of the scene where the list is placed
	 */
	public void setStationList(Parent root){
		stationList = (TableView<Station>) root.lookup("#StationList");
		TableColumn<Station, String> stationNames = (TableColumn<Station, String>) stationList.getColumns().get(0);
		stationNames.setCellValueFactory(new PropertyValueFactory<Station,String>("name"));
		ObservableList<Station> stationListObs = FXCollections.observableArrayList();
		Station currentStation;
		for(Integer currentStationId : StationController.IntegerlistOfStationsID()){
			currentStation = StationController.getStationById(currentStationId);
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
