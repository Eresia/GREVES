package ucp.greves.model.line.builder;

import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.exceptions.PropertyNotFoundException;
import ucp.greves.model.exceptions.railway.DoubledRailwayException;
import ucp.greves.model.line.Line;

public class LineBuilder {
	
	static public Line buildLine(ConfigurationEnvironment conf) throws DoubledRailwayException {

		try{
			String build_configuration = (String) conf.getProperty("BUILD_CONFIGURATION").getValue();
			switch(build_configuration){
			
//			case "XML"{
//				  
//			}
			default : 
				return LineBuilderSimple.BuildLine();
			
			}
		} catch(PropertyNotFoundException e){
			return LineBuilderSimple.BuildLine();
		}
	
	}

	
}
