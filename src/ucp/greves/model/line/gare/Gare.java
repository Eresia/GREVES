package ucp.greves.model.line.gare;

public class Gare {
	
	private String name;
	private int waitTime;
	
	public Gare(String name, int waitTime){
		this.name = name;
		this.waitTime = waitTime;
	}
	
	public String getName(){
		return name;
	}
	
	public int getWaitTime(){
		return waitTime;
	}

}
