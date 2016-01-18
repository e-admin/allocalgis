package org.agil.core.coverage;

import java.awt.image.BufferedImage;

import com.vividsolutions.jts.geom.Envelope;

/**
 *  ImageDataAccesor que permite acceder a origenes de datos raster en formato
 *  TIFF
 *
 *@author     alvaro zabala
 *@created    17 de septiembre de 2003
 */
public class TiffImageDataAccesor implements ImageDataAccesor {

	/**
	 *  Permite recuperar un fragemento de imagen de su origen persistente TIFF
	 *
	 *@param  envelope  ventana geografica de la zona que se pretende recuperar
	 *@param  width     ancho en pixels de la imagen devuelta
	 *@param  height    alto en pixels
	 *@return           imagen de la zona solicitada
	 *@see              org.agil.kernel.jump.coverage.ImageDataAccesor#getImagen(com.vividsolutions.jts.
	 *      geom.Envelope, int, int)
	 *@roseuid          3F70387B00DA
	 */
	public BufferedImage getImagen(Envelope envelope, int width, int height) {
		// TODO Auto-generated method stub
		return null;
	}


	/*
	 *  (non-Javadoc)
	 *  @see org.agil.kernel.jump.coverage.ImageDataAccesor#getEnvelope()
	 */
	/**
	 *  Gets the Envelope attribute of the TiffImageDataAccesor object
	 *
	 *@return    The Envelope value
	 */
	public Envelope getEnvelope() {
		// TODO Auto-generated method stub
		return null;
	}
}
