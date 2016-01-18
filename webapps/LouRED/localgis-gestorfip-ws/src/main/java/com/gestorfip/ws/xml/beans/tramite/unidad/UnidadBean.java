package com.gestorfip.ws.xml.beans.tramite.unidad;

import java.io.Serializable;

public class UnidadBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4914072233279993814L;

	private String abreviatura;
	private String definicion;
	private String determinacion;
	
	public UnidadBean() {
		// TODO Auto-generated constructor stub
	}

	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public String getDefinicion() {
		return definicion;
	}

	public void setDefinicion(String definicion) {
		this.definicion = definicion;
	}

	public String getDeterminacion() {
		return determinacion;
	}

	public void setDeterminacion(String determinacion) {
		this.determinacion = determinacion;
	}

}
