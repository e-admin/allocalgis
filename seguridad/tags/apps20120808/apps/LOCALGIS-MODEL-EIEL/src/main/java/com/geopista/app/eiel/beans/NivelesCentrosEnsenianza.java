package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;

public class NivelesCentrosEnsenianza implements Serializable{

	private String nivel = null;
	
	private Integer unidades = null;
	
	private Integer numeroPlazas = null;
	
	private Integer numeroAlumnos = null;
	
	private Date fechaCurso = null;
	
	private Date fechaRevision = null;
	
	private String estadoRevision = null;
	
	private String codigoOrdenNivel = null;
	
	private String observacionesNivel = null;

	private VersionEiel version = null;
	
	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public Integer getNumeroPlazas() {
		return numeroPlazas;
	}

	public void setNumeroPlazas(Integer numeroPlazas) {
		this.numeroPlazas = numeroPlazas;
	}

	public Integer getNumeroAlumnos() {
		return numeroAlumnos;
	}

	public void setNumeroAlumnos(Integer numeroAlumnos) {
		this.numeroAlumnos = numeroAlumnos;
	}

	public Date getFechaCurso() {
		return fechaCurso;
	}

	public void setFechaCurso(Date fechaCurso) {
		this.fechaCurso = fechaCurso;
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

	public String getCodigoOrdenNivel() {
		return codigoOrdenNivel;
	}

	public void setCodigoOrdenNivel(String codigoOrdenNivel) {
		this.codigoOrdenNivel = codigoOrdenNivel;
	}

	public String getObservacionesNivel() {
		return observacionesNivel;
	}

	public void setObservacionesNivel(String observacionesNivel) {
		this.observacionesNivel = observacionesNivel;
	}

	public Integer getUnidades() {
		return unidades;
	}

	public void setUnidades(Integer unidades) {
		this.unidades = unidades;
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
