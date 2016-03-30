package test.data.time;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ucp.greves.data.time.Time;

public class TimeTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		Time time = new Time();
		assertEquals(time.getDays(), 0);
		assertEquals(time.getHours(), 0);
		assertEquals(time.getMinutes(), 0);
		assertEquals(time.getSeconds(), 0);
		
		/*
		 * Constructor without change
		 */
		Time timeAll = new Time(23,59,1);
		assertEquals(0, timeAll.getDays());
		assertEquals(23, timeAll.getHours());
		assertEquals(59, timeAll.getMinutes());
		assertEquals(1, timeAll.getSeconds());
		/*
		 * Constructor with changes
		 */
		Time timeRAZ = new Time(24,59,60);
		assertEquals(1, timeRAZ.getDays());
		assertEquals(1, timeRAZ.getHours());
		assertEquals(0, timeRAZ.getMinutes());
		assertEquals(0, timeRAZ.getSeconds());
		
		Time timeTwo = new Time(1,1);
		assertEquals(timeTwo.getDays(), 0);
		assertEquals(timeTwo.getHours(), 1);
		assertEquals(timeTwo.getMinutes(), 1);
		assertEquals(timeTwo.getSeconds(), 0);
		
		Time timeThree = new Time(1);
		assertEquals(timeThree.getDays(), 0);
		assertEquals(timeThree.getHours(), 1);
		assertEquals(timeThree.getMinutes(), 0);
		assertEquals(timeThree.getSeconds(), 0);
		
		Time timeFour = new Time(time);
		assertEquals(timeFour.getDays(), 0);
		assertEquals(timeFour.getHours(), 0);
		assertEquals(timeFour.getMinutes(), 0);
		assertEquals(timeFour.getSeconds(), 0);
	}
	
	public TimeTest(){

	}
	
	@Test
	public void incrementTests(){
		Time time = new Time();
		time.incrementDay();
		assertEquals(1, time.getDays());
		time.incrementHour();
		assertEquals(1, time.getHours());
		time.incrementMinute();
		assertEquals(1, time.getMinutes());
		time.incrementSecond();
		assertEquals(1, time.getSeconds());
		
		Time timeTwo = new Time(23, 59, 59);
		timeTwo.incrementHour();
		assertEquals(0, timeTwo.getHours());
		timeTwo.incrementMinute();
		assertEquals(0, timeTwo.getMinutes());
		assertEquals(1, timeTwo.getHours());
		timeTwo.incrementSecond();
		assertEquals(0, timeTwo.getSeconds());
		assertEquals(1, timeTwo.getMinutes());
		assertEquals(1, timeTwo.getHours());
	}
	
	@Test
	public void addTests(){
		Time time = new Time();
		Time secondTime = new Time(10,1,2);
		time.addTime(secondTime);
		assertEquals(0, time.getDays());
		assertEquals(10, time.getHours());
		assertEquals(1, time.getMinutes());
		assertEquals(2, time.getSeconds());
	}
	
	@Test
	public void setTest(){
		Time time = new Time(1,2,3);
		time.setHours(2);
		assertEquals(2, time.getHours());
		time.setMinutes(5);
		assertEquals(5, time.getMinutes());
		time.setSeconds(10);
		assertEquals(10, time.getSeconds());
		
		time.setHours(25);
		assertEquals(1, time.getDays());
	}
	
	@Test
	public void isSuperiorTest(){
		Time superiorTime = new Time(24,0,59);
		Time inferiorTime = new Time(23,58,1);
		
		assertTrue(superiorTime.isSuperior(inferiorTime, true));
		assertFalse(inferiorTime.isSuperior(superiorTime, true));
		
		/*
		 * Check without days
		 */
		assertFalse(superiorTime.isSuperior(inferiorTime));
		assertTrue(inferiorTime.isSuperior(superiorTime));
		/*
		 * Check if hours are equals
		 */
		inferiorTime.setHours(24);
		inferiorTime.setMinutes(0);
		assertTrue(superiorTime.isSuperior(inferiorTime, true));
		assertFalse(inferiorTime.isSuperior(superiorTime, true));
		
		/*
		 * Chack if minute are equals
		 */
		superiorTime.setMinutes(58);
		superiorTime.setSeconds(2);
		assertTrue(superiorTime.isSuperior(inferiorTime, true));
		assertFalse(inferiorTime.isSuperior(superiorTime, true));
		
		inferiorTime.setHours(24);
		assertFalse(superiorTime.isSuperior(inferiorTime, true));
		assertTrue(inferiorTime.isSuperior(superiorTime, true));
	}
	
	@Test
	public void isInferiorTest(){
		Time superiorTime = new Time(24,0,0);
		Time inferiorTime = new Time(23,59,59);
		assertTrue(inferiorTime.isInferior(superiorTime, true));
		assertFalse(superiorTime.isInferior(inferiorTime, true));
		
		assertFalse(inferiorTime.isInferior(superiorTime));
		assertTrue(superiorTime.isInferior(inferiorTime));
	}

}
