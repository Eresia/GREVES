package ucp.greves.model;

import java.util.HashMap;

public class Registry {

	private static int canton_id_register = 0 ;
	private static int railway_id_register = 0 ;
	private static int train_id_register = 0;
	private static HashMap<Integer, Canton> canton_registry = new HashMap<Integer, Canton>();
	private static HashMap<Integer, Train> train_registry = new HashMap<Integer, Train>();
	
	public synchronized static int register_canton(Canton canton){
		canton_id_register++;
		canton_registry.put(canton_id_register, canton);
		return canton_id_register;
		
	}
	
	public synchronized static int register_railway(){
		railway_id_register++;
		return railway_id_register;
	}
	
	public synchronized static int register_train(Train train) {
		train_id_register++;
		train_registry.put(train_id_register, train);
		return train_id_register;
	}
}
