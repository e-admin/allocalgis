/**
 * GeometryValuePair.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.agil.core.coverage;

import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

/**
 *  Clase abstracta cuyas implementaciones son elementos de listas que definen
 *  las relaciones de los elementos de una funcion discreta (variables
 *  dependientes e independientes)
 *
 *@author     alvaro zabala
 *@version    1.1
 */
public abstract class GeometryValuePair {

	/**
	 *@roseuid    3F6AD0910271
	 */
	public GeometryValuePair() {

	}


	/**
	 *  devuelve la geometria -variable independiente- de la funcion
	 *
	 *@return     com.vividsolutions.jts.geom.Geometry
	 *@roseuid    3F6ACBED01D4
	 */
	public Geometry geom() {
		return null;
	}


	/**
	 *  devuelve el vector de valores asociados a una geometria
	 *
	 *@return     java.util.List
	 *@roseuid    3F6ACC1401A5
	 */
	public List value() {
		return null;
	}
}
