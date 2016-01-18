/**
 * TiffImageDataAccesor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
