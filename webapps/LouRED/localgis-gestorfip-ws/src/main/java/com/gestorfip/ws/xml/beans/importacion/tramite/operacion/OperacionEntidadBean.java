package com.gestorfip.ws.xml.beans.importacion.tramite.operacion;

import java.io.Serializable;

public class OperacionEntidadBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2159157794132792745L;

	private String tipo;
	private long orden;
	private String texto;
	private String operada_entidad;
	private String operadora_entidad;
	private double propiedadesadscripcion_cuantia;
	private String propiedadesadscripcion_texto;
	private String propiedadesadscripcion_unidad_determinacion;
	private String propiedadesadscripcion_tipo_determinacion;
	
	public OperacionEntidadBean() {
		// TODO Auto-generated constructor stub
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public long getOrden() {
		return orden;
	}

	public void setOrden(long orden) {
		this.orden = orden;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getOperada_entidad() {
		return operada_entidad;
	}

	public void setOperada_entidad(String operadaEntidad) {
		operada_entidad = operadaEntidad;
	}

	public String getOperadora_entidad() {
		return operadora_entidad;
	}

	public void setOperadora_entidad(String operadoraEntidad) {
		operadora_entidad = operadoraEntidad;
	}

	public double getPropiedadesadscripcion_cuantia() {
		return propiedadesadscripcion_cuantia;
	}

	public void setPropiedadesadscripcion_cuantia(double propiedadesadscripcionCuantia) {
		propiedadesadscripcion_cuantia = propiedadesadscripcionCuantia;
	}

	public String getPropiedadesadscripcion_texto() {
		return propiedadesadscripcion_texto;
	}

	public void setPropiedadesadscripcion_texto(String propiedadesadscripcionTexto) {
		propiedadesadscripcion_texto = propiedadesadscripcionTexto;
	}

	public String getPropiedadesadscripcion_unidad_determinacion() {
		return propiedadesadscripcion_unidad_determinacion;
	}

	public void setPropiedadesadscripcion_unidad_determinacion(
			String propiedadesadscripcionUnidadDeterminacion) {
		propiedadesadscripcion_unidad_determinacion = propiedadesadscripcionUnidadDeterminacion;
	}

	public String getPropiedadesadscripcion_tipo_determinacion() {
		return propiedadesadscripcion_tipo_determinacion;
	}

	public void setPropiedadesadscripcion_tipo_determinacion(
			String propiedadesadscripcionTipoDeterminacion) {
		propiedadesadscripcion_tipo_determinacion = propiedadesadscripcionTipoDeterminacion;
	}

}
