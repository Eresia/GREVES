package ucp.greves.model.line.builder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.JSONListAdapter;
import ucp.greves.model.ControlLine;
import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.exceptions.BadControlInformationException;
import ucp.greves.model.exceptions.PropertyNotFoundException;
import ucp.greves.model.exceptions.canton.CantonHasAlreadyStationException;
import ucp.greves.model.exceptions.canton.CantonNotExistException;
import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.exceptions.line.InvalidXMLException;
import ucp.greves.model.exceptions.railway.DoubledRailwayException;
import ucp.greves.model.exceptions.railway.PathNotExistException;
import ucp.greves.model.exceptions.railway.RailWayNotExistException;
import ucp.greves.model.exceptions.roadmap.BadRoadMapException;
import ucp.greves.model.exceptions.roadmap.RoadMapAlreadyExistException;
import ucp.greves.model.exceptions.station.StationNotFoundException;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.RailWay;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.station.Station;

public class LineBuilder {

	static public void buildLine(ConfigurationEnvironment conf)
			throws DoubledRailwayException, CantonHasAlreadyStationException, CantonNotExistException, InvalidXMLException {

		try {
			String build_configuration = (String) conf.getProperty("BUILD_CONFIGURATION").getValue();
			switch (build_configuration) {
			case "XML":
				buildLineFromXml("Configuration/Line/line_A.xml");
				break;
			case "JSON":
				buildLineFromJson("Configuration/Line/line_A.json");
				break;
			default:
				LineBuilderSimple.BuildLine();
				break;
			}
		} catch (PropertyNotFoundException e) {
			LineBuilderSimple.BuildLine();
		}

	}
	
	private static void buildLineFromXml(String filepath) throws InvalidXMLException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = (Document) dBuilder.parse(filepath);
			
			HashMap<Integer, RailWay> newRailways = new HashMap<>();
			
			NodeList railwayList = doc.getElementsByTagName("railway");
			/* For each railway */
			for (int rwI = 0; rwI < railwayList.getLength(); rwI ++) {
				Element rwEl = (Element) railwayList.item(rwI);
				
				int rwId;
				try {
					rwId = Integer.valueOf(rwEl.getAttribute("id"));
				}
				catch(NumberFormatException nfe) {
					rwId = -1;
					throw new InvalidXMLException();
				}
				
				RailWay rw = new RailWay(rwId);
				
				NodeList cantonList = rwEl.getElementsByTagName("canton");
				

				if (ConfigurationEnvironment.inDebug()) {
					System.err.println("Railway n°"+rwId+" ("+cantonList.getLength()+" cantons) :");
				}
				
				/* For each canton in the railway */
				for(int cantonI = cantonList.getLength()-1 ; cantonI >= 0  ; cantonI--) {
					Element cantonEl = (Element) cantonList.item(cantonI);
					
					int cantonSize;
					try {
						cantonSize = Integer.valueOf(cantonEl.getAttribute("size"));
					}
					catch(NumberFormatException nfe) {
						cantonSize = 1000;
					}
					
					rw.addCanton(cantonSize);
					Canton canton = rw.getFirstCanton();

					if (ConfigurationEnvironment.inDebug()) {
						System.err.println("\tCanton "+(cantonI+1)+" : size = "+cantonSize);
					}

					NodeList stationList = cantonEl.getElementsByTagName("station");
					
					if(stationList.getLength() > 0) {
						Element stationEl = (Element) stationList.item(0);
						
						String stationName = stationEl.getAttribute("name");
						int stationWaitTime;
						
						try {
							stationWaitTime = Integer.valueOf(stationEl.getAttribute("wait_time"));
						}
						catch(NumberFormatException nfe) {
							stationWaitTime = Station.getWaitTimeConfig();
						}

						if (ConfigurationEnvironment.inDebug()) {
							System.err.println("\t\tStation : name = \""+stationName+"\" - wait_time = "+stationWaitTime);
						}
						
						try {
							canton.setStation(new Station(canton.getId(), stationName, stationWaitTime), canton.getLength()/2);
						} catch (CantonHasAlreadyStationException
								| CantonNotExistException e) {
							e.printStackTrace();
						}
					}
				}
				
				newRailways.put(rw.getId(), rw);
			}
			
			System.out.println();

