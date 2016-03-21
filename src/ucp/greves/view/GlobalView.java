package ucp.greves.view;

import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.stage.Stage;
import ucp.greves.model.ControlLine;
import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.exceptions.BadControlInformationException;
import ucp.greves.model.exceptions.PropertyNotFoundException;
import ucp.greves.model.exceptions.railway.RailWayNotExistException;
import ucp.greves.model.exceptions.roadmap.BadRoadMapException;
import ucp.greves.model.line.Line;

public class GlobalView extends Application{
	
	IntegerProperty paneWidth, paneHeight;
	
	public static void main(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("global_view.fxml"));
		Scene scene = new Scene(root,800,600);
		primaryStage.setTitle("G.R.E.V.E.S. - Vue globale");

		this.paneHeight = new SimpleIntegerProperty();
		this.paneWidth = new SimpleIntegerProperty();

		ScrollPane lineDraw = (ScrollPane) root.lookup("#lineDraw"); //Get the borderPane from the root
		setButton(root);
	      
		primaryStage.setScene(scene);
		primaryStage.show();  

		// Décommenter ces 2 lignes pour utiliser le json
		ConfigurationEnvironment.getInstance().setProperty("BUILD_CONFIGURATION", "JSON");
		Line.getInstance();
  
		modify(lineDraw);
		//addStation(root);
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
				String roadmapName = "Line A";
				
				try {
					ControlLine.getInstance().launchTrain(roadmapName, speed);
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
		
		//Button buttonRemove = (Button) root.lookup("#buttonRemove");
		//buttonRemove.setOnAction(new EventHandler<ActionEvent>() {
			
			//@Override
			/*public void handle(ActionEvent event) {
				
				/* TODO
				 * Faire en sorte que "train" prenne la valeur du train sélectionné par l'utilisateur, afin de pouvoir le supprimer
				 *
				
				//train = 
				
				//ControlLine.getInstance().removeTrain(train);
			}
		});*/
		
		//Button buttonSlow = (Button) root.lookup("#buttonSlow");
		//buttonSlow.setOnAction(new EventHandler<ActionEvent>() {
			
			//@Override
			/*public void handle(ActionEvent event) {
				System.out.println("buttonSlow");	//Action to do			
			}
		});*/
		
		//Button buttonBlock = (Button) root.lookup("#buttonBlock");
		//buttonBlock.setOnAction(new EventHandler<ActionEvent>() {
			
			/*@Override
			public void handle(ActionEvent event) {
				System.out.println("buttonBlock");	//Action to do
			}
		});*/
		
		Slider changeSpeed = (Slider) root.lookup("#changeSpeed");
		changeSpeed.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				ControlLine.getInstance().changeSimulationSpeed((int) changeSpeed.getValue());
			}
		});
		changeSpeed.setValue(0);
		
		//Boutton d'affichage de la vue de la gares (Boutton "Horaires")
		Button stationViewButton = (Button) root.lookup("#StationViewButton");
		stationViewButton.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				new StationView();
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

}
