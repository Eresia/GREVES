package ucp.greves.model;


public class RoadMap {
/**
 * <pre>
 *           0..1     0..1
 * RoadMap ------------------------- Train
 *           roadMap        &gt;       train
 * </pre>
 */
private Train train;

public void setTrain(Train value) {
   this.train = value;
}

public Train getTrain() {
   return this.train;
}

}
