package com.gestorfip.ws.xml.beans.importacion.planeamientovigente;

import java.io.Serializable;

import com.gestorfip.ws.xml.beans.importacion.tramite.TramiteBean;



public class PlaneamientoVigenteBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5285509089536894973L;

	private String ambito;
	private String nombre;
	private TramiteBean tramite;
	
	public PlaneamientoVigenteBean() {
	}

	public String getAmbito() {
		return ambito;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TramiteBean getTramite() {
		return tramite;
	}

	public void setTramite(TramiteBean tramite) {
		this.tramite = tramite;
	}

}
