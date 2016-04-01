package ucp.greves.model.line;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import ucp.greves.data.exceptions.canton.CantonHasAlreadyStationException;
import ucp.greves.data.exceptions.canton.CantonNotExistException;
import ucp.greves.data.exceptions.line.InvalidXMLException;
import ucp.greves.data.exceptions.railway.DoubledRailwayException;
import ucp.greves.data.exceptions.roadmap.RoadMapAlreadyExistException;
import ucp.greves.data.line.canton.Canton;
import ucp.greves.data.line.railWay.RailWay;
import ucp.greves.data.line.roadMap.RoadMap;
import ucp.greves.data.line.station.DepositeryStation;
import ucp.greves.data.line.station.GlobalStation;
import ucp.greves.data.line.station.Station;
import ucp.greves.data.time.Time;
import ucp.greves.data.train.Train;
import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.line.builder.LineBuilder;
import ucp.greves.model.schedule.LaunchTrainInformation;
import ucp.greves.model.schedule.Schedule;

/**
 * Line is the class of this project which contains every data (trains, stations, cantons, railways, roadmaps, schedules)
 * 
 * It is a singleton class
 *
 */
public class Line extends Observable implements Observer {
	private static Line instance;
	private  int canton_id_register = 0 ;
	private  int train_id_register = 0;
	private  HashMap<Integer, RailWay> railway_registry;
	private  HashMap<Integer, Canton> canton_registry;
	private  HashMap<Integer, Train> train_registry;
	private	 HashMap<Integer, Train> arrived_train_registry;
	private  HashMap<Integer, Station> station_registry;
	private	 HashMap<String, GlobalStation> global_station_registry;
	private  HashMap<String, RoadMap> roadmap_registry;
	
	private Schedule schedule;
	private DepositeryStation stockRemoveTrain;
	
	/**
	 * Creates a Line and initializes every registries
	 */
	private Line(){
		canton_id_register = 0 ;
		train_id_register = 0;
		railway_registry = new HashMap<Integer, RailWay>();
		canton_registry = new HashMap<Integer, Canton>();
		train_registry = new HashMap<Integer, Train>();
		arrived_train_registry = new HashMap<Integer, Train>();
		station_registry = new HashMap<Integer, Station>();
		global_station_registry = new HashMap<String, GlobalStation>();
		roadmap_registry = new HashMap<String, RoadMap>();
		schedule = new Schedule();
		stockRemoveTrain = new DepositeryStation("Removed Train Stockage");
	}
	
	/**
	 * Builds the Line and gets its instance
	 * 
	 * @return (static Line) Returns the instance of the line
	 */
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
	
	/**
	 * Adds a railway to the registry
	 * 
	 * @param railway
	 * 		(RailWay) The railway to add
	 * @throws DoubledRailwayException if the railway is already added
	 */
	public synchronized static void register_railway(RailWay railway) throws DoubledRailwayException{
		if(instance.railway_registry.containsKey(railway.getId())){
			throw new DoubledRailwayException("RailWay " + railway.getId() + " already exist");
		}
		instance.railway_registry.put(railway.getId(), railway);
	}
	
	/**
	 * Adds a canton to the registry
	 * 
	 * @param canton
	 * 		(Canton) The canton to add
	 * @return (Integer) Returns the ID of the canton
	 */
	public synchronized static int register_canton(Canton canton){
		getInstance();
		canton.addObserver(instance);
		instance.canton_id_register++;
		instance.canton_registry.put(instance.canton_id_register, canton);
		return instance.canton_id_register;	
	}
	
	/**
	 * Adds a train to the registry
	 * 
	 * @param train
	 * 		(Train) The train to add
	 * @return (Integer) Returns the ID of the train
	 */
	public synchronized static int register_train(Train train) {
		getInstance();
		train.addObserver(instance);
		instance.train_id_register++;
		instance.train_registry.put(instance.train_id_register, train);
		return instance.train_id_register;
	}
	
	/**
	 * Switch a train from the train registry to the arrived trains registry
	 * 
	 * @param id
	 * 		(Integer) The ID of the train
	 */
	public synchronized static void register_arrived_train(int id) {
		getInstance();
		if(instance.train_registry.containsKey(id)){
			instance.arrived_train_registry.put(id, instance.train_registry.get(id));
			instance.train_registry.remove(id);
		}
	}
	
