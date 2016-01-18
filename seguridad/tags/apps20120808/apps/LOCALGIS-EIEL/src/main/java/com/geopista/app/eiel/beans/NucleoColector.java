package com.geopista.app.eiel.beans;

import java.sql.Date;

public class NucleoColector {
	private String claveColector = null;	
	private String codProvColector = null;	
	private String codMunicColector = null;	
	private String codOrdenColector = null;		
	private Float pmi = null;
	private Float pmf = null;
	private String codProvNucleo = null;
	private String codMunicNucleo = null;
	private String codEntNucleo = null;
	private String codPoblNucleo = null;
	private String observaciones = null;
	private Date fechaRevision = null;	
	private Integer estadoRevision = null;
	private String clave = null;

	private VersionEiel version = null;
	
	public String getClaveColector() {
		return claveColector;
	}

	public void setClaveColector(String claveColector) {
		this.claveColector = claveColector;
	}

	public String getCodProvColector() {
		return codProvColector;
	}

	public void setCodProvColector(String codProvColector) {
		this.codProvColector = codProvColector;
	}

	public String getCodMunicColector() {
		return codMunicColector;
	}

	public void setCodMunicColector(String codMunicColector) {
		this.codMunicColector = codMunicColector;
	}

	public String getCodOrdenColector() {
		return codOrdenColector;
	}

	public void setCodOrdenColector(String codOrdenColector) {
		this.codOrdenColector = codOrdenColector;
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

	public String getCodPoblNucleo() {
		return codPoblNucleo;
	}

	public void setCodPoblNucleo(String codPoblNucleo) {
		this.codPoblNucleo = codPoblNucleo;
	}

	public String getCodEntNucleo() {
		return codEntNucleo;
	}

	public void setCodEntNucleo(String codEntNucleo) {
		this.codEntNucleo = codEntNucleo;
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

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
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
