package ucp.greves.temp;

import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.line.Line;

public class MainTestLineBuilder {
	public static void main(String[] args) {
		
		ConfigurationEnvironment.getInstance().setProperty("BUILD_CONFIGURATION", "XML");
		ConfigurationEnvironment.getInstance().setProperty("DEBUG", true);
		Line.getInstance();
		
	}
}
