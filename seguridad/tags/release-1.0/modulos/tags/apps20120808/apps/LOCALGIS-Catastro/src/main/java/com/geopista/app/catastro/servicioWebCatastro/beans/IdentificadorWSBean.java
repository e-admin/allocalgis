package com.geopista.app.catastro.servicioWebCatastro.beans;


public class IdentificadorWSBean {

	private String tipo;
		
	private OrigenWSBean origen;

	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public OrigenWSBean getOrigen() {
		return origen;
	}

	public void setOrigen(OrigenWSBean origen) {
		this.origen = origen;
	}
}
