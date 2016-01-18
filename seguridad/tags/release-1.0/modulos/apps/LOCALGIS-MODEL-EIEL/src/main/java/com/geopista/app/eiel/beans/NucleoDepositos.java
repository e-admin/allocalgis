package com.geopista.app.eiel.beans;

import java.sql.Date;

public class NucleoDepositos {
	
	private String claveDepositos = null;
	
	private String codProvDepositos = null;
	
	private String codMunicDepositos = null;
	
	private String codOrdenDepositos = null;
	
	private String codProvNucleo = null;
	
	private String codMunicNucleo = null;
	
	private String codEntNucleo = null;
	
	private String codPoblNucleo = null;
	
	private String observaciones = null;
	
	private Date fechaRevision = null;
	
	private Integer estadoRevision = null;

	private VersionEiel version = null;
	
	public String getClaveDepositos() {
		return claveDepositos;
	}

	public void setClaveDepositos(String claveDepositos) {
		this.claveDepositos = claveDepositos;
	}

	public String getCodProvDepositos() {
		return codProvDepositos;
	}

	public void setCodProvDepositos(String codProvDepositos) {
		this.codProvDepositos = codProvDepositos;
	}

	public String getCodMunicDepositos() {
		return codMunicDepositos;
	}

	public void setCodMunicDepositos(String codMunicDepositos) {
		this.codMunicDepositos = codMunicDepositos;
	}

	public String getCodOrdenDepositos() {
		return codOrdenDepositos;
	}

	public void setCodOrdenDepositos(String codOrdenDepositos) {
		this.codOrdenDepositos = codOrdenDepositos;
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
