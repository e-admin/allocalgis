/**
 * ValidationError.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.feature;



public class ValidationError
{
	public Domain domain; // Dominio que a informado de este error
	public String attName; // Atributo que contiene el error
	public String message; // Descripción del error
	/**
	 * @param attname2
	 * @param description2
	 * @param domain2
	 */
	public ValidationError(String attname2, String description2, Domain domain2) {
		domain=domain2;
		attName=attname2;
		message=description2;
	}
	public String toString()
	{
	return attName+": "+message;
	}
}
