package ucp.greves.view;

import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import ucp.greves.controller.RoadMapController;
import ucp.greves.controller.TrainController;
import ucp.greves.data.exceptions.BadControlInformationException;
import ucp.greves.data.exceptions.railway.RailWayNotExistException;
import ucp.greves.data.exceptions.roadmap.BadRoadMapException;

public class addTrainView extends Application{
	
	private ComboBox<String> roadmapList;
	
	public addTrainView() {
		Stage stage = new Stage();
		try {
			start(stage);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("addTrain_view.fxml"));
		Scene scene = new Scene(root,300,50);
		primaryStage.setTitle("G.R.E.V.E.S. - Ajouter un train");
		
		setComponents(root, primaryStage);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public void setComponents(Parent root, Stage primaryStage){
		
		Button buttonAdd = (Button) root.lookup("#AddTrain");
		buttonAdd.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				String roadmapName = roadmapList.getValue();
				
				if(roadmapName != null) {
					try {
						TrainController.launchTrain(roadmapName);
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
				//primaryStage.hide();
			}
		});
		
		roadmapList = (ComboBox<String>) root.lookup("#RoadMapsList");
		ArrayList<String> roadmapNameList = RoadMapController.StringlistOfRoadMapNames();
		
		for(int i = 0; i < roadmapNameList.size(); i++) {
			roadmapList.getItems().add(roadmapNameList.get(i));
		}
		
	}
}
