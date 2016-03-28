package ucp.greves.controller;

import ucp.greves.data.time.Time;
import ucp.greves.model.schedule.Clock;

public class ScheduleController {
	
	public static Time getCurrentTime(){
		return Clock.getTime();
	}
	
	public static String getClockString(){
		return Clock.getInstance().getText();
	}

}
