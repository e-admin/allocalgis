/**
 * 
 */
package com.gestorfip.ws.xml.beans.importacion.diccionario;

import java.io.Serializable;

import com.vividsolutions.jts.geom.Geometry;

/**
 * @author davidriou
 *
 */
public class AmbitoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4256291554569769999L;
	
	private String nombre;
	private String codigo;
	private String ine;
	private String tipoambito;
	private Geometry geometria;
	
	public AmbitoBean() {
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getIne() {
		return ine;
	}
	public void setIne(String ine) {
		this.ine = ine;
	}
	public String getTipoambito() {
		return tipoambito;
	}
	public void setTipoambito(String tipoambito) {
		this.tipoambito = tipoambito;
	}
	public Geometry getGeometria() {
		return geometria;
	}
	public void setGeometria(Geometry geometria) {
		this.geometria = geometria;
	}

	
}
