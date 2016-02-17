package ucp.greves.temp;

import java.util.HashMap;

import ucp.greves.model.ControlLine;
import ucp.greves.model.exceptions.BadControlInformationException;
import ucp.greves.model.exceptions.canton.ManyTrainInSameCantonException;
import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.exceptions.railway.DoubledRailwayException;
import ucp.greves.model.exceptions.railway.RailWayNotExistException;
import ucp.greves.model.exceptions.roadmap.BadRoadMapException;
import ucp.greves.model.line.RailWay;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.RoadMap;
import ucp.greves.model.line.builder.LineBuilderSimple;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.canton.Terminus;
import ucp.greves.model.train.Train;

public class TempMain {

	public static void main(String[] args) {
		try {
			LineBuilderSimple.BuildLine();
			ControlLine control = ControlLine.getInstance();
			RoadMap rm = new RoadMap("test");
			rm.addRailWay(0);
			rm.addRailWay(1);
			control.addRoad(rm.getName(), rm);
			control.launchTrain(rm.getName(), 60);
			control.launchTrain(rm.getName(), 80);
			control.launchTrain(rm.getName(), 50);
			control.launchTrain(rm.getName(), 10);
			boolean notArrived = true;
			while (notArrived) {
				printLine(Line.getTrains());
				Thread.sleep(100);
				notArrived = false;
				HashMap<Integer, Train> trains = Line.getTrains();
				for (Integer tKey : trains.keySet()) {
					Train t = trains.get(tKey);
					if (!t.hasArrived()) {
						notArrived = true;
					}
				}
			}
		} catch (DoubledRailwayException | BadRoadMapException | RailWayNotExistException
				| BadControlInformationException | InterruptedException | ManyTrainInSameCantonException e) {
			e.printStackTrace();
		}
	}

	public static void printTrainInLine(HashMap<Integer, Train> trains) {
		for (Integer tKey : trains.keySet()) {
			Train t = trains.get(tKey);
			System.out.println(
					"Train " + t.getTrainID() + " - Pos : " + t.getPosition() + ", " + t.getCurrentCanton().toString());
		}
	}

	public static void printLine(HashMap<Integer, Train> trains) throws ManyTrainInSameCantonException {
		for (Integer rwI : Line.getRailWays().keySet()) {
			RailWay rw = Line.getRailWays().get(rwI);
			Canton canton = null;
			do {
				if (canton == null) {
					canton = rw.getFirstCanton();
				} else {
					try {
						canton = canton.getNextCanton(null);
					} catch (TerminusException e) {
						e.printStackTrace();
					}
				}
				String c;
				if(canton.hasStation()){
					c = "G";
				}
				else{
					c = "=";
				}
				for (Integer tKey : trains.keySet()) {
					Train t = trains.get(tKey);
					if(!t.hasArrived()){
						if (t.getCurrentCanton().equals(canton)) {
							if (c.equals("=") || c.equals("G")) {
								c = String.valueOf(t.getTrainID());
							} else {
								throw new ManyTrainInSameCantonException("Train " + c + " and " + t.getTrainID() + "are in the same canton " + canton.getId());
							}
						}
					}
				}
				System.out.print(c);
			} while (!canton.getClass().equals(Terminus.class));
			System.out.println();
		}
		System.out.println("\n\n\n");
	}

}