	/**
	 * Adds a station to the registry
	 * 
	 * @param id
	 * 		(Integer) The canton where the station is
	 * @param station
	 * 		(Station) The station to add to the registry
	 * @throws CantonHasAlreadyStationException if the canton already has a station
	 * @throws CantonNotExistException if the canton deosn't exist
	 */
	public synchronized static void register_station(int id, Station station) throws CantonHasAlreadyStationException, CantonNotExistException{
		getInstance();
		if(!instance.canton_registry.containsKey(id)){
			throw new CantonNotExistException("Canton " + id + " already exist");
		}
		if(instance.station_registry.containsKey(id)){
			throw new CantonHasAlreadyStationException("Canton " + id + " has already the station " + instance.station_registry.get(id).getName());
		}
		instance.station_registry.put(id, station);
		String name = station.getName();
		if(!instance.global_station_registry.containsKey(name)){
			instance.global_station_registry.put(name, new GlobalStation(name));
		}
		instance.global_station_registry.get(name).addStation(id);
	}
	
	/**
	 * Adds a roadmap to the registry
	 * 
	 * @param name
	 * 		(String) The name of the roadmap
	 * @param road
	 * 		(RoadMap) The roadmap to add
	 * @throws RoadMapAlreadyExistException if the roadmap already exists
	 */
	public synchronized static void register_roadMap(String name, RoadMap road) throws RoadMapAlreadyExistException {
		getInstance();
		if(instance.roadmap_registry.containsKey(name)){
			throw new RoadMapAlreadyExistException("RoadMap " + name + " already exist");
		}
		instance.roadmap_registry.put(name, road);
	}
	
	/**
	 * Programs a launch to the schedule
	 * @param roadMap
	 * 		(String) The name of the roadmap to follow
	 * @param time
	 * 		(Time) The time of departure
	 */
	public synchronized static void addLaunch(String roadMap, Time time){
		instance.schedule.addInformation(new LaunchTrainInformation(time, roadMap));
	}
	
	/**
	 * @return (Schedule) The schedule of departures
	 */
	public Schedule getSchedule(){
		return schedule;
	}
	
	/**
	 * @return (Integer) The total length of the line
	 */
	public int getTotalLenght() {
		getInstance();
		int length = 0;
		for(Integer rkey : railway_registry.keySet()){
			length += railway_registry.get(rkey).getLength();
		}
		return length;
	}
	
	/**
	 * @return (HashMap<Integer, RailWay>) Returns the list of railways
	 */
	public static HashMap<Integer, RailWay> getRailWays(){
		return instance.railway_registry;
	}
	
	/**
	 * Return the list of cantons
	 * @return (HashMap<Integer, Canton>) Returns the list of cantons
	 */
	public static HashMap<Integer, Canton> getCantons(){
		return instance.canton_registry;
	}
	
	/**
	 * @return (HashMap<Integer, Train>) Returns the list of train
	 */
	public static HashMap<Integer, Train> getTrains(){
		return instance.train_registry;
	}
	
	/**
	 * @return (HashMap<Integer, Train>) Returns the list of arrived trains
	 */
	public static HashMap<Integer, Train> getArrivedTrains(){
		return instance.arrived_train_registry;
	}
	
	/**
	 * @return (HashMap<Integer, Station>) Returns the list of stations
	 */
	public static HashMap<Integer, Station> getStations(){
		return instance.station_registry;
	}
	
	/**
	 * @return (HashMap<String, GlobalStation>) Returns the list of stations with different names
	 */
	public static HashMap<String, GlobalStation> getGlobalStations(){
		return instance.global_station_registry;
	}
	
	/**
	 * @return (HashMap<String, RoadMap>) Returns the list of roadmaps
	 */
	public static HashMap<String, RoadMap> getRoadMaps(){
		return instance.roadmap_registry;
	}
	
	/**
	 * Removes a train from the registry
	 * @param train
	 * 		(Integer) The ID of the train to remove
	 */
	public static void removeTrain(int train) {
		if(instance.train_registry.containsKey(train)) {
			instance.train_registry.get(train).remove(instance.stockRemoveTrain);
		}
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
