/**
 * Grid.java
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
 *  Funcion definida por una coleccion finita de PointValuePairs cuyos puntos
 *  cubre el espacio formando un grid. El valor de cualquier punto interior al
 *  Grid es calculado con un GridEvaluator (que implementa un algoritmo
 *  determinado)
 *
 *@author     alvaro zabala
 *@created    22 de septiembre de 2003
 *@version    1.1
 */
public class Grid
		 extends C_Function {

	/**
	 *  Description of the Field
	 */
	public GridEvaluator theGridEvaluator;

	/**
	 *  Description of the Field
	 */
	public GridMatrixValues theGridMatrixValues;

	/**
	 *  Tipo de interpolacion a utilizar
	 */
	private String INTERPOLATION_TYPE;


	/**
	 *@roseuid    3F6EA273007D
	 */
	public Grid() {

	}


	/**
	 *@param  width
	 *@param  height
	 *@param  envelope
	 *@return           java.awt.image.BufferedImage
	 *@roseuid          3F6F0A9003C8
	 */
	public BufferedImage getImage(int width, int height, Envelope envelope) {
		return null;
	}


	/**
	 *@param  punto
	 *@return        java.util.List
	 *@roseuid       3F6F0671033C
	 */
	public List evaluate(Point punto) {
		return null;
	}
}
