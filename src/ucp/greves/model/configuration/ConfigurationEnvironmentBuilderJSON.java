package ucp.greves.model.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ConfigurationEnvironmentBuilderJSON {
	
	public final static String DEFAULT_FOLDER = "Configuration/";
	private static ScriptEngine engine;

	public static void BuildEnvironment(String filename) {
		build(filename, ConfigurationEnvironment.getInstance());
	}

	public static void BuildEnvironment(String filename, ConfigurationEnvironment configuration) {
		build(filename, configuration);
	}
	
	private static void build(String filename, ConfigurationEnvironment configuration) {
		ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
		engine = scriptEngineManager.getEngineByName("javascript");
		
		try {
			String json = new String(Files.readAllBytes(Paths.get(DEFAULT_FOLDER+"config_default.json")));
			String script = "Java.asJSONCompatible("+json+")";
			Object result = engine.eval(script);
			
			@SuppressWarnings("unchecked")
			Map<String, Map<String, Object>> contents = (Map<String, Map<String, Object>>) result;
			
			contents.forEach((keyTop, valueTop) -> {
				if(keyTop.equalsIgnoreCase("properties")) {
					valueTop.forEach((name, value) -> {
						configuration.setProperty(name.toUpperCase(), value);
					});
				}
			});
		} catch (IOException | ScriptException e) {
			e.printStackTrace();
		}
	}
}
