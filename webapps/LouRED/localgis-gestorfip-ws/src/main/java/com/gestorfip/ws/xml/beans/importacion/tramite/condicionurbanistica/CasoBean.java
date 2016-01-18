package com.gestorfip.ws.xml.beans.importacion.tramite.condicionurbanistica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CasoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6758481119962968422L;

	private String nombre;
	private String codigo;
	
	private List<Caso_VinculoBean> vinculos = new ArrayList<Caso_VinculoBean>();
	private List<Caso_RegimenBean> regimenes = new ArrayList<Caso_RegimenBean>();
	private List<Caso_DocumentoBean> documentos = new ArrayList<Caso_DocumentoBean>();
	private int seq_id;
	
	public CasoBean() {
		// TODO Auto-generated constructor stub
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

	public List<Caso_VinculoBean> getVinculos() {
		return vinculos;
	}

	public void setVinculos(List<Caso_VinculoBean> vinculos) {
		this.vinculos = vinculos;
	}

	public List<Caso_RegimenBean> getRegimenes() {
		return regimenes;
	}

	public void setRegimenes(List<Caso_RegimenBean> regimenes) {
		this.regimenes = regimenes;
	}

	public List<Caso_DocumentoBean> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(List<Caso_DocumentoBean> documentos) {
		this.documentos = documentos;
	}

	public int getSeq_id() {
		return seq_id;
	}

	public void setSeq_id(int seqId) {
		seq_id = seqId;
	}

}
