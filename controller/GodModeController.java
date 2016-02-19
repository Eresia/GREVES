package ucp.greves.controller;

import java.util.HashMap;

import ucp.greves.model.configuration.*;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.RailWay;
import ucp.greves.model.line.builder.LineBuilder;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.station.Station;
import ucp.greves.model.train.Train;
import ucp.greves.model.exceptions.railway.*;
import ucp.greves.view.*;

public class GodModeController {
	private  Line model;
	
	
	private GodModeController(){
		this.model = Line.GetInstance();
	}
	public  HashMap<Integer, RailWay> listOfRailWays(){
		return Line.getRailWays();
	}
	
	public  HashMap<Integer, Canton> listOfCantons(){
		return Line.getCantons();
	}
	
	public  HashMap<Integer, Train> listOfTrains(){
		return Line.getTrains();
	}
	
	public  HashMap<Integer, Station> listOfStation(){
		return Line.getStations();
	}
	public  void main(String[] args) {
		

	}

}
