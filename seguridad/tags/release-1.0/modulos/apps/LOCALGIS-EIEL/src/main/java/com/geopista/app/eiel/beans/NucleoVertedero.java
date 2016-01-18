package com.geopista.app.eiel.beans;

import java.sql.Date;

public class NucleoVertedero {
	
	private String claveVertedero = null;
	
	private String codProvVertedero = null;
	
	private String codMunicVertedero = null;
	
	private String codOrdenVertedero = null;
	
	private String codProvNucleo = null;
	
	private String codMunicNucleo = null;
	
	private String codEntNucleo = null;
	
	private String codPoblNucleo = null;
	
	private Date fechaAlta = null;
	
	private String observaciones = null;
	
	private Date fechaRevision = null;
	
	private Integer estadoRevision = null;

	private VersionEiel version = null;
	
	public String getClaveVertedero() {
		return claveVertedero;
	}

	public void setClaveVertedero(String claveVertedero) {
		this.claveVertedero = claveVertedero;
	}

	public String getCodProvVertedero() {
		return codProvVertedero;
	}

	public void setCodProvVertedero(String codProvVertedero) {
		this.codProvVertedero = codProvVertedero;
	}

	public String getCodMunicVertedero() {
		return codMunicVertedero;
	}

	public void setCodMunicVertedero(String codMunicVertedero) {
		this.codMunicVertedero = codMunicVertedero;
	}

	public String getCodOrdenVertedero() {
		return codOrdenVertedero;
	}

	public void setCodOrdenVertedero(String codOrdenVertedero) {
		this.codOrdenVertedero = codOrdenVertedero;
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
	
	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
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
