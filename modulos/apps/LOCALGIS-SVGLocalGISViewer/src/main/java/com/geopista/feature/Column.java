/**
 * Column.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.feature;

/**
 * @author juacas Descripción de una columna del modelo de base de datos
 */
public class Column {
	/**
	 *  
	 */
	private String name;
	private String description;
	private Domain domain;

	public Column ()
	{
		super();    
	}

	public Column(String name, String description, Domain domain) {
		super();
		this.name=name;
		this.description=description;
		this.domain=domain;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the domain.
	 */
	public Domain getDomain() {
		return domain;
	}
	/**
	 * @param domain The domain to set.
	 */
	public void setDomain(Domain domain) {
		this.domain = domain;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	public String toString()
	{
		return getName();
	}


}
