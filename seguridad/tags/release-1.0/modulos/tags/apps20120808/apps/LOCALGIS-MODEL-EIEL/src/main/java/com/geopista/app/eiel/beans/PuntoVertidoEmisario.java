package com.geopista.app.eiel.beans;

import java.sql.Date;

public class PuntoVertidoEmisario {
	private String claveEmisario = null;	
	private String codProvEmisario = null;	
	private String codMunicEmisario = null;	
	private String codOrdenEmisario = null;		
	private Float pmi = null;
	private Float pmf = null;
	private String codProvPuntoVertido = null;
	private String codMunicPuntoVertido = null;
	private String codClavePuntoVertido = null;
	private String codOrdenPuntoVertido = null;
	private String observaciones = null;
	private Date fechaRevision = null;	
	private Integer estadoRevision = null;

	
	private VersionEiel version = null;
	
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

	public String getCodOrdenEmisario() {
		return codOrdenEmisario;
	}

	public void setCodOrdenEmisario(String codOrdenEmisario) {
		this.codOrdenEmisario = codOrdenEmisario;
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

	public String getCodProvPuntoVertido() {
		return codProvPuntoVertido;
	}

	public void setCodProvPuntoVertido(String codProvNucleo) {
		this.codProvPuntoVertido = codProvNucleo;
	}

	public String getCodMunicPuntoVertido() {
		return codMunicPuntoVertido;
	}

	public void setCodMunicPuntoVertido(String codMunicNucleo) {
		this.codMunicPuntoVertido = codMunicNucleo;
	}

	public String getCodOrdenPuntoVertido() {
		return codOrdenPuntoVertido;
	}

	public void setCodOrdenPuntoVertido(String codPoblNucleo) {
		this.codOrdenPuntoVertido = codPoblNucleo;
	}

	public String getCodClavePuntoVertido() {
		return codClavePuntoVertido;
	}

	public void setCodClavePuntoVertido(String codEntNucleo) {
		this.codClavePuntoVertido = codEntNucleo;
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
