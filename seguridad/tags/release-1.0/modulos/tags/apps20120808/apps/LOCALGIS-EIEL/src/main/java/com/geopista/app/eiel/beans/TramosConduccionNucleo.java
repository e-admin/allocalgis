package com.geopista.app.eiel.beans;

import java.sql.Date;

public class TramosConduccionNucleo {

	private TramosConduccionEIEL conduccion = null;
	
	private NucleosPoblacionEIEL nucleo = null;
	
	private String observaciones = null;
	
	private Integer pmi = null;
	
	private Integer pmf = null;
	
	private Date fechaRevision = null;
	
	private Integer estadorevision = null;
	
	
	private VersionEiel version = null;
	
	public TramosConduccionEIEL getTramosConduccion() {
		return conduccion;
	}

	public void setTramosConduccion(TramosConduccionEIEL conduccion) {
		this.conduccion = conduccion;
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

	public Integer getPmi() {
		return pmi;
	}

	public void setPmi(Integer pmi) {
		this.pmi = pmi;
	}
	
	public Integer getPmf() {
		return pmf;
	}

	public void setPmf(Integer pmf) {
		this.pmf = pmf;
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
