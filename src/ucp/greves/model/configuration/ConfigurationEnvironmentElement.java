package ucp.greves.model.configuration;


public class ConfigurationEnvironmentElement<T> {
	private  Class<T> type ;
	T value;
	
	public ConfigurationEnvironmentElement( T element){
		this.type = (Class<T>)element.getClass();
		this.value = element;
	}

	
	
	
	public T getValue(){
		return this.value;
	}
	public Class<T> getType(){
		return this.type;
	}
	public void SetValue(T value){
		this.value = value;
	}
}
