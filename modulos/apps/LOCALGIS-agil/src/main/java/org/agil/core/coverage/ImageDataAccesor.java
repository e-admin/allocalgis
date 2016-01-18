/**
 * ImageDataAccesor.java
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
