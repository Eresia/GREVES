package ucp.greves.data.time;

import ucp.greves.model.exceptions.time.UndefinedTimeException;

/**
 * When the Time is undefined
 * 
 * @see Time
 * @see TimeDecorator
 */
public class UndefinedTime implements TimeDecorator{
	
	/**
	 * @throws UndefinedTimeException
	 */
	@Override
	public String getString() throws UndefinedTimeException{
		throw new UndefinedTimeException();
	}

}
