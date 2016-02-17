package ucp.greves.model.line.station;

public class Station {
	
	private int id;
	private String name;
	private int waitTime;
	
	public Station(String name, int waitTime){
		this.name = name;
		this.waitTime = waitTime;
	}
	
	public int getId(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public int getWaitTime(){
		return waitTime;
	}

}
