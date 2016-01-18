package com.gestorfip.ws.xml.beans.importacion.tramite.determinacion;

import java.io.Serializable;

public class DeterminacionReguladoraBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2876636632825348606L;

	private String determinacion;
	private String codigoTramite;
	
	public String getCodigoTramite() {
		return codigoTramite;
	}

	public void setCodigoTramite(String codigoTramite) {
		this.codigoTramite = codigoTramite;
	}

	public DeterminacionReguladoraBean() {
		// TODO Auto-generated constructor stub
	}

	public String getDeterminacion() {
		return determinacion;
	}

	public void setDeterminacion(String determinacion) {
		this.determinacion = determinacion;
	}

}
