package ucp.greves.model.simulation;

import ucp.greves.model.configuration.ConfigurationEnvironment;
import ucp.greves.model.configuration.ConfigurationEnvironmentElement;
import ucp.greves.model.exceptions.PropertyNotFoundException;

public class SimulationSpeed {

	private final static int FRAME_DURATION_DEFAULT = 50;
	private final static int FRAME_DURATION = setFrameDuration();
	
	private volatile static int simulationSpeed = 0;
	
	public static void changeSimulationSpeed(int duration){
		simulationSpeed = duration;
	}
	
	public static void waitFrameTime() throws InterruptedException{
		int waitVar = 0;
		while(waitVar < FRAME_DURATION){
			Thread.sleep(1);
			waitVar += simulationSpeed;
		}
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
