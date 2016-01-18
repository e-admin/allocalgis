package com.gestorfip.ws.xml.beans.importacion.tramite.determinacion;

import java.io.Serializable;

public class RegulacionEspecificaBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5740079834235597407L;

	private int orden;
	private String nombre;
	private String texto;
	private String madre;
	private int seq_id;
	
	public RegulacionEspecificaBean() {
		// TODO Auto-generated constructor stub
	}

	public int getOrden() {
		return orden;
	}

	public void setOrden(int orden) {
		this.orden = orden;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getMadre() {
		return madre;
	}

	public void setMadre(String madre) {
		this.madre = madre;
	}

	public int getSeq_id() {
		return seq_id;
	}

	public void setSeq_id(int seqId) {
		seq_id = seqId;
	}

}
