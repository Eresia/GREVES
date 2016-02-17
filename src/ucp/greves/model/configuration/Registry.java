package ucp.greves.model.configuration;

import java.util.HashMap;

import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.station.Station;
import ucp.greves.model.train.Train;

public class Registry {

	private static int canton_id_register = 0 ;
	private static int railway_id_register = 0 ;
	private static int train_id_register = 0;
	private static int station_id_register = 0;
	private static HashMap<Integer, Canton> canton_registry = new HashMap<Integer, Canton>();
	private static HashMap<Integer, Train> train_registry = new HashMap<Integer, Train>();
	private static HashMap<Integer, Station> station_registry = new HashMap<Integer, Station>();
	
	public synchronized static int register_canton(Canton canton){
		canton_id_register++;
		canton_registry.put(canton_id_register, canton);
		return canton_id_register;	
	}
	
	public synchronized static int register_railway(){
		railway_id_register++;
		return railway_id_register;
	}
	
	public synchronized static int register_train(Train train) {
		train_id_register++;
		train_registry.put(train_id_register, train);
		return train_id_register;
	}
	
	public synchronized static int register_station(Station station) {
		station_id_register++;
		station_registry.put(station_id_register, station);
		return station_id_register;
	}
	
	public static HashMap<Integer, Canton> getCantonsRegistry(){
		return canton_registry;
	}
	
	public static HashMap<Integer, Train> getTrainRegistry(){
		return train_registry;
	}
	
	public static HashMap<Integer, Station> getStationRegistry(){
		return station_registry;
	}
}
