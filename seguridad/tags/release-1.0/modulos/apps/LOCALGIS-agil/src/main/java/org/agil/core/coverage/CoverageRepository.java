package org.agil.core.coverage;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.agil.core.xml.XmlGridCoverage;
import org.xml.sax.SAXParseException;

import com.vividsolutions.jts.geom.Envelope;

/**
 *  <p>
 *
 *  Título: CoverageRepository </p> <p>
 *
 *  Descripción: Clase especializada que se encarga de gestionar las Coverages
 *  cargadas en el sistema. Debe ser un Singleton, pues nos interesa que
 *  recursos tan costosos como las imagenes se gestionen de forma
 *  centralizada. </p> <p>
 *
 *  Copyright: Que tal LGPL?</p> <p>
 *
 *  Empresa: </p>
 *
 *@author     azabala
 *@version    1.0
 */

public class CoverageRepository
		 implements GridCoverageRepositoryIF {
	/**
	 *  Graphics que nos permite dibujar
	 *
	 *@todo    QUITAR ESTO DE AQUI. YA HACE FALTA SER CUTRE. LLEVAR EL
	 *      GISGRAPHICS A UN COMANDO, O LO QUE SEA.
	 */
	private GISGraphics graphics;

	/**
	 *  Repositorio que tiene todas las Coverages manejadas por el servidor
	 */
	private HashMap imagenes;

	private static CoverageRepository instance = new CoverageRepository();


	/**
	 *  Constructor. Crea un dispositivo de renderizado.
	 */
	private CoverageRepository() {
		graphics = new GISGraphics();
		imagenes = new HashMap();
	}


	/**
	 *  Devuelve el envelope de una cobertura
	 *
	 *@param  idCoverage  identificador de la cobertura cuyo envelope se quiere
	 *      obtener
	 *@return             envelope solicitado
	 */
	public Envelope getEnvelope(long idCoverage) {
		Envelope solucion = null;
		GridCoverage coverage = (GridCoverage) imagenes.get(new Long(idCoverage));
		if (coverage != null) {
			solucion = coverage.getEnvelope();
		}
		return solucion;
	}


	/*
	 *  permite acceder al coverage cuyo identificador se proporciona como parametro
	 *  @param idCoverage identificador de la GridCoverage solicitada
	 *  @see org.agil.kernel.jump.coverage.GridCoverageRepositoryIF#getCoverage(long)
	 */
	/**
	 *  Gets the Coverage attribute of the CoverageRepository object
	 *
	 *@param  idCoverage  Description of Parameter
	 *@return             The Coverage value
	 */
	public GridCoverage getCoverage(long idCoverage) {

		return (GridCoverage) imagenes.get(new Long(idCoverage));
	}


	/**
	 *  Description of the Method
	 *
	 *@param  rect        Description of the Parameter
	 *@param  w           Description of the Parameter
	 *@param  h           Description of the Parameter
	 *@param  idCoverage  Description of Parameter
	 *@return             Description of the Return Value
	 */
	public BufferedImage procesa(Envelope rect, int w, int h, long idCoverage) {

		//preparamos el graphics para que sea capaz de dibujar sobre una imagen en memoria
		BufferedImage solucion = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics g = solucion.getGraphics();
		graphics.setRect(rect);
		graphics.setAlto(h);
		graphics.setAncho(w);
		graphics.setGraphics(g);

		//recuperamos la Coverage solicitada
		GridCoverage coverage = (GridCoverage) imagenes.get(new Long(idCoverage));
		if (coverage == null) {
			return null;
		}
		Envelope imageEnvelope = coverage.getEnvelope();

		//comprobamos que el area solicitada solapa con la zona de la imagen
		if (!rect.intersects(imageEnvelope)) {
			return null;
			//era un mal candidato
		}
		graphics.drawImagen(coverage);
		return solucion;
	}


	/**
	 *  anyade una Coverage a la lista de coverages servidas por el servidor (ojo
	 *  que este servidor solo sirve Coverages. Habria que hacer un Servidor de
	 *  Features)
	 *
	 *@param  gridCoverage  coverage añadida al repositorio
	 *@see                  org.agil.kernel.jump.coverage.GridCoverageRepositoryIF#addCoverage(org.agil.kernel.jump.coverage.GridCoverage)
	 */
	public void addCoverage(GridCoverage gridCoverage) {
		Long key = new Long(gridCoverage.getIdentificador());
		imagenes.put(key, gridCoverage);

	}


	/**
	 *  parsea el documento xml que contiene la definicion del Catalogo
	 *
	 *@param  path  Description of Parameter
	 */
	public void parsearFicheroCatalogo(InputStream path) {
		try {
			XmlGridCoverage parseador = new XmlGridCoverage(this);
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			saxParser.parse(path, parseador);
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


	/**
	 *  Devuelve la unica instancia que puede existir de esta clase
	 *
	 *@return    instancia unica -singleton
	 */
	public static CoverageRepository getInstance() {
		return instance;
	}
}
