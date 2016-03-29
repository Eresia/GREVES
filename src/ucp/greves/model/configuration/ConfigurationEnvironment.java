package ucp.greves.model.configuration;

import java.util.HashMap;

import ucp.greves.data.exceptions.PropertyNotFoundException;
/**
 * This class has for aim to
 * set the configuration of the environment.
 * 
 * Configurations are saved in .xml files and determine several variables such as:
 *  - Duration of simulation.
 *  - Max speed of trains.
 *  - Speed of trains when near a station.
 *  - Distance from a station at which a train has to slow down.
 *  - The time a train stays in a station.
 *  - Enable/Disable DEBUG mode.
 * 
 * @author	REGNIER Antoine
 *
 */
public class ConfigurationEnvironment {
	private HashMap<String, ConfigurationEnvironmentElement> configurationAttribute;
	
	private final static String CONFIG_DEFAULT_XML = "config_default.xml";
	private final static String CONFIG_DEFAULT_JSON = "config_default.json";
	private final static boolean DEBUG_DEFAULT = true;

	private static ConfigurationEnvironment instance = new ConfigurationEnvironment();

	/**
	 * Initialise the environment with the default configuration.
	 * Default configuration is :
	 *  - Simulation duration = -1
	 *  - Train max speed = 100
	 *  - Speed near station = 15
	 *  - Distance to slow down = 100
	 *  - Time a train stays in a station = 1000
	 *  - DEBUG mode = false
	 */
	private ConfigurationEnvironment() {
		build("xml");
	}
	
	/**
	 * Build the environment with the xml or json file
	 * @param type
	 * 			(String) the file type to use (json or xml)
	 */
	public void build(String type) {
		switch(type.toLowerCase()) {
		case "xml":
			this.configurationAttribute = new HashMap<String, ConfigurationEnvironmentElement>();
			ConfigurationEnvironmentBuilderXML.BuildEnvironment(CONFIG_DEFAULT_XML, this);
			setProperty("BUILD_CONFIGURATION", "XML");
			break;
		case "json":
			this.configurationAttribute = new HashMap<String, ConfigurationEnvironmentElement>();
			ConfigurationEnvironmentBuilderJSON.BuildEnvironment(CONFIG_DEFAULT_JSON, this);
			setProperty("BUILD_CONFIGURATION", "JSON");
			break;
		}
	}

	/**
	 * @return
	 * 			(ConfigurationEnvironment) Returns the current instance of ConfigurationEnvironment.
	 */
	public static ConfigurationEnvironment getInstance() {
		return ConfigurationEnvironment.instance;
	}

	/**
	 * Permits to recover a specific ConfigurationEnvironmentElement from the current ConfigurationEnvironment.
	 * 
	 * @param property
	 * 			(String) Name of the configuration property that we want to access.
	 * 
	 * @return
	 * 			(ConfigurationEnvironmentElement) Returns the ConfigurationEnvironmentElement related
	 * 			to the property name given as the method's argument.
	 * 
	 * @throws
	 * 			PropertyNotFoundException If the property we are trying to access does not exist.
	 */
	public synchronized ConfigurationEnvironmentElement getProperty(String property) throws PropertyNotFoundException{
		String propertyUpper = property.toUpperCase();
		if (!configurationAttribute.containsKey(propertyUpper)) {
			throw new PropertyNotFoundException("Property " + propertyUpper + " not found");
		} 
		return configurationAttribute.get(propertyUpper);
	}

	/**
	 * Permits to change the value of a property in the current ConfigurationEnvironment.
	 * 
	 * @param property
	 * 			(String) Name of the property we want to change to value of.
	 * 
	 * @param value
	 * 			(Object) Value we want to give to the related property.
	 */
	public synchronized void setProperty(String property, Object value) {
		String propertyUpper = property.toUpperCase();
		if (!configurationAttribute.containsKey(propertyUpper)) {
			configurationAttribute.put(propertyUpper, new ConfigurationEnvironmentElement(value));
		} else {
			configurationAttribute.get(propertyUpper).SetValue(value);
		}

	}
	
	/**
	 * Checks if the DEBUG mode is enabled or not in the current ConfigurationEnvironment.
	 * 
	 * @return
	 * 			(Boolean) Returns true if the DEBUG mode is enabled, false if not.
	 */
	public static Boolean inDebug(){
		try {
			ConfigurationEnvironmentElement el = instance.getProperty("DEBUG");
			if(!el.getType().equals(Boolean.class)){
				return DEBUG_DEFAULT;
			}
			else{
				return (Boolean) el.getValue();
			}
		} catch (PropertyNotFoundException e) {
			return DEBUG_DEFAULT;
		}
	}

}
