package org.agil.core.jump.coverage.datasource;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXParseException;
import org.agil.core.xml.XmlGridCoverageDiferido;
import org.agil.core.coverage.Coverage;
import org.agil.core.coverage.TfwNoAvailableException;

/**
 *@author    alvaro zabala 29-sep-2003
 */
public class CatalogoCoverageLocal implements CatalogoCoverageIF {
	/**
	 *  parser que procesa el documento XML, guardando metadatos sobre sus
	 *  entradas
	 */
	private XmlGridCoverageDiferido metadatosXML;


	/**
	 *  Constructor: recibe la ruta donde se encuentra el fichero XML donde se
	 *  encuentran todos los origenes de datos raster.
	 *
	 *@param  pathCatalogo  ruta del documento XML.
	 */
	public CatalogoCoverageLocal(String pathCatalogo) {
		try {
			FileInputStream input = new FileInputStream(pathCatalogo);
			parsearFicheroCatalogo(input);
		}
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}


	/*
	 *  (non-Javadoc)
	 *  @see org.agil.kernel.jump.datasource.coverage.CatalogoCoverageIF#getEntrys()
	 */
	/**
	 *  Gets the Entrys attribute of the CatalogoCoverageLocal object
	 *
	 *@return    The Entrys value
	 */
	public Collection getEntrys() {
		return metadatosXML.getEntrys();
	}


	/*
	 *  (non-Javadoc)
	 *  @see org.agil.kernel.jump.datasource.coverage.CatalogoCoverageIF#getCoverage(java.lang.String)
	 */
	/**
	 *  Gets the Coverage attribute of the CatalogoCoverageLocal object
	 *
	 *@param  entry  Description of Parameter
	 *@return        The Coverage value
	 */
	public Coverage getCoverage(String entry)  {
		try {
			return metadatosXML.getCoverage(entry);
		} catch (TfwNoAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}


	/**
	 *  parsea el documento xml que contiene la definicion del Catalogo
	 *
	 *@param  path  Description of Parameter
	 */
	private void parsearFicheroCatalogo(InputStream path) {
		try {
			metadatosXML = new XmlGridCoverageDiferido();
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			saxParser.parse(path, metadatosXML);
		}
		catch (SAXParseException spe) {
			System.out.println("Error leyendo el fichero de elementos");
			System.out.println("\n** Parsing error"
					+ ", line " + spe.getLineNumber()
					+ ", uri " + spe.getSystemId());
			System.out.println("   " + spe.getMessage());
			spe.printStackTrace();
		}
		catch (Exception e) {
			System.out.println("Error leyendo el fichero de elementos");
			e.printStackTrace();
		}
		//catch
	}
}
