package com.geopista.app.eiel.beans.filter;

import java.io.Serializable;

public class LCGCampoCapaTablaEIEL implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1651606207113289269L;
	private String campoCapa;
	private String campoBD;
	private String tabla;
	private String method;
	
	
	public LCGCampoCapaTablaEIEL() {
		super();
	}
	public LCGCampoCapaTablaEIEL(String campoCapa, String campoBD,
			String tabla, String method) {
		super();
		this.campoCapa = campoCapa;
		this.campoBD = campoBD;
		this.tabla = tabla;
		this.method = method;
	}
	public String getCampoCapa() {
		return campoCapa;
	}
	public void setCampoCapa(String campoCapa) {
		this.campoCapa = campoCapa;
	}
	public String getCampoBD() {
		return campoBD;
	}
	public void setCampoBD(String campoBD) {
		this.campoBD = campoBD;
	}
	
	public String getTabla() {
		return tabla;
	}
	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}

	
	
}
