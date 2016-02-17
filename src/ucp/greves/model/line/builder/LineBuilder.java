package ucp.greves.model.line.builder;

import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.exceptions.PropertyNotFoundException;
import ucp.greves.model.exceptions.canton.CantonHasAlreadyStationException;
import ucp.greves.model.exceptions.canton.CantonNotExistException;
import ucp.greves.model.exceptions.railway.DoubledRailwayException;

public class LineBuilder {
	
	static public void buildLine(ConfigurationEnvironment conf) throws DoubledRailwayException, CantonHasAlreadyStationException, CantonNotExistException {

		try{
			String build_configuration = (String) conf.getProperty("BUILD_CONFIGURATION").getValue();
			switch(build_configuration){
			
//			case "XML"{
//				  
//			}
			default : 
				LineBuilderSimple.BuildLine();
			
			}
		} catch(PropertyNotFoundException e){
			LineBuilderSimple.BuildLine();
		}
	
	}

	
}
