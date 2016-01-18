package com.gestorfip.ws.xml.beans.importacion.tramite.condicionurbanistica;

import java.io.Serializable;

public class Caso_Regimen_RegimenEspecificoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3275855229192350827L;

	private int orden;
	private String nombre;
	private String texto;
	private String padre;
	private int seq_id;
	
	public Caso_Regimen_RegimenEspecificoBean() {
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

	public String getPadre() {
		return padre;
	}

	public void setPadre(String padre) {
		this.padre = padre;
	}

	public int getSeq_id() {
		return seq_id;
	}

	public void setSeq_id(int seqId) {
		seq_id = seqId;
	}

}
