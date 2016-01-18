package com.gestorfip.ws.xml.beans.importacion.tramite.entidad;

import java.io.Serializable;

public class DocumentoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 700358137421573796L;

	private String documento;
	
	public DocumentoBean() {
		// TODO Auto-generated constructor stub
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

}
