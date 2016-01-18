package com.gestorfip.ws.xml.beans.importacion.diccionario;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DiccionarioBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8827526807311248622L;

	private List<TipoAmbitoBean> tiposAmbito = new ArrayList<TipoAmbitoBean>();
	private List<AmbitoBean> ambitos = new ArrayList<AmbitoBean>();
	private List<RelacionBean> organigramaAmbitos = new ArrayList<RelacionBean>();
	private List<CaracterDeterminacionBean> caracteresDeterminacion = new ArrayList<CaracterDeterminacionBean>();
	private List<TipoEntidadBean> tiposEntidad = new ArrayList<TipoEntidadBean>();
	private List<TipoOperacionEntidadBean> tiposOperacionEntidad = new ArrayList<TipoOperacionEntidadBean>();
	private List<TipoOperacionDeterminacionBean> tiposOperacionDeterminacion = new ArrayList<TipoOperacionDeterminacionBean>();
	private List<OperacionCaracterBean> operacionesCaracteres = new ArrayList<OperacionCaracterBean>();
	private List<TipoDocumentoBean> tiposDocumento = new ArrayList<TipoDocumentoBean>();
	private List<GrupoDocumentoBean> gruposDocumento = new ArrayList<GrupoDocumentoBean>();
	private List<TipoTramiteBean> tiposTramites = new ArrayList<TipoTramiteBean>();
	private List<InstrumentoBean> instrumentos = new ArrayList<InstrumentoBean>();
	
	public DiccionarioBean() {
	}

	public List<TipoAmbitoBean> getTiposAmbito() {
		return tiposAmbito;
	}

	public void setTiposAmbito(List<TipoAmbitoBean> tiposAmbito) {
		this.tiposAmbito = tiposAmbito;
	}

	public List<AmbitoBean> getAmbitos() {
		return ambitos;
	}

	public void setAmbitos(List<AmbitoBean> ambitos) {
		this.ambitos = ambitos;
	}

	public List<RelacionBean> getOrganigramaAmbitos() {
		return organigramaAmbitos;
	}

	public void setOrganigramaAmbitos(List<RelacionBean> organigramaAmbitos) {
		this.organigramaAmbitos = organigramaAmbitos;
	}

	public List<CaracterDeterminacionBean> getCaracteresDeterminacion() {
		return caracteresDeterminacion;
	}

	public void setCaracteresDeterminacion(
			List<CaracterDeterminacionBean> caracteresDeterminacion) {
		this.caracteresDeterminacion = caracteresDeterminacion;
	}

	public List<TipoEntidadBean> getTiposEntidad() {
		return tiposEntidad;
	}

	public void setTiposEntidad(List<TipoEntidadBean> tiposEntidad) {
		this.tiposEntidad = tiposEntidad;
	}

	public List<TipoOperacionEntidadBean> getTiposOperacionEntidad() {
		return tiposOperacionEntidad;
	}

	public void setTiposOperacionEntidad(
			List<TipoOperacionEntidadBean> tiposOperacionEntidad) {
		this.tiposOperacionEntidad = tiposOperacionEntidad;
	}

	public List<TipoOperacionDeterminacionBean> getTiposOperacionDeterminacion() {
		return tiposOperacionDeterminacion;
	}

	public void setTiposOperacionDeterminacion(
			List<TipoOperacionDeterminacionBean> tiposOperacionDeterminacion) {
		this.tiposOperacionDeterminacion = tiposOperacionDeterminacion;
	}

	public List<OperacionCaracterBean> getOperacionesCaracteres() {
		return operacionesCaracteres;
	}

	public void setOperacionesCaracteres(
			List<OperacionCaracterBean> operacionesCaracteres) {
		this.operacionesCaracteres = operacionesCaracteres;
	}

	public List<TipoDocumentoBean> getTiposDocumento() {
		return tiposDocumento;
	}

	public void setTiposDocumento(List<TipoDocumentoBean> tiposDocumento) {
		this.tiposDocumento = tiposDocumento;
	}

	public List<GrupoDocumentoBean> getGruposDocumento() {
		return gruposDocumento;
	}

	public void setGruposDocumento(List<GrupoDocumentoBean> gruposDocumento) {
		this.gruposDocumento = gruposDocumento;
	}

	public List<TipoTramiteBean> getTiposTramites() {
		return tiposTramites;
	}

	public void setTiposTramites(List<TipoTramiteBean> tiposTramites) {
		this.tiposTramites = tiposTramites;
	}

	public List<InstrumentoBean> getInstrumentos() {
		return instrumentos;
	}

	public void setInstrumentos(List<InstrumentoBean> instrumentos) {
		this.instrumentos = instrumentos;
	}
	
	

}
