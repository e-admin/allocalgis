/**
 * DiscreteC_Function.java
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
 *  Una funcion discreta, frente a su homologa C_Function, se caracteriza
 *  porque su dominio no es continuo. (OGC Coverage Type and Subtypes)
 *
 *@author     alvaro zabala
 *@version    1.1
 */
public abstract class DiscreteC_Function
		 implements C_FunctionIF {

	/**
	 *@roseuid    3F6EA26502BF
	 */
	public DiscreteC_Function() {

	}


	/**
	 *  Devuelve la cardinalidad del dominio de la funcion (1 si es para lineas,
	 *  como por ejemplo un perfil topografico, 2 para superficies, etc etc)
	 *
	 *@return     int
	 *@roseuid    3F6EA26502CE
	 */
	public abstract int num();


	/**
	 *@param  geometry  - geometria para la que se quiere evaluar la funcion
	 *@return           java.util.List
	 *@roseuid          3F6EA265032C
	 */
	public abstract List evaluate(Geometry geometry);


	/**
	 *  Devuelve la imagen completa de la funcion (todos los valores asociados a
	 *  los puntos discretos de su dominio)
	 *
	 *@return     java.util.List[]
	 *@roseuid    3F6EE652009C
	 */
	public abstract List[] values();


	/**
	 *  Devuelve las geometrias discretas del dominio de la funcion
	 *
	 *@return     com.vividsolutions.jts.geom.Geometry[]
	 *@roseuid    3F6EE6520109
	 */
	public abstract Geometry[] domain();
}
