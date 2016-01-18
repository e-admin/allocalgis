package es.satec.localgismobile.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.deegree.framework.xml.XMLTools;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.japisoft.fastparser.Parser;
import com.japisoft.fastparser.document.Document;
import com.japisoft.fastparser.dom.DomNodeFactory;

import es.satec.localgismobile.fw.Global;

public class Applications {

	private static Applications instance;
	
	private Vector applications;
	
	private static Logger logger = Global.getLoggerFor(Applications.class);
	
	private static final String APPLICATIONS_CONFIG_FILE = "applications.xml";
	
	private Applications() {
		applications = new Vector();

		// Fichero de configuracion de las aplicaciones
		String filePath = Global.APP_PATH + File.separator + Global.CONFIG + File.separator + APPLICATIONS_CONFIG_FILE;
		FileInputStream fis = null;
		try {
			logger.debug("Interpretando fichero de aplicaciones");
			// Parsing del fichero
			fis = new FileInputStream(filePath);
			Parser p = new Parser();
			p.setNodeFactory(new DomNodeFactory());
			p.setInputStream(fis);
			p.parse();
			Document doc = p.getDocument();
			Element root = (Element) doc.getRoot();
			// Aplicaciones
			NodeList apps = root.getChildNodes();
			for (int i=0; i<apps.getLength(); i++) {
				if (apps.item(i) instanceof Element) {
					Element app = (Element) apps.item(i);
					String name = XMLTools.getRequiredAttrValue("name", app);
					String layerId = XMLTools.getRequiredAttrValue("layerId", app);
					//String keyAttribute = XMLTools.getRequiredAttrValue("keyAttribute", app);
					ArrayList keyAttribute = XMLTools.getRequiredAttrValues("keyAttribute", app);
					
					//keyAttribute = URLDecoder.decode(keyAttribute, "UTF-8");
					logger.debug("Aplicacion " + name + " capa: " + layerId + " clave: " + keyAttribute);
					// Opciones de cada aplicacion
					NodeList options = app.getChildNodes();
					Vector appOptions = new Vector();
					Application application = new Application(name, layerId, keyAttribute, appOptions);
					for (int j=0; j<options.getLength(); j++) {
						if (options.item(j) instanceof Element) {
							Element option = (Element) options.item(j);
							String messageKey = XMLTools.getRequiredAttrValue("messageKey", option);
							String screenClass = XMLTools.getRequiredAttrValue("class", option);
							Hashtable params = new Hashtable();
							logger.debug("Opción mensaje: " + messageKey + " pantalla: " + screenClass);
							// Parametros de la opcion
							NodeList paramNodes = option.getChildNodes();
							for (int k=0; k<paramNodes.getLength(); k++) {
								if (paramNodes.item(k) instanceof Element) {
									Element paramNode = (Element) paramNodes.item(k);
									String paramName = XMLTools.getRequiredAttrValue("name", paramNode);
									String paramValue = XMLTools.getRequiredAttrValue("value", paramNode);
									logger.debug("Parámetro: " + paramName + "=" + paramValue);
									params.put(paramName, paramValue);
								}
							}
							appOptions.addElement(new ApplicationOption(application, messageKey, screenClass, params));
						}
					}
					applications.addElement(application);
				}
			}
		} catch (Exception e) {
			logger.error("Error al interpretar el fichero de aplicaciones", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {}
			}
		}

	}
	
	public static Applications getInstance() {
		if (instance == null) {
			instance = new Applications();
		}
		return instance;
	}
	
	public Vector getApplications() {
		return applications;
	}

	public Application getApplicationByName(String name) {
		Enumeration e = applications.elements();
		while (e.hasMoreElements()) {
			Application app = (Application) e.nextElement();
			if (app.getName().equals(name)) {
				return app;
			}
		}
		return null;
	}

	public Vector getApplicationsByLayerId(String layerId) {
		Vector apps = new Vector();
		Enumeration e = applications.elements();
		while (e.hasMoreElements()) {
			Application app = (Application) e.nextElement();
			if (app.getLayerId().equals(layerId)) {
				apps.addElement(app);
			}
		}
		return apps;
	}
	
	public Application getApplicationByLayerId(String layerId) {		
		Enumeration e = applications.elements();
		while (e.hasMoreElements()) {
			Application app = (Application) e.nextElement();
			if (app.getLayerId().equals(layerId)) {
				return app;
			}
		}
		return null;
	}
}
