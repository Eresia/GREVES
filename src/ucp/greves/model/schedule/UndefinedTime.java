package ucp.greves.model.schedule;

import ucp.greves.model.exceptions.time.UndefinedTimeException;

public class UndefinedTime implements TimeDecorator{
	
	@Override
	public String getString() throws UndefinedTimeException{
		throw new UndefinedTimeException();
	}

}