			/* Parsing the connections */
			NodeList connectList = doc.getElementsByTagName("connect");
			for(int connectI=0 ; connectI < connectList.getLength() ; connectI++) {
				Element connectEl = (Element) connectList.item(connectI);
				
				int fromId = -1;
				try {
					fromId = Integer.valueOf(connectEl.getAttribute("from"));
				}
				catch(NumberFormatException nfe) {
					fromId = -1;
					throw new InvalidXMLException();
				}
				
				int toId = -1;
				try {
					toId = Integer.valueOf(connectEl.getAttribute("to"));
				}
				catch(NumberFormatException nfe) {
					toId = -1;
					throw new InvalidXMLException();
				}
				
				RailWay connectRw = newRailways.get(fromId);
				connectRw.connectTo(newRailways.get(toId));

				if (ConfigurationEnvironment.inDebug()) {
					System.err.println("Connection from "+fromId+" to "+toId);
				}
			}
			
			/* Adding the railways to the line */
			for(RailWay rw : newRailways.values()) {
				try {
					Line.register_railway(rw);
				} catch (DoubledRailwayException e) {
					e.printStackTrace();
				}
			}
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		
		ControlLine control = ControlLine.getInstance();
		try {
			Line.getRailWays().get(0).getFirstCanton().getStation();
			Line.getRailWays().get(4).getTerminus().getStation();
			ControlLine.getInstance().addRoad("Line A", Line.getRailWays().get(0).getFirstCanton().getStation(), Line.getRailWays().get(1).getTerminus().getStation());			
			control.launchTrain("Line A", 150);
			
		} catch (BadControlInformationException | BadRoadMapException | RailWayNotExistException | RoadMapAlreadyExistException | CantonNotExistException | PathNotExistException | StationNotFoundException e) {
			e.printStackTrace();
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
				if (ConfigurationEnvironment.inDebug()) {
					System.err.println(topEntry);
				}
				switch (topEntry.getKey().toLowerCase()) {
				case "line":
					// If we are declaring a line
					Set<Map.Entry<String, Object>> lineMap = ((ScriptObjectMirror) topEntry.getValue()).entrySet();

					// For each entry in line
					for (Entry<String, Object> lineEntry : lineMap) {
						if (ConfigurationEnvironment.inDebug()) {
							System.err.println("  " + lineEntry);
						}
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
									if (ConfigurationEnvironment.inDebug()) {
										System.err.println("    " + rwEntry);
									}									
									
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

											Canton canton = null;
											
											// For each item in canton (size [& station])
											for (Entry<String, Object> cantonEntry : cantonMap) {
												if (ConfigurationEnvironment.inDebug()) {
													System.err.println("      " + ++nbCantons);
													System.err.println("      " + cantonEntry);;
												}			
												switch (cantonEntry.getKey().toLowerCase()) {
												// If we are on the size part
												case "size":
													int length = (Integer) cantonEntry.getValue();
													rw.addCanton(length);
													canton = rw.getFirstCanton();
													break;
												// If we are on the station part
												case "station":
													ScriptObjectMirror stationSOM = (ScriptObjectMirror) cantonEntry
															.getValue();
													Set<Map.Entry<String, Object>> stationMap = stationSOM.entrySet();

													String name = "";
													int waitTime = Station.getWaitTimeConfig();

													// For each item in the station part (name [& wait_time])
													for (Entry<String, Object> stationEntry : stationMap) {
														if (ConfigurationEnvironment.inDebug()) {
															System.err.println("        " + stationEntry);
														}	
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
						if (ConfigurationEnvironment.inDebug()) {
							System.err.println("  " + connectionsEntry);
						}

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
									if (ConfigurationEnvironment.inDebug()) {
										System.err.println("    " + connectEntry);
									}
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
		//RoadMap rm = new RoadMap("test");
		// Adding the railways to the controller
		/*for(RailWay rw : newRailways.values()) {
			try {
				rm.addRailWay(rw.getId());
			} catch (DoubledRailwayException e) {
				e.printStackTrace();
			}
		}*/
		try {
			Line.getRailWays().get(0).getFirstCanton().getStation();
			Line.getRailWays().get(4).getTerminus().getStation();
			ControlLine.getInstance().addRoad("Line A", Line.getRailWays().get(0).getFirstCanton().getStation(), Line.getRailWays().get(1).getTerminus().getStation());			
			control.launchTrain("Line A", 150);
			
		} catch (BadControlInformationException | BadRoadMapException | RailWayNotExistException | RoadMapAlreadyExistException | CantonNotExistException | PathNotExistException | StationNotFoundException e) {
			e.printStackTrace();
		}

	}
}
