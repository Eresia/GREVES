package ucp.greves.model;

import java.util.ArrayList;
import java.util.HashMap;

import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.exceptions.BadControlInformationException;
import ucp.greves.model.exceptions.canton.CantonNotExistException;
import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.exceptions.railway.DoubledRailwayException;
import ucp.greves.model.exceptions.railway.PathNotExistException;
import ucp.greves.model.exceptions.railway.RailWayNotExistException;
import ucp.greves.model.exceptions.roadmap.BadRoadMapException;
import ucp.greves.model.exceptions.roadmap.EmptyRoadMapException;
import ucp.greves.model.exceptions.roadmap.RoadMapAlreadyExistException;
import ucp.greves.model.exceptions.roadmap.RoadMapHaveAlreadyStationException;
import ucp.greves.model.exceptions.roadmap.RoadMapNameNotExistException;
import ucp.greves.model.exceptions.station.StationNotFoundException;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.RailWay;
import ucp.greves.model.line.RoadMap;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.station.DepositeryStation;
import ucp.greves.model.line.station.Station;
import ucp.greves.model.simulation.SimulationSpeed;
import ucp.greves.model.train.Train;

public class ControlLine {
	
	private static ControlLine instance = new ControlLine();
	private HashMap<String, RoadMap> roads;
	private DepositeryStation stockRemoveTrain;
	
	private ControlLine(){
		roads = new HashMap<String, RoadMap>();
		stockRemoveTrain = new DepositeryStation("Removed Train Stockage");
	}
	
	public static ControlLine getInstance(){
		return instance;
	}
	
	public void changeSimulationSpeed(int duration){
		SimulationSpeed.changeSimulationSpeed(duration);
	}
	
	public RoadMap addRoad(String name, Station firstStation, Station lastStation) throws RoadMapAlreadyExistException, RailWayNotExistException, CantonNotExistException, PathNotExistException{
		if(roads.containsKey(name)){
			throw new RoadMapAlreadyExistException("RoadMap " + name + " already exist");
		}
		int firstRailWay = findRailWay(firstStation);
		int lastRailWay = findRailWay(lastStation);
		RailWay r = Line.getRailWays().get(firstRailWay);
		RoadMap road = new RoadMap(name);
		try{
			do{
				Canton canton = r.getFirstCanton();
				do{
					try{
					if(canton.hasStation()){
						road.addStation(canton.getStation().getName());
					}
						canton = canton.getNextCanton(null);
					} catch (RoadMapHaveAlreadyStationException | StationNotFoundException e) {
						if(ConfigurationEnvironment.inDebug()){
							System.err.println("Station not exist or already in road");
						}
					} catch(TerminusException e){
						break;
					}
				}while(true);
				road.addRailWay(r.getId());
				r = r.getTerminus().getNextRailWay();
			}while(r.getId() != lastRailWay);
			road.addRailWay(lastRailWay);
		} catch (DoubledRailwayException e){
			throw new PathNotExistException("Path beetween station " + firstStation.getName() + " and station " + lastStation.getName() + "don't exist (loop)");
		} catch (TerminusException e) {
			throw new PathNotExistException("Path beetween station " + firstStation.getName() + " and station " + lastStation.getName() + "don't exist (terminus)");
		}
		roads.put(name, road);
		return road;
	}
	
	public void launchTrain(String road, int speed) throws BadControlInformationException, BadRoadMapException, RailWayNotExistException{
		verifyInformation();
		if(!roads.containsKey(road)){
			throw new RoadMapNameNotExistException("Impossible to launch the train - the road don't exist");
		}
		ArrayList<Integer> rails = roads.get(road).getRailwaysIDs();
		if(rails.size() == 0){
			throw new EmptyRoadMapException("Impossible to launch the train - the road is empty");
		}
		if(Line.getRailWays().get(rails.get(0)) == null){
			throw new RailWayNotExistException("Impossible to launch the train - the rail way don't exist");
		}
		Train t = new Train(Line.getRailWays().get(rails.get(0)).getFirstCanton(), roads.get(road), speed);
		Thread tThread = new Thread(t);
		tThread.start();
	}
	
	public void removeTrain(int train){
		Line.getTrains().get(train).remove(stockRemoveTrain);
	}
	
	public void addRoad(String name, RoadMap road){
		roads.put(name, road);
	}
	
	public void removeRoad(String name){
		roads.remove(name);
	}
	
	public RoadMap getRoad(String name){
		return roads.get(name);
	}
	
	public int findRailWay(int canton) throws RailWayNotExistException{
		int railway = -1;
		
		for(Integer i : Line.getRailWays().keySet()){
			Canton c = Line.getRailWays().get(i).getFirstCanton();
			while((c != null) && (railway == -1)){
				try{
					if(c.getId() == canton){
						railway = i;
						break;
					}
					else{
						c = c.getNextCanton(null);
					}
				} catch(TerminusException e){
					break;
				}
			}
			if(railway != -1){
				break;
			}
		}
		if(railway == -1){
			throw new RailWayNotExistException("Canton " + canton + "is not in a railway");
		}
		return railway;
	}
	
	public int findRailWay(Station station) throws RailWayNotExistException, CantonNotExistException{
		return findRailWay(findCanton(station));
	}
	
	public int findCanton(Station station) throws CantonNotExistException{
		int canton = -1;
		if(!Line.getStations().containsValue(station)){
			throw new CantonNotExistException("Any canton contains the station " + station.getName());
		}
		for(Integer i : Line.getStations().keySet()){
			if(Line.getStations().get(i) == station){
				canton = i;
				break;
			}
		}
		return canton;
	}
	
	private void verifyInformation() throws BadControlInformationException{
		if(Line.getRailWays().size() == 0){
			throw new BadControlInformationException("No RailWay");
		}
		if(Line.getCantons().size() == 0){
			throw new BadControlInformationException("No Canton");
		}
		if(roads.size() == 0){
			throw new BadControlInformationException("No road");
		}
	}

}
