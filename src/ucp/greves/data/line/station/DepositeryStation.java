package ucp.greves.data.line.station;

import java.util.ArrayList;

import ucp.greves.data.exceptions.canton.CantonHasAlreadyStationException;
import ucp.greves.data.exceptions.canton.CantonNotExistException;
import ucp.greves.data.train.Train;

/**
 * DepositeryStation is a special station where trains are stocked
 */
public class DepositeryStation extends Station {

	ArrayList<Train> stockTrains;

	/**
	 * Creates a DepositeryStation
	 * 
	 * @param canton
	 *            (Integer) The ID of the canton where the station is
	 * @param name
	 *            (String) The name of the station
	 * 
	 * @throws CantonHasAlreadyStationException
	 *             if the canton already has a station
	 * @throws CantonNotExistException
	 *             if the canton doesn't exist
	 */
	public DepositeryStation(int canton, String name) throws CantonHasAlreadyStationException, CantonNotExistException {
		super(canton, name);
		stockTrains = new ArrayList<Train>();
	}

	@Override
	public void enter(Train train) {
		train.remove(this);
	}

	/**
	 * Creates a DepositeryStation
	 * 
	 * @param name
	 *            (String) The name of the station
	 */
	public DepositeryStation(String name) {
		super(name);
		stockTrains = new ArrayList<Train>();
	}

	/**
	 * Adds a train to stock
	 * 
	 * @param train
	 *            (Train) The train to be stocked
	 */
	public void stockTrain(Train train) {
		stockTrains.add(train);
	}

	/**
	 * Removes a train from stock
	 * 
	 * @param train
	 *            (Train) The train to remove from the stock
	 */
	public void removeTrain(Train train) {
		stockTrains.remove(train);
	}

	@Override
	public void waitInStation() throws InterruptedException {
		// Thread.sleep(waitTime);
	}

}
