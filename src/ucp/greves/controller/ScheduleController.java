package ucp.greves.controller;

import ucp.greves.data.time.Time;
import ucp.greves.model.schedule.Schedule;

/**
 * Controller of schedules
 *
 */
public class ScheduleController {
	
	/**
	 * Add a train to launch at a time given
	 * @param roadMap
	 * 		(RoadMap) The road map for the launch
	 * @param time
	 * 		(Time) The time of the launch
	 */
	public static void addLaunchTrainSchedule(String roadMap, Time time){
		Schedule.addLaunchTrainSchedule(roadMap, time);
	}

}
