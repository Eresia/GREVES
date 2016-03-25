package ucp.greves.model.schedule;

public class Time {
	
	private int hours;
	private int minutes;
	private int seconds;
	
	private int nbDays;
	
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
		
		nbDays = 0;
	}
	
	public Time(Time other){
		hours = other.hours;
		minutes = other.minutes;
		seconds = other.seconds;
		nbDays = other.nbDays;
	}
	
	public Time clone(){
		return new Time(this);
	}
	
	public void incrementDay(){
		nbDays++;
	}
	
	public void incrementHour(){
		hours++;
		if(hours >= 24){
			hours = 0;
			incrementDay();
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
	
	public void addSeconds(int seconds){
		this.seconds = (this.seconds + seconds) % 60;
		addMinutes((this.seconds + seconds) / 60);
	}
	
	public void addMinutes(int minutes){
		this.minutes = (this.minutes + minutes) % 60;
		addHours((this.minutes + minutes) / 60);
	}
	
	public void addHours(int hours){
		this.hours = (this.hours + hours) % 24;
		addDays((this.hours + hours) / 24);
	}
	
	public void addDays(int nbDays){
		this.nbDays += nbDays;
	}
	
	public void addTime(Time other){
		addSeconds(other.seconds);
		addMinutes(minutes);
	}
	
	public boolean isInferiorOrEquals(Time other){
		return isInferiorOrEquals(other, false);
	}
	
	public boolean isInferiorOrEquals(Time other, boolean countDay){
		if(countDay){
			if (nbDays > other.nbDays){
				return false;
			}
			
			if(nbDays < other.nbDays){
				return true;
			}
		}
		if(hours == other.hours){
			if(minutes == other.minutes){
				return seconds <= other.seconds;
			}
			return minutes < other.minutes;
		}
		return hours < other.hours;
	}
	
	public boolean isSuperiorOrEquals(Time other){
		return isSuperiorOrEquals(other, false);
	}
	
	public boolean isSuperiorOrEquals(Time other, boolean countDay){
		if(countDay){
			if (nbDays > other.nbDays){
				return true;
			}
			
			if(nbDays < other.nbDays){
				return false;
			}
		}
		if(hours == other.hours){
			if(minutes == other.minutes){
				return seconds >= other.seconds;
			}
			return minutes > other.minutes;
		}
		return hours > other.hours;
	}
	
	public boolean isInferior(Time other){
		return isInferior(other, false);
	}
	
	public boolean isInferior(Time other, boolean countDay){
		if(countDay){
			if (nbDays > other.nbDays){
				return false;
			}
			
			if(nbDays < other.nbDays){
				return true;
			}
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
		if(countDay){
			if (nbDays > other.nbDays){
				return true;
			}
			
			if(nbDays < other.nbDays){
				return false;
			}
		}
		if(hours == other.hours){
			if(minutes == other.minutes){
				return seconds > other.seconds;
			}
			return minutes > other.minutes;
		}
		return hours > other.hours;
	}
	
	public int getDays(){
		return nbDays;
	}
	
	public int getHours() {
		return hours;
	}

	public void setHours(int hours) {
		this.hours = hours%24;
	}

	public int getMinutes() {
		return minutes;
	}

	public void setMinutes(int minutes) {
		this.minutes = minutes%60;
	}

	public int getSeconds() {
		return seconds;
	}

	public void setSeconds(int seconds) {
		this.seconds = seconds%60;
	}

	@Override
	public String toString(){
		return hours + ":" + minutes + ":" + seconds;
	}

}
