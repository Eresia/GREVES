package ucp.greves.data.time;

import java.util.Observable;

import ucp.greves.data.exceptions.time.UndefinedTimeException;

/**
 * Time class used for the schedule of arrivals of trains in stations
 * 
 * {@link TimeDecorator}
 * @see UndefinedTime
 */
public class Time extends Observable implements TimeDecorator{
	
	private int hours;
	private int minutes;
	private int seconds;
	
	private int nbDays;
	
	/**
	 * Creates basic Time
	 */
	public Time(){
		this(0, 0, 0);
	}
	
	/**
	 * Creates Time basic Time, except for hours
	 * @param hours
	 * 		(Integer) The hours to specify
	 */
	public Time(int hours){
		this(hours, 0, 0);
	}
	
	/**
	 * Creates basic Time, except for hours
	 * 
	 * @param hours
	 * 		(Integer) The hours to specify
	 * @param minutes
	 * 		(Integer) The minutes to specify
	 */
	public Time(int hours, int minutes){
		this(hours, minutes, 0);
	}
	
	/**
	 * Creates Time
	 * 
	 * @param hours
	 * 		(Integer) The hours to specify
	 * @param minutes
	 * 		(Integer) The minutes to specify
	 * @param seconds
	 * 		(Integer) The seconds to specify
	 */
	public Time(int hours, int minutes, int seconds){
		this.hours = 0;
		this.minutes = 0;
		this.seconds = 0;
		this.nbDays = 0;
		
		addHours(hours);
		addMinutes(minutes);
		addSeconds(seconds);
		
	}
	
	/**
	 * Creates a duplicated Time
	 * @param other
	 * 		(Time) The Time to duplicate
	 */
	public Time(Time other){
		hours = other.hours;
		minutes = other.minutes;
		seconds = other.seconds;
		nbDays = other.nbDays;
	}
	
	/**
	 * Creates a duplicates
	 */
	public Time clone(){
		return new Time(this);
	}
	
	/**
	 * Adds a day
	 */
	public void incrementDay(){
		nbDays++;
	}
	
	/**
	 * Adds an hour
	 */
	public void incrementHour(){
		hours++;
		if(hours >= 24){
			hours = 0;
			incrementDay();
		}
	}
	
	/**
	 * Adds a minute
	 */
	public void incrementMinute(){
		minutes++;
		if(minutes >= 60){
			minutes = 0;
			incrementHour();
		}
	}

	
	/**
	 * Adds a second
	 */
	public void incrementSecond(){
		seconds++;
		if(seconds >= 60){
			seconds = 0;
			incrementMinute();
		}
	}
	
	/**
	 * Adds seconds
	 * @param seconds
	 * 		(Integer) The number of seconds to add
	 */
	public void addSeconds(int seconds){
		addMinutes((this.seconds + seconds) / 60);
		this.seconds = (this.seconds + seconds) % 60;
	}
	
	/**
	 * Adds minutes
	 * @param minutes
	 * 		(Integer) The number of minutes to add
	 */
	public void addMinutes(int minutes){
		addHours((this.minutes + minutes) / 60);
		this.minutes = (this.minutes + minutes) % 60;
	}
	
	/**
	 * Adds hours
	 * @param hours
	 * 		(Integer) The number of hours to add
	 */
	public void addHours(int hours){
		addDays((this.hours + hours) / 24);
		this.hours = (this.hours + hours) % 24;
	}
	
	/**
	 * Adds days
	 * @param nbDays
	 * 		(Integer) The number of days to add
	 */
	public void addDays(int nbDays){
		this.nbDays += nbDays;
	}
	
	/**
	 * Adds 2 Times
	 * @param other
	 * 		(Time) The Time to add the current one
	 */
	public void addTime(Time other){
		addSeconds(other.seconds);
		addMinutes(other.minutes);
		addHours(other.hours);
		addDays(other.nbDays);
	}
	
	/**
	 * Multiplies seconds
	 * @param mult
	 * 		(Integer) The factor to multiply the seconds
	 */
	private void multSeconds(int mult){
		addMinutes((seconds * mult) / 60);
		seconds = (seconds * mult) % 60;
	}
	
	/**
	 * Multiplies minutes
	 * @param mult
	 * 		(Integer) The factor to multiply the minutes
	 */
	private void multMinutes(int mult){
		addHours((minutes * mult) / 60);
		minutes = (minutes * mult) % 60;
	}
	
	/**
	 * Multiplies hours
	 * @param mult
	 * 		(Integer) The factor to multiply the hours
	 */
	private void multHours(int mult){
		addDays((hours * mult) / 24);
		hours = (hours * mult) % 24;
	}
	
	/**
	 * Multiplies days
	 * @param mult
	 * 		(Integer) The factor to multiply the days
	 */
	private void multDays(int mult){
		this.nbDays *= mult;
	}
	
