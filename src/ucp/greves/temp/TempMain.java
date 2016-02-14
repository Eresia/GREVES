package ucp.greves.temp;

import ucp.greves.model.line.Line;
import ucp.greves.model.line.LineBuilderSimple;

public class TempMain {

	public static void main(String[] args) {
		Line line = LineBuilderSimple.BuildLine();
		System.out.println(line.getTotalLenght());
	}

}
