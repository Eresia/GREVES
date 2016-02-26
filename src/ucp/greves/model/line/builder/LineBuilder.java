package ucp.greves.model.line.builder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.sun.jndi.url.rmi.rmiURLContext;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.JSONListAdapter;
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
import ucp.greves.model.line.canton.Terminus;
import ucp.greves.model.line.station.Station;

public class LineBuilder {

	static public void buildLine(ConfigurationEnvironment conf)
			throws DoubledRailwayException, CantonHasAlreadyStationException, CantonNotExistException {

		try {
			String build_configuration = (String) conf.getProperty("BUILD_CONFIGURATION").getValue();
			switch (build_configuration) {
			case "XML":

				break;
			case "JSON":
				buildLineFromJson("Configuration/Line/line_test.json");
				break;
			default:
				LineBuilderSimple.BuildLine();
				break;
			}
		} catch (PropertyNotFoundException e) {
			LineBuilderSimple.BuildLine();
		}

	}

	private static void buildLineFromJson(String filepath) {
		int nbCantons = 0;
		ScriptEngine engine;

		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		engine = scriptEngineManager.getEngineByName("javascript");
		
		HashMap<Integer, RailWay> newRailways = new HashMap<>();

		String json;
		try {
			json = new String(Files.readAllBytes(Paths.get(filepath)));
			String script = "Java.asJSONCompatible(" + json + ")";
			Object result = engine.eval(script);

			Set<Map.Entry<String, Object>> jsonMap = ((ScriptObjectMirror) result).entrySet();

			// For each item in the first layer (line & connections)
			for (Entry<String, Object> topEntry : jsonMap) {
				System.out.println(topEntry);
				switch (topEntry.getKey().toLowerCase()) {
				case "line":
					// If we are declaring a line
					Set<Map.Entry<String, Object>> lineMap = ((ScriptObjectMirror) topEntry.getValue()).entrySet();

					// For each entry in line
					for (Entry<String, Object> lineEntry : lineMap) {
						System.out.println("  " + lineEntry);
						// If we are parsing a railway
						if (lineEntry.getKey().equalsIgnoreCase("railways")) {
							Collection<Object> railwayCol = ((JSONListAdapter) lineEntry.getValue()).values();

							// For each railway
							for (Object rwColValue : railwayCol) {
								ScriptObjectMirror rwSOM = (ScriptObjectMirror) rwColValue;
								Set<Map.Entry<String, Object>> rwMap = rwSOM.entrySet();

								RailWay rw = null;
								
								// For each item in railway (id & cantons)
								for (Entry<String, Object> rwEntry : rwMap) {
									System.out.println("    " + rwEntry);
									
									
									// If we are parsing the id, we add the railway to the line
									if (rwEntry.getKey().equalsIgnoreCase("id")) {
										int rwId = (Integer) rwEntry.getValue();
										rw = new RailWay(rwId);
									}
									// Else if we are parsing cantons
									else if (rwEntry.getKey().equalsIgnoreCase("cantons")) {
										JSONListAdapter cantonLA = (JSONListAdapter) rwEntry.getValue();
										Collection<Object> cantonCol = cantonLA.values();
										
										ArrayList<Object> cantonList = new ArrayList<>(cantonCol);
										ListIterator<Object> cantonIt = cantonList.listIterator(cantonList.size());

										// Parsing cantons
										while (cantonIt.hasPrevious()) {
											ScriptObjectMirror cantonSOM = (ScriptObjectMirror) cantonIt.previous();
											Set<Map.Entry<String, Object>> cantonMap = cantonSOM.entrySet();

											Canton canton = rw.getFirstCanton();
											
											// For each item in canton (size [& station])
											for (Entry<String, Object> cantonEntry : cantonMap) {
												System.out.println("      " + ++nbCantons);
												System.out.println("      " + cantonEntry);
												switch (cantonEntry.getKey().toLowerCase()) {
												// If we are on the size part
												case "size":
													int length = (Integer) cantonEntry.getValue();
													rw.addCanton(length);
													break;
												// If we are on the station part
												case "station":
													ScriptObjectMirror stationSOM = (ScriptObjectMirror) cantonEntry
															.getValue();
													Set<Map.Entry<String, Object>> stationMap = stationSOM.entrySet();

													String name = "";
													int waitTime = 0;

													// For each item in the station part (name [& wait_time])
													for (Entry<String, Object> stationEntry : stationMap) {
														System.out.println("        " + stationEntry);
														switch (stationEntry.getKey().toLowerCase()) {
														case "name":
															name = stationEntry.toString();
															break;
														case "wait_time":
															waitTime = (Integer) stationEntry.getValue();
															break;
														}

													}
													
													try {
														canton.setStation(new Station(canton.getId(), name, waitTime), canton.getLength()/2);
													} catch (CantonHasAlreadyStationException
															| CantonNotExistException e) {
														e.printStackTrace();
													}
													break;
												}
											}
										}
									}
								}
								newRailways.put(rw.getId(), rw);
							}
						}
					}

					break;
				case "connections":

					Set<Map.Entry<String, Object>> connectionsMap = ((ScriptObjectMirror) topEntry.getValue())
							.entrySet();

					// For each entry in line
					for (Entry<String, Object> connectionsEntry : connectionsMap) {
						System.out.println("  " + connectionsEntry);

						if (connectionsEntry.getKey().equalsIgnoreCase("connect")) {
							Collection<Object> connectCol = ((JSONListAdapter) connectionsEntry.getValue()).values();

							// For each connection
							for (Object connectColValue : connectCol) {
								ScriptObjectMirror connectSOM = (ScriptObjectMirror) connectColValue;
								Set<Map.Entry<String, Object>> connectMap = connectSOM.entrySet();

								int idFrom = 0, idTo = 0;
								boolean from = false, to = false;
								
								// For each item in railway
								for (Entry<String, Object> connectEntry : connectMap) {
									System.out.println("    " + connectEntry);
									switch (connectEntry.getKey().toLowerCase()) {
									case "from":
										idFrom = (Integer) connectEntry.getValue();
										from = true;
										break;
									case "to":
										idTo = (Integer) connectEntry.getValue();
										to = true;
										break;
									}
								}
								if(to && from) {
									RailWay connectRw = newRailways.get(idFrom);
									connectRw.connectTo(newRailways.get(idTo));
									newRailways.put(connectRw.getId(), connectRw);
								}
							}
						}
					}
					break;
				}
			}

		} catch (IOException | ScriptException e) {
			e.printStackTrace();
		}
		
		// Adding the railways to the line
		for(RailWay rw : newRailways.values()) {
			try {
				Line.register_railway(rw);
			} catch (DoubledRailwayException e) {
				e.printStackTrace();
			}
		}
		
		ControlLine control = ControlLine.getInstance();
		RoadMap rm = new RoadMap("test");
		// Adding the railways to the controller
		for(RailWay rw : newRailways.values()) {
			try {
				rm.addRailWay(rw.getId());
			} catch (DoubledRailwayException e) {
				e.printStackTrace();
			}
		}
		try {
			for (Integer s : Line.getStations().keySet()) {
				rm.addStation(Line.getStations().get(s).getName());
				System.out.println(Line.getStations().get(s).getName());
			}
			control.addRoad(rm.getName(), rm);
			
			control.launchTrain(rm.getName(), 150);
			
		} catch (BadControlInformationException | BadRoadMapException | RailWayNotExistException e) {
			e.printStackTrace();
		}

	}
}
