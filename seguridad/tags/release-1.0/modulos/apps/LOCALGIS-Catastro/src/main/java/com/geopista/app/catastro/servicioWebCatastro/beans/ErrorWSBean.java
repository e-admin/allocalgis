package com.geopista.app.catastro.servicioWebCatastro.beans;

public class ErrorWSBean {

	private String codigo;
	
	private String descripcion;
	
	private String tipo;
	
	public static final String WS_ERROR_REFERENCIA_NO_ACTUALIZADA = "14";

	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
}

