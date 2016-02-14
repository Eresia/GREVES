package ucp.greves.model.exceptions;

/**
 * Thrown when a train is on a terminus
 */
public class TerminusException extends Exception {

	public TerminusException(){
		super();
	}
	
	public TerminusException(String s){
		super(s);
	}
}
