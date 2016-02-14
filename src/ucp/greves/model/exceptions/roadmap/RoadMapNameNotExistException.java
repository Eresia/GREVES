package ucp.greves.model.exceptions.roadmap;

public class RoadMapNameNotExistException extends BadRoadMapException {

	public RoadMapNameNotExistException(){
		super();
	}
	
	public RoadMapNameNotExistException(String s){
		super(s);
	}
}
