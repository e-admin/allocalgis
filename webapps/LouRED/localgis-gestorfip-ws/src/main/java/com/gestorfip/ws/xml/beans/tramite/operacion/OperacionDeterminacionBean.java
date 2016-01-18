package com.gestorfip.ws.xml.beans.tramite.operacion;

import java.io.Serializable;

public class OperacionDeterminacionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1646548010776513118L;

	private String tipo;
	private long orden;
	private String texto;
	private String operada_determinacion;
	private String operadora_determinacion;
	
	public OperacionDeterminacionBean() {
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

	public String getOperada_determinacion() {
		return operada_determinacion;
	}

	public void setOperada_determinacion(String operadaDeterminacion) {
		operada_determinacion = operadaDeterminacion;
	}

	public String getOperadora_determinacion() {
		return operadora_determinacion;
	}

	public void setOperadora_determinacion(String operadoraDeterminacion) {
		operadora_determinacion = operadoraDeterminacion;
	}

}
