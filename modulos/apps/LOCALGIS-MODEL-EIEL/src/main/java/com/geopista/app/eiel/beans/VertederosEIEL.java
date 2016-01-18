/**
 * VertederosEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
 package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class VertederosEIEL extends WorkflowEIEL implements Serializable, EIELPanel {
	
	public VertederosEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_vt","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_vt","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_t_vt","getClave"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Orden","orden_vt","eiel_t_vt","getCodOrden"));
	}	
	
	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);
		fields.put("clave", clave);
		fields.put("orden_vt", codOrden);		
		return fields;
	}
	
	private String clave = null;
	private String codOrden = null;
	
	private String tipo = null;
	private String titularidad = null;
	private String gestion = null;
	
	private String olores = null;
	private String humos = null;
	private String contAnimal = null;
	private String rsgoInundacion = null;
	private String filtraciones = null;
	private String imptVisual = null;
	private String frecAverias = null;
	private String estado = null;	
	private String inestabilidad = null;
	private String saturacion =null;
	private String otros = null;
	private String categoria = null;
	private String actividad = null;
	private String posbAmpliacion = null;	
	private String observaciones = null;
	
	private Date fechaRevision = null;
	private Integer estadoRevision = null;
	
	private Integer capTotal = null;
	private Integer capOcupada = null;
	private Integer capTransform = null;
	private Integer vidaUtil = null;
	private Integer fechaApertura = null;

	
	private String obra_ejecutada=null;
	
	
	private VersionEiel version = null;
	
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getCodOrden() {
		return codOrden;
	}
	public void setCodOrden(String codOrden) {
		this.codOrden = codOrden;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}	
	public String getOlores() {
		return olores;
	}
	public void setOlores(String olores) {
		this.olores = olores;
	}
	public String getHumos() {
		return humos;
	}
	public void setHumos(String humos) {
		this.humos = humos;
	}
	public String getContAnimal() {
		return contAnimal;
	}
	public void setContAnimal(String contAnimal) {
		this.contAnimal = contAnimal;
	}
	public String getRsgoInundacion() {
		return rsgoInundacion;
	}
	public void setRsgoInundacion(String rsgoInundacion) {
		this.rsgoInundacion = rsgoInundacion;
	}
	public String getFiltraciones() {
		return filtraciones;
	}
	public void setFiltraciones(String filtraciones) {
		this.filtraciones = filtraciones;
	}
	public String getImptVisual() {
		return imptVisual;
	}
	public void setImptVisual(String imptVisual) {
		this.imptVisual = imptVisual;
	}
	public String getFrecAverias() {
		return frecAverias;
	}
	public void setFrecAverias(String frecAverias) {
		this.frecAverias = frecAverias;
	}
	public String getInestabilidad() {
		return inestabilidad;
	}
	public void setInestabilidad(String inestabilidad) {
		this.inestabilidad = inestabilidad;
	}
	public String getOtros() {
		return otros;
	}
	public void setOtros(String otros) {
		this.otros = otros;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public String getActividad() {
		return actividad;
	}
	public void setActividad(String actividad) {
		this.actividad = actividad;
	}
	public String getPosbAmpliacion() {
		return posbAmpliacion;
	}
	public void setPosbAmpliacion(String posbAmpliacion) {
		this.posbAmpliacion = posbAmpliacion;
	}	
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public Date getFechaRevision() {
		return fechaRevision;
	}
	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}	
	public Integer getCapTotal() {
		return capTotal;
	}
	public void setCapTotal(Integer capTotal) {
		this.capTotal = capTotal;
	}
	public Integer getCapOcupada() {
		return capOcupada;
	}
	public void setCapOcupada(Integer capOcupada) {
		this.capOcupada = capOcupada;
	}
	public Integer getCapTransform() {
		return capTransform;
	}
	public void setCapTransform(Integer capTransform) {
		this.capTransform = capTransform;
	}
	public Integer getVidaUtil() {
		return vidaUtil;
	}
	public void setVidaUtil(Integer vidaUtil) {
		this.vidaUtil = vidaUtil;
	}
	public Integer getFechaApertura() {
		return fechaApertura;
	}
	public void setFechaApertura(Integer fechaApertura) {
		this.fechaApertura = fechaApertura;
	}
	public Integer getEstadoRevision() {
		return estadoRevision;
	}
	public void setEstadoRevision(Integer estadoRevision) {
		this.estadoRevision = estadoRevision;
	}
	public String getSaturacion() {
		return saturacion;
	}
	public void setSaturacion(String saturacion) {
		this.saturacion = saturacion;
	}
	public String getObra_ejecutada() {
		return obra_ejecutada;
	}
	public void setObra_ejecutada(String obra_ejecutada) {
		this.obra_ejecutada = obra_ejecutada;
	}
	public String getTitularidad() {
		return titularidad;
	}
	public void setTitularidad(String titularidad) {
		this.titularidad = titularidad;
	}
	public String getGestion() {
		return gestion;
	}
	public void setGestion(String gestion) {
		this.gestion = gestion;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(VersionEiel version) {
		this.version = version;
	}
	/**
	 * @return the version
	 */
	public VersionEiel getVersion() {
		return version;
	}	
}
