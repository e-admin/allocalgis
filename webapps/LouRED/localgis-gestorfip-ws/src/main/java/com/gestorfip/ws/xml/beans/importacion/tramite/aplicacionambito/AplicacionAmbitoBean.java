package com.gestorfip.ws.xml.beans.importacion.tramite.aplicacionambito;

import java.io.Serializable;

public class AplicacionAmbitoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 60820013059551885L;

	private String ambito;
	private String entidad;
	
	public AplicacionAmbitoBean() {
		// TODO Auto-generated constructor stub
	}

	public String getAmbito() {
		return ambito;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	public String getEntidad() {
		return entidad;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

}
