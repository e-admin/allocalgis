/**
 * EarthImage.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.agil.core.coverage;

import java.awt.image.BufferedImage;
import java.util.List;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Point;

/**
 *  Función que representa una imagen de satelite, que mide la luminancia
 *  (función de distribución bidimensional de la iluminación)
 *
 *@author     alvaro zabala
 *@version    1.1
 */
public class EarthImage extends C_Function {

	/**
	 *  Se encarga de acceder a los datos de la imagen almacenados
	 *  persistentemente (o de solicitarlos a un servidor de datos remoto)
	 */
	private ImageDataAccesor dataAccesor;


	/**
	 *@roseuid    3F6F098303C8
	 */
	public EarthImage() {
	}


	/**
	 *  Establece la clase encargada de leer datos raster de su fuente de
	 *  almacenamiento.
	 *
	 *@param  accesor
	 *@roseuid         3F70387101A5
	 */
	public void setDataAccesor(ImageDataAccesor accesor) {
		dataAccesor = accesor;
		setDomain(dataAccesor.getEnvelope());
	}


	/**
	 *@param  width     - ancho de la imagen que se quiere obtener
	 *@param  height    - alto de la imagen que se quiere obtener
	 *@param  envelope  - ventana geografica de la que se quiere obtener una
	 *      imagen
	 *@return           java.awt.image.BufferedImage
	 *@roseuid          3F6F09BB0177
	 */
	public BufferedImage getImage(int width, int height, Envelope envelope) {
		return dataAccesor.getImagen(envelope, width, height);
	}


	/**
	 *  Devuelve la instancia de DataAccesor que lee los datos de imagenes
	 *
	 *@return     implementacion de la interfaz ImageDataAccesor
	 *@roseuid    3F7038710138
	 */
	public ImageDataAccesor getDataAccesor() {
		return dataAccesor;
	}


	/**
	 *@param  punto
	 *@return        java.util.List
	 *@roseuid       3F6F098400DA
	 */
	public List evaluate(Point punto) {
		return null;
	}
}
