package ucp.greves.model.simulation;

import ucp.greves.data.exceptions.PropertyNotFoundException;
import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.configuration.ConfigurationEnvironmentElement;

public class SimulationInfo {

	private final static int FRAME_DURATION_DEFAULT = 50;
	private final static int FRAME_DURATION = setFrameDuration();
	
	private volatile static int simulationSpeed = 0;
	public volatile static boolean simulationStopped = false;
	
	public static void changeSimulationSpeed(int duration){
		simulationSpeed = duration;
	}
	
	public static void waitFrameTime() throws InterruptedException{
		/*int waitVar = 0;
		while(waitVar < FRAME_DURATION){
			Thread.sleep(1);
			waitVar += simulationSpeed;
		}*/
		int wait;
		do{
			wait = simulationSpeed;
			if(wait == 0){
				Thread.sleep(10);
			}
		}while(wait == 0 && !simulationStopped);
		if(!simulationStopped){
			Thread.sleep(FRAME_DURATION/wait);
		}
	}
	
	public static void stopSimulation(){
		simulationStopped = true;
	}
	
	public static boolean stopped(){
		return simulationStopped;
	}
	
	private static int setFrameDuration() {
		int fD = FRAME_DURATION_DEFAULT;
		try {
			ConfigurationEnvironmentElement frameElement = ConfigurationEnvironment.getInstance()
					.getProperty("frame_simulation_duration");
			if (!frameElement.getType().equals(Integer.class)) {
				System.err.println(
						"Frame duration has not the right type, default value " + FRAME_DURATION_DEFAULT + " used");
			} else {
				fD = (Integer) frameElement.getValue();
			}
		} catch (PropertyNotFoundException e) {
			System.err.println("Frame duration not defined, default value " + FRAME_DURATION_DEFAULT + " used");
		}
		return fD;
	}
	
}
