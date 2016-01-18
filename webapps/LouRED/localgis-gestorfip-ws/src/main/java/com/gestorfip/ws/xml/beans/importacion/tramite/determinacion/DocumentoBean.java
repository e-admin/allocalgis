package com.gestorfip.ws.xml.beans.importacion.tramite.determinacion;

import java.io.Serializable;

public class DocumentoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4356792886562291881L;

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
