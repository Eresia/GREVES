package ucp.greves.data.line.station;

import java.util.ArrayList;
import java.util.HashMap;

import ucp.greves.data.exceptions.PropertyNotFoundException;
import ucp.greves.data.exceptions.canton.CantonHasAlreadyStationException;
import ucp.greves.data.exceptions.canton.CantonIsBlockedException;
import ucp.greves.data.exceptions.canton.CantonNotExistException;
import ucp.greves.data.exceptions.canton.TerminusException;
import ucp.greves.data.exceptions.station.StationNotFoundException;
import ucp.greves.data.line.canton.Canton;
import ucp.greves.data.line.roadMap.RoadMap;
import ucp.greves.data.time.Time;
import ucp.greves.data.time.TimeDecorator;
import ucp.greves.data.time.UndefinedTime;
import ucp.greves.data.train.Train;
import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.configuration.ConfigurationEnvironmentElement;
import ucp.greves.model.line.Line;
import ucp.greves.model.schedule.Clock;
import ucp.greves.model.simulation.SimulationInfo;
import ucp.greves.model.train.ModifiedTrainInformation;

/**
 * The station is defined by its name & the id of its canton
 *
 */
public class Station {
	
	private final static int WAIT_TIME_DEFAULT = 20;
	private final static int WAIT_TIME_CONFIG= set_wait_time_default();
	
	private String name;
	private int canton;
	private int waitTime;
	private HashMap<Integer, Integer> nextStation;
	private volatile HashMap<Integer, TimeDecorator> nextTrains;
	
	private final static Object keyNextTrains = new Object();
	private final static Object keyNextStations = new Object();
	
	/**
	 * Creates a station and registers it
	 * 
	 * @param canton
	 * 		(Integer) The ID of the {@link Canton} where the station is
	 * @param name
	 * 		(String) The name of the station
	 * @param waitTime
	 * 		(Integer) The time for the train to wait in the station
	 * 
	 * @throws CantonHasAlreadyStationException is there is already a station in this canton
	 * @throws CantonNotExistException if the canton doesn't exist
	 * 
	 * @see Line#register_station
	 */
	public Station(int canton, String name, int waitTime) throws CantonHasAlreadyStationException, CantonNotExistException{
		this.name = name;
		Line.register_station(canton, this);
		this.canton = canton;
		this.waitTime = waitTime;
		nextStation = new HashMap<Integer, Integer>();
		nextTrains = new HashMap<Integer, TimeDecorator>();
	}
	
	/**
	 * Creates a station
	 * 
	 * @param canton
	 * 		(Integer) The ID of the {@link Canton}
	 * @param name
	 * 		(String) The name of the station
	 * 
	 * @throws CantonHasAlreadyStationException is there is already a station in this canton
	 * @throws CantonNotExistException if the canton doesn't exist
	 */
	protected Station(int canton, String name) throws CantonHasAlreadyStationException, CantonNotExistException{
		this(canton, name, -1);
	}
	
	/**
	 * Creates a station
	 * 
	 * @param name
	 * 		(String) The name of the station
	 */
	protected Station(String name){
		this.name = name;
		this.waitTime = -1;
		canton = -1;
	}
	
	/**
	 * Adds the next station for the train to stop to
	 * 
	 * @param railWay
	 * 		(Integer) The id of the {@link ucp.greves.data.line.railWay.RailWay} where the next station is 
	 * @param station
	 * 		(Integer) The id of the {@link Canton} where the next station is
	 */
	public void addNextStation(int railWay, int station){
		nextStation.put(railWay, station);
	}
	
