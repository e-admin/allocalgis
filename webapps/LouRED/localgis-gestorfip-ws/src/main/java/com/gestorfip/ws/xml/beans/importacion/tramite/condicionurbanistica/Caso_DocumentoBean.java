package com.gestorfip.ws.xml.beans.importacion.tramite.condicionurbanistica;

import java.io.Serializable;

public class Caso_DocumentoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1690598232342963968L;

	public Caso_DocumentoBean() {
		// TODO Auto-generated constructor stub
	}
	
	private String documento;
	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

}
