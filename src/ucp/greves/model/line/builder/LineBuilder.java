package ucp.greves.model.line.builder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
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
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import jdk.nashorn.internal.runtime.JSONListAdapter;
import ucp.greves.controller.GodModeController;
import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.exceptions.PropertyNotFoundException;
import ucp.greves.model.exceptions.canton.CantonHasAlreadyStationException;
import ucp.greves.model.exceptions.canton.CantonNotExistException;
import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.exceptions.line.InvalidXMLException;
import ucp.greves.model.exceptions.railway.DoubledRailwayException;
import ucp.greves.model.exceptions.roadmap.RoadMapAlreadyExistException;
import ucp.greves.model.exceptions.station.StationNotFoundException;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.RailWay;
import ucp.greves.model.line.RoadMap;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.line.canton.Terminus;
import ucp.greves.model.line.station.Station;
import ucp.greves.model.schedule.Time;

/**
 * This class has for aim to construct the line and different road maps from files configuration.
 * 
 * 
 * @author Bastien LEPESANT, Vincent MONOT &#38; Antoine REGNIER
 *
 */
public class LineBuilder {

	/**
	 * Method to construct the line the road maps and the schedule from a configuration (XML or JSON)
	 * @param conf (ConfigurationEnvironment) The Object which contains the program configuration
	 * @throws DoubledRailwayException If many railway has the same ID in the configuration
	 * @throws CantonHasAlreadyStationException If there are many stations placed on the same Canton in the configuration
	 * @throws CantonNotExistException If a station is placed in an inexistent canton
	 * @throws InvalidXMLException If XML syntax is bad
	 * @see ConfigurationEnvironment
	 */
	static public void buildLine(ConfigurationEnvironment conf)
			throws DoubledRailwayException, CantonHasAlreadyStationException, CantonNotExistException, InvalidXMLException {

		try {
			String build_configuration = (String) conf.getProperty("BUILD_CONFIGURATION").getValue();
			switch (build_configuration) {
			case "XML":
				buildLineFromXml("Configuration/Line/line_A.xml");
				buildRoadFromXml("Configuration/RoadMap/road_A.xml");
				buildScheduleFromXml("Configuration/Schedule/schedule_A.xml");
				break;
			case "JSON":
				buildLineFromJson("Configuration/Line/line_A.json");
				buildRoadFromJson("Configuration/RoadMap/road_A.json");
				buildScheduleFromJson("Configuration/Schedule/schedule_A.json");
				break;
			default:
				LineBuilderSimple.BuildLine();
				break;
			}
		} catch (PropertyNotFoundException e) {
			LineBuilderSimple.BuildLine();
		}

	}
	
	/**
	 * Method to construct the line from a XML configuration
	 * @param (String) filepath The XML file path
	 * @throws InvalidXMLException If XML syntax is bad
	 */
	private static void buildLineFromXml(String filepath) throws InvalidXMLException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = (Document) dBuilder.parse(filepath);
			
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
				
				RailWay rw;
				try {
					rw = new RailWay(rwId);
					
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
					
				} catch (DoubledRailwayException e1) {
					e1.printStackTrace();
				}
				
				
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
				
				RailWay connectRw = Line.getRailWays().get(fromId);
				connectRw.connectTo(Line.getRailWays().get(toId));

