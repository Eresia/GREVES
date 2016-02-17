package ucp.greves.model.line;

import java.util.HashMap;

import ucp.greves.model.exceptions.canton.CantonHasAlreadyStationException;
import ucp.greves.model.exceptions.canton.CantonNotExistException;
import ucp.greves.model.exceptions.railway.DoubledRailwayException;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.station.Station;
import ucp.greves.model.train.Train;

public class Line {

	private static int canton_id_register = 0 ;
	private static int train_id_register = 0;
	private static HashMap<Integer, RailWay> railway_registry = new HashMap<Integer, RailWay>();
	private static HashMap<Integer, Canton> canton_registry = new HashMap<Integer, Canton>();
	private static HashMap<Integer, Train> train_registry = new HashMap<Integer, Train>();
	private static HashMap<Integer, Station> station_registry = new HashMap<Integer, Station>();
	
	public synchronized static void register_railway(RailWay railway) throws DoubledRailwayException{
		if(railway_registry.containsKey(railway.getId())){
			throw new DoubledRailwayException("RailWay " + railway.getId() + " already exist");
		}
		railway_registry.put(railway.getId(), railway);
	}
	
	public synchronized static int register_canton(Canton canton){
		canton_id_register++;
		canton_registry.put(canton_id_register, canton);
		return canton_id_register;	
	}
	
	public synchronized static int register_train(Train train) {
		train_id_register++;
		train_registry.put(train_id_register, train);
		return train_id_register;
	}
	
	public synchronized static void register_station(int id, Station station) throws CantonHasAlreadyStationException, CantonNotExistException{
		if(!canton_registry.containsKey(id)){
			throw new CantonNotExistException("Canton " + id + " already exist");
		}
		if(station_registry.containsKey(id)){
			throw new CantonHasAlreadyStationException("Canton " + id + " has already the station " + station_registry.get(id).getName());
		}
		station_registry.put(id, station);
	}
	
	public int getTotalLenght() {
		int length = 0;
		for(Integer rkey : railway_registry.keySet()){
			length += railway_registry.get(rkey).getLength();
		}
		return length;
	}
	
	public static HashMap<Integer, RailWay> getRailWays(){
		return railway_registry;
	}
	
	public static HashMap<Integer, Canton> getCantons(){
		return canton_registry;
	}
	
	public static HashMap<Integer, Train> getTrains(){
		return train_registry;
	}
	
	public static HashMap<Integer, Station> getStations(){
		return station_registry;
	}
}
