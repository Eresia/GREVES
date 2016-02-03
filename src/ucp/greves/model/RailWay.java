package ucp.greves.model;

public class RailWay {
/**
    * <pre>
    *           0..*     0..1
    * RailWay ------------------------- Terminus
    *           railWayAvailable        &lt;       terminus
    * </pre>
    */
   private Terminus terminus;
   
   public void setTerminus(Terminus value) {
      this.terminus = value;
   }
   
   public Terminus getTerminus() {
      return this.terminus;
   }
   
   /**
    * <pre>
    *           0..1     0..1
    * RailWay ------------------------- Terminus
    *           railWay        &gt;       nextTerminus
    * </pre>
    */
   private Terminus nextTerminus;
   
   public void setNextTerminus(Terminus value) {
      this.nextTerminus = value;
   }
   
   public Terminus getNextTerminus() {
      return this.nextTerminus;
   }
   

   


   
   public void addCanton(int id, int cantonLength) {
;
      	}
   
//   public Canton getCantonByPosition(int position)throws TerminusException {
//	   //return new Canton(id, length, startPoint);
//}
   }
