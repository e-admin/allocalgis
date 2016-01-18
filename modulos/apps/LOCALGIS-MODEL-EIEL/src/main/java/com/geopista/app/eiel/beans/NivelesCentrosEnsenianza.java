/**
 * NivelesCentrosEnsenianza.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;

public class NivelesCentrosEnsenianza implements Serializable{

	private String nivel = null;
	
	private Integer unidades = null;
	
	private Integer numeroPlazas = null;
	
	private Integer numeroAlumnos = null;
	
	private Date fechaCurso = null;
	
	private Date fechaRevision = null;
	
	private String estadoRevision = null;
	
	private String codigoOrdenNivel = null;
	
	private String observacionesNivel = null;

	private VersionEiel version = null;
	
	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public Integer getNumeroPlazas() {
		return numeroPlazas;
	}

	public void setNumeroPlazas(Integer numeroPlazas) {
		this.numeroPlazas = numeroPlazas;
	}

	public Integer getNumeroAlumnos() {
		return numeroAlumnos;
	}

	public void setNumeroAlumnos(Integer numeroAlumnos) {
		this.numeroAlumnos = numeroAlumnos;
	}

	public Date getFechaCurso() {
		return fechaCurso;
	}

	public void setFechaCurso(Date fechaCurso) {
		this.fechaCurso = fechaCurso;
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

	public String getCodigoOrdenNivel() {
		return codigoOrdenNivel;
	}

	public void setCodigoOrdenNivel(String codigoOrdenNivel) {
		this.codigoOrdenNivel = codigoOrdenNivel;
	}

	public String getObservacionesNivel() {
		return observacionesNivel;
	}

	public void setObservacionesNivel(String observacionesNivel) {
		this.observacionesNivel = observacionesNivel;
	}

	public Integer getUnidades() {
		return unidades;
	}

	public void setUnidades(Integer unidades) {
		this.unidades = unidades;
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
