/**
 * DiscretePointC_Function.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.agil.core.coverage;

/**
 *  Segun OGC, una funcion discreta cuyo dominio esta formado por geometrias
 *  puntuales (de esta forma, la funcion de la Coverage puede representarse
 *  mediante una lista de puntos-valor (siendo el valor el vector o evaluacion
 *  de la funcion en el punto)
 *
 *@author     alvaro zabala
 *@version    1.1
 */
public abstract class DiscretePointC_Function
		 extends DiscreteC_Function {
	/**
	 *  Description of the Field
	 */
	protected PointValuePair definingSet;


	/**
	 *@roseuid    3F6EA2680148
	 */
	public DiscretePointC_Function() {

	}


	/**
	 *  Sets the value of the definingSet property.
	 *
	 *@param  aDefiningSet  the new value of the definingSet property
	 *@roseuid              3F6EE65A03C8
	 */
	public void setDefiningSet(PointValuePair aDefiningSet) {
		definingSet = aDefiningSet;
	}


	/**
	 *  Access method for the definingSet property.
	 *
	 *@return     the current value of the definingSet property
	 *@roseuid    3F6EE65A036B
	 */
	public PointValuePair getDefiningSet() {
		return definingSet;
	}
}
