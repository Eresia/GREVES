package ucp.greves.controller;

import ucp.greves.data.time.Time;
import ucp.greves.model.line.Line;
import ucp.greves.model.schedule.Clock;
import ucp.greves.model.schedule.LaunchTrainInformation;
import ucp.greves.model.schedule.Schedule;

public class ScheduleController {
	
	public static void addLaunchTrainSchedule(String roadMap, Time time){
		Schedule.addLaunchTrainSchedule(roadMap, time);
	}

}
