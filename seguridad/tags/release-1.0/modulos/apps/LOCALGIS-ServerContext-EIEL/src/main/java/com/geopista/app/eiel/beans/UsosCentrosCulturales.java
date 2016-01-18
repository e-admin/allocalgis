package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;

public class UsosCentrosCulturales implements Serializable{

	private String uso = null;
	
	private Integer superficieUso = null;
	
	private Date fechaUso = null;
	
	private String observacionesUso = null;
	
	private Date fechaRevision = null;
	
	private String estadoRevision = null;
	
	private String codigoOrdenUso = null;
	
	private VersionEiel version = null;
	
	public String getUso() {
		return uso;
	}

	public void setUso(String uso) {
		this.uso = uso;
	}

	public Integer getSuperficieUso() {
		return superficieUso;
	}

	public void setSuperficieUso(Integer superficieUso) {
		this.superficieUso = superficieUso;
	}

	

	public String getCodigoOrdenUso() {
		return codigoOrdenUso;
	}

	public void setCodigoOrdenUso(String codigoOrdenUso) {
		this.codigoOrdenUso = codigoOrdenUso;
	}

	public String getObservacionesUso() {
		return observacionesUso;
	}

	public void setObservacionesUso(String observacionesUso) {
		this.observacionesUso = observacionesUso;
	}

	public Date getFechaRevision() {
		return fechaRevision;
	}

	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}

	public String getEstadoRevision() {
		return estadoRevision;
	}

	public void setEstadoRevision(String estadoRevision) {
		this.estadoRevision = estadoRevision;
	}

	public Date getFechaUso() {
		return fechaUso;
	}

	public void setFechaUso(Date fechaUso) {
		this.fechaUso = fechaUso;
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
