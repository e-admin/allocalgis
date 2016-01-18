package com.gestorfip.ws.xml.beans.importacion.diccionario;

import java.io.Serializable;

public class RelacionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3923675911303921360L;

	private String padre;
	private String hijo;
	
	public RelacionBean() {
	}

	public String getPadre() {
		return padre;
	}

	public void setPadre(String padre) {
		this.padre = padre;
	}

	public String getHijo() {
		return hijo;
	}

	public void setHijo(String hijo) {
		this.hijo = hijo;
	}
	
	

}
