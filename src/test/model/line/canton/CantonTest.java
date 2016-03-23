package test.model.line.canton;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.exceptions.roadmap.RoadMapAlreadyExistException;
import ucp.greves.model.line.RoadMap;
import ucp.greves.model.line.canton.Canton;
import ucp.greves.model.train.Train;

/// TODO : Tests à implémenter
public class CantonTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testCantonCantonInt() {
		int length = 10;
		Canton canton = new Canton(null,100);
		length = canton.getLength();
		assertEquals(100,length);

	}

	@Test
	public void testCantonInt() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetNextCanton() {
		Canton firstCanton = new Canton(null,100);
		Canton secondCanton = new Canton(firstCanton,10);
		Canton thirdCanton = null;
		try {
			thirdCanton = secondCanton.getNextCanton(new RoadMap("name"));
		} catch (TerminusException | RoadMapAlreadyExistException e) {
		}
		assertEquals(thirdCanton, firstCanton);
		try {
			thirdCanton = thirdCanton.getNextCanton(new RoadMap("name"));
		} catch (TerminusException | RoadMapAlreadyExistException e) {
		}
		assertEquals(thirdCanton, null);
	}

	@Test
	public void testGetStartPoint() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetLength() {
		//fail("Not yet implemented");
	}

	@Test
	public void testEnter() {
		Canton secondCanton = new Canton(null, 2, 100);
		Canton firstCanton = new Canton(secondCanton, 2, 100);
		Train train = null;
		Train secondTrain = null;
		try {
			train = new Train(firstCanton, new RoadMap("test"), 10);
			secondTrain = new Train(firstCanton, new RoadMap("test"), 10);
		} catch (RoadMapAlreadyExistException e) {
		}
		
		/*The first train can enter in the next canton because he is free*/
		firstCanton.enter(train);
		assertEquals(train.getCurrentCanton(), firstCanton);
		
		/*the second can't enter because the first is in the canton*/
		secondCanton.enter(secondTrain);
		assertEquals(train.getCurrentCanton(), firstCanton);
		
	}

	@Test
	public void testExit() {
		//fail("Not yet implemented");
	}

	@Test
	public void testIsFree() {
		//fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetId() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetEndPoint() {
		//fail("Not yet implemented");
	}

	@Test
	public void testEnterInStation() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetStationPosition() {
		//fail("Not yet implemented");
	}

	@Test
	public void testSetStation() {
		//fail("Not yet implemented");
	}

	@Test
	public void testHasStation() {
		//fail("Not yet implemented");
	}

	@Test
	public void testGetStation() {
		//fail("Not yet implemented");
	}

	@Test
	public void testEqualsObject() {
		int firstLength = 10;
		int secondLength = 100;
		Canton firstCanton = new Canton(null, firstLength);
		Canton secondCanton = new Canton(null, secondLength);
		Canton thirdCanton = new Canton(secondCanton, firstLength);
		Canton fourthCanton = new Canton(null, firstLength);
		
		assertFalse(firstCanton.equals(secondCanton));
		assertFalse(firstCanton.equals(thirdCanton));
		assertFalse(firstCanton.equals(null));
		assertFalse(firstCanton.equals(new Integer(3)));
		assertFalse(firstCanton.equals(fourthCanton));
		
		assertTrue(thirdCanton.equals(thirdCanton));
		
	}

}
