package com.gestorfip.ws.xml.beans.tramite.documento;

import java.io.Serializable;

public class HojaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5947185060231326067L;

	private String nombre;
	private String geometria;
	
	public HojaBean() {
		// TODO Auto-generated constructor stub
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getGeometria() {
		return geometria;
	}

	public void setGeometria(String geometria) {
		this.geometria = geometria;
	}

}
