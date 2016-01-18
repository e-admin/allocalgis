package com.geopista.protocol.cementerios;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase que implementa un objeto de tipo Plaza de unidad de enterramiento
 */
//public class PlazaBean extends CementerioBean implements Serializable{
public class PlazaBean  implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String descripcion;
	private String situacion;
	private boolean estado;
	private Date modificado;
	private int idPlaza = -1; 
	private int idUnidadEnterramiento;
	private DifuntoBean difunto;

	
	
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getSituacion() {
		return situacion;
	}
	public void setSituacion(String situacion) {
		this.situacion = situacion;
	}

	public Date getModificado() {
		return modificado;
	}
	public void setModificado(Date modificado) {
		this.modificado = modificado;
	}

	public DifuntoBean getDifunto() {
		return difunto;
	}
	public void setDifunto(DifuntoBean difunto) {
		this.difunto = difunto;
	}
	public boolean isEstado() {
		return estado;
	}
	public void setEstado(boolean estado) {
		this.estado = estado;
	}
	public int getIdUnidadEnterramiento() {
		return idUnidadEnterramiento;
	}
	public void setIdUnidadEnterramiento(int idUnidadEnterramiento) {
		this.idUnidadEnterramiento = idUnidadEnterramiento;
	}
	public int getIdPlaza() {
		return idPlaza;
	}
	public void setIdPlaza(int idPlaza) {
		this.idPlaza = idPlaza;
	}
	
}
