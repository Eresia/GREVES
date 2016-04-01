package ucp.greves.controller;

import java.util.ArrayList;

import ucp.greves.data.exceptions.BadControlInformationException;
import ucp.greves.data.exceptions.railway.RailWayNotExistException;
import ucp.greves.data.exceptions.roadmap.BadRoadMapException;
import ucp.greves.data.exceptions.train.TrainNotExistException;
import ucp.greves.data.train.Train;
import ucp.greves.model.line.Line;

/**
 * Controller of the trains
 *
 */
public class TrainController {

	/**
	 * Return Returns the list of running trains id
	 * @return 
	 * 		(ArrayList<Integer>) the list of running trains id
	 */
	public static ArrayList<Integer> integerListOfRunningTrainsID(){
		return new ArrayList<Integer>(Line.getTrains().keySet());
	}
	
	/**
	 * Return a running train by its id
	 * @param id 
	 * 		(Integer)The running train id
	 * @return
	 * 		(Station) The running train with id given
	 */
	public static Train getRunningTrainById(int id) throws TrainNotExistException{
		Train  t;
		if(( t  = Line.getTrains().get(id)) == null){
			throw new TrainNotExistException();
		}
		return t;
	}
	
	/**
	 * Return Returns the list of arrived trains id
	 * @return 
	 * 		(ArrayList<Integer>) the list of arrived trains id
	 */
	public static ArrayList<Integer> integerListOfArrivedTrainsID(){
		return new ArrayList<Integer>(Line.getArrivedTrains().keySet());
	}
	
	/**
	 * Return a arrived train by its id
	 * @param id 
	 * 		(Integer)The arrived train id
	 * @return
	 * 		(Station) The arrived train with id given
	 */
	public static Train getArrivedTrainById(int id) throws TrainNotExistException{
		Train  t;
		if(( t  = Line.getArrivedTrains().get(id)) == null){
			throw new TrainNotExistException();
		}
		return t;

	}
	
	/***
	 * Launch a train to a road map
	 * @param road
	 * 		(String) the road map name
	 * @throws BadControlInformationException If ligne is bas setup
	 * @throws BadRoadMapException If the road map not exist
	 * @throws RailWayNotExistException If the railway is not exist
	 */
	public static void launchTrain(String road)	throws BadControlInformationException, BadRoadMapException, RailWayNotExistException {
		Train.launchTrain(road);
	}
	
	/**
	 * Remove a train
	 * @param train
	 * 		(Integer) Id of the trains
	 */
	public static void removeTrain(int train) {
		Line.removeTrain(train);
	}
	
	/**
	 * Block a train
	 * @param train
	 * 		(Integer) The train id
	 * @throws TrainNotExistException If the train not exist
	 */
	public static void blockTrain(int train) throws TrainNotExistException{
		if(!Line.getTrains().containsKey(train)){
			throw new TrainNotExistException("Train " + train + " not exist");
		}
		Line.getTrains().get(train).blockTrain();
	}
	
	/**
	 * Unblock a train
	 * @param train
	 * 		(Integer) The train id
	 * @throws TrainNotExistException If the train not exist
	 */
	public static void unblockTrain(int train) throws TrainNotExistException{
		if(!Line.getTrains().containsKey(train)){
			throw new TrainNotExistException("Train " + train + " not exist");
		}
		Line.getTrains().get(train).unblockTrain();
	}
	
}
