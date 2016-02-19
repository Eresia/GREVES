package test.model.configuration;

import static org.junit.Assert.*;
import ucp.greves.model.*;
import ucp.greves.model.configuration.ConfigurationEnvironmentBuilderXML;

import org.junit.Before;
import org.junit.Test;

public class ConfigurationEnvironmentBuilderXMLTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testBuildEnvironment() {
		ConfigurationEnvironmentBuilderXML.BuildEnvironment("Configuration/config_test.xml");
		
	}

}
