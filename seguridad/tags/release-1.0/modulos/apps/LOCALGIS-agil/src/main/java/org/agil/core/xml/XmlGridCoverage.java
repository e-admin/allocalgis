package org.agil.core.xml;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;
import org.xml.sax.Attributes;
import org.agil.core.coverage.GridCoverageExchange;
import org.agil.core.coverage.GridCoverageRepositoryIF;
import org.agil.core.coverage.GridCoverage;
import org.agil.core.coverage.TfwNoAvailableException;

/**
 *  Clase encargada de procesar documentos XML que contienen la definicion de
 *  GridCoverages. Es de utilidad a la hora de definir de forma sencilla los
 *  origenes de datos que puede cargar una aplicacion. La estructura del
 *  documento XML procesado es la siguiente: <GridCoverages> <GridCoverage
 *  id="22" xmin="0" xmax="222" ymin="0" ymax="444"
 *  url="multi-earth-image|ecw|RGB|pathDirectorio" /> </GridCoverages>
 *
 *@author    alvaro zabala 26-sep-2003
 */
public class XmlGridCoverage extends DefaultHandler {

	/**
	 *  repositorio de GridCoverages
	 */
	private GridCoverageRepositoryIF coverageRepository;


	/**
	 *  Constructor. Recibe el repositorio donde se iran guardando los
	 *  GridCoverages leidos
	 *
	 *@param  coverageRepository  repositorio que ira guardando los gridCoverage
	 *      leidos
	 */
	public XmlGridCoverage(GridCoverageRepositoryIF coverageRepository) {
		this.coverageRepository = coverageRepository;
	}


	/**
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
	 *  Metodo encargado de procesar la etiqueta XML <GridCoverage>
	 *
	 *@param  attrs
	 */
	private void leeGridCoverage(Attributes attrs) {
		int idCoverage = -1;
//		double xmin = -1, xmax = -1, ymin = -1, ymax = -1;
		String url = null;
		String name = null;

		for (int i = 0; i < attrs.getLength(); i++) {
			String nattr = attrs.getQName(i);
			String val = attrs.getValue(i);
			if (nattr.equalsIgnoreCase("id")) {
				idCoverage = Integer.parseInt(val);
			}
			else if (nattr.equalsIgnoreCase("url")) {
				url = val;
			}
			else if (nattr.equalsIgnoreCase("name")) {
				name = val;
			}
			/*
			 *  else if(nattr.equalsIgnoreCase("xmin")){
			 *  xmin = Double.parseDouble(val);
			 *  }else if(nattr.equalsIgnoreCase("ymin")){
			 *  ymin = Double.parseDouble(val);
			 *  }else if(nattr.equalsIgnoreCase("xmax")){
			 *  xmax = Double.parseDouble(val);
			 *  }else if(nattr.equalsIgnoreCase("ymax")){
			 *  ymax = Double.parseDouble(val);
			 *  }else if(nattr.equalsIgnoreCase("url")){
			 *  ymax = Double.parseDouble(val);
			 *  }
			 */
		}
		//for
		/*
		 *  if(idCoverage<0 || xmin<0 || xmax<0 || ymin<0 || ymax<0 || url==null){
		 *  System.out.println("Entrada incompleta, nos la saltamos");
		 *  return;
		 *  }
		 *  Envelope envelope = new Envelope(xmin, xmax, ymin, ymax );
		 */
		GridCoverage coverage = null;
		try {
			coverage = GridCoverageExchange.getInstance().createFromName(url);
			coverage.setIdentificador(idCoverage);
			coverageRepository.addCoverage(coverage);
		} catch (TfwNoAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
