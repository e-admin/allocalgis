package com.gestorfip.ws.xml.beans.tramite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.gestorfip.ws.xml.beans.tramite.adscripcion.AdscripcionBean;
import com.gestorfip.ws.xml.beans.tramite.aplicacionambito.AplicacionAmbitoBean;
import com.gestorfip.ws.xml.beans.tramite.condicionurbanistica.CondicionUrbanisticaBean;
import com.gestorfip.ws.xml.beans.tramite.determinacion.DeterminacionBean;
import com.gestorfip.ws.xml.beans.tramite.documento.DocumentoBean;
import com.gestorfip.ws.xml.beans.tramite.entidad.EntidadBean;
import com.gestorfip.ws.xml.beans.tramite.operacion.OperacionDeterminacionBean;
import com.gestorfip.ws.xml.beans.tramite.operacion.OperacionEntidadBean;
import com.gestorfip.ws.xml.beans.tramite.unidad.UnidadBean;



public class TramiteBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7761285713493365599L;

	private String tipotramite;
	private String codigo;
	private String texto;

	private List<AdscripcionBean> adscripciones = new ArrayList<AdscripcionBean>();
	private List<AplicacionAmbitoBean> aplicacionambitos = new ArrayList<AplicacionAmbitoBean>();
	private List<CondicionUrbanisticaBean> condicionesurbanisticas = new ArrayList<CondicionUrbanisticaBean>();
	private List<DeterminacionBean> determinaciones = new ArrayList<DeterminacionBean>();
	private List<DocumentoBean> documentos = new ArrayList<DocumentoBean>();
	private List<EntidadBean> entidades = new ArrayList<EntidadBean>();
	private List<OperacionDeterminacionBean> operacionesdeterminaciones = new ArrayList<OperacionDeterminacionBean>();
	private List<OperacionEntidadBean> operacionesentidades = new ArrayList<OperacionEntidadBean>();
	private List<UnidadBean> unidades = new ArrayList<UnidadBean>();
	
	public TramiteBean() {
	}
	
	

	public List<AdscripcionBean> getAdscripciones() {
		return adscripciones;
	}



	public void setAdscripciones(List<AdscripcionBean> adscripciones) {
		this.adscripciones = adscripciones;
	}



	public List<AplicacionAmbitoBean> getAplicacionambitos() {
		return aplicacionambitos;
	}



	public void setAplicacionambitos(List<AplicacionAmbitoBean> aplicacionambitos) {
		this.aplicacionambitos = aplicacionambitos;
	}



	public List<CondicionUrbanisticaBean> getCondicionesurbanisticas() {
		return condicionesurbanisticas;
	}



	public void setCondicionesurbanisticas(
			List<CondicionUrbanisticaBean> condicionesurbanisticas) {
		this.condicionesurbanisticas = condicionesurbanisticas;
	}



	public List<DeterminacionBean> getDeterminaciones() {
		return determinaciones;
	}



	public void setDeterminaciones(List<DeterminacionBean> determinaciones) {
		this.determinaciones = determinaciones;
	}



	public List<DocumentoBean> getDocumentos() {
		return documentos;
	}



	public void setDocumentos(List<DocumentoBean> documentos) {
		this.documentos = documentos;
	}



	public List<EntidadBean> getEntidades() {
		return entidades;
	}



	public void setEntidades(List<EntidadBean> entidades) {
		this.entidades = entidades;
	}



	public List<OperacionDeterminacionBean> getOperacionesdeterminaciones() {
		return operacionesdeterminaciones;
	}



	public void setOperacionesdeterminaciones(
			List<OperacionDeterminacionBean> operacionesdeterminaciones) {
		this.operacionesdeterminaciones = operacionesdeterminaciones;
	}



	public List<OperacionEntidadBean> getOperacionesentidades() {
		return operacionesentidades;
	}



	public void setOperacionesentidades(
			List<OperacionEntidadBean> operacionesentidades) {
		this.operacionesentidades = operacionesentidades;
	}



	public List<UnidadBean> getUnidades() {
		return unidades;
	}



	public void setUnidades(List<UnidadBean> unidades) {
		this.unidades = unidades;
	}



	public String getTipotramite() {
		return tipotramite;
	}

	public void setTipotramite(String tipotramite) {
		this.tipotramite = tipotramite;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

}
