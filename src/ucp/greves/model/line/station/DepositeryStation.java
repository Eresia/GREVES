package ucp.greves.model.line.station;

import java.util.ArrayList;

import ucp.greves.model.train.Train;

public class DepositeryStation extends Station{

	ArrayList<Train> stockTrains;
	
	public DepositeryStation(String name, int waitTime) {
		super(name, waitTime);
		stockTrains = new ArrayList<Train>();
	}
	
	public void stockTrain(Train train){
		stockTrains.add(train);
	}
	
	public void removeTrain(Train train){
		stockTrains.remove(train);
	}
	
	

}
