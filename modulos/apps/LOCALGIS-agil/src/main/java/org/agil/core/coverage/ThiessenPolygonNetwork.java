/**
 * ThiessenPolygonNetwork.java
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
 *  Red de poligonos de Thiessen. Es el dual de un TIN.
 *
 *@author     alvaro zabala
 *@created    22 de septiembre de 2003
 *@version    1.1
 */
public class ThiessenPolygonNetwork
		 extends C_Function {

	/**
	 *  Description of the Field
	 */
	public PointValuePair thePointValuePair;

	/**
	 *  Description of the Field
	 */
	public ThiessenPolygon theThiessenPolygon;

	private String INTERPOLATION_TYPE;
	private String TRIANGULATION_TYPE;


	/**
	 *@roseuid    3F6EA28800BB
	 */
	public ThiessenPolygonNetwork() {

	}


	/**
	 *@param  width
	 *@param  height
	 *@param  envelope
	 *@return           java.awt.image.BufferedImage
	 *@roseuid          3F6F0A92005D
	 */
	public BufferedImage getImage(int width, int height, Envelope envelope) {
		return null;
	}


	/**
	 *@param  punto
	 *@return        java.util.List
	 *@roseuid       3F6F067201E4
	 */
	public List evaluate(Point punto) {
		return null;
	}
}
