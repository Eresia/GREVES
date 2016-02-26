package ucp.greves.model.line.builder;

import java.util.Random;

import ucp.greves.model.ControlLine;
import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.exceptions.BadControlInformationException;
import ucp.greves.model.exceptions.PropertyNotFoundException;
import ucp.greves.model.exceptions.canton.CantonHasAlreadyStationException;
import ucp.greves.model.exceptions.canton.CantonNotExistException;
import ucp.greves.model.exceptions.railway.DoubledRailwayException;
import ucp.greves.model.exceptions.railway.RailWayNotExistException;
import ucp.greves.model.exceptions.roadmap.BadRoadMapException;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.RailWay;
import ucp.greves.model.line.RoadMap;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.station.Station;

public class LineBuilderSimple {

	static public void BuildLine()
			throws DoubledRailwayException, CantonHasAlreadyStationException, CantonNotExistException {
		RailWay railWay = new RailWay(0);
		RailWay railWay2 = new RailWay(1);
		Random rn = new Random();
		while (railWay.getLength() < 10000) {
			addCanton(railWay, rn);
		}
		while (railWay2.getLength() < 10000) {
			addCanton(railWay2, rn);
		}
		railWay.connectTo(railWay2);
		railWay2.connectTo(railWay);

		Line.register_railway(railWay);
		Line.register_railway(railWay2);

		ControlLine control = ControlLine.getInstance();

		RoadMap rm = new RoadMap("test");
		rm.addRailWay(0);
		rm.addRailWay(1);
		try {
			for (Integer s : Line.getStations().keySet()) {
				rm.addStation(s);
			}
			control.addRoad(rm.getName(), rm);
			control.launchTrain(rm.getName(), 150);
			control.launchTrain(rm.getName(), 250);
			control.launchTrain(rm.getName(), 200);
			control.launchTrain(rm.getName(), 320);

		} catch (BadControlInformationException | BadRoadMapException | RailWayNotExistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void addCanton(RailWay rw, Random rn)
			throws CantonHasAlreadyStationException, CantonNotExistException {
		rw.addCanton(rn.nextInt(300) + 700);
		Canton canton = rw.getFirstCanton();
		int hasStation = rn.nextInt(5);
		if (hasStation == 3) {
			int waitTime;
			try {
				waitTime = (int) ConfigurationEnvironment.getInstance().getProperty("station_wait_time").getValue();
			} catch (PropertyNotFoundException e) {
				e.printStackTrace();
				waitTime = 500;
			}
			canton.setStation(new Station(canton.getId(), "TestSTation" + canton.getId(), waitTime),(canton.getLength() / 2));
		}
	}

}
