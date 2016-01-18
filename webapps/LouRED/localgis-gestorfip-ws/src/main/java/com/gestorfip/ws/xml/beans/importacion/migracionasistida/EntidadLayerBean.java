package com.gestorfip.ws.xml.beans.importacion.migracionasistida;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EntidadLayerBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5767562377467898557L;

	private String codigo;
	private String nombre;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