	/**
	 * Multiplies Time
	 * @param mult
	 * 		(Integer) The factor to multiply the Time
	 */
	public void multTime(int mult){
		multDays(mult);
		multHours(mult);
		multMinutes(mult);
		multSeconds(mult);
	}
	
	/**
	 * Calls {@link Time#isInferiorOrEquals(Time, boolean)} with (other, false)
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
	@Override
	public boolean isInferiorOrEquals(TimeDecorator other, boolean countDay){
		if(other.getClass().equals(UndefinedTime.class)){
			return true;
		}
		Time otherT = (Time) other;
		if(countDay){
			if (nbDays > otherT.nbDays){
				return false;
			}
			else if(nbDays < otherT.nbDays){
				return true;
			}
		}
		if(hours == otherT.hours){
			if(minutes == otherT.minutes){
				return seconds <= otherT.seconds;
			}
			return minutes < otherT.minutes;
		}
		return hours < otherT.hours;
	}

	/**
	 * Calls {@link Time#isSuperiorOrEquals(Time, boolean)} with (other, false)
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @return (boolean) Returns if the current Time is superior or equal to other
	 */
	@Override
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
	@Override
	public boolean isSuperiorOrEquals(TimeDecorator other, boolean countDay){
		if(other.getClass().equals(UndefinedTime.class)){
			return false;
		}
		Time otherT = (Time) other;
		if(countDay){
			if (nbDays > otherT.nbDays){
				return true;
			}
			
			if(nbDays < otherT.nbDays){
				return false;
			}
		}
		if(hours == otherT.hours){
			if(minutes == otherT.minutes){
				return seconds >= otherT.seconds;
			}
			return minutes > otherT.minutes;
		}
		return hours > otherT.hours;
	}
	
	/**
	 * Calls {@link Time#isInferior(Time, boolean)} with (other, false)
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @return (boolean) Returns if the current Time is inferior to other
	 */
	@Override
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
	@Override
	public boolean isInferior(TimeDecorator other, boolean countDay){
		if(other.getClass().equals(UndefinedTime.class)){
			return true;
		}
		Time otherT = (Time) other;
		if(countDay){
			if (nbDays > otherT.nbDays){
				return false;
			}	
			else if(nbDays < otherT.nbDays){
				return true;
			}
		}
		if(hours == otherT.hours){
			if(minutes == otherT.minutes){
				return seconds < otherT.seconds;
			}
			return minutes < otherT.minutes;
		}
		return hours < otherT.hours;
	}

	/**
	 * Calls {@link Time#isSuperior(Time, boolean)} with (other, false)
	 * 
	 * @param other
	 * 		(TimeDecorator) The Time to compare
	 * @return (boolean) Returns if the current Time is superior to other
	 */
	@Override
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
	@Override
	public boolean isSuperior(TimeDecorator other, boolean countDay){
		if(other.getClass().equals(UndefinedTime.class)){
			return false;
		}
		Time otherT = (Time) other;
		if(countDay){
			if (nbDays > otherT.nbDays){
				return true;
			}	
			else if(nbDays < otherT.nbDays){
				return false;
			}
		}
		if(hours == otherT.hours){
			if(minutes == otherT.minutes){
				return seconds > otherT.seconds;
			}
			return minutes > otherT.minutes;
		}
		return hours > otherT.hours;
	}
	
	/**
	 * @return (Integer) Returns the number of days
	 */
	public int getDays(){
		return nbDays;
	}
	
	/**
	 * @return (Integer) Returns the number of hours
	 */
	public int getHours() {
		return hours;
	}

	/**
	 * Sets the number of hours
	 * @param hours
	 * 		(Integer) The new value of hours
	 */
	public void setHours(int hours) {
		this.hours = hours%24;
	}
	
	/**
	 * @return (Integer) Returns the number of minutes
	 */
	public int getMinutes() {
		return minutes;
	}

	/**
	 * Sets the number of minutes
	 * @param minutes
	 * 		(Integer) The new value of minutes
	 */
	public void setMinutes(int minutes) {
		this.minutes = minutes%60;
	}
	
	/**
	 * @return (Integer) Returns the number of seconds
	 */
	public int getSeconds() {
		return seconds;
	}

	/**
	 * Sets the number of seconds
	 * @param seconds
	 * 		(Integer) The new value of seconds
	 */
	public void setSeconds(int seconds) {
		this.seconds = seconds%60;
	}
	
	public void updateTime(){
		if(countObservers() > 0){
			this.setChanged();
			this.notifyObservers();
		}
	}

	@Override
	public String toString(){
		return String.format("%02d", hours) + ":" + String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
	}

	@Override
	public String getString() throws UndefinedTimeException {
		return toString();
	}

}
