package ucp.greves.model.line.station;

import java.util.ArrayList;

import ucp.greves.model.exceptions.canton.CantonHasAlreadyStationException;
import ucp.greves.model.exceptions.canton.CantonNotExistException;
import ucp.greves.model.train.Train;

public class DepositeryStation extends Station{

	ArrayList<Train> stockTrains;
	
	public DepositeryStation(int canton, String name) throws CantonHasAlreadyStationException, CantonNotExistException {
		super(canton, name);
		stockTrains = new ArrayList<Train>();
	}
	
	public DepositeryStation(String name){
		super(name);
		stockTrains = new ArrayList<Train>();
	}
	
	public void stockTrain(Train train){
		stockTrains.add(train);
	}
	
	public void removeTrain(Train train){
		stockTrains.remove(train);
	}
	
	@Override
	public void waitInStation() throws InterruptedException{
		//Thread.sleep(waitTime);
	}

}
