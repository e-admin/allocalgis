package com.gestorfip.ws.xml.beans.importacion.layers;

import java.io.Serializable;

import com.gestorfip.ws.xml.beans.importacion.tramite.TramiteBean;



public class CatalogoSistematizadoLayerBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5285509089536894973L;

	private String ambito;
	private String nombre;
	private TramiteLayerBean tramite;
	
	public CatalogoSistematizadoLayerBean() {
	}

	public String getAmbito() {
		return ambito;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public TramiteLayerBean getTramite() {
		return tramite;
	}

	public void setTramite(TramiteLayerBean tramite) {
		this.tramite = tramite;
	}

}
