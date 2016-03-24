package ucp.greves.controller;

import java.util.ArrayList;

import ucp.greves.model.line.Line;
import ucp.greves.model.train.Train;

public class TrainController {

	public static ArrayList<Integer> IntegerlistOfTrainsID(){
		return new ArrayList<Integer>(Line.getTrains().keySet());
	}
	
	public static Train getTrainById(int id){
		return Line.getTrains().get(id);
	}
	
}
