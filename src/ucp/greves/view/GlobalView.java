package ucp.greves.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GlobalView extends Application{
	
	public static void main(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
	      Parent root = FXMLLoader.load(getClass().getResource("global_view.fxml"));
	      Scene scene = new Scene(root,800,600);
	      primaryStage.setTitle("G.R.E.V.E.S.");
	      
	      BorderPane sandBox = (BorderPane) root.lookup("#sandBox"); //Get the borderPane from the root
	      modify(sandBox);
	     
	      primaryStage.setScene(scene);
	      primaryStage.show();
	     
	}
	
	public void modify(BorderPane pane){ //You can make test here
		Text text = new Text("COUCOU!!!"); //Just an example
	    pane.getChildren().add(text);
	    LineView lv = new LineView();
	    pane.getChildren().add(lv);
	}

}
