package ucp.greves.model.line;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import sun.security.jca.GetInstance;
import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.exceptions.canton.CantonHasAlreadyStationException;
import ucp.greves.model.exceptions.canton.CantonNotExistException;
import ucp.greves.model.exceptions.railway.DoubledRailwayException;
import ucp.greves.model.exceptions.line.InvalidXMLException;
import ucp.greves.model.line.builder.LineBuilder;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.station.Station;
import ucp.greves.model.train.Train;

public class Line extends Observable implements Observer {
	private static Line instance;
	private  int canton_id_register = 0 ;
	private  int train_id_register = 0;
	private  HashMap<Integer, RailWay> railway_registry;
	private  HashMap<Integer, Canton> canton_registry;
	private  HashMap<Integer, Train> train_registry;
	private  HashMap<Integer, Station> station_registry;
	
	private Line(){
		canton_id_register = 0 ;
		train_id_register = 0;
		railway_registry = new HashMap<Integer, RailWay>();
		canton_registry = new HashMap<Integer, Canton>();
		train_registry = new HashMap<Integer, Train>();
		station_registry = new HashMap<Integer, Station>();
	}
	public static Line getInstance(){
		if(instance == null){
			instance = new Line();
			try {
				LineBuilder.buildLine(ConfigurationEnvironment.getInstance());
			} catch (DoubledRailwayException | CantonHasAlreadyStationException
					| CantonNotExistException | InvalidXMLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return instance ; 
	}
	
	public synchronized static void register_railway(RailWay railway) throws DoubledRailwayException{
		if(instance.railway_registry.containsKey(railway.getId())){
			throw new DoubledRailwayException("RailWay " + railway.getId() + " already exist");
		}
		instance.railway_registry.put(railway.getId(), railway);
	}
	
	public synchronized static int register_canton(Canton canton){
		getInstance();
		canton.addObserver(instance);
		instance.canton_id_register++;
		instance.canton_registry.put(instance.canton_id_register, canton);
		return instance.canton_id_register;	
	}
	
	public synchronized static int register_train(Train train) {
		getInstance();
		train.addObserver(instance);
		instance.train_id_register++;
		instance.train_registry.put(instance.train_id_register, train);
		return instance.train_id_register;
	}
	
	public synchronized static void register_station(int id, Station station) throws CantonHasAlreadyStationException, CantonNotExistException{
		getInstance();
		if(!instance.canton_registry.containsKey(id)){
			throw new CantonNotExistException("Canton " + id + " already exist");
		}
		if(instance.station_registry.containsKey(id)){
			throw new CantonHasAlreadyStationException("Canton " + id + " has already the station " + instance.station_registry.get(id).getName());
		}
		instance.station_registry.put(id, station);
	}
	
	public int getTotalLenght() {
		getInstance();
		int length = 0;
		for(Integer rkey : railway_registry.keySet()){
			length += railway_registry.get(rkey).getLength();
		}
		return length;
	}
	
	public static HashMap<Integer, RailWay> getRailWays(){
		return instance.railway_registry;
	}
	
	public static HashMap<Integer, Canton> getCantons(){
		return instance.canton_registry;
	}
	
	public static HashMap<Integer, Train> getTrains(){
		return instance.train_registry;
	}
	
	public static HashMap<Integer, Station> getStations(){
		return instance.station_registry;
	}
	@Override
	public void update(Observable o, Object arg) {
		if(ConfigurationEnvironment.inDebug() == true){
			if(o instanceof Canton)
			System.out.println("MAJ du Modele canton "+((Canton)o).getId()+" modified");
			
			if(o instanceof Train)
			System.out.println("MAJ du Modele train "+((Train)o).getTrainID()+"modified");
		}
		notifyObservers();
	}
}
