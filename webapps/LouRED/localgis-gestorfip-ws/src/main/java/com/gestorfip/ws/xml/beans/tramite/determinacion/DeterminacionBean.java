package com.gestorfip.ws.xml.beans.tramite.determinacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DeterminacionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5767562377467898557L;

	private String codigo;
	private String caracter;
	private String apartado;
	private String nombre;
	private String etiqueta;
	private boolean suspendida;
	private String texto;
	private String unidad_determinacion;
	private String base_determinacion;
	private String madre;
	
	private List<DeterminacionReguladoraBean> determinacionesreguladoras = new ArrayList<DeterminacionReguladoraBean>();
	private List<RegulacionEspecificaBean> regulacionesespecificas = new ArrayList<RegulacionEspecificaBean>();
	private List<DocumentoBean> documentos = new ArrayList<DocumentoBean>();
	private List<GrupoAplicacionBean> gruposaplicaciones = new ArrayList<GrupoAplicacionBean>();
	private List<ValorReferenciaBean> valoresreferencia = new ArrayList<ValorReferenciaBean>();
	private int seq_id;
	
	public DeterminacionBean() {
		// TODO Auto-generated constructor stub
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCaracter() {
		return caracter;
	}

	public void setCaracter(String caracter) {
		this.caracter = caracter;
	}

	public String getApartado() {
		return apartado;
	}

	public void setApartado(String apartado) {
		this.apartado = apartado;
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

	public boolean isSuspendida() {
		return suspendida;
	}

	public void setSuspendida(boolean suspendida) {
		this.suspendida = suspendida;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getUnidad_determinacion() {
		return unidad_determinacion;
	}

	public void setUnidad_determinacion(String unidadDeterminacion) {
		unidad_determinacion = unidadDeterminacion;
	}

	public String getBase_determinacion() {
		return base_determinacion;
	}

	public void setBase_determinacion(String baseDeterminacion) {
		base_determinacion = baseDeterminacion;
	}

	public String getMadre() {
		return madre;
	}

	public void setMadre(String madre) {
		this.madre = madre;
	}

	public List<DeterminacionReguladoraBean> getDeterminacionesreguladoras() {
		return determinacionesreguladoras;
	}

	public void setDeterminacionesreguladoras(
			List<DeterminacionReguladoraBean> determinacionesreguladoras) {
		this.determinacionesreguladoras = determinacionesreguladoras;
	}

	public List<DocumentoBean> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(List<DocumentoBean> documentos) {
		this.documentos = documentos;
	}

	public List<GrupoAplicacionBean> getGruposaplicaciones() {
		return gruposaplicaciones;
	}

	public void setGruposaplicaciones(List<GrupoAplicacionBean> gruposaplicaciones) {
		this.gruposaplicaciones = gruposaplicaciones;
	}

	public List<ValorReferenciaBean> getValoresreferencia() {
		return valoresreferencia;
	}

	public void setValoresreferencia(List<ValorReferenciaBean> valoresreferencia) {
		this.valoresreferencia = valoresreferencia;
	}

	public List<RegulacionEspecificaBean> getRegulacionesespecificas() {
		return regulacionesespecificas;
	}

	public void setRegulacionesespecificas(
			List<RegulacionEspecificaBean> regulacionesespecificas) {
		this.regulacionesespecificas = regulacionesespecificas;
	}

	public int getSeq_id() {
		return seq_id;
	}

	public void setSeq_id(int seqId) {
		seq_id = seqId;
	}

	
}
