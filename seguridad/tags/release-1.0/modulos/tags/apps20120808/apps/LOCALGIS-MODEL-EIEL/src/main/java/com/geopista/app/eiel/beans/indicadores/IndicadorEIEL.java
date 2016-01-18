package com.geopista.app.eiel.beans.indicadores;

import java.io.Serializable;

public class IndicadorEIEL implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2398878193852911999L;
	private String nombreIndicador;
	private String mapaSeleccionado;
	
	public String getNombreIndicador() {
		return nombreIndicador;
	}
	public void setNombreIndicador(String nombreIndicador) {
		this.nombreIndicador = nombreIndicador;
	}
	public String getMapaSeleccionado() {
		return mapaSeleccionado;
	}
	public void setMapaSeleccionado(String mapaSeleccionado) {
		this.mapaSeleccionado = mapaSeleccionado;
	}


}
