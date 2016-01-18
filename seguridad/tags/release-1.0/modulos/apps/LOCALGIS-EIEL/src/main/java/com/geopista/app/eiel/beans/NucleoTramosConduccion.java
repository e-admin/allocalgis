package com.geopista.app.eiel.beans;

import java.sql.Date;

public class NucleoTramosConduccion {
	
	private String claveTramosConduccion = null;
	
	private String codProvTramosConduccion = null;
	
	private String codMunicTramosConduccion = null;
	
	private String codOrdenTramosConduccion = null;
	
	private String codProvNucleo = null;
	
	private String codMunicNucleo = null;
	
	private String codEntNucleo = null;
	
	private String codPoblNucleo = null;
	
	private String observaciones = null;
	
	private Float pmi = null;
	
	private Float pmf = null;
	
	private Date fechaRevision = null;
	
	private Integer estadoRevision = null;

	private VersionEiel version = null;
	
	public String getClaveTramosConduccion() {
		return claveTramosConduccion;
	}

	public void setClaveTramosConduccion(String claveTramosConduccion) {
		this.claveTramosConduccion = claveTramosConduccion;
	}

	public String getCodProvTramosConduccion() {
		return codProvTramosConduccion;
	}

	public void setCodProvTramosConduccion(String codProvTramosConduccion) {
		this.codProvTramosConduccion = codProvTramosConduccion;
	}

	public String getCodMunicTramosConduccion() {
		return codMunicTramosConduccion;
	}

	public void setCodMunicTramosConduccion(String codMunicTramosConduccion) {
		this.codMunicTramosConduccion = codMunicTramosConduccion;
	}

	public String getCodOrdenTramosConduccion() {
		return codOrdenTramosConduccion;
	}

	public void setCodOrdenTramosConduccion(String codOrdenTramosConduccion) {
		this.codOrdenTramosConduccion = codOrdenTramosConduccion;
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
