package ucp.greves.data.time;

import ucp.greves.data.exceptions.time.UndefinedTimeException;

/**
 * Decorator Pattern for Time
 * 
 * @see Time
 * @see UndefinedTime
 */
public interface TimeDecorator {
	
	public String getString() throws UndefinedTimeException;
	
	/**
	 * Calls {@link TimeDecorator#isInferiorOrEquals(Time, boolean)} with (other, false)
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @return (boolean) Returns if the current Time is inferior or equal to other
	 */
	public boolean isInferiorOrEquals(TimeDecorator other);
	
	/**
	 * Checks if the current Time is inferior or equal to the one passed as parameter
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @param countDay
	 * 		(boolean) If the number of days is also checked
	 * @return (boolean) Returns if the current Time is inferior or equal to other
	 */
	public boolean isInferiorOrEquals(TimeDecorator other, boolean countDay);

	/**
	 * Calls {@link TimeDecorator#isSuperiorOrEquals(Time, boolean)} with (other, false)
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @return (boolean) Returns if the current Time is superior or equal to other
	 */
	public boolean isSuperiorOrEquals(TimeDecorator other);
	
	/**
	 * Checks if the current Time is superior or equal to the one passed as parameter
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @param countDay
	 * 		(boolean) If the number of days is also checked
	 * @return (boolean) Returns if the current Time is superior or equal to other
	 */
	public boolean isSuperiorOrEquals(TimeDecorator other, boolean countDay);
	
	/**
	 * Calls {@link TimeDecorator#isInferior(Time, boolean)} with (other, false)
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @return (boolean) Returns if the current Time is inferior to other
	 */
	public boolean isInferior(TimeDecorator other);
	
	/**
	 * Checks if the current Time is inferior to the one passed as parameter
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @param countDay
	 * 		(boolean) If the number of days is also checked
	 * @return (boolean) Returns if the current Time is inferior to other
	 */
	public boolean isInferior(TimeDecorator other, boolean countDay);

	/**
	 * Calls {@link TimeDecorator#isSuperior(Time, boolean)} with (other, false)
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @return (boolean) Returns if the current Time is superior to other
	 */
	public boolean isSuperior(TimeDecorator other);
	
	/**
	 * Checks if the current Time is superior to the one passed as parameter
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @param countDay
	 * 		(boolean) If the number of days is also checked
	 * @return (boolean) Returns if the current Time is superior to other
	 */
	public boolean isSuperior(TimeDecorator other, boolean countDay);

}
