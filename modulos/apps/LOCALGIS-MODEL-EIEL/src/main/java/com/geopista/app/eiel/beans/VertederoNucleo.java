/**
 * VertederoNucleo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

import java.sql.Date;

public class VertederoNucleo {

	private VertederosEIEL vertedero = null;
	
	private NucleosPoblacionEIEL nucleo = null;
	
	private Date fechaInstalacion = null;
	
	private String observaciones = null;	
	
	private Date fechaAlta = null;
	
	private Date fecharevision = null;
	
	private Integer estadorevision = null;
	
	
	private VersionEiel version = null;
	
	public VertederosEIEL getVertedero() {
		return vertedero;
	}

	public void setVertedero(VertederosEIEL vertedero) {
		this.vertedero = vertedero;
	}

	public NucleosPoblacionEIEL getNucleo() {
		return nucleo;
	}

	public void setNucleo(NucleosPoblacionEIEL nucleo) {
		this.nucleo = nucleo;
	}
	
	public Date getFechaInstalacion() {
		return fechaInstalacion;
	}

	public void setFechaInstalacion(Date fechaInstalacion) {
		this.fechaInstalacion = fechaInstalacion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public Integer getEstadorevision() {
		return estadorevision;
	}

	public void setEstadorevision(Integer estadorevision) {
		this.estadorevision = estadorevision;
	}

	public Date getFecharevision() {
		return fecharevision;
	}

	public void setFecharevision(Date fecharevision) {
		this.fecharevision = fecharevision;
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
