package com.geopista.app.eiel.beans;

import java.sql.Date;

public class PuntosVertidoNucleo {

	private PuntosVertidoEIEL vertidos = null;
	
	private NucleosPoblacionEIEL nucleo = null;
	
	private String observaciones = null;	
	
	private Date fechaRevision = null;
	
	private Integer estadorevision = null;
	
	
	private VersionEiel version = null;
	
	public PuntosVertidoEIEL getPuntosVertido() {
		return vertidos;
	}

	public void setPuntosVertido(PuntosVertidoEIEL vertidos) {
		this.vertidos = vertidos;
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
