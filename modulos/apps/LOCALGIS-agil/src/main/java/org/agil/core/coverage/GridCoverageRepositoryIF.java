/**
 * GridCoverageRepositoryIF.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.agil.core.coverage;

/**
 *  Interfaz que deben implementar todas aquellas clases que sean repositorio
 *  de GridCoverages, es decir, que guarden y permitan acceder a coberturas.
 *
 *@author    alvaro zabala 26-sep-2003
 */
public interface GridCoverageRepositoryIF {
	/**
	 *  Metodo que anyade una GridCoverage al repositorio
	 *
	 *@param  gridCoverage  The feature to be added to the Coverage attribute
	 */
	public void addCoverage(GridCoverage gridCoverage);


	/**
	 *  Permite recuperar un GridCoverage a partir de su identificador
	 *
	 *@param  idCoverage  Description of Parameter
	 *@return             The Coverage value
	 */
	public GridCoverage getCoverage(long idCoverage);
}
