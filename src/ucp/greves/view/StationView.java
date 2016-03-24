/*
 * TODO : See javadoc
 */
package ucp.greves.view;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ucp.greves.model.line.station.Station;

public class StationView extends Application {
	
	private Station station;

	public StationView(Station station) {
		this.station = station;
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
		primaryStage.setTitle("G.R.E.V.E.S. - Vue de la gare : " + station.getName());
		setTableView(root);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	/**
	 * Get the Tableview which are supposed to contain the usefull informations and fill them
	 * 
	 * @param root
	 * 		 (Parent) The root of the scene where the TableView are placed
	 */
	public void setTableView(Parent root){
		TableView<Station> fisrtNextStation = (TableView<Station>) root.lookup("#nextStationFirstTable");
		TableColumn<Station, String> fisrtNextStationColumn = (TableColumn<Station, String>) fisrtNextStation.getColumns().get(0);
		fisrtNextStationColumn.setCellValueFactory(new PropertyValueFactory<Station,String>("name"));
		ObservableList<Station> stationListOne = FXCollections.observableArrayList();
		//Station testS = StationController.getStationById(StationController.IntegerlistOfStationsID().get(0));
		//stationListOne.add(testS);
		fisrtNextStation.setItems(stationListOne);
	}
}
