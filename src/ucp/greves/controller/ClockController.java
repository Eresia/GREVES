package ucp.greves.controller;

import ucp.greves.data.time.Time;
import ucp.greves.model.schedule.Clock;

public class ClockController {
	
	public static Time getCurrentTime(){
		return Clock.getTime();
	}
	
	public static int getNbSecondByFrame(){
		return Clock.nbSecondByFrame();
	}
	
	public static int getNbFrame(int nbSecond){
		return nbSecond/Clock.nbSecondByFrame();
	}

}
