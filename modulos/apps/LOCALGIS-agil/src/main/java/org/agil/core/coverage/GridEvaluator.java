/**
 * GridEvaluator.java
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
 *  Representa un algoritmo de interpolacion para un Grid.
 *
 *@author     alvaro zabala
 *@version    1.1
 */
public class GridEvaluator {

	/**
	 *@roseuid    3F6EA2680203
	 */
	public GridEvaluator() {

	}


	/**
	 *  Devuelve el vector de valores asociado al punto especificado
	 *
	 *@param  punto  - punto que se desea evaluar
	 *@return        java.util.List
	 *@roseuid       3F6EA2680263
	 */
	public List evaluate(Point punto) {
		return null;
	}


	/**
	 *  Devuelve true si se ha podido inicializar correctamente
	 *
	 *@param  puntos    - Puntos para los que se tiene que calcular un modelo
	 *      numerico interpolatorio
	 *@param  valores   - Array de vectores (con la misma longitud que el array
	 *      de puntos) que contiene los vectores asociados al modelo (z, tª,
	 *      humedad, etc)
	 *@param  numFilas  - numero de filas
	 *@param  numCols   - numero de columnas
	 *@return           boolean
	 *@roseuid          3F6EE65B01B5
	 */
	public boolean initialize(Point[] puntos, List[] valores, int numFilas,
			int numCols) {
		return true;
	}
}

