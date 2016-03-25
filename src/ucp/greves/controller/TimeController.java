package ucp.greves.controller;

import ucp.greves.model.schedule.Clock;
import ucp.greves.model.schedule.Time;

public class TimeController {
	
	public static Time getCurrentTime(){
		return Clock.getTime();
	}

}