				if (ConfigurationEnvironment.inDebug()) {
					System.err.println("Connection from "+fromId+" to "+toId);
				}
			}
			
			buildStationInformation();
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Method to construct the road maps from a XML configuration
	 * @param (String) filepath The XML file path
	 * @throws InvalidXMLException If XML syntax is bad
	 */
	private static void buildRoadFromXml(String filepath) throws InvalidXMLException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = (Document) dBuilder.parse(filepath);
			
			NodeList roadList = doc.getElementsByTagName("road");
			/* For each road */
			for (int rmI = 0; rmI < roadList.getLength(); rmI ++) {
				Element rmEl = (Element) roadList.item(rmI);
				
				String rmName;
				try {
					rmName = rmEl.getAttribute("name");
				}
				catch(NumberFormatException nfe) {
					rmName = "";
					throw new InvalidXMLException();
				}
				
				try {
					RoadMap road = new RoadMap(rmName);
					
					NodeList rwList = rmEl.getElementsByTagName("railway");
					
					/*For each railway*/
					for(int rwI = 0; rwI < rwList.getLength(); rwI++){
						Element rwEl = (Element) rwList.item(rwI);
						
						Integer rwId;
						try {
							rwId = Integer.valueOf(rwEl.getAttribute("id"));
						}
						catch(NumberFormatException nfe) {
							rwId = -1;
							throw new InvalidXMLException();
						}
						
						ArrayList<String> dontPass = new ArrayList<String>();
						NodeList sList = rwEl.getElementsByTagName("dontPass");
						if(sList.getLength() > 0){
							for(int sI = 0; sI < sList.getLength(); sI++){
								Element sEl = (Element) sList.item(sI);
								
								String sName;
								try {
									sName = sEl.getAttribute("name");
								}
								catch(NumberFormatException nfe) {
									sName = "";
									throw new InvalidXMLException();
								}
								dontPass.add(sName);
							}
						}
						
						try {
							road.addRailWay(rwId, dontPass);
						} catch (DoubledRailwayException e) {
							e.printStackTrace();
						}
					}
				} catch (RoadMapAlreadyExistException e) {
					e.printStackTrace();
				}
			}
				
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Method to construct the schedule from a XML configuration
	 * @param (String) filepath The XML file path
	 * @throws InvalidXMLException If XML syntax is bad
	 */
	private static void buildScheduleFromXml(String filepath) throws InvalidXMLException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = (Document) dBuilder.parse(filepath);
			
			NodeList launchList = doc.getElementsByTagName("launch");
			if(launchList.getLength() > 0){
				/* For each launch */
				for (int lI = 0; lI < launchList.getLength(); lI ++) {
					Element lEl = (Element) launchList.item(lI);
					
					String rmName;
					String[] sTime;
					Time time;
					try {
						rmName = lEl.getAttribute("road");
						sTime = lEl.getAttribute("time").split(":");
						if(sTime.length != 3){
							throw new InvalidXMLException("Bad Time");
						}
						time = new Time(Integer.valueOf(sTime[0]), Integer.valueOf(sTime[1]), Integer.valueOf(sTime[2]));
						GodModeController.getInstance().addLaunch(rmName, time);
					}
					catch(NumberFormatException nfe) {
						rmName = "";
						throw new InvalidXMLException();
					}
					
					
				}
			}
				
			
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method to construct the line from a JSON configuration
	 * @param (String) filepath The JSON file path
	 * @throws InvalidXMLException If JSON syntax is bad
	 */
	private static void buildLineFromJson(String filepath) {
		int nbCantons = 0;
		ScriptEngine engine;

		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		engine = scriptEngineManager.getEngineByName("javascript");

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
								try{
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
																name = stationEntry.getValue().toString();
																if (ConfigurationEnvironment.inDebug()) {
																	System.err.println("        " + name);
																}	
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
									
								} catch (DoubledRailwayException e){
									e.printStackTrace();
								}
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
									RailWay connectRw = Line.getRailWays().get(idFrom);
									connectRw.connectTo(Line.getRailWays().get(idTo));
								}
							}
						}
					}
					break;
				}
			}
			
			buildStationInformation();

		} catch (IOException | ScriptException e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * Method to construct the road maps from a JSON configuration
	 * @param (String) filepath The JSON file path
	 * @throws InvalidXMLException If JSON syntax is bad
	 */
	private static void buildRoadFromJson(String filepath) {
		//TODO
	}
	
	/**
	 * Method to construct the schedule from a JSON configuration
	 * @param (String) filepath The JSON file path
	 * @throws InvalidXMLException If JSON syntax is bad
	 */
	private static void buildScheduleFromJson(String filepath) {
		//TODO
	}
	
	private static void buildStationInformation(){
		for(Integer rwI : Line.getRailWays().keySet()){
			Canton canton = Line.getRailWays().get(rwI).getFirstCanton();
			try{
				while(true){
					try {
						Station s = canton.getStation();
						
					} catch (StationNotFoundException e) {
						
					}
					canton = canton.getNextCanton(null);
				}
			} catch(TerminusException e){
				
			}
		}
	}
}
