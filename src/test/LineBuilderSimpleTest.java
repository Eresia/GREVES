package test;
import ucp.greves.model.*;

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
		Line ln = ucp.greves.model.LineBuilder.buildLine(ConfigurationEnvironment.getInstance());
	}

}
