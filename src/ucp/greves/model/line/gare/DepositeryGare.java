package ucp.greves.model.line.gare;

import java.util.ArrayList;

import ucp.greves.model.train.Train;

public class DepositeryGare extends Gare{

	ArrayList<Train> stockTrains;
	
	public DepositeryGare(String name, int waitTime) {
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
