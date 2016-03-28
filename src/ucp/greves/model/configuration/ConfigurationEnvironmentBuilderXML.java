package ucp.greves.model.configuration;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ucp.greves.data.time.Time;

/**
 * This class is used to build the ConfigurationEnvironment from a .xml configuration file.
 * 
 * @author REGNIER Antoine
 * 
 * @see ucp.greves.model.configuration.ConfigurationEnvironmentBuilderJSON for the .json version.
 */
public class ConfigurationEnvironmentBuilderXML {

	public final static String DEFAULT_FOLDER = "Configuration/";

	/**
	 * Calls the build function with the specified file. Uses the current ConfigurationEnvironment.
	 * 
	 * @param filename
	 * 			(String) Name of the .xml file in which the configuration to load is contained.
	 * 
	 * @see	ConfigurationEnvironmentBuilderXML#build(String, ConfigurationEnvironment)
	 */
	public static void BuildEnvironment(String filename) {
		build(filename, ConfigurationEnvironment.getInstance());
	}

	/**
	 * Calls the build function with the specified file and ConfigurationEnvironment.
	 * 
	 * @param filename
	 * 			(String) Name of the .xml file in which the configuration to load is contained.
	 * @param configuration
	 * 			(ConfigurationEnvironment) ConfigurationEnvironment in which the configuration
	 * 			of the given file has to be loaded.
	 * 
	 * @see	ConfigurationEnvironmentBuilderXML#build(String, ConfigurationEnvironment)
	 * @see ConfigurationEnvironment
	 */
	public static void BuildEnvironment(String filename, ConfigurationEnvironment configuration) {
		build(filename, configuration);
	}

	/**
	 * Loads the data contained in the specified .xml configuration file into
	 *  the specified ConfigurationEnvironment.
	 * 
	 * @param filename
	 * 			(String) Name of the .xml file in which the configuration to load is contained.
	 * @param configuration
	 * 			(ConfigurationEnvironment) ConfigurationEnvironment in which the configuration
	 * 			of the given file is loaded.
	 * 
	 * @see ConfigurationEnvironment
	 */
	private static void build(String filename, ConfigurationEnvironment configuration) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;

		try {
			dBuilder = dbFactory.newDocumentBuilder();

			Document doc = (Document) dBuilder.parse(DEFAULT_FOLDER + filename);
			NodeList configuration_list = doc.getElementsByTagName("property");
			for (int i = 0; i < configuration_list.getLength(); i++) {
				Element el = (Element) configuration_list.item(i);
				String type = el.getAttribute("type").toLowerCase();
				switch (type) {
				case "integer":
					configuration.setProperty(el.getAttribute("name").toUpperCase(),
							Integer.parseInt(el.getAttribute("value")));
					break;
				case "boolean":
					String bool = (el.getAttribute("value").toLowerCase());
					if (bool.equalsIgnoreCase("true") || bool.equalsIgnoreCase("1")) {
						configuration.setProperty(el.getAttribute("name").toUpperCase(), true);
					} else {
						configuration.setProperty(el.getAttribute("name").toUpperCase(), false);
					}
					break;
				case "double":
					configuration.setProperty(el.getAttribute("name").toUpperCase(),
							Double.parseDouble(el.getAttribute("value")));
					break;
				case "string":
					configuration.setProperty(el.getAttribute("name").toUpperCase(), el.getAttribute("value"));
					break;
				case "time":
					String[] sTime = el.getAttribute("value").split(":");
					if(sTime.length == 3){
						Time time = new Time(Integer.valueOf(sTime[0]), Integer.valueOf(sTime[1]), Integer.valueOf(sTime[2]));
						configuration.setProperty(el.getAttribute("name").toUpperCase(), time);
					}

				default:
					break;
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
