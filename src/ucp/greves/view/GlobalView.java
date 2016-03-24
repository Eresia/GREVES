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
import ucp.greves.model.exceptions.PropertyNotFoundException;
import ucp.greves.model.exceptions.railway.RailWayNotExistException;
import ucp.greves.model.exceptions.roadmap.BadRoadMapException;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.station.Station;
import ucp.greves.model.schedule.Clock;
import ucp.greves.model.train.Train;

public class GlobalView extends Application{
	
	IntegerProperty paneWidth, paneHeight;
	
	private TableView<Station> stationList;
	
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

		ScrollPane lineDraw = (ScrollPane) root.lookup("#lineDraw"); //Get the borderPane from the root
		setButton(root);
	      
		primaryStage.setScene(scene);
		primaryStage.show();  

		//ConfigurationEnvironment.getInstance().setProperty("BUILD_CONFIGURATION", "JSON");
		ConfigurationEnvironment.getInstance().setProperty("BUILD_CONFIGURATION", "XML");
		Line.getInstance();
		
		//Launch Time witch this method
		//GodModeController.getInstance().startStimulation();
  
		modify(lineDraw);
		//addStation(root);
		setStationList(root);
	}
	
	public void modify(ScrollPane pane){
		paneWidth.setValue(pane.getWidth());
		paneHeight.setValue(pane.getHeight());
		LineView lv = new LineView(paneWidth, paneHeight);	    
		pane.setContent(lv);
	}
	
	public void setButton(Parent root){
		Button buttonAdd = (Button) root.lookup("#addTrain");
		buttonAdd.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				/*TODO : fenêtre popup demandant à l'utilisateur le parcours et vitesse qu'il veut pour le train.
				 * 	(=> besoin de la liste des roadmap pour les proposer).
				 */
				
				int speed = 100;
				try {
					speed = (int) ConfigurationEnvironment.getInstance().getProperty("train_speed_max").getValue();
				} catch (PropertyNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
		
		Slider changeSpeed = (Slider) root.lookup("#changeSpeed");
		changeSpeed.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				GodModeController.getInstance().changeSimulationSpeed((int) changeSpeed.getValue());
			}
		});
		changeSpeed.setValue(0);
		
		//Boutton d'affichage de la vue de la gares (Boutton "Horaires")
		Button stationViewButton = (Button) root.lookup("#StationViewButton");
		stationViewButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				new StationView(stationList.getSelectionModel().getSelectedItem());
			}
		});
		
		//Bouton d'affichage de la vue conducteur (bouton "vue conducteur")
		Button DriverViewButton = (Button) root.lookup("#DriverViewButton");
		DriverViewButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				new DriverView();
			}
		});
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

}
