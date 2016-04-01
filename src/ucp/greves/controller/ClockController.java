package ucp.greves.controller;

import ucp.greves.data.time.Time;
import ucp.greves.model.schedule.Clock;

/**
 * Controller of the Clock
 *
 */
public class ClockController {
	
	/**
	 * Return the current time of the simulation
	 * @return
	 * 		(Time) the current time
	 */
	public static Time getCurrentTime(){
		return Clock.getTime();
	}
	
	/**
	 * Return the number of seconds by frame of the simulation
	 * @return
	 * 		(Integer) The number of seconds by frame of the simulation
	 */
	public static int getNbSecondByFrame(){
		return Clock.nbSecondByFrame();
	}
	
	/**
	 * Return the number of frames for a number of seconds given
	 * @param nbSeconds
	 * 		(Integer) nbSeconds The number of seconds given
	 * @return 
	 * 		(Integer) The number of frame
	 */
	public static int getNbFrame(int nbSeconds){
		return Clock.getNbFrame(nbSeconds);
	}

}
