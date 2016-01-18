package com.gestorfip.ws.xml.beans.importacion.tramite.determinacion;

import java.io.Serializable;

public class ValorReferenciaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4955236731071976805L;
	
	private String determinacion;
	
	public ValorReferenciaBean() {
		// TODO Auto-generated constructor stub
	}

	public String getDeterminacion() {
		return determinacion;
	}

	public void setDeterminacion(String determinacion) {
		this.determinacion = determinacion;
	}

}
