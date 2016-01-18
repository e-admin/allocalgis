package org.agil.core.coverage;

import java.awt.image.BufferedImage;

import com.vividsolutions.jts.geom.Envelope;

/**
 *  Esta interfaz define el esqueleto basico de todas aquellas clases que
 *  permiten acceder a datos de imagenes Raster. Las clases que la implementen
 *  se encargarán de especializarse en funcion de los detalles específicos de
 *  cada fuente de datos (formato de fichero, protocolo de acceso en el caso
 *  de que se trate de una imagen remota, etc,etc)
 *
 *@author     ogomez
 *@created    17 de septiembre de 2003
 */
public interface ImageDataAccesor {

	/**
	 *  Permite recuperar, desde su origen persistente, un fragmento de imagen
	 *  con las dimensiones en pixels solicitadas.
	 *
	 *@param  envelope
	 *@param  width     Description of the Parameter
	 *@param  height    Description of the Parameter
	 *@return           The imagen value
	 *@roseuid          3F68727B01F4
	 */
	public BufferedImage getImagen(Envelope envelope, int width, int height);


	/**
	 *  Devuelve el envelope de los datos raster a los que accede.
	 *
	 *@return    envelope rectangulo que envuelve a los datos raster servidos.
	 */
	public Envelope getEnvelope();
}
