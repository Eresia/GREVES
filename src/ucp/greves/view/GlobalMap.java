package ucp.greves.view;

import java.awt.Dimension;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ucp.greves.controller.GodModeController;

public class GlobalMap extends Application{
	
	private LineView line;
	private ArrayList<LineView> list;
	
	public GlobalMap(ArrayList<LineView> list){
		this.line = new LineView(true);
		this.list = list;
		this.list.add(line);
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
		Dimension dimension = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int height;
		if(dimension.getHeight() < 600){
			height = (int) dimension.getHeight();
		}
		else{
			height = 600;
		}
		Scene scene = new Scene(root, dimension.getWidth()-20, height);
		primaryStage.setTitle("G.R.E.V.E.S. - Map globale ligne A");

		ScrollPane globalPane = (ScrollPane) root.lookup("#GlobalMap");	
		globalPane.setContent(line);
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent event) {
				list.remove(line);		
			}
		});
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
