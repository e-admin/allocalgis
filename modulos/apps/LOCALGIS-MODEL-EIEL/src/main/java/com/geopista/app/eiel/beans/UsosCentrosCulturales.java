/**
 * UsosCentrosCulturales.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;

public class UsosCentrosCulturales implements Serializable{

	private String uso = null;
	
	private Integer superficieUso = null;
	
	private Date fechaUso = null;
	
	private String observacionesUso = null;
	
	private Date fechaRevision = null;
	
	private String estadoRevision = null;
	
	private String codigoOrdenUso = null;
	
	private VersionEiel version = null;
	
	public String getUso() {
		return uso;
	}

	public void setUso(String uso) {
		this.uso = uso;
	}

	public Integer getSuperficieUso() {
		return superficieUso;
	}

	public void setSuperficieUso(Integer superficieUso) {
		this.superficieUso = superficieUso;
	}

	

	public String getCodigoOrdenUso() {
		return codigoOrdenUso;
	}

	public void setCodigoOrdenUso(String codigoOrdenUso) {
		this.codigoOrdenUso = codigoOrdenUso;
	}

	public String getObservacionesUso() {
		return observacionesUso;
	}

	public void setObservacionesUso(String observacionesUso) {
		this.observacionesUso = observacionesUso;
	}

	public Date getFechaRevision() {
		return fechaRevision;
	}

	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}

	public String getEstadoRevision() {
		return estadoRevision;
	}

	public void setEstadoRevision(String estadoRevision) {
		this.estadoRevision = estadoRevision;
	}

	public Date getFechaUso() {
		return fechaUso;
	}

	public void setFechaUso(Date fechaUso) {
		this.fechaUso = fechaUso;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(VersionEiel version) {
		this.version = version;
	}

	/**
	 * @return the version
	 */
	public VersionEiel getVersion() {
		return version;
	}
	
	
}
