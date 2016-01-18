/**
 * NucleoEmisario.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

import java.sql.Date;

public class NucleoEmisario {
	
	
	private String claveEmisario = null;
	
	private String codProvEmisario = null;
	
	private String codMunicEmisario = null;
	
	private String codTramoEmisario = null;
	
	private String codProvNucleo = null;
	
	private String codMunicNucleo = null;
	
	private String codEntNucleo = null;
	
	private String codPoblNucleo = null;
	
	private String observaciones = null;
	
	private Date fechaRevision = null;
	
	private Integer estadoRevision = null;
	
	private Float pmi=null;
	
	private Float pmf=null;
	
	private VersionEiel version = null;
	

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

	public String getClaveEmisario() {
		return claveEmisario;
	}

	public void setClaveEmisario(String claveEmisario) {
		this.claveEmisario = claveEmisario;
	}

	public String getCodProvEmisario() {
		return codProvEmisario;
	}

	public void setCodProvEmisario(String codProvEmisario) {
		this.codProvEmisario = codProvEmisario;
	}

	public String getCodMunicEmisario() {
		return codMunicEmisario;
	}

	public void setCodMunicEmisario(String codMunicEmisario) {
		this.codMunicEmisario = codMunicEmisario;
	}

	public String getCodTramoEmisario() {
		return codTramoEmisario;
	}

	public void setCodTramoEmisario(String codTramoEmisario) {
		this.codTramoEmisario = codTramoEmisario;
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

	public Float getPmi() {
		return pmi;
	}

	public void setPmi(Float pmi) {
		this.pmi = pmi;
	}

	public Float getPmf() {
		return pmf;
	}

	public void setPmf(Float pmf) {
		this.pmf = pmf;
	}
	

}
