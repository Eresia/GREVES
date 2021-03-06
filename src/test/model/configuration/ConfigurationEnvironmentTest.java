package test.model.configuration;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ucp.greves.data.exceptions.PropertyNotFoundException;
import ucp.greves.model.configuration.ConfigurationEnvironment;

public class ConfigurationEnvironmentTest{

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testGetSetProperty() {
		String a = "helloword";
		Integer b = 3;
		Double c = 3.5;
		ConfigurationEnvironment conf = ConfigurationEnvironment.getInstance();
		conf.setProperty("string", a);
		
		conf.setProperty("int", b);
		conf.setProperty("double", c);
		
		String a1;
		try {
			a1 = (String)conf.getProperty("string").getValue();
			assertEquals(a, a1);
			Integer b1 = (Integer)conf.getProperty("int").getValue();
			assertEquals(b, b1);
			Double c1 = (Double)conf.getProperty("double").getValue();
			assertEquals(c, c1, 0.001);
		} catch (PropertyNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



}
