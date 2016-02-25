package ucp.greves.temp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jdk.nashorn.internal.runtime.JSONListAdapter;
import ucp.greves.model.exceptions.railway.DoubledRailwayException;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.RailWay;

public class MainTestLineBuilder {
	private final static String DEFAULT_FOLDER = "Configuration/Line/";

	public static void main(String[] args) {
		ScriptEngine engine;

		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		engine = scriptEngineManager.getEngineByName("javascript");
		
		String json;
		try {
			json = new String(Files.readAllBytes(Paths.get(DEFAULT_FOLDER+"line_test.json")));
			String script = "Java.asJSONCompatible("+json+")";
			Object result = engine.eval(script);
			
			@SuppressWarnings("unchecked")
			Map<String, Object> jsonMap = (Map<String, Object>) result;
			
			// Running through the first layer (line & connections)
			for(Entry<String, Object> entryTop : jsonMap.entrySet()) {
				System.out.println(entryTop);
				switch(entryTop.getKey().toLowerCase()) {
					case "line" :
						Map<String, Object> lineMap = (Map<String, Object>) entryTop.getValue();
						
						// Getting the railways of the line
						for(Entry<String, Object> entryLine : lineMap.entrySet()) {
							System.out.println("  "+entryLine);
							if(entryLine.getKey().equalsIgnoreCase("railways")) {
								Collection<Object> railWayCol = ((JSONListAdapter) entryLine.getValue()).values();
								railWayCol.forEach((testValue) -> {
									System.out.println("    testValue="+testValue);
									
									
								});
								
								Map<Object, Object> railwayMap = (Map<Object, Object>) entryLine.getValue();
								boolean idCreated = false;
								
								// Getting the railway intelligence (id & cantons)
								for(Entry<Object, Object> entryRailway : railwayMap.entrySet()) {
									System.out.println("    "+entryRailway);
									if(entryRailway.getKey().toString().equalsIgnoreCase("id") && !idCreated) {
										int rwId = (Integer) entryRailway.getValue();
										idCreated = true;
										RailWay rw = new RailWay(rwId);
										/*
										try {
											Line.register_railway(rw);
										} catch (DoubledRailwayException e) {
											e.printStackTrace();
										}
										*/
									}
									else if(entryRailway.getKey().toString().equalsIgnoreCase("cantons")) {
										Map<String, Object> cantonMap = (Map<String, Object>) entryRailway.getValue();
										
										for(Entry<String, Object> entryCanton : cantonMap.entrySet()) {
											System.out.println("      "+entryCanton.getKey());
											
										}
									}
								}
							}
						}
						
						break;
					case "connections" :
						
						break;
				}
			}
			
			/*
			jsonMap.forEach((keyTop, valueTop) -> {
				if(keyTop.equalsIgnoreCase("line")) {
					Map<String, Object> lineMap = (Map<String, Object>) valueTop;
					
					
					lineMap.forEach((keyLine, valueLine) -> {
						
					});
				}
				else if(keyTop.equalsIgnoreCase("connections")) {
					
				}
			});
			*/
			
		} catch (IOException | ScriptException e) {
			e.printStackTrace();
		}
	}

}
