package test;
import ucp.greves.data.exceptions.canton.CantonHasAlreadyStationException;
import ucp.greves.data.exceptions.canton.CantonNotExistException;
import ucp.greves.data.exceptions.line.InvalidXMLException;
import ucp.greves.data.exceptions.railway.DoubledRailwayException;
import ucp.greves.model.*;
import ucp.greves.model.configuration.ConfigurationEnvironment;

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
