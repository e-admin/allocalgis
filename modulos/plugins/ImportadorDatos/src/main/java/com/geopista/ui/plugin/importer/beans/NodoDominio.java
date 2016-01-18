/**
 * NodoDominio.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.importer.beans;

import java.io.Serializable;

public class NodoDominio implements Serializable {


	private String idDomain;
	private String patron;
	private String descripcion;


	public NodoDominio(){

	}
	
	public NodoDominio(String idDomain, String patron, String descripcion){
		this.idDomain = idDomain;
		this.patron = patron;
		this.descripcion = descripcion;
	}

	public String getIdDomain() {
		return idDomain;
	}

	public void setIdDomain(String idDomain) {
		this.idDomain = idDomain;
	}

	public String getPatron() {
		return patron;
	}

	public void setPatron(String patron) {
		this.patron = patron;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}
