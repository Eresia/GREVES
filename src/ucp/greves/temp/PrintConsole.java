package ucp.greves.temp;

import java.util.HashMap;

import ucp.greves.data.line.canton.Canton;
import ucp.greves.data.line.canton.Terminus;
import ucp.greves.data.line.railWay.RailWay;
import ucp.greves.data.train.Train;
import ucp.greves.model.exceptions.canton.ManyTrainInSameCantonException;
import ucp.greves.model.exceptions.canton.TerminusException;
import ucp.greves.model.line.Line;

public class PrintConsole extends Thread{
	
	
	@Override
	public void run() {
		try {
			boolean notArrived = true;
			while (notArrived) {
				printLine(Line.getTrains());
				Thread.sleep(100);
				notArrived = false;
				HashMap<Integer, Train> trains = Line.getTrains();
				for (Integer tKey : trains.keySet()) {
					Train t = trains.get(tKey);
					if (!t.hasArrived()) {
						notArrived = true;
					}
				}
			}
		} catch (InterruptedException | ManyTrainInSameCantonException e) {
			e.printStackTrace();
		}
	}

	public void printTrainInLine(HashMap<Integer, Train> trains) {
		for (Integer tKey : trains.keySet()) {
			Train t = trains.get(tKey);
			System.out.println(
					"Train " + t.getTrainID() + " - Pos : " + t.getPosition() + ", " + t.getCurrentCanton().toString());
		}
	}

	public void printLine(HashMap<Integer, Train> trains) throws ManyTrainInSameCantonException {
		for (Integer rwI : Line.getRailWays().keySet()) {
			RailWay rw = Line.getRailWays().get(rwI);
			Canton canton = null;
			do {
				if (canton == null) {
					canton = rw.getFirstCanton();
				} else {
					try {
						canton = canton.getNextCanton(null);
					} catch (TerminusException e) {
						e.printStackTrace();
					}
				}
				String c;
				if(canton.hasStation()){
					c = "G";
				}
				else{
					c = "=";
				}
				for (Integer tKey : trains.keySet()) {
					Train t = trains.get(tKey);
					if(!t.hasArrived()){
						if (t.getCurrentCanton().equals(canton)) {
							if (c.equals("=") || c.equals("G")) {
								c = String.valueOf(t.getTrainID());
							} else {
								throw new ManyTrainInSameCantonException("Train " + c + " and " + t.getTrainID() + "are in the same canton " + canton.getId());
							}
						}
					}
				}
				System.out.print(c);
			} while (!canton.getClass().equals(Terminus.class));
			System.out.println();
		}
		System.out.println("\n\n\n");
	}

}
