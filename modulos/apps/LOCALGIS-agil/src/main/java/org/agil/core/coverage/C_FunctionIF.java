/**
 * C_FunctionIF.java
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
 *
 */
public interface C_FunctionIF {

	/**
	 *  Evalua el punto especificado
	 *
	 *@param  punto  - Punto para el que se evalua la funcion
	 *@return        java.util.List
	 *@roseuid       3F6F1D17000F
	 */
	public List evaluate(Point punto);


	/**
	 *@param  width
	 *@param  height
	 *@param  envelope
	 *@return           java.awt.image.BufferedImage
	 *@roseuid          3F6F1D170138
	 */
	public BufferedImage getImage(int width, int height, Envelope envelope);
}
