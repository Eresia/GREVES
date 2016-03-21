package ucp.greves.model.train;

public class ModifiedTrainInformation {
	
	private final int updatedPosition;
	private boolean stationCrossed;
	
	public ModifiedTrainInformation(int updatedPosition){
		this.updatedPosition = updatedPosition;
		this.stationCrossed = false;
	}
	
	public void setStationCrossed(boolean stationCrossed){
		this.stationCrossed = stationCrossed;
	}
	
	public int getUpdatedPosition(){
		return updatedPosition;
	}
	
	public boolean getStationCrossed(){
		return stationCrossed;
	}

}
