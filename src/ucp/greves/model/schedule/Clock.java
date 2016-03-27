package ucp.greves.model.schedule;

import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.configuration.ConfigurationEnvironmentElement;
import ucp.greves.model.exceptions.PropertyNotFoundException;
import ucp.greves.model.simulation.SimulationInfo;

public class Clock extends Thread{

	public volatile Time time;
	private volatile String text;
	
	private final static Time BASE_SIMULATION_TIME_DEFAULT = new Time(8);
	private final static int NB_SECOND_BY_FRAME_DEFAULT = 1;
	
	private final static Time BASE_SIMULATION_TIME = setBaseTime();
	private final static int NB_SECOND_BY_FRAME = setNbSecondByFrame();
	
	private static Clock instance = new Clock();
	
	private Clock(){
		time = BASE_SIMULATION_TIME.clone();
		text = "bbb";
	}
	
	@Override
	public void run(){
		try{
			while(!SimulationInfo.stopped()){
				SimulationInfo.waitFrameTime();
				for(int i = 0; i < NB_SECOND_BY_FRAME; i++){
					time.incrementSecond();
				}
				updateText();
//				System.out.println(time.toString());
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void updateText(){
		text = time.toString();
	}
	
	public String getText(){
		return text;
	}
	
	public static final int nbSecondByFrame(){
		return NB_SECOND_BY_FRAME;
	}
	
	public static Time getTime(){
		return instance.time;
	}
	
	public static Clock getInstance(){
		return instance;
	}
	
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
		return new String(text);
	}
	
}
