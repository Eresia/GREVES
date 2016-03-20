package test;
import ucp.greves.model.*;
import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.exceptions.canton.CantonHasAlreadyStationException;
import ucp.greves.model.exceptions.canton.CantonNotExistException;
import ucp.greves.model.exceptions.line.InvalidXMLException;
import ucp.greves.model.exceptions.railway.DoubledRailwayException;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class LineBuilderSimpleTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testBuildLine() throws DoubledRailwayException, CantonHasAlreadyStationException, CantonNotExistException {
		ConfigurationEnvironment.getInstance();
		try {
			ucp.greves.model.line.builder.LineBuilder.buildLine(ConfigurationEnvironment.getInstance());
		} catch (InvalidXMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
