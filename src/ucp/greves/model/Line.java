package ucp.greves.model;
import java.util.HashSet;
import java.util.Set;




/**
 * @author tliu@u-cergy.fr
 */
public class Line {



/**
    * <pre>
    *           1..1     0..*
    * Line ------------------------>RailWay
    *                   &gt;       railWay
    * </pre>
    */
   private Set<RailWay> railWay;
   
   public Set<RailWay> getRailWay() {
      if (this.railWay == null) {
         this.railWay = new HashSet<RailWay>();
      }
      return this.railWay;
   }
   
   
   
	private int totalLenght;
	private int usedLength = 0;

	public Line(int totalLenght) {
		this.totalLenght = totalLenght;
	}

	public boolean isFull() {
		return usedLength == totalLenght;
	}

	public int getTotalLenght() {
		return totalLenght;
	}

	public int getUsedLength() {
		return usedLength;
	}

}
