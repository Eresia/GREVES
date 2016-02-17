package ucp.greves.model.line.station;

import java.util.ArrayList;

import ucp.greves.model.exceptions.canton.CantonHasAlreadyStationException;
import ucp.greves.model.exceptions.canton.CantonNotExistException;
import ucp.greves.model.train.Train;

public class DepositeryStation extends Station{

	ArrayList<Train> stockTrains;
	
	public DepositeryStation(int canton, String name, int waitTime) throws CantonHasAlreadyStationException, CantonNotExistException {
		super(canton, name, waitTime);
		stockTrains = new ArrayList<Train>();
	}
	
	public void stockTrain(Train train){
		stockTrains.add(train);
	}
	
	public void removeTrain(Train train){
		stockTrains.remove(train);
	}
	
	

}
