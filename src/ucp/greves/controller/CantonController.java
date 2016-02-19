package ucp.greves.controller;
import ucp.greves.model.line.Line;
import ucp.greves.model.line.canton.*;

import java.util.ArrayList;
public class CantonController {
	private ArrayList<Canton> CantonList;
	

	public Canton getCantonById(int id){
		return Line.getCantons().get(id);
	}
}
