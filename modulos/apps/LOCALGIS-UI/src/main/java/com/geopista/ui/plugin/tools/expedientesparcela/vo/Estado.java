/**
 * Estado.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.tools.expedientesparcela.vo;

public class Estado {
	
	private Integer idEstado;
	private Integer idDictionary;	
	private String nombreEstado;
	
	public Integer getIdEstado() {
		return idEstado;
	}
	
	public void setIdEstado(Integer idEstado) {
		this.idEstado = idEstado;
	}
	
	public String getNombreEstado() {
		return nombreEstado;
	}
	
	public void setNombreEstado(String nombreEstado) {
		this.nombreEstado = nombreEstado;
	}

	public Integer getIdDictionary() {
		return idDictionary;
	}

	public void setIdDictionary(Integer idDictionary) {
		this.idDictionary = idDictionary;
	}
	
}
