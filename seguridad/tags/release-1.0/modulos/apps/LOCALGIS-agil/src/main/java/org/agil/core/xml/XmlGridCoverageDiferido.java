package org.agil.core.xml;

import java.util.Collection;
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.agil.core.coverage.GridCoverageExchange;
import org.agil.core.coverage.GridCoverage;
import org.agil.core.coverage.TfwNoAvailableException;

/**
 *  Parser que nos permite procesar documentos XML de definicion de origenes
 *  de datos raster, tal que así: <GridCoverages> <GridCoverage id="22"
 *  name="Raster1" url="multi-earth-image|ecw|RGB|pathDirectorio" />
 *  </GridCoverages> A diferencia del parser XmlGridCoverage, que va
 *  construyendo los origenes de datos segun los procesa (ideal para
 *  servidores), este parser construye representaciones en memoria, y no
 *  construye los origenes de datos hasta que no se le solicite
 *
 *@author    alvaro zabala 29-sep-2003
 */
public class XmlGridCoverageDiferido extends DefaultHandler {

	private HashMap metadata_name;


	/**
	 *  Constructor
	 */
	public XmlGridCoverageDiferido() {
		metadata_name = new HashMap();
	}


	/**
	 *  Gets the Coverage attribute of the XmlGridCoverageDiferido object
	 *
	 *@param  metadata  Description of Parameter
	 *@return           The Coverage value
	 */
	public GridCoverage getCoverage(GridCoverageMetaData metadata) throws TfwNoAvailableException {
		GridCoverage coverage = GridCoverageExchange.getInstance().createFromName(metadata.getUrlCoverage());
		coverage.setIdentificador(metadata.getId());
		return coverage;
	}


	/**
	 *  Gets the Coverage attribute of the XmlGridCoverageDiferido object
	 *
	 *@param  coverageName  Description of Parameter
	 *@return               The Coverage value
	 */
	public GridCoverage getCoverage(String coverageName) throws TfwNoAvailableException {
		GridCoverageMetaData metadataCoverage = (GridCoverageMetaData) metadata_name.get(coverageName);
		return getCoverage(metadataCoverage);
	}


	/**
	 *  Devuelve una coleccion con todas las entradas del documento XML parseado.
	 *
	 *@return    coleccion con todas las entradas almacenadas
	 */
	public Collection getEntrys() {
		return metadata_name.values();
	}


	/**
	 *  Procesa eventos de inicio de etiqueta XML.
	 *
	 *@param  namespaceURI      Description of Parameter
	 *@param  localName         Description of Parameter
	 *@param  name              Description of Parameter
	 *@param  attrs             Description of Parameter
	 *@exception  SAXException  Description of Exception
	 *@see                      org.xml.sax.ContentHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	public void startElement(String namespaceURI, String localName, String name, Attributes attrs) throws SAXException {
		if (name.equalsIgnoreCase("GridCoverage")) {
			leeGridCoverage(attrs);
		}
	}


	/**
	 *  Se encarga de procesar la etiqueta <GridCoverage />
	 *
	 *@param  attrs  atributos de la etiqueta
	 */
	private void leeGridCoverage(Attributes attrs) {
		GridCoverageMetaData metaData = new GridCoverageMetaData();
		for (int i = 0; i < attrs.getLength(); i++) {
			String nattr = attrs.getQName(i);
			String val = attrs.getValue(i);
			if (nattr.equalsIgnoreCase("id")) {
				metaData.setId(Integer.parseInt(val));
			}
			else if (nattr.equalsIgnoreCase("url")) {
				metaData.setUrlCoverage(val);
			}
			else if (nattr.equalsIgnoreCase("name")) {
				metaData.setCoverageName(val);
			}
		}
		//for

		metadata_name.put(metaData.getCoverageName(), metaData);

	}
}

/**
 *  Contiene metadatos sobre las entradas GridCoverage en el documento xml
 *  parseado.
 *
 *@author    Chris Seguin
 */
class GridCoverageMetaData {


	/**
	 *  identificador del coverage
	 */
	private int id;
	/**
	 *  url que describe el origen de datos de coverages
	 */
	private String urlCoverage;
	/**
	 *  nombre descriptor de la cobertura
	 */
	private String coverageName;


	/**
	 *  Constructor for the GridCoverageMetaData object
	 *
	 *@param  idCoverage   Description of Parameter
	 *@param  urlCoverage  Description of Parameter
	 *@param  name         Description of Parameter
	 */
	public GridCoverageMetaData(int idCoverage, String urlCoverage, String name) {
		this.id = idCoverage;
		this.urlCoverage = urlCoverage;
		this.coverageName = name;
	}


	/**
	 *  Constructor for the GridCoverageMetaData object
	 */
	public GridCoverageMetaData() {
	}


	/**
	 *  setea el id de coverage
	 *
	 *@param  i
	 */
	public void setId(int i) {
		id = i;
	}


	/**
	 *  establece la url de la coverage
	 *
	 *@param  string
	 */
	public void setUrlCoverage(String string) {
		urlCoverage = string;
	}


	/**
	 *  establece el nombre de la cobertura
	 *
	 *@param  string  nombre de la cobertura
	 */
	public void setCoverageName(String string) {
		coverageName = string;
	}


	/**
	 *  devuelve el id de coveraeg
	 *
	 *@return
	 */
	public int getId() {
		return id;
	}


	/**
	 *  devuelve el url del origen de datos de la coverage
	 *
	 *@return
	 */
	public String getUrlCoverage() {
		return urlCoverage;
	}


	/**
	 *  devuelve el nombre de la cobertura
	 *
	 *@return    nombre de la cobertura
	 */
	public String getCoverageName() {
		return coverageName;
	}


	/**
	 *  Converts to a String representation of the object.
	 *
	 *@return    A string representation of the object.
	 */
	public String toString() {
		return coverageName;
	}

}
