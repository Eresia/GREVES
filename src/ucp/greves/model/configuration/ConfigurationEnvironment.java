package ucp.greves.model.configuration;

import java.util.HashMap;

import ucp.greves.model.exceptions.PropertyNotFoundException;

public class ConfigurationEnvironment {
	private HashMap<String, ConfigurationEnvironmentElement> configurationAttribute;
	
	private final static String CONFIG_DEFAULT = "config_default.xml";
	private final static boolean DEBUG_DEFAULT = true;

	private static ConfigurationEnvironment instance = new ConfigurationEnvironment();

	private ConfigurationEnvironment() {
		this.configurationAttribute = new HashMap<String, ConfigurationEnvironmentElement>();
		ConfigurationEnvironmentBuilderXML.BuildEnvironment(CONFIG_DEFAULT, this);
	}

	public static ConfigurationEnvironment getInstance() {
		return ConfigurationEnvironment.instance;
	}

	public synchronized ConfigurationEnvironmentElement getProperty(String property) throws PropertyNotFoundException{
		String propertyUpper = property.toUpperCase();
		if (!configurationAttribute.containsKey(propertyUpper)) {
			throw new PropertyNotFoundException("Property " + propertyUpper + " not found");
		} 
		return configurationAttribute.get(propertyUpper);
	}

	public synchronized void setProperty(String property, Object value) {
		String propertyUpper = property.toUpperCase();
		if (!configurationAttribute.containsKey(propertyUpper)) {
			configurationAttribute.put(propertyUpper, new ConfigurationEnvironmentElement(value));
		} else {
			configurationAttribute.get(propertyUpper).SetValue(value);
		}

	}
	
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
