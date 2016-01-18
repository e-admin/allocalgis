package com.gestorfip.ws.xml.beans.importacion.tramite.entidad;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Geometry;

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
	private Geometry geometria;
	private String base_entidad;
	private String base_codigoTramite;
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

	public Geometry getGeometria() {
		return geometria;
	}

	public void setGeometria(Geometry geometria) {
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

	public String getBase_codigoTramite() {
		return base_codigoTramite;
	}

	public void setBase_codigoTramite(String baseCodigoTramite) {
		base_codigoTramite = baseCodigoTramite;
	}

}
