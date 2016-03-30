package ucp.greves.model.schedule;

import ucp.greves.data.exceptions.PropertyNotFoundException;
import ucp.greves.data.time.Time;
import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.configuration.ConfigurationEnvironmentElement;
import ucp.greves.model.simulation.SimulationInfo;

/**
 * This class handles the simulation time
 * This is a singleton class
 * 
 * @see Time
 */
public class Clock extends Thread{

	public volatile Time time;
	
	private final static Time BASE_SIMULATION_TIME_DEFAULT = new Time(8);
	private final static int NB_SECOND_BY_FRAME_DEFAULT = 1;
	
	private final static Time BASE_SIMULATION_TIME = setBaseTime();
	private final static int NB_SECOND_BY_FRAME = setNbSecondByFrame();
	
	private static Clock instance = new Clock();
	
	private Clock(){
		time = BASE_SIMULATION_TIME.clone();
	}
	
	@Override
	public void run(){
		try{
			updateClock();
			while(!SimulationInfo.stopped()){
				SimulationInfo.waitFrameTime();
				for(int i = 0; i < NB_SECOND_BY_FRAME; i++){
					time.incrementSecond();
				}
				updateClock();
//				System.out.println(time.toString());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates the clock
	 */
	private void updateClock(){
		time.updateTime();
	}
	
	/**
	 * @return (Integer) Returns the number of seconds in a frame
	 */
	public static final int nbSecondByFrame(){
		return NB_SECOND_BY_FRAME;
	}
	
	/**
	 * Gets the number of frames for a number of seconds
	 * 
	 * @param nbSeconds
	 * 		(Integer) The number of seconds to check
	 * @return
	 * 		(Integer) The total number of frames for the duration of the parameter
	 */
	public static int getNbFrame(int nbSeconds){
		return nbSeconds/Clock.nbSecondByFrame();
	}
	
	/**
	 * @return
	 * 		(Time) Returns the current time of the clock
	 */
	public static Time getTime(){
		return instance.time;
	}
	
	/**
	 * Gets the instance of the class
	 * @return
	 * 		(Clock) Returns the instance
	 */
	public static Clock getInstance(){
		return instance;
	}
	
	/**
	 * Sets the initial time of the clock
	 * @return
	 * 		(Time) Returns the basis time
	 */
	private static Time setBaseTime() {
		Time t = BASE_SIMULATION_TIME_DEFAULT;
		try {
			ConfigurationEnvironmentElement timeElement = ConfigurationEnvironment.getInstance()
					.getProperty("base_simulation_time");
			if (!timeElement.getType().equals(Time.class)) {
				System.err.println("Base Simulation Time has not the right type, default value "
						+ BASE_SIMULATION_TIME_DEFAULT + " used");
			} else {
				t = (Time) timeElement.getValue();
			}
		} catch (PropertyNotFoundException e) {
			System.err
					.println("Base Simulation Time not defined, default value " + BASE_SIMULATION_TIME_DEFAULT + " used");
		}
		return t;
	}
	
	/**
	 * Sets the number of seconds in a frame
	 * @return
	 * 		(Integer) Returns the number of seconds in a frame
	 */
	private static int setNbSecondByFrame() {
		int n = NB_SECOND_BY_FRAME_DEFAULT;
		try {
			ConfigurationEnvironmentElement nbElement = ConfigurationEnvironment.getInstance()
					.getProperty("nb_second_by_frame");
			if (!nbElement.getType().equals(Integer.class)) {
				System.err.println("Nb Seconds By Frame has not the right type, default value "
						+ NB_SECOND_BY_FRAME_DEFAULT + " used");
			} else {
				n = (Integer) nbElement.getValue();
			}
		} catch (PropertyNotFoundException e) {
			System.err
					.println("Nb Seconds By Frame not defined, default value " + NB_SECOND_BY_FRAME_DEFAULT + " used");
		}
		return n;
	}
	
	public String toString(){
		return time.toString();
	}
	
}
