package ucp.greves.data.exceptions.canton;
/**
 * Exception throwed when we try to get the train on an empty Canton
 * 
 * @author antoine
 *
 */
public class CantonIsEmptyException extends Exception {

	public CantonIsEmptyException() {
		super();
		
	}

	public CantonIsEmptyException(String message) {
		super(message);
		
	}

	
}
