package com.gestorfip.ws.xml.beans.importacion.tramite.adscripcion;

import java.io.Serializable;

public class AdscripcionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4957526903932649848L;

	private String entidadorigen;
	private String entidaddestino;
	private double propiedades_cuantia;
	private String propiedades_texto;
	private String propiedades_unidad_determinacion;
	private String propiedades_tipo_determinacion;	
	private String propiedades_tramite_unidad_determinacion;
	private String propiedades_tramite_tipo_determinacion;
	


	public AdscripcionBean() {
		// TODO Auto-generated constructor stub
	}

	public String getEntidadorigen() {
		return entidadorigen;
	}

	public void setEntidadorigen(String entidadorigen) {
		this.entidadorigen = entidadorigen;
	}

	public String getEntidaddestino() {
		return entidaddestino;
	}

	public void setEntidaddestino(String entidaddestino) {
		this.entidaddestino = entidaddestino;
	}

	public double getPropiedades_cuantia() {
		return propiedades_cuantia;
	}

	public void setPropiedades_cuantia(double propiedadesCuantia) {
		propiedades_cuantia = propiedadesCuantia;
	}
	
	public String getPropiedades_texto() {
		return propiedades_texto;
	}

	public void setPropiedades_texto(String propiedadesTexto) {
		propiedades_texto = propiedadesTexto;
	}

	public String getPropiedades_unidad_determinacion() {
		return propiedades_unidad_determinacion;
	}

	public void setPropiedades_unidad_determinacion(
			String propiedadesUnidadDeterminacion) {
		propiedades_unidad_determinacion = propiedadesUnidadDeterminacion;
	}

	public String getPropiedades_tipo_determinacion() {
		return propiedades_tipo_determinacion;
	}

	public void setPropiedades_tipo_determinacion(
			String propiedadesTipoDeterminacion) {
		propiedades_tipo_determinacion = propiedadesTipoDeterminacion;
	}
	

	public String getPropiedades_tramite_unidad_determinacion() {
		return propiedades_tramite_unidad_determinacion;
	}

	public void setPropiedades_tramite_unidad_determinacion(
			String propiedades_tramite_unidad_determinacion) {
		this.propiedades_tramite_unidad_determinacion = propiedades_tramite_unidad_determinacion;
	}

	public String getPropiedades_tramite_tipo_determinacion() {
		return propiedades_tramite_tipo_determinacion;
	}

	public void setPropiedades_tramite_tipo_determinacion(
			String propiedades_tramite_tipo_determinacion) {
		this.propiedades_tramite_tipo_determinacion = propiedades_tramite_tipo_determinacion;
	}

}
