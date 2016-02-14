package ucp.greves.model.exceptions.roadmap;

public class RoadMapNameNotExistException extends BadRoadmapException {

	public RoadMapNameNotExistException(){
		super();
	}
	
	public RoadMapNameNotExistException(String s){
		super(s);
	}
}
