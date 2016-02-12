package ucp.greves.view;

import javax.swing.JFrame;

import ucp.greves.model.*;

/**
 * @author tliu@u-cergy.fr
 */
public class SimulationGUI extends JFrame implements Runnable {
	private SimulationDashboard dashboard = new SimulationDashboard();

	public SimulationGUI() {
		super("Train simulation");
		getContentPane().add(dashboard);
		setSize(1000, 300);
		setVisible(true);
	}

	public static void main(String[] args) {
		SimulationGUI simulationGUI = new SimulationGUI();
		Thread simulationThread = new Thread(simulationGUI);
		simulationThread.start();
	}

	@Override
	public void run() {
//		int trainBasicSpeed = TRAIN_BASIC_SPEED;
//		while (currentTime <= SIMULATION_DURATION) {
//			System.out.println("Current time : " + currentTime);
//			if (currentTime % 12 == 0) {
//				Line line = dashboard.getLine();
//				Canton firstCanton = line.getCantons().get(0);
//				if (firstCanton.isFree()) {
//					Train newTrain = new Train(line, firstCanton, trainBasicSpeed);
//					dashboard.addTrain(newTrain);
//					newTrain.start();
//					System.out.println("New Train created " + newTrain.toString());
//					trainBasicSpeed += TRAIN_SPEED_VARIATION;
//					System.out.println(trainBasicSpeed);
//				}
//
//			}
//			dashboard.repaint();
//			try {
//				Thread.sleep(TIME_UNIT);
//			} catch (InterruptedException e) {
//				System.err.println(e.getMessage());
//			}
//			currentTime++;
//		}
	}
}
