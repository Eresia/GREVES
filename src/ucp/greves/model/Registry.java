package ucp.greves.model;

import java.util.HashMap;

public class Registry {

	private static int canton_id_register = 0 ;
	private static int railway_id_register = 0 ;
	private static HashMap<Integer, Canton> canton_registry = new HashMap<Integer, Canton>();
	
	public synchronized static int register_canton(Canton canton){
		canton_id_register++;
		canton_registry.put(canton_id_register, canton);
		return canton_id_register;
		
	}
	
	public synchronized static int register_railway(){
		railway_id_register++;
		return railway_id_register;
	}
}
