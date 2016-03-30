package ucp.greves.controller;

import java.util.ArrayList;
import java.util.HashMap;

import ucp.greves.data.exceptions.BadControlInformationException;
import ucp.greves.data.exceptions.railway.RailWayNotExistException;
import ucp.greves.data.exceptions.roadmap.BadRoadMapException;
import ucp.greves.data.exceptions.roadmap.EmptyRoadMapException;
import ucp.greves.data.exceptions.roadmap.RoadMapNameNotExistException;
import ucp.greves.data.exceptions.train.TrainNotExistException;
import ucp.greves.data.line.roadMap.RoadMap;
import ucp.greves.data.train.Train;
import ucp.greves.model.line.Line;

public class TrainController {

	public static ArrayList<Integer> integerListOfRunningTrainsID(){
		return new ArrayList<Integer>(Line.getTrains().keySet());
	}
	
	public static Train getRunningTrainById(int id) throws TrainNotExistException{
		Train  t;
		if(( t  = Line.getTrains().get(id)) == null){
			throw new TrainNotExistException();
		}
		return t;
	}
	
	public static ArrayList<Integer> integerListOfArrivedTrainsID(){
		return new ArrayList<Integer>(Line.getArrivedTrains().keySet());
	}
	
	public static Train getArrivedTrainById(int id) throws TrainNotExistException{
		Train  t;
		if(( t  = Line.getArrivedTrains().get(id)) == null){
			throw new TrainNotExistException();
		}
		return t;

	}
	
	public static void launchTrain(String road)	throws BadControlInformationException, BadRoadMapException, RailWayNotExistException {
		Train.launchTrain(road);
	}
	
	public static void removeTrain(int train) {
		Line.removeTrain(train);
	}
	
	public static void blockTrain(int train) throws TrainNotExistException{
		if(!Line.getTrains().containsKey(train)){
			throw new TrainNotExistException("Train " + train + " not exist");
		}
		Line.getTrains().get(train).blockTrain();
	}
	
	public static void unblockTrain(int train) throws TrainNotExistException{
		if(!Line.getTrains().containsKey(train)){
			throw new TrainNotExistException("Train " + train + " not exist");
		}
		Line.getTrains().get(train).unblockTrain();
	}
	
}
