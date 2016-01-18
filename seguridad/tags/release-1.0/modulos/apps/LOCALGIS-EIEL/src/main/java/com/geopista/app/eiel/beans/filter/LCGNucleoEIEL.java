package com.geopista.app.eiel.beans.filter;

import java.io.Serializable;

public class LCGNucleoEIEL implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2398878193852911999L;
	private String codentidad;
	private String codpoblamiento;
	private String denominacion;
	public String getCodentidad() {
		return codentidad;
	}
	public void setCodentidad(String codentidad) {
		this.codentidad = codentidad;
	}
	public String getCodpoblamiento() {
		return codpoblamiento;
	}
	public void setCodpoblamiento(String codpoblamiento) {
		this.codpoblamiento = codpoblamiento;
	}
	public String getDenominacion() {
		return denominacion;
	}
	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}
	@Override
	public String toString() {
		return "LCGNucleoIEL [codentidad=" + codentidad + ", codpoblamiento="
				+ codpoblamiento + ", denominacion=" + denominacion + "]";
	}
	

}
