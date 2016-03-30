package ucp.greves.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class GlobalMap extends Application{
	
	private LineView line;
	
	public GlobalMap(){
		this.line = new LineView(true);
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
		Parent root = FXMLLoader.load(getClass().getResource("globalMap_view.fxml"));
		Scene scene = new Scene(root,800,600);
		primaryStage.setTitle("G.R.E.V.E.S. - Map globale ligne A");

		ScrollPane globalPane = (ScrollPane) root.lookup("#GlobalMap");	
		globalPane.setContent(line);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
