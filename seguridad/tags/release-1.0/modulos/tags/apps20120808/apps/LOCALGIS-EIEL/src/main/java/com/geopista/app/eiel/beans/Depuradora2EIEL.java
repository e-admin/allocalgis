package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class Depuradora2EIEL extends WorkflowEIEL implements Serializable, EIELPanel {
	
	public Depuradora2EIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t2_saneam_ed","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t2_saneam_ed","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_t2_saneam_ed","getClave"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Orden","orden_ed","eiel_t2_saneam_ed","getCodOrden"));
	}
	
	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();		
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);
		fields.put("clave", clave);
		fields.put("orden_ed", codOrden);		
		return fields;
	}
	
	private String clave = null;
	
	private String codOrden = null;
	private String titular = null;
	private String gestor = null;
	
	private Integer capacidad = null;
	
	private String problemas1 = null;
	private String problemas2 = null;
	private String problemas3 = null;
	
	private String gestionLodos = null;
	
	private Integer lodosVertedero = null;
	private Integer lodosIncineracion = null;
	private Integer lodosAgrConCompostaje = null;
	private Integer lodosAgrSinCompostaje = null;
	private Integer lodosOtroFinal = null;
	
	private Date fechaInstalacion = null;
	private String observaciones = null;
	
	private Date fechaRevision = null;
	private Integer estadoRevision = null;
	
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
	
	public String getProblemas1() {
		return problemas1;
	}
	public void setProblemas1(String problemas1) {
		this.problemas1 = problemas1;
	}
	public String getProblemas2() {
		return problemas2;
	}
	public void setProblemas2(String problemas2) {
		this.problemas2 = problemas2;
	}
	public String getProblemas3() {
		return problemas3;
	}
	public void setProblemas3(String problemas3) {
		this.problemas3 = problemas3;
	}
	public String getGestionLodos() {
		return gestionLodos;
	}
	public void setGestionLodos(String gestionLodos) {
		this.gestionLodos = gestionLodos;
	}
	public Integer getLodosVertedero() {
		return lodosVertedero;
	}
	public void setLodosVertedero(Integer lodosVertedero) {
		this.lodosVertedero = lodosVertedero;
	}
	public Integer getLodosIncineracion() {
		return lodosIncineracion;
	}
	public void setLodosIncineracion(Integer lodosIncineracion) {
		this.lodosIncineracion = lodosIncineracion;
	}
	public Integer getLodosAgrConCompostaje() {
		return lodosAgrConCompostaje;
	}
	public void setLodosAgrConCompostaje(Integer lodosAgrConCompostaje) {
		this.lodosAgrConCompostaje = lodosAgrConCompostaje;
	}
	public Integer getLodosAgrSinCompostaje() {
		return lodosAgrSinCompostaje;
	}
	public void setLodosAgrSinCompostaje(Integer lodosAgrSinCompostaje) {
		this.lodosAgrSinCompostaje = lodosAgrSinCompostaje;
	}
	public Integer getLodosOtroFinal() {
		return lodosOtroFinal;
	}
	public void setLodosOtroFinal(Integer lodosOtroFinal) {
		this.lodosOtroFinal = lodosOtroFinal;
	}
	public Date getFechaInstalacion() {
		return fechaInstalacion;
	}
	public void setFechaInstalacion(Date fechaInstalacion) {
		this.fechaInstalacion = fechaInstalacion;
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
	public Integer getEstadoRevision() {
		return estadoRevision;
	}
	public void setEstadoRevision(Integer estadoRevision) {
		this.estadoRevision = estadoRevision;
	}
	public Integer getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(Integer capacidad) {
		this.capacidad = capacidad;
	}
	
	public String getTitular() {
		return titular;
	}
	public void setTitular(String titular) {
		this.titular = titular;
	}
	public String getGestor() {
		return gestor;
	}
	public void setGestor(String gestor) {
		this.gestor = gestor;
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
