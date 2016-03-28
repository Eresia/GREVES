package ucp.greves.data.time;

import ucp.greves.model.exceptions.time.UndefinedTimeException;

/**
 * Decorator Pattern for Time
 * 
 * @see Time
 * @see UndefinedTime
 */
public interface TimeDecorator {
	
	public String getString() throws UndefinedTimeException;

}
