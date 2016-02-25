package ucp.greves.model.exceptions.railway;

public class PathNotExistException extends Exception{

	public PathNotExistException(){
		super();
	}
	
	public PathNotExistException(String s){
		super(s);
	}
}
