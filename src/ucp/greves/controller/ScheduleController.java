package ucp.greves.controller;

import ucp.greves.data.time.Time;
import ucp.greves.model.line.Line;
import ucp.greves.model.schedule.Clock;
import ucp.greves.model.schedule.LaunchTrainInformation;

public class ScheduleController {
	
	public static Time getCurrentTime(){
		return Clock.getTime();
	}
	
	public static void addLaunchTrainSchedule(String roadMap, Time time){
		Line.getInstance().getSchedule().addInformation(new LaunchTrainInformation(time, roadMap));
	}

}
