package ucp.greves.controller;

import java.util.ArrayList;

import ucp.greves.data.train.Train;
import ucp.greves.model.line.Line;

public class TrainController {

	public static ArrayList<Integer> integerListOfRunningTrainsID(){
		return new ArrayList<Integer>(Line.getTrains().keySet());
	}
	
	public static Train getRunningTrainById(int id){
		return Line.getTrains().get(id);
	}
	
	public static ArrayList<Integer> integerListOfArrivedTrainsID(){
		return new ArrayList<Integer>(Line.getArrivedTrains().keySet());
	}
	
	public static Train getArrivedTrainById(int id){
		return Line.getArrivedTrains().get(id);
	}
	
}
