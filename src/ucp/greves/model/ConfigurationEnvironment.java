package ucp.greves.model;
import java.util.HashMap;

 public   class ConfigurationEnvironment {
	  private HashMap<String,ConfigurationEnvironmentElement> configurationAttribute ; 
		 
	 static private ConfigurationEnvironment instance;
	 private ConfigurationEnvironment() {
		 this.configurationAttribute = new HashMap<String,ConfigurationEnvironmentElement>();
		
	}
	 public static ConfigurationEnvironment getInstance(){
		 if(ConfigurationEnvironment.instance == null){
			 ConfigurationEnvironment.instance = new ConfigurationEnvironment();
		 }
		 return ConfigurationEnvironment.instance;
	 }
	 
	  public synchronized ConfigurationEnvironmentElement getProperty(String property){
		 if( !configurationAttribute.containsKey(property) ){
			  /// TODO ajouter une exeption porperty not found throw new 
		 return null;
		 }else{
			 return  configurationAttribute.get(property);
		 }
	 }
	  public synchronized void setProperty(String property, Object value){
			 if( !configurationAttribute.containsKey(property) ){
				 configurationAttribute.put(property, new ConfigurationEnvironmentElement(value));
			 }else{
				   configurationAttribute.get(property).SetValue(value);
			 }
		 
	 }
	  
	 
}
