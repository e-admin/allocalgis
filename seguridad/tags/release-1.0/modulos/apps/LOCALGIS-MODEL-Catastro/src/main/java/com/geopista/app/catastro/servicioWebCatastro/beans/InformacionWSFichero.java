package com.geopista.app.catastro.servicioWebCatastro.beans;

import java.util.Date;

public class InformacionWSFichero {
	
	private String horaGenerancion;
	
	private String fechaGeneracion;
	
	private String tipo;

	public String getHoraGenerancion() {
		return horaGenerancion;
	}

	public void setHoraGeneracion(String horaGenerancion) {
		this.horaGenerancion = horaGenerancion;
	}

	public String getFechaGeneracion() {
		return fechaGeneracion;
	}

	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
