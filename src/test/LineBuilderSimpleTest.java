package test;
import ucp.greves.model.*;
import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.line.Line;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import demothreading.LineBuilder;

public class LineBuilderSimpleTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testBuildLine() {
		ConfigurationEnvironment.getInstance();
		Line ln = ucp.greves.model.line.LineBuilder.buildLine(ConfigurationEnvironment.getInstance());
	}

}
