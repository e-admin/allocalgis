package com.geopista.app.eiel.beans;

import java.sql.Date;

public class NucleoTratamientosPotabilizacion {
	
	private String claveTratamientosPotabilizacion = null;
	
	private String codProvTratamientosPotabilizacion = null;
	
	private String codMunicTratamientosPotabilizacion = null;
	
	private String codOrdenTratamientosPotabilizacion = null;
	
	private String codProvNucleo = null;
	
	private String codMunicNucleo = null;
	
	private String codEntNucleo = null;
	
	private String codPoblNucleo = null;
	
	private String observaciones = null;
	
	private Date fechaRevision = null;
	
	private Integer estadoRevision = null;

	private VersionEiel version = null;
	
	public String getClaveTratamientosPotabilizacion() {
		return claveTratamientosPotabilizacion;
	}

	public void setClaveTratamientosPotabilizacion(String claveTratamientosPotabilizacion) {
		this.claveTratamientosPotabilizacion = claveTratamientosPotabilizacion;
	}

	public String getCodProvTratamientosPotabilizacion() {
		return codProvTratamientosPotabilizacion;
	}

	public void setCodProvTratamientosPotabilizacion(String codProvTratamientosPotabilizacion) {
		this.codProvTratamientosPotabilizacion = codProvTratamientosPotabilizacion;
	}

	public String getCodMunicTratamientosPotabilizacion() {
		return codMunicTratamientosPotabilizacion;
	}

	public void setCodMunicTratamientosPotabilizacion(String codMunicTratamientosPotabilizacion) {
		this.codMunicTratamientosPotabilizacion = codMunicTratamientosPotabilizacion;
	}

	public String getCodOrdenTratamientosPotabilizacion() {
		return codOrdenTratamientosPotabilizacion;
	}

	public void setCodOrdenTratamientosPotabilizacion(String codOrdenTratamientosPotabilizacion) {
		this.codOrdenTratamientosPotabilizacion = codOrdenTratamientosPotabilizacion;
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
