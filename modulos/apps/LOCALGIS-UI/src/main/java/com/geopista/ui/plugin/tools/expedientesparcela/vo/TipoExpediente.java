/**
 * TipoExpediente.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.tools.expedientesparcela.vo;

public class TipoExpediente {
	
	private Integer idTipoExpediente;
	private Integer idDictionary;
	private String descripcion;
	
	
	public TipoExpediente() {
		super();
	}

	public Integer getIdTipoExpediente() {
		return idTipoExpediente;
	}
	
	public void setIdTipoExpediente(Integer idTipoExpediente) {
		this.idTipoExpediente = idTipoExpediente;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getIdDictionary() {
		return idDictionary;
	}

	public void setIdDictionary(Integer idDictionary) {
		this.idDictionary = idDictionary;
	}
	
}
