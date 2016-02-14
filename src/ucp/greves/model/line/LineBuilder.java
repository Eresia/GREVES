package ucp.greves.model.line;

import ucp.greves.model.configuration.ConfigurationEnvironment;

public class LineBuilder {
	
	static public Line buildLine(ConfigurationEnvironment conf) {
	
		if(conf.getProperty("BUILD_CONFIGURATION") == null){
			return LineBuilderSimple.BuildLine();
			
		}else{
			String build_configuration = (String) conf.getProperty("BUILD_CONFIGURATION").getValue();
			switch(build_configuration){
			
//			case "XML"{
//				  
//			}
			default : 
				return LineBuilderSimple.BuildLine();
			
			}
		}
	
	}

	
}
