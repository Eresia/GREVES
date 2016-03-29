package test.model.train;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ucp.greves.data.exceptions.railway.DoubledRailwayException;
import ucp.greves.data.exceptions.roadmap.RoadMapAlreadyExistException;
import ucp.greves.data.line.canton.Canton;
import ucp.greves.data.line.roadMap.RoadMap;
import ucp.greves.data.train.Train;
import ucp.greves.model.line.Line;


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
		Train train = null;		
		RoadMap map = null;
		try {
			 map = new RoadMap("testTrain");
			 map.addRailWay(0);
		} catch (RoadMapAlreadyExistException | DoubledRailwayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		train = new Train(new Canton(null,100), map);
		
		assertNotNull(train);
	}

	@Test
	public void testGetSetTrainID() {
		int id = 5;
		Train train = null;		
		RoadMap map = null;
		try {
			 map = new RoadMap("testGetSetTrainID");
			 map.addRailWay(0);
		} catch (RoadMapAlreadyExistException | DoubledRailwayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		train = new Train(Line.getCantons().get(0), map);
		
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
		Train train = null;
		RoadMap map = null;
		try {
			 map = new RoadMap("testGetSetPosition");
			 map.addRailWay(0);
		} catch (RoadMapAlreadyExistException | DoubledRailwayException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		train = new Train(Line.getCantons().get(0), map);
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
