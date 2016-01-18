package com.geopista.feature;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.deegree.framework.xml.XMLParsingException;
import org.deegree.framework.xml.XMLTools;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.japisoft.fastparser.ParseException;
import com.japisoft.fastparser.Parser;
import com.japisoft.fastparser.document.Document;
import com.japisoft.fastparser.dom.DomNodeFactory;

public class GeopistaSchemaFactory {

	private static Logger logger = (Logger) Logger.getInstance(GeopistaSchemaFactory.class);

	public static GeopistaSchema loadGeopistaSchema(InputStream is) throws XMLParsingException {
		logger.debug("Cargando geopista schema");
		try {
			Parser p = new Parser();
			p.setNodeFactory(new DomNodeFactory());
			p.setInputStream(is);
			p.parse();
			Document d = p.getDocument();
			// Extract the DOM root
			Element e = (Element) d.getRoot();

			GeopistaSchema geopistaSchema = new GeopistaSchema();
			
			// Attributes
			NodeList nodelist = e.getChildNodes();

			for (int i = 0; i < nodelist.getLength(); i++) {
				if ( nodelist.item( i ) instanceof Element ) {
					Element child = (Element) nodelist.item( i );

					String childName = child.getLocalName();
					if ( childName.equals( "attribute" ) ) {
						Attribute att = createAttribute(child);
						att.setSchema(geopistaSchema);
						geopistaSchema.addAttribute(att);
					}
				}
			}

			return geopistaSchema;
			
		} catch (ParseException ex) {
			throw new XMLParsingException(ex.getMessage());
		} catch (Exception e) {
			logger.error("Error al parsear el SLD");
			throw new XMLParsingException(e);
		}

	}

	private static Attribute createAttribute(Element attElement) throws XMLParsingException {
 
		
		// Name
		String name = XMLTools.getRequiredStringValue("name", attElement);
		 
		logger.debug("Creando Attribute name bytes ->" + GeopistaSchema.toHexString(name.getBytes()));
			 
		 
		// Type
		String type = XMLTools.getRequiredStringValue("type", attElement);
		// Column
		Element colElement = XMLTools.getRequiredChildElement("column", attElement);
		Column column = createColumn(colElement);
		// AccessType
		String accessType = XMLTools.getRequiredStringValue("accessType", attElement);
		// Category
		String category = null;
		Element catElement = XMLTools.getRequiredChildElement("category", attElement);
		if (catElement != null) {
			String nullAtt = catElement.getAttribute("null");
			if (nullAtt != null && nullAtt.equals("true")) {
				category = null;
			}
			else {
				category = XMLTools.getStringValue(catElement);
			}
		}
		
		Attribute att = new Attribute();
		att.setName(name);
		att.setType(type);
		att.setColumn(column);
		att.setAccessType(accessType);
		att.setCategory(category);
		return att;
	}

	private static Column createColumn(Element colElement) throws XMLParsingException {
//		logger.debug("Creando Column");
		
		// Name
		String name = XMLTools.getRequiredStringValue("name", colElement);
		// Description
		String description = XMLTools.getRequiredStringValue("description", colElement);
		// Domain
		Element domElement = XMLTools.getRequiredChildElement("domain", colElement);
		Domain domain = createDomain(domElement);

		return new Column(name, description, domain);
	}

	private static Domain createDomain(Element domElement) throws XMLParsingException {
//		logger.debug("Creando Domain");

		// Class
		String clase = domElement.getAttribute("class");
		if (clase == null || clase.equals("")) {
			return null;
		}
		else {
			// Name
			String name = XMLTools.getRequiredStringValue("name", domElement);
			// Pattern
			String pattern = XMLTools.getRequiredStringValue("pattern", domElement);
			// Description
			String description = null;
			Element descElement = XMLTools.getRequiredChildElement("description", domElement);
			if (descElement != null) {
				String nullAtt = descElement.getAttribute("null");
				if (nullAtt != null && nullAtt.equals("true")) {
					description = null;
				}
				else {
					description = XMLTools.getStringValue(descElement);
				}
			}

			Domain domain = null;
			try {
				// Crear el domain por reflection
				Class c = Class.forName(clase);
				Class[] paramTypes = {String.class, String.class };
				Constructor constructor = c.getConstructor(paramTypes);
				Object[] params = { name, description };
				domain = (Domain) constructor.newInstance(params);
				domain.setPattern(pattern);
				// Child domains
				NodeList nodelist = domElement.getChildNodes();
				for (int i = 0; i < nodelist.getLength(); i++) {
					if ( nodelist.item( i ) instanceof Element ) {
						Element child = (Element) nodelist.item( i );

						String childName = child.getLocalName();
						if ( childName.equals( "child" ) ) {
							Domain childDomain = createDomain(child);
							if (childDomain != null) {
								domain.addChild(childDomain);
							}
						}
					}
				}
			} catch (Exception e) {
				logger.error("Error al crear el dominio de la clase " + clase, e);
			}
			return domain;
		}
	}

	public static void main(String args[]) {
		try {
			Properties props = new Properties();
			props.setProperty("log4j.rootLogger", "DEBUG, A1");
			props.setProperty("log4j.appender.A1", "org.apache.log4j.ConsoleAppender");
			props.setProperty("log4j.appender.A1.layout", "org.apache.log4j.PatternLayout");
			props.setProperty("log4j.appender.A1.layout.ConversionPattern", "[%d] [%p] [%c{1}.%M()] - %m%n");
			props.setProperty("", "");
			PropertyConfigurator.configure(props);

			GeopistaSchema gs = loadGeopistaSchema(new FileInputStream("\\Tramos de abastecimiento.sch"));
			System.out.println(gs);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
