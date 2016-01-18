package com.geopista.app.eiel.beans;

import java.sql.Date;

public class CaptacionNucleo {

	private CaptacionesEIEL captacion = null;
	
	private NucleosPoblacionEIEL nucleo = null;
	
	private String observaciones = null;
	
	private Date fechaInicioServ = null;
	
	private Date fechaRevision = null;
	
	private Integer estadorevision = null;
	
	private VersionEiel version = null;

	public CaptacionesEIEL getCaptacion() {
		return captacion;
	}

	public void setCaptacion(CaptacionesEIEL captacion) {
		this.captacion = captacion;
	}

	public NucleosPoblacionEIEL getNucleo() {
		return nucleo;
	}

	public void setNucleo(NucleosPoblacionEIEL nucleo) {
		this.nucleo = nucleo;
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

	public Integer getEstadorevision() {
		return estadorevision;
	}

	public void setEstadorevision(Integer estadorevision) {
		this.estadorevision = estadorevision;
	}

	public Date getFechaInicioServ() {
		return fechaInicioServ;
	}

	public void setFechaInicioServ(Date fechaInicioServ) {
		this.fechaInicioServ = fechaInicioServ;
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
