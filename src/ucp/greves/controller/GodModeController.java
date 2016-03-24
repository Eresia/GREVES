package ucp.greves.controller;

import java.util.HashMap;

import ucp.greves.model.line.Line;
import ucp.greves.model.line.RailWay;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.station.Station;
import ucp.greves.model.train.Train;

@Deprecated //See others controller
public class GodModeController {
	
	@Deprecated //see RailWayController
	public static HashMap<Integer, RailWay> listOfRailWays(){
		return Line.getRailWays();
	}
	
	@Deprecated //see CantonController
	public static HashMap<Integer, Canton> listOfCantons(){
		return Line.getCantons();
	}
	
	@Deprecated //see TrainController
	public static HashMap<Integer, Train> listOfTrains(){
		return Line.getTrains();
	}
	
	@Deprecated //see StationController
	public static HashMap<Integer, Station> listOfStation(){
		return Line.getStations();
	}

}
