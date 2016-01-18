package com.geopista.app.eiel.beans;

import java.sql.Date;

public class NucleoCaptacion {
	
	private String claveCaptacion = null;
	
	private String codProvCaptacion = null;
	
	private String codMunicCaptacion = null;
	
	private String codOrdenCaptacion = null;
	
	private String codProvNucleo = null;
	
	private String codMunicNucleo = null;
	
	private String codEntNucleo = null;
	
	private String codPoblNucleo = null;
	
	private String observaciones = null;
	
	private Date fechaInicio = null;
	
	private Date fechaRevision = null;
	
	private Integer estadoRevision = null;

	private VersionEiel version = null;
	
	public String getClaveCaptacion() {
		return claveCaptacion;
	}

	public void setClaveCaptacion(String claveCaptacion) {
		this.claveCaptacion = claveCaptacion;
	}

	public String getCodProvCaptacion() {
		return codProvCaptacion;
	}

	public void setCodProvCaptacion(String codProvCaptacion) {
		this.codProvCaptacion = codProvCaptacion;
	}

	public String getCodMunicCaptacion() {
		return codMunicCaptacion;
	}

	public void setCodMunicCaptacion(String codMunicCaptacion) {
		this.codMunicCaptacion = codMunicCaptacion;
	}

	public String getCodOrdenCaptacion() {
		return codOrdenCaptacion;
	}

	public void setCodOrdenCaptacion(String codOrdenCaptacion) {
		this.codOrdenCaptacion = codOrdenCaptacion;
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

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
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
