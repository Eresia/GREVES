package ucp.greves.model;
import java.util.HashSet;

import java.util.Set;


public class RailWaySwitch {

/**
 * <pre>
 *           0..1     Switch     0..*
 *  ------------------------- 
 *           railWayAvailable        &gt;       railWayAvailable
 * </pre>
 */
private Set<RailWay> railWayAvailable;

public Set<RailWay> getRailWayAvailable() {
   if (this.railWayAvailable == null) {
this.railWayAvailable = new HashSet<RailWay>();
   }
   return this.railWayAvailable;
}

}
