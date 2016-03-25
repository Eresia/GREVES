package ucp.greves.model.line.station;

import java.util.HashMap;

import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.configuration.ConfigurationEnvironmentElement;
import ucp.greves.model.exceptions.PropertyNotFoundException;
import ucp.greves.model.exceptions.canton.CantonHasAlreadyStationException;
import ucp.greves.model.exceptions.canton.CantonNotExistException;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.RoadMap;
import ucp.greves.model.schedule.Clock;
import ucp.greves.model.schedule.Time;
import ucp.greves.model.simulation.SimulationSpeed;
import ucp.greves.model.train.Train;

public class Station {
	
	private final static int WAIT_TIME_DEFAULT = 20;
	private final static int WAIT_TIME_CONFIG= set_wait_time_default();
	
	private String name;
	private int canton;
	private int waitTime;
	private HashMap<Integer, Integer> nextStation;
	private HashMap<Integer, Time> nextTrains;
	
	public Station(int canton, String name, int waitTime) throws CantonHasAlreadyStationException, CantonNotExistException{
		this(name);
		Line.register_station(canton, this);
		this.canton = canton;
		this.waitTime = waitTime;
	}
	
	protected Station(int canton, String name) throws CantonHasAlreadyStationException, CantonNotExistException{
		this(canton, name, -1);
	}
	
	protected Station(String name){
		this.name = name;
		this.waitTime = -1;
	}
	
	public void addNextStation(int railWay, int station, Time timeToTravel){
		nextStation.put(railWay, station);
	}
	
	public void changeTimeOfNextTrain(Train train, Time time){
		RoadMap map = train.getRoadMap();
		nextTrains.put(train.getTrainID(), time);
		for(Integer rw : nextStation.keySet()){
			if(map.getRailwaysIDs().contains(rw)){
				Station next = Line.getStations().get(nextStation.get(rw));
				Time nextTime = time.clone();
				if(map.cross(canton)){
					Time wait = new Time();
					wait.addSeconds(waitTime*Clock.nbSecondByFrame());
					nextTime.addTime(wait);
				}
				nextTime.addTime(timeToNextStation(rw));
				next.changeTimeOfNextTrain(train, nextTime);
			}
		}
	}
	
	private Time timeToNextStation(int rw){
		Time time = new Time();
		
		return time;
	}
	
	public void enter(Train train){
		try {
			changeTimeOfNextTrain(train, new Time());
			waitInStation();
			nextTrains.remove(train.getTrainID());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public HashMap<Integer, Time> getNextTrains(){
		return nextTrains;
	}
	
	public String getName(){
		return name;
	}
	
	public int getWaitTime(){
		return waitTime;
	}
	
	public void waitInStation() throws InterruptedException{
		waitInStation(waitTime);
	}
	
	public void waitInStation(int specialTime) throws InterruptedException{
		for(int i = 0; i < specialTime; i++){
			SimulationSpeed.waitFrameTime();
		}
	}
	
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
	
	public static int getWaitTimeConfig(){
		return WAIT_TIME_CONFIG;
	}

}
