package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class PlaneamientoUrbanoEIEL extends WorkflowEIEL implements Serializable, EIELPanel {
	
	public PlaneamientoUrbanoEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_planeam_urban","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_planeam_urban","getCodINEMunicipio"));
	}
	
	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);		
		return fields;
	}
	
	private String tipo = null;
	private String estado = null;
	private String denominacion = null;
	private String observaciones = null;
	
	private Float supUrbano = null;
	private Float supUrbanizable = null;
	private Float supNoUrbanizable = null;
	private Float supNoUrbanizableEsp = null;
	
	private Date fechaRevision = null;
	private Date fechaPublicacion = null;
	
	private Integer estadoRevision = null;
	private Float supMunicipal = null;
	private String orden=null;
	
	
	
	private VersionEiel version = null;
	
	
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
	public String getDenominacion() {
		return denominacion;
	}
	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}	
	public Float getSupUrbano() {
		return supUrbano;
	}
	public void setSupUrbano(Float supUrbano) {
		this.supUrbano = supUrbano;
	}
	public Float getSupUrbanizable() {
		return supUrbanizable;
	}
	public void setSupUrbanizable(Float supUrbanizable) {
		this.supUrbanizable = supUrbanizable;
	}
	public Float getSupNoUrbanizable() {
		return supNoUrbanizable;
	}
	public void setSupNoUrbanizable(Float supNoUrbanizable) {
		this.supNoUrbanizable = supNoUrbanizable;
	}
	public Float getSupNoUrbanizableEsp() {
		return supNoUrbanizableEsp;
	}
	public void setSupNoUrbanizableEsp(Float supNoUrbanizableEsp) {
		this.supNoUrbanizableEsp = supNoUrbanizableEsp;
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
	public Date getFechaPublicacion() {
		return fechaPublicacion;
	}
	public void setFechaPublicacion(Date fechaPublicacion) {
		this.fechaPublicacion = fechaPublicacion;
	}
	public Integer getEstadoRevision() {
		return estadoRevision;
	}
	public void setEstadoRevision(Integer estadoRevision) {
		this.estadoRevision = estadoRevision;
	}
	public Float getSupMunicipal() {
		return supMunicipal;
	}
	public void setSupMunicipal(Float supMunicipal) {
		this.supMunicipal = supMunicipal;
	}
	public String getOrden() {
		return orden;
	}
	public void setOrden(String orden) {
		this.orden = orden;
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
