package com.gestorfip.ws.xml.beans.tramite.determinacion;

import java.io.Serializable;

public class GrupoAplicacionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 125097579005750486L;

	private String determinacion;
	
	public GrupoAplicacionBean() {
		// TODO Auto-generated constructor stub
	}

	public String getDeterminacion() {
		return determinacion;
	}

	public void setDeterminacion(String determinacion) {
		this.determinacion = determinacion;
	}

}