	/**
	 * Changes the time when the next train arrives
	 * 
	 * @param train
	 * 		(Train) The next train to arrive
	 * @param time
	 * 		(Time) The time of the next train to arrive
	 */
	private synchronized void changeTimeOfNextTrain(Train train, Time time){
		RoadMap map = train.getRoadMap();
		
		HashMap<Integer, Integer> nextStation;
		
		synchronized (keyNextTrains) {
			this.nextTrains.put(train.getTrainID(), time);
		}
		
		synchronized (keyNextStations) {
			nextStation = (HashMap<Integer, Integer>) this.nextStation.clone();
		}
		
		for(Integer rw : nextStation.keySet()){
			
			if(map.getRailwaysIDs().contains(rw)){
				Station next = Line.getStations().get(nextStation.get(rw));
				Time nextTime = time.clone();
				if(map.cross(canton)){
					nextTime.addSeconds(Clock.getNbFrame(waitTime));
				}
				try{
					nextTime.addTime(timeToNextStation(rw, nextStation.get(rw)));
					next.changeTimeOfNextTrain(train, nextTime);
				} catch(CantonIsBlockedException e){
					next.undefinedTimeOfNextTrain(train);
				}
			}
		}
	}
	
	/**
	 * Sets the time of arrival of the next trains to undefined
	 * 
	 * @param train
	 * 		(Train) The first train to change its ETA to undefined
	 */
	private synchronized void undefinedTimeOfNextTrain(Train train){
		RoadMap map = train.getRoadMap();
		
		HashMap<Integer, Integer> nextStation;
		
		synchronized (keyNextTrains) {
			this.nextTrains.put(train.getTrainID(), new UndefinedTime());
		}
		
		synchronized (keyNextStations) {
			nextStation = (HashMap<Integer, Integer>) this.nextStation.clone();
		}
		
		for(Integer rw : nextStation.keySet()){
			if(map.getRailwaysIDs().contains(rw)){
				Station next = Line.getStations().get(nextStation.get(rw));
				next.undefinedTimeOfNextTrain(train);
			}
		}
	}
	
	/**
	 * Gets the time to arrive to the next station
	 * 
	 * @param rw
	 * 		(Integer) The railway of the nextStation
	 * @return 
	 * 		(Time) The time to arrive to the next station
	 * @throws CantonIsBlockedException if the canton is blocked
	 */
	private synchronized Time timeToNextStation(int rw, int nextStation) throws CantonIsBlockedException{
		
		Canton canton = Line.getCantons().get(this.canton);
		try {
			int position = canton.getStartPoint() - canton.getStationPosition();
		
			return timeToNextStation(this.canton, position, nextStation, rw);
		} catch (StationNotFoundException | CantonNotExistException e) {
			e.printStackTrace();
			return new Time();
		}
	}
	
	/**
	 * Gets the time to arrive to the next station
	 * 
	 * @param canton
	 * 		(Integer) Id of actual canton
	 * @param positionOnRailWay
	 * 		(Integer) Start position
	 * @param nextStation
	 * 		(Integer) Canton id of the next station
	 * @param nextStationRailWay
	 * 		(Integer) The current railway
	 * @return 
	 * 		(Time) The time to arrive to the next station
	 * @throws CantonIsBlockedException if the canton is blocked
	 */
	public static Time timeToNextStation(int canton, int positionOnRailWay, int nextStation, int nextStationRailWay) throws CantonNotExistException, StationNotFoundException, CantonIsBlockedException{
		Time result = null;
		Canton c = Line.getCantons().get(canton);
		if(c == null){
			throw new CantonNotExistException();
		}
		
		Station s = Line.getStations().get(nextStation);
		if(s == null){
			throw new StationNotFoundException();
		}
		
		int nbFrame = 0;
		int position = positionOnRailWay;
		
		try {
			while(true){
				if (position - c.getTrainSpeed(position) <= c.getEndPoint()) {
					c = c.getNextCanton(nextStationRailWay);
					position = c.simulateEnter();
					nbFrame++;
				} 
				
				
				ModifiedTrainInformation info = c.updatedTrainPosition(position, true);
				nbFrame++;
				
				if(info.getStationCrossed()){
					if(c.getStation().getCanton() == nextStation){
						break;
					}
					else{
						nbFrame += Clock.getNbFrame(c.getStation().getWaitTime());
					}
				}
				position -= info.getUpdatedPosition();
			}
		} catch (TerminusException e) {
			return new Time();
		}
		
		int nbSecondsByFrame =  Clock.nbSecondByFrame();
		int seconds = nbSecondsByFrame * nbFrame;
		result = new Time(0, 0, seconds);
		return result;
	}
	
