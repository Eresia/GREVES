package ucp.greves.controller;

import java.util.ArrayList;
import java.util.HashMap;

import ucp.greves.data.line.roadMap.RoadMap;
import ucp.greves.data.train.Train;
import ucp.greves.model.exceptions.BadControlInformationException;
import ucp.greves.model.exceptions.railway.RailWayNotExistException;
import ucp.greves.model.exceptions.roadmap.BadRoadMapException;
import ucp.greves.model.exceptions.roadmap.EmptyRoadMapException;
import ucp.greves.model.exceptions.roadmap.RoadMapNameNotExistException;
import ucp.greves.model.exceptions.train.TrainNotExistException;
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
	
	public static void launchTrain(String road)	throws BadControlInformationException, BadRoadMapException, RailWayNotExistException {
		verifyInformation();
		HashMap<String, RoadMap> roads = Line.getRoadMaps();
		if (!roads.containsKey(road)) {
			throw new RoadMapNameNotExistException("Impossible to launch the train - the road don't exist");
		}
		ArrayList<Integer> rails = roads.get(road).getRailwaysIDs();
		if (rails.size() == 0) {
			throw new EmptyRoadMapException("Impossible to launch the train - the road is empty");
		}
		if (Line.getRailWays().get(rails.get(0)) == null) {
			throw new RailWayNotExistException("Impossible to launch the train - the rail way don't exist");
		}
		Train t = new Train(Line.getRailWays().get(rails.get(0)).getFirstCanton(), roads.get(road));
		Thread tThread = new Thread(t);
		tThread.start();
	}
	
	public static void removeTrain(int train) {
		Line.removeTrain(train);
	}
	
	private static void verifyInformation() throws BadControlInformationException {
		if (Line.getRailWays().size() == 0) {
			throw new BadControlInformationException("No RailWay");
		}
		if (Line.getCantons().size() == 0) {
			throw new BadControlInformationException("No Canton");
		}
		if (Line.getRoadMaps().size() == 0) {
			throw new BadControlInformationException("No road");
		}
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
