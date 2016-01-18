package com.geopista.app.eiel.beans.indicadores;

import java.io.Serializable;

public class MapaEIEL implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2398878193852911999L;
	private int idMapa;
	private String nombreMapa;
	
	public int getIdMapa() {
		return idMapa;
	}
	public void setIdMapa(int idMapa) {
		this.idMapa = idMapa;
	}
	
	public String getNombreMapa() {
		return nombreMapa;
	}
	
	public void setNombreMapa(String nombreMapa) {
		this.nombreMapa = nombreMapa;
	}
	

}
