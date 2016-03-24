package ucp.greves.model.schedule;

public class Time {
	
	private int hours;
	private int minutes;
	private int seconds;
	
	private int nbDay;
	
	public Time(){
		this(0, 0, 0);
	}
	
	public Time(int hours){
		this(hours, 0, 0);
	}
	
	public Time(int hours, int minutes){
		this(hours, minutes, 0);
	}
	
	public Time(int hours, int minutes, int seconds){
		this.hours = hours;
		this.minutes = minutes;
		this.seconds = seconds;
		
		nbDay = 0;
	}
	
	public Time(Time other){
		hours = other.hours;
		minutes = other.minutes;
		seconds = other.seconds;
		nbDay = other.nbDay;
	}
	
	public Time clone(){
		return new Time(this);
	}
	
	public void incrementHour(){
		hours++;
		if(hours >= 24){
			hours = 0;
			nbDay++;
		}
	}
	
	public void incrementMinute(){
		minutes++;
		if(minutes >= 60){
			minutes = 0;
			incrementHour();
		}
	}
	
	public void incrementSecond(){
		seconds++;
		if(seconds >= 60){
			seconds = 0;
			incrementMinute();
		}
	}
	
	public boolean isInferiorOrEquals(Time other){
		return isInferiorOrEquals(other, false);
	}
	
	public boolean isInferiorOrEquals(Time other, boolean countDay){
		if(countDay && (nbDay < other.nbDay)){
			return true;
		}
		if(hours == other.hours){
			if(minutes == other.minutes){
				return seconds < other.seconds;
			}
			return minutes <= other.minutes;
		}
		return hours < other.hours;
	}
	
	public boolean isSuperiorOrEquals(Time other){
		return isSuperiorOrEquals(other, false);
	}
	
	public boolean isSuperiorOrEquals(Time other, boolean countDay){
		if(countDay && (nbDay > other.nbDay)){
			return true;
		}
		if(hours == other.hours){
			if(minutes == other.minutes){
				return seconds > other.seconds;
			}
			return minutes >= other.minutes;
		}
		return hours > other.hours;
	}
	
	public boolean isInferior(Time other){
		return isInferior(other, false);
	}
	
	public boolean isInferior(Time other, boolean countDay){
		if(countDay && (nbDay < other.nbDay)){
			return true;
		}
		if(hours == other.hours){
			if(minutes == other.minutes){
				return seconds < other.seconds;
			}
			return minutes < other.minutes;
		}
		return hours < other.hours;
	}
	
	public boolean isSuperior(Time other){
		return isSuperior(other, false);
	}
	
	public boolean isSuperior(Time other, boolean countDay){
		if(countDay && (nbDay > other.nbDay)){
			return true;
		}
		if(hours == other.hours){
			if(minutes == other.minutes){
				return seconds > other.seconds;
			}
			return minutes > other.minutes;
		}
		return hours > other.hours;
	}
	
	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}

	@Override
	public String toString(){
		return hours + ":" + minutes + ":" + seconds;
	}

}
