package test;
import ucp.greves.model.*;
import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.exceptions.railway.DoubledRailwayException;
import ucp.greves.model.line.Line_a;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import demothreading.LineBuilder;

public class LineBuilderSimpleTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testBuildLine() throws DoubledRailwayException {
		ConfigurationEnvironment.getInstance();
		Line_a ln = ucp.greves.model.line.builder.LineBuilder.buildLine(ConfigurationEnvironment.getInstance());
	}

}
