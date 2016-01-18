/**
 * InventarioEIELBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.inventario;

import java.io.Serializable;

public class InventarioEIELBean implements Serializable{
	
	private String nombreEIEL;
	private String estadoEIEL;
	private String gestionEIEL;
	private String tipoEIEL;
	private String unionClaveEIEL;
	private long idBien;
	private Integer epigInventario;
	private String titularidadMunicipal;
	private Integer idMunicipio;
	
	
	public long getIdBien() {
		return idBien;
	}
	public void setIdBien(long idBien) {
		this.idBien = idBien;
	}
	public Integer getEpigInventario() {
		return epigInventario;
	}
	public void setEpigInventario(Integer epigInventario) {
		this.epigInventario = epigInventario;
	}
	public String getTitularidadMunicipal() {
		return titularidadMunicipal;
	}
	public void setTitularidadMunicipal(String titularidadMunicipal) {
		this.titularidadMunicipal = titularidadMunicipal;
	}
	public Integer getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(Integer idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public String getUnionClaveEIEL() {
		return unionClaveEIEL;
	}
	public void setUnionClaveEIEL(String unionClaveEIEL) {
		this.unionClaveEIEL = unionClaveEIEL;
	}
	public String getNombreEIEL() {
		return nombreEIEL;
	}
	public void setNombreEIEL(String nombreEIEL) {
		this.nombreEIEL = nombreEIEL;
	}
	public String getEstadoEIEL() {
		return estadoEIEL;
	}
	public void setEstadoEIEL(String estadoEIEL) {
		this.estadoEIEL = estadoEIEL;
	}
	public String getGestionEIEL() {
		return gestionEIEL;
	}
	public void setGestionEIEL(String gestionEIEL) {
		this.gestionEIEL = gestionEIEL;
	}
	public String getTipoEIEL() {
		return tipoEIEL;
	}
	public void setTipoEIEL(String tipoEIEL) {
		this.tipoEIEL = tipoEIEL;
	}
	

}
