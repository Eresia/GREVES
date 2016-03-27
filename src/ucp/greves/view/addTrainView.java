package ucp.greves.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class addTrainView extends Application{
	
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
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
