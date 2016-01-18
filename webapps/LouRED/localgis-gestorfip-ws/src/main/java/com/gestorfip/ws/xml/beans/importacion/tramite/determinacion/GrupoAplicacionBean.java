package com.gestorfip.ws.xml.beans.importacion.tramite.determinacion;

import java.io.Serializable;

public class GrupoAplicacionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 125097579005750486L;

	private String determinacion;
	private String codigoTramite;
	
	public String getCodigoTramite() {
		return codigoTramite;
	}

	public void setCodigoTramite(String codigoTramite) {
		this.codigoTramite = codigoTramite;
	}

	public GrupoAplicacionBean() {
		// TODO Auto-generated constructor stub
	}

	public String getDeterminacion() {
		return determinacion;
	}

	public void setDeterminacion(String determinacion) {
		this.determinacion = determinacion;
	}

}
