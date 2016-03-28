package ucp.greves.data.time;

import ucp.greves.model.exceptions.time.UndefinedTimeException;

public interface TimeDecorator {
	
	public String getString() throws UndefinedTimeException;

}
