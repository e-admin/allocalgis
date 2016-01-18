package com.gestorfip.ws.xml.beans.importacion.migracionasistida;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeterminacionLayerBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5767562377467898557L;

	private String codigo;
	private String nombre;
	private String etiqueta;
	private String apartado;
	private String caracter;
	private String codTramite;

//	private DeterminacionLayerBean[] lstDeterminacionesHijas;


	public String getCodTramite() {
		return codTramite;
	}

	public void setCodTramite(String codTramite) {
		this.codTramite = codTramite;
	}

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

	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}
//
//	public DeterminacionLayerBean[] getLstDeterminacionesHijas() {
//		return lstDeterminacionesHijas;
//	}
//
//	public void setLstDeterminacionesHijas(
//			DeterminacionLayerBean[] lstDeterminacionesHijas) {
//		this.lstDeterminacionesHijas = lstDeterminacionesHijas;
//	}
	
	public String getApartado() {
		return apartado;
	}

	public void setApartado(String apartado) {
		this.apartado = apartado;
	}
	
	public String getCaracter() {
		return caracter;
	}

	public void setCaracter(String caracter) {
		this.caracter = caracter;
	}

	
}
