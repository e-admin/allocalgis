/**
 * EIELMapTask.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.model;

import com.vividsolutions.jump.workbench.model.Task;

/**Representa un mapa externo o mapa de la EIEL
 * @author sgrodriguez
 *
 */
public class EIELMapTask extends Task{
	//id del mapa en la base de datos de la EIEL
	private String idMap;

	public String getIdMap() {
		return idMap;
	}

	public void setIdMap(String idMap) {
		this.idMap = idMap;
	}
	
	

}