	/**
	 * Makes a train enter the station
	 * 
	 * @param train
	 * 		(Train) The train which enters the station
	 */
	public void enter(Train train){
		try {
			changeTimeOfNextTrain(train, new Time());
			waitInStation();
			nextTrains.remove(train.getTrainID());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return (HashMap<Integer, TimeDecorator>) Returns the next trains to arrive at the station
	 */
	public ArrayList<NextTrainInformations> getNextTrains(){
		return getNextTrains(nextTrains.size());
	}
	
	public synchronized ArrayList<NextTrainInformations> getNextTrains(int nb){
		ArrayList<NextTrainInformations> result = new ArrayList<NextTrainInformations>();
		
		HashMap<Integer, TimeDecorator> nextTrains;
		
		synchronized (keyNextTrains) {
			nextTrains = (HashMap<Integer, TimeDecorator>) this.nextTrains.clone();
		}
		
		ArrayList<Integer> keys = new ArrayList<Integer>(nextTrains.keySet());
		int nbMax;
		if(nextTrains.size() != 0){
			if(nb > nextTrains.size()-1){
				nbMax = nextTrains.size()-1;
			}
			else{
				nbMax = nb;
			}
			for(int i = 0; i < nextTrains.size(); i++){
				boolean isPlaced = false;
				TimeDecorator actualTime = nextTrains.get(keys.get(i));
				for(int j = 0; j < result.size(); j++){
					NextTrainInformations info = result.get(j);
					if(actualTime.isInferior(info.getTime())){
						result.add(j, new NextTrainInformations(keys.get(i), nextTrains.get(keys.get(i))));
						isPlaced = true;
						break;
					}
				}
				if(!isPlaced){
					result.add(new NextTrainInformations(keys.get(i), nextTrains.get(keys.get(i))));
				}
			}
			return new ArrayList<NextTrainInformations>(result.subList(0, nbMax));
		}
		else{
			return new ArrayList<NextTrainInformations>();
		}
	}
	
	/**
	 * @return (String) Returns the name of the station
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * @return (Integer) Returns the time for the train to wait at the station
	 */
	public int getWaitTime(){
		return waitTime;
	}
	
	/**
	 * Calls {@link Station#waitInStation(int)} with (waitTime)
	 * @throws InterruptedException if the wait is interrupted
	 */
	public void waitInStation() throws InterruptedException{
		waitInStation(waitTime);
	}
	
	/**
	 * Makes the train wait in the station
	 * 
	 * @param specialTime
	 * 		(Integer) The time to wait
	 * @throws InterruptedException if the wait is interrupted
	 */
	public void waitInStation(int specialTime) throws InterruptedException{
		int nbFrame = Clock.getNbFrame(specialTime);
		for(int i = 0; i < nbFrame; i++){
			SimulationInfo.waitFrameTime();
		}
	}
	
	/**
	 * Sets the default wait time
	 * @return (Integer) The default wait time
	 */
	private static int set_wait_time_default() {
		int w = WAIT_TIME_DEFAULT;
		try {
			ConfigurationEnvironmentElement waitElement = ConfigurationEnvironment.getInstance()
					.getProperty("station_wait_time");
			if (!waitElement.getType().equals(Integer.class)) {
				System.err.println(
						"Station wait time has not the right type, default value " + WAIT_TIME_DEFAULT + " used");
			} else {
				w = (Integer) waitElement.getValue();
			}
		} catch (PropertyNotFoundException e) {
			System.err.println("Station wait time not defined, default value " + WAIT_TIME_DEFAULT + " used");
		}
		return w;
	}
	
	/**
	 * @return (Integer) The wait time defined if {@link ucp.greves.model.configuration.ConfigurationEnvironment}
	 */
	public static int getWaitTimeConfig(){
		return WAIT_TIME_CONFIG;
	}	
	
	public int getCanton(){
		return canton;
	}

}
