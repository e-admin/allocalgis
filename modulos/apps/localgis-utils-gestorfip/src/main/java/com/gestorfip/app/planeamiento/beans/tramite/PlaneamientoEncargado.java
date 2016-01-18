/**
 * PlaneamientoEncargado.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.beans.tramite;

import com.gestorfip.app.planeamiento.beans.diccionario.TipoTramiteBean;

public class PlaneamientoEncargado {
	
	private int id;
	private String nombre;
	private TipoTramiteBean tipoTramite;
	private int idFip;
	private String municipio;
	
	
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public int getIdFip() {
		return idFip;
	}
	public void setIdFip(int idFip) {
		this.idFip = idFip;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public TipoTramiteBean getTipoTramite() {
		return tipoTramite;
	}
	public void setTipoTramite(TipoTramiteBean tipoTramite) {
		this.tipoTramite = tipoTramite;
	}

}
