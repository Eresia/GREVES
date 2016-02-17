package ucp.greves.controller;

import java.util.HashMap;

import ucp.greves.model.configuration.Registry;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.station.Station;
import ucp.greves.model.train.Train;

public class GodModeController {
/*private static final int TRAIN_SPEED_VARIATION = 3;

private static final int TRAIN_BASIC_SPEED = 2;

private int currentTime = 0;
private static final int SIMULATION_DURATION = 1000;

public static final int TIME_UNIT = 50;*/
	
	public static HashMap<Integer, Canton> listOfCantons(){
		return Registry.getCantonsRegistry();
	}
	
	public static HashMap<Integer, Train> listOfTrains(){
		return Registry.getTrainRegistry();
	}
	
	public static HashMap<Integer, Station> listOfStation(){
		return Registry.getStationRegistry();
	}

}
