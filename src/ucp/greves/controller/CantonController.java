package ucp.greves.controller;
import java.util.ArrayList;

import ucp.greves.model.line.Line;
import ucp.greves.model.line.canton.Canton;
public class CantonController {
	
	public static ArrayList<Integer> IntegerlistOfCantonID(){
		return new ArrayList<Integer>(Line.getCantons().keySet());
	}

	public static Canton getCantonById(int id){
		return Line.getCantons().get(id);
	}
}
