package ucp.greves.temp;

import java.util.ArrayList;

import ucp.greves.model.ControlLine;
import ucp.greves.model.exceptions.BadControlInformationException;
import ucp.greves.model.exceptions.railway.DoubledRailwayException;
import ucp.greves.model.exceptions.railway.RailWayNotExistException;
import ucp.greves.model.exceptions.roadmap.BadRoadmapException;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.RoadMap;
import ucp.greves.model.line.builder.LineBuilderSimple;
import ucp.greves.model.train.Train;

public class TempMain {

	public static void main(String[] args) {
		try {
			Line line = LineBuilderSimple.BuildLine();
			ControlLine control = ControlLine.getInstance();
			control.setLine(line);
			RoadMap rm = new RoadMap("test");
			rm.addRailWay(1);
			rm.addRailWay(2);
			control.addRoad(rm.getName(), rm);
			control.launchTrain(rm.getName(), 60);
			while(!control.getTrains().get(0).hasArrived()){
				printLine(line, control.getTrains());
				Thread.sleep(100);
			}
		} catch (DoubledRailwayException | BadRoadmapException | RailWayNotExistException | BadControlInformationException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void printLine(Line line, ArrayList<Train> trains){
		for(Train t : trains){
			System.out.println(t.getPosition());
		}
	}

}
