package ucp.greves.model.configuration;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigurationEnvironmentBuilderXML {

	public final static String DEFAULT_FOLDER = "Configuration/";

	public static void BuildEnvironment(String filename) {
		build(filename, ConfigurationEnvironment.getInstance());
	}

	public static void BuildEnvironment(String filename, ConfigurationEnvironment configuration) {
		build(filename, configuration);
	}

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
					if (bool == "true" || bool == "1") {
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
