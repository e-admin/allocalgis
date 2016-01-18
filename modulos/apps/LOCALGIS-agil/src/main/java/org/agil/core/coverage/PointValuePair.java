/**
 * PointValuePair.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.agil.core.coverage;

import java.util.List;

import com.vividsolutions.jts.geom.Point;

/**
 *  Par ordenado donde el primer elemento es un Punto del dominio de la
 *  funcion a la que está asociado y el segundo es un valor (vector) que
 *  expresa el valor de la función
 *
 *@author     alvaro zabala
 *@version    1.1
 */
public class PointValuePair
		 extends GeometryValuePair {

	/**
	 *  punto del dominio discreto de la funcion (variable independiente)
	 */
	private Point point;

	/**
	 *  Valor asociado al punto (vector de valores)
	 */
	private List value;


	/**
	 *@roseuid    3F6EA267006D
	 */
	public PointValuePair() {

	}
}
