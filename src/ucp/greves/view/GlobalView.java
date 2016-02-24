package ucp.greves.view;

import javafx.application.Application;
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
import ucp.greves.controller.GodModeController;
import ucp.greves.model.ControlLine;

public class GlobalView extends Application{
	
	public static void main(String[] args){
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
	      Parent root = FXMLLoader.load(getClass().getResource("global_view.fxml"));
	      Scene scene = new Scene(root,800,600);
	      primaryStage.setTitle("G.R.E.V.E.S.");
	      
	      ScrollPane lineDraw = (ScrollPane) root.lookup("#lineDraw"); //Get the borderPane from the root
	      modify(lineDraw);
	      setButton(root);
	      
	      primaryStage.setScene(scene);
	      primaryStage.show();
	     
	}
	
	public void modify(ScrollPane pane){
	    LineView lv = new LineView();
	    pane.setContent(lv);
	}
	
	public void setButton(Parent root){
		Button buttonAdd = (Button) root.lookup("#buttonAdd");
		buttonAdd.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				System.out.println("buttonAdd");	//Action to do	
			}
		});
		
		Button buttonRemove = (Button) root.lookup("#buttonRemove");
		buttonRemove.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				System.out.println("buttonRemove");		//Action to do		
			}
		});
		
		Button buttonSlow = (Button) root.lookup("#buttonSlow");
		buttonSlow.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				System.out.println("buttonSlow");	//Action to do			
			}
		});
		
		Button buttonBlock = (Button) root.lookup("#buttonBlock");
		buttonBlock.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				System.out.println("buttonBlock");	//Action to do
			}
		});
		
		Slider changeSpeed = (Slider) root.lookup("#changeSpeed");
		changeSpeed.valueProperty().addListener(new ChangeListener<Object>() {

			@Override
			public void changed(ObservableValue<?> observable, Object oldValue, Object newValue) {
				ControlLine.getInstance().changeSimulationSpeed((int) changeSpeed.getValue());
			}
		});
		changeSpeed.setValue(0);
	}

}
