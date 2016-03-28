package ucp.greves.model.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import ucp.greves.data.time.Time;

/**
 * This class is used to build the ConfigurationEnvironment from a .json configuration file.
 * 
 * @author MONOT Vincent
 * 
 * @see ucp.greves.model.configuration.ConfigurationEnvironmentBuilderXML for the .xml version.
 */
public class ConfigurationEnvironmentBuilderJSON {
	
	public final static String DEFAULT_FOLDER = "Configuration/";
	private static ScriptEngine engine;

	/**
	 * Calls the build function with the specified file. Uses the current ConfigurationEnvironment.
	 * 
	 * @param filename
	 * 			(String) Name of the .json file in which the configuration to load is contained.
	 * 
	 * @see	ConfigurationEnvironmentBuilderJSON#build(String, ConfigurationEnvironment)
	 */
	public static void BuildEnvironment(String filename) {
		build(filename, ConfigurationEnvironment.getInstance());
	}
	
	/**
	 * Calls the build function with the specified file and ConfigurationEnvironment.
	 * 
	 * @param filename
	 * 			(String) Name of the .json file in which the configuration to load is contained.
	 * @param configuration
	 * 			(ConfigurationEnvironment) ConfigurationEnvironment in which the configuration
	 * 			of the given file has to be loaded.
	 * 
	 * @see	ConfigurationEnvironmentBuilderXML#build(String, ConfigurationEnvironment)
	 * @see ConfigurationEnvironment
	 */
	public static void BuildEnvironment(String filename, ConfigurationEnvironment configuration) {
		build(filename, configuration);
	}
	
	/**
	 * Loads the data contained in the specified .json configuration file into
	 *  the specified ConfigurationEnvironment.
	 * 
	 * @param filename
	 * 			(String) Name of the .json file in which the configuration to load is contained.
	 * @param configuration
	 * 			(ConfigurationEnvironment) ConfigurationEnvironment in which the configuration
	 * 			of the given file is loaded.
	 * 
	 * @see ConfigurationEnvironment
	 */
	private static void build(String filename, ConfigurationEnvironment configuration) {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		engine = scriptEngineManager.getEngineByName("javascript");
		
		try {
			String json = new String(Files.readAllBytes(Paths.get(DEFAULT_FOLDER+"config_default.json")));
			String script = "Java.asJSONCompatible("+json+")";
			Object result = engine.eval(script);
			
			Set<Map.Entry<String, Object>> jsonMap = ((ScriptObjectMirror) result).entrySet();

			// For each item in the first layer (line & connections)
			for (Entry<String, Object> topEntry : jsonMap) {
				Set<Map.Entry<String, Object>> propertiesMap = ((ScriptObjectMirror) topEntry.getValue()).entrySet();

				// For each entry in line
				for (Entry<String, Object> propEntry : propertiesMap) {
					if(propEntry.getKey().equalsIgnoreCase("base_simulation_time")) {
						String[] sTime = propEntry.getValue().toString().split(":");
						if(sTime.length == 3){
							Time time = new Time(Integer.valueOf(sTime[0]), Integer.valueOf(sTime[1]), Integer.valueOf(sTime[2]));
							configuration.setProperty(propEntry.getKey().toUpperCase(), time);
						}
					}
					else {
						configuration.setProperty(propEntry.getKey().toUpperCase(), propEntry.getValue());
					}
				}
			}
		} catch (IOException | ScriptException e) {
			e.printStackTrace();
		}
	}
}
