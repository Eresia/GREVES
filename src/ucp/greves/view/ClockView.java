package ucp.greves.view;

import java.util.Observable;
import java.util.Observer;

import javafx.application.Platform;
import javafx.scene.control.Label;
import ucp.greves.controller.ScheduleController;
import ucp.greves.data.time.Time;

public class ClockView implements Observer{
	
	private Label label;
	
	public ClockView(Label label) {
		this.label = label;
		ScheduleController.getCurrentTime().addObserver(this);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		Time clock = (Time) arg0;
		Platform.runLater(() -> label.setText(clock.toString()));
	}

}
