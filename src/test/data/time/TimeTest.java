package test.data.time;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.sun.javafx.geom.transform.GeneralTransform3D;

import jdk.nashorn.internal.runtime.Undefined;
import ucp.greves.data.time.Time;
import ucp.greves.data.time.UndefinedTime;

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
		assertEquals(0,time.getDays());
	}
	
	@Test
	public void isSuperiorTest(){
		Time superiorTime = new Time(24,0,59);
		Time inferiorTime = new Time(22,58,1);
		Time equalTime = new Time(24,0,59);
		UndefinedTime uTime = new UndefinedTime();
		
		assertTrue(superiorTime.isSuperior(inferiorTime, true));
		assertFalse(inferiorTime.isSuperior(superiorTime, true));
		assertFalse(superiorTime.isSuperior(equalTime, true));
		assertFalse(superiorTime.isSuperior(uTime));
		
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
		superiorTime.setMinutes(1);
		assertTrue(superiorTime.isSuperior(inferiorTime));
		assertFalse(inferiorTime.isSuperior(superiorTime));
		
		/*
		 * Chack if minute are equals
		 */
		superiorTime.setMinutes(0);
		superiorTime.setSeconds(2);
		assertTrue(superiorTime.isSuperior(inferiorTime));
		assertFalse(inferiorTime.isSuperior(superiorTime));
		
	}
	
	@Test
	public void isInferiorTest(){
		Time superiorTime = new Time(24,0,59);
		Time inferiorTime = new Time(22,58,1);
		Time equalTime = new Time(24,0,59);
		UndefinedTime uTime = new UndefinedTime();
		
		assertFalse(superiorTime.isInferior(inferiorTime, true));
		assertTrue(inferiorTime.isInferior(superiorTime, true));
		assertFalse(superiorTime.isInferior(equalTime, true));
		assertTrue(superiorTime.isInferior(uTime));
		
		/*
		 * Check without days
		 */
		assertTrue(superiorTime.isInferior(inferiorTime));
		assertFalse(inferiorTime.isInferior(superiorTime));
		/*
		 * Check if hours are equals
		 */
		inferiorTime.setHours(24);
		inferiorTime.setMinutes(0);
		superiorTime.setMinutes(1);
		assertFalse(superiorTime.isInferior(inferiorTime));
		assertTrue(inferiorTime.isInferior(superiorTime));
		
		/*
		 * Chack if minute are equals
		 */
		superiorTime.setMinutes(0);
		superiorTime.setSeconds(2);
		assertFalse(superiorTime.isInferior(inferiorTime));
		assertTrue(inferiorTime.isInferior(superiorTime));
	}
	
	@Test
	public void isInferiorOrEqualTest(){
		Time superiorTime = new Time(24,1,59);
		Time inferiorTime = new Time(22,58,1);
		Time equalTime = new Time(24,1,59);
		UndefinedTime uTime = new UndefinedTime();
		
		assertFalse(superiorTime.isInferiorOrEquals(inferiorTime, true));
		assertTrue(inferiorTime.isInferiorOrEquals(superiorTime, true));
		assertTrue(superiorTime.isInferiorOrEquals(equalTime, true));
		assertTrue(superiorTime.isInferiorOrEquals(uTime));
		
		/*
		 * Check without days
		 */
		assertTrue(superiorTime.isInferiorOrEquals(inferiorTime));
		assertFalse(inferiorTime.isInferiorOrEquals(superiorTime));
		/*
		 * Check if hours are equals
		 */
		inferiorTime.setHours(24);
		inferiorTime.setMinutes(0);
		superiorTime.setMinutes(1);
		assertFalse(superiorTime.isInferiorOrEquals(inferiorTime));
		assertTrue(inferiorTime.isInferiorOrEquals(superiorTime));
		
		/*
		 * Chack if minute are equals
		 */
		superiorTime.setMinutes(0);
		superiorTime.setSeconds(2);
		assertFalse(superiorTime.isInferiorOrEquals(inferiorTime));
		assertTrue(inferiorTime.isInferiorOrEquals(superiorTime));
	}
	
	@Test
	public void isSuperiorOrEqualTest(){
		Time superiorTime = new Time(24,1,59);
		Time inferiorTime = new Time(22,58,1);
		Time equalTime = new Time(24,1,59);
		UndefinedTime uTime = new UndefinedTime();
		
		assertTrue(superiorTime.isSuperiorOrEquals(inferiorTime, true));
		assertFalse(inferiorTime.isSuperiorOrEquals(superiorTime, true));
		assertTrue(superiorTime.isSuperiorOrEquals(equalTime, true));
		assertFalse(superiorTime.isSuperiorOrEquals(uTime));
		
		/*
		 * Check without days
		 */
		assertFalse(superiorTime.isSuperiorOrEquals(inferiorTime));
		assertTrue(inferiorTime.isSuperiorOrEquals(superiorTime));
		/*
		 * Check if hours are equals
		 */
		inferiorTime.setHours(24);
		inferiorTime.setMinutes(0);
		superiorTime.setMinutes(1);
		assertTrue(superiorTime.isSuperiorOrEquals(inferiorTime));
		assertFalse(inferiorTime.isSuperiorOrEquals(superiorTime));
		
		/*
		 * Chack if minute are equals
		 */
		superiorTime.setMinutes(0);
		superiorTime.setSeconds(2);
		assertTrue(superiorTime.isSuperiorOrEquals(inferiorTime));
		assertFalse(inferiorTime.isSuperiorOrEquals(superiorTime));
	}
	@Test
	public void multTest(){
		Time time = new Time(1,2,3);
		time.multTime(2);
		assertEquals(0, time.getDays());
		assertEquals(2,time.getHours());
		assertEquals(4,time.getMinutes());
		assertEquals(6, time.getSeconds());
		
		time.multTime(12);
		assertEquals(1, time.getDays());
		assertEquals(0,time.getHours());
		assertEquals(49,time.getMinutes());
		assertEquals(12, time.getSeconds());
		
		time.multTime(2);
		assertEquals(2, time.getDays());
		assertEquals(1,time.getHours());
		assertEquals(38,time.getMinutes());
		assertEquals(24, time.getSeconds());
	}

}
