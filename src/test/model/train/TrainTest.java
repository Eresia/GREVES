package test.model.train;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ucp.greves.model.line.Line;
import ucp.greves.model.line.RoadMap;
import ucp.greves.model.train.Train;


/// TODO : Tests à implémenter
public class TrainTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testTrain() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSetTrainID() {
		int id = 5;
		Train train = new Train(Line.getCantons().get(0), new RoadMap("test"), 100);
		
		train.setTrainID(id);
		int idGet = train.getTrainID();
		
		assertEquals(id, idGet);
	}

	@Test
	public void testGetRoadMap() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetRoadMap() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSetPosition() {
		int pos = 27;
		Train train = new Train(Line.getCantons().get(0), new RoadMap("test"), 100);
		
		train.setPosition(pos);
		int posGet = train.getPosition();
		
		assertEquals(pos, posGet);
	}

	@Test
	public void testGetCurrentCanton() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetCurrentCanton() {
		fail("Not yet implemented");
	}

	@Test
	public void testRun() {
		fail("Not yet implemented");
	}

	@Test
	public void testHasArrived() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdatePosition() {
		fail("Not yet implemented");
	}

}
