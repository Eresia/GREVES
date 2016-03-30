package ucp.greves.model.train;

/**
 * This class is used to store the information about the trains
 * 
 * @see Train
 */
public class ModifiedTrainInformation {
	
	private int updatedPosition;
	private boolean stationCrossed;
	
	/**
	 * Creates a ModifiedTrainInformation instance
	 * @param updatedPosition
	 * 		(Integer) The initial position of the train
	 */
	public ModifiedTrainInformation(int updatedPosition){
		this.updatedPosition = updatedPosition;
		this.stationCrossed = false;
	}
	
	/**
	 * Updates the position
	 * @param updatedPosition
	 * 		(Integer) The new position
	 */
	public void setUpdatedPosition(int updatedPosition){
		this.updatedPosition = updatedPosition;
	}
	
	/**
	 * Sets the boolean to check is the train has crossed the station
	 * @param stationCrossed
	 * 		(Boolean) If the station has been crossed
	 */
	public void setStationCrossed(boolean stationCrossed){
		this.stationCrossed = stationCrossed;
	}
	
	/**
	 * @return (Integer) Returns the updated position of the train
	 */
	public int getUpdatedPosition(){
		return updatedPosition;
	}
	
	/**
	 * @return (Boolean) Returns if the train has crossed the station
	 */
	public boolean getStationCrossed(){
		return stationCrossed;
	}

}
