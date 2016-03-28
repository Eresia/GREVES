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
	
	/**
	 * Calls {@link UndefinedTime#isInferiorOrEquals(Time, boolean)} with (other, false)
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @return (boolean) Returns if the current Time is inferior or equal to other
	 */
	@Override
	public boolean isInferiorOrEquals(TimeDecorator other){
		return isInferiorOrEquals(other, false);
	}
	
	/**
	 * Checks if the current Time is inferior or equal to the one passed as parameter
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @param countDay
	 * 		(boolean) If the number of days is also checked
	 * @return (boolean) Returns if the current Time is inferior or equal to other
	 */
	public boolean isInferiorOrEquals(TimeDecorator other, boolean countDay){
		if(other.getClass().equals(UndefinedTime.class)){
			return true;
		}
		else{
			return false;
		}
	}

	/**
	 * Calls {@link UndefinedTime#isSuperiorOrEquals(Time, boolean)} with (other, false)
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @return (boolean) Returns if the current Time is superior or equal to other
	 */
	public boolean isSuperiorOrEquals(TimeDecorator other){
		return isSuperiorOrEquals(other, false);
	}
	
	/**
	 * Checks if the current Time is superior or equal to the one passed as parameter
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @param countDay
	 * 		(boolean) If the number of days is also checked
	 * @return (boolean) Returns if the current Time is superior or equal to other
	 */
	public boolean isSuperiorOrEquals(TimeDecorator other, boolean countDay){
		return true;
	}
	
	/**
	 * Calls {@link UndefinedTime#isInferior(Time, boolean)} with (other, false)
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @return (boolean) Returns if the current Time is inferior to other
	 */
	public boolean isInferior(TimeDecorator other){
		return isInferior(other, false);
	}
	
	/**
	 * Checks if the current Time is inferior to the one passed as parameter
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @param countDay
	 * 		(boolean) If the number of days is also checked
	 * @return (boolean) Returns if the current Time is inferior to other
	 */
	public boolean isInferior(TimeDecorator other, boolean countDay){
		return false;
	}

	/**
	 * Calls {@link UndefinedTime#isSuperior(Time, boolean)} with (other, false)
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @return (boolean) Returns if the current Time is superior to other
	 */
	public boolean isSuperior(TimeDecorator other){
		return isSuperior(other, false);
	}
	
	/**
	 * Checks if the current Time is superior to the one passed as parameter
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @param countDay
	 * 		(boolean) If the number of days is also checked
	 * @return (boolean) Returns if the current Time is superior to other
	 */
	public boolean isSuperior(TimeDecorator other, boolean countDay){
		if(other.getClass().equals(UndefinedTime.class)){
			return false;
		}
		else{
			return true;
		}
	}

}
