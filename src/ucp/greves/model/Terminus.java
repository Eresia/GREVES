package ucp.greves.model;
import java.util.HashSet;
import java.util.Set;


public class Terminus extends Canton {
/**
 * <pre>
 *           0..1     0..*
 * Terminus ------------------------- RailWay
 *           terminus        &gt;       railWayAvailable
 * </pre>
 */
private Set<RailWay> railWayAvailable;

public Set<RailWay> getRailWayAvailable() {
   if (this.railWayAvailable == null) {
this.railWayAvailable = new HashSet<RailWay>();
   }
   return this.railWayAvailable;
}

/**
 * <pre>
 *           0..1     0..1
 * Terminus ------------------------- RailWay
 *           nextTerminus        &lt;       railWay
 * </pre>
 */
private RailWay railWay;

public void setRailWay(RailWay value) {
   this.railWay = value;
}

public RailWay getRailWay() {
   return this.railWay;
}


	public Terminus(int id, int length, int startPoint) {
		super(id, length, startPoint);
		// TODO Auto-generated constructor stub
	}
	
	
	
}
