package com.gestorfip.ws.xml.beans.importacion.planeamientoencargado;

import java.io.Serializable;

import com.gestorfip.ws.xml.beans.importacion.tramite.TramiteBean;
import com.vividsolutions.jts.geom.Geometry;



public class PlaneamientoEncargadoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5285509089536894973L;

	private String ambito;
	private String nombre;
	private String instrumento;
	private int iteracion;
	private String tipotramite;
	private String codigotramite;
	private Geometry geometria;
	
	public PlaneamientoEncargadoBean() {
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

	public String getInstrumento() {
		return instrumento;
	}

	public void setInstrumento(String instrumento) {
		this.instrumento = instrumento;
	}

	public int getIteracion() {
		return iteracion;
	}

	public void setIteracion(int iteracion) {
		this.iteracion = iteracion;
	}

	public String getTipotramite() {
		return tipotramite;
	}

	public void setTipotramite(String tipotramite) {
		this.tipotramite = tipotramite;
	}

	public String getCodigotramite() {
		return codigotramite;
	}

	public void setCodigotramite(String codigotramite) {
		this.codigotramite = codigotramite;
	}

	public Geometry getGeometria() {
		return geometria;
	}

	public void setGeometria(Geometry geometria) {
		this.geometria = geometria;
	}

}
