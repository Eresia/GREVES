package ucp.greves.controller;

import ucp.greves.model.configuration.ConfigurationEnvironment;

public class ConfigurationController {
	
	public static void buildConfiguration(){
		//ConfigurationEnvironment.getInstance().setProperty("BUILD_CONFIGURATION", "JSON");
		ConfigurationEnvironment.getInstance().setProperty("BUILD_CONFIGURATION", "XML");
	}

}
