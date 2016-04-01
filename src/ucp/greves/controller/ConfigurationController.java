package ucp.greves.controller;

import ucp.greves.model.configuration.ConfigurationEnvironment;

/**
 * Controller of configuration
 *
 */
public class ConfigurationController {
	
	/**
	 * Build the configuration
	 */
	public static void buildConfiguration(){
		//ConfigurationEnvironment.getInstance().setProperty("BUILD_CONFIGURATION", "JSON");
		ConfigurationEnvironment.getInstance().setProperty("BUILD_CONFIGURATION", "XML");
	}

}
