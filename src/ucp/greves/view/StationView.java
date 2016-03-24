/*
 * TODO : See javadoc
 */
package ucp.greves.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StationView extends Application {

	public StationView() {
		Stage stage = new Stage();
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
		primaryStage.setTitle("G.R.E.V.E.S. - Vue de la gare");
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
