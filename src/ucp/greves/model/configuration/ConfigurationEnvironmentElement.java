package ucp.greves.model.configuration;

/* TODO
 * 
 * precise Javadoc for <T>
 */

/**
 * This class permits to compose the several elements needed to configure a ConfigurationEnvironment.
 * 
 * @author REGNIER Antoine
 *
 * @param <T>
 */
public class ConfigurationEnvironmentElement<T> {
	private  Class<T> type ;
	T value;
	
	/**
	 * Initialise the values of the current ConfigurationEnvironmentElement.
	 * 
	 * @param element
	 */
	public ConfigurationEnvironmentElement(T element){
		this.type = (Class<T>)element.getClass();
		this.value = element;
	}

	/**
	 * 
	 * @return
	 * 			(T) Returns the value of the current ConfigurationEnvironmentElement.
	 */
	public T getValue(){
		return this.value;
	}
	
	/**
	 * 
	 * @return
	 * 			(Class<T>) Returns the type of the current ConfigurationEnvironmentElement.
	 */
	public Class<T> getType(){
		return this.type;
	}
	
	/**
	 * 
	 * @param value
	 * 			(T) New value of the current ConfigurationEnvironmentElement.
	 */
	public void SetValue(T value){
		this.value = value;
	}
}
