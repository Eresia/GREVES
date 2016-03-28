package ucp.greves.controller;
import java.util.ArrayList;

import ucp.greves.data.line.canton.Canton;
import ucp.greves.model.line.Line;
public class CantonController {
	
	public static ArrayList<Integer> IntegerlistOfCantonID(){
		return new ArrayList<Integer>(Line.getCantons().keySet());
	}

	public static Canton getCantonById(int id){
		return Line.getCantons().get(id);
	}
}
