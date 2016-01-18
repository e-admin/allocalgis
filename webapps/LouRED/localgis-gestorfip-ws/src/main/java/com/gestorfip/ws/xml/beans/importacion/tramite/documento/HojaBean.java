package com.gestorfip.ws.xml.beans.importacion.tramite.documento;

import java.io.Serializable;

import com.vividsolutions.jts.geom.Geometry;

public class HojaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5947185060231326067L;

	private String nombre;
	private Geometry geometria;
	
	public HojaBean() {
		// TODO Auto-generated constructor stub
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Geometry getGeometria() {
		return geometria;
	}

	public void setGeometria(Geometry geometria) {
		this.geometria = geometria;
	}

}
