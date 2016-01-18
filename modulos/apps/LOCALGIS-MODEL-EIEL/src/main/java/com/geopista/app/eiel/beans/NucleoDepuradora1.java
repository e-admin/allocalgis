/**
 * NucleoDepuradora1.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

import java.sql.Date;

public class NucleoDepuradora1 {
	
	private String claveDepuradora1 = null;
	
	private String codProvDepuradora1 = null;
	
	private String codMunicDepuradora1 = null;
	
	private String codOrdenDepuradora1 = null;
	
	private String codProvNucleo = null;
	
	private String codMunicNucleo = null;
	
	private String codEntNucleo = null;
	
	private String codPoblNucleo = null;
	
	private String observaciones = null;
	
	private Date fechaRevision = null;
	
	private Integer estadoRevision = null;

	private VersionEiel version = null;
	
	public String getClaveDepuradora1() {
		return claveDepuradora1;
	}

	public void setClaveDepuradora1(String claveDepuradora1) {
		this.claveDepuradora1 = claveDepuradora1;
	}

	public String getCodProvDepuradora1() {
		return codProvDepuradora1;
	}

	public void setCodProvDepuradora1(String codProvDepuradora1) {
		this.codProvDepuradora1 = codProvDepuradora1;
	}

	public String getCodMunicDepuradora1() {
		return codMunicDepuradora1;
	}

	public void setCodMunicDepuradora1(String codMunicDepuradora1) {
		this.codMunicDepuradora1 = codMunicDepuradora1;
	}

	public String getCodOrdenDepuradora1() {
		return codOrdenDepuradora1;
	}

	public void setCodOrdenDepuradora1(String codOrdenDepuradora1) {
		this.codOrdenDepuradora1 = codOrdenDepuradora1;
	}

	public String getCodProvNucleo() {
		return codProvNucleo;
	}

	public void setCodProvNucleo(String codProvNucleo) {
		this.codProvNucleo = codProvNucleo;
	}

	public String getCodMunicNucleo() {
		return codMunicNucleo;
	}

	public void setCodMunicNucleo(String codMunicNucleo) {
		this.codMunicNucleo = codMunicNucleo;
	}

	public String getCodEntNucleo() {
		return codEntNucleo;
	}

	public void setCodEntNucleo(String codEntNucleo) {
		this.codEntNucleo = codEntNucleo;
	}

	public String getCodPoblNucleo() {
		return codPoblNucleo;
	}

	public void setCodPoblNucleo(String codPoblNucleo) {
		this.codPoblNucleo = codPoblNucleo;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Date getFechaRevision() {
		return fechaRevision;
	}

	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}

	public Integer getEstadoRevision() {
		return estadoRevision;
	}

	public void setEstadoRevision(Integer estadoRevision) {
		this.estadoRevision = estadoRevision;
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
