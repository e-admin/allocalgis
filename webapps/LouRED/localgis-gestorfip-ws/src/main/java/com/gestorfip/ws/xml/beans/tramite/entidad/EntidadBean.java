package com.gestorfip.ws.xml.beans.tramite.entidad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EntidadBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5200532742123321594L;

	private String codigo;
	private String etiqueta;
	private String clave;
	private String nombre;
	private boolean suspendida;
	private String geometria;
	private String base_entidad;
	private String madre;
	private List<DocumentoBean> documentos = new ArrayList<DocumentoBean>();
	private int seq_id;
	
	public EntidadBean() {
		// TODO Auto-generated constructor stub
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getEtiqueta() {
		return etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public boolean isSuspendida() {
		return suspendida;
	}

	public void setSuspendida(boolean suspendida) {
		this.suspendida = suspendida;
	}

	public String getGeometria() {
		return geometria;
	}

	public void setGeometria(String geometria) {
		this.geometria = geometria;
	}

	public String getBase_entidad() {
		return base_entidad;
	}

	public void setBase_entidad(String baseEntidad) {
		base_entidad = baseEntidad;
	}


	public String getMadre() {
		return madre;
	}

	public void setMadre(String madre) {
		this.madre = madre;
	}

	public List<DocumentoBean> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(List<DocumentoBean> documentos) {
		this.documentos = documentos;
	}

	public int getSeq_id() {
		return seq_id;
	}

	public void setSeq_id(int seqId) {
		seq_id = seqId;
	}

}
