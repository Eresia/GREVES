package ucp.greves.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DriverView extends Application{

	public DriverView() {
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
		Parent root = FXMLLoader.load(getClass().getResource("driver_view.fxml"));
		Scene scene = new Scene(root,600,400);
		primaryStage.setTitle("G.R.E.V.E.S. - Vue conducteur");
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

}
