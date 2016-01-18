package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class NucleosPoblacionEIEL extends WorkflowEIEL implements Serializable, EIELPanel{
	
	public NucleosPoblacionEIEL(){
		//REVISAR TABLA eiel_t_?
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Entidad","codentidad","eiel_t_","getCodINEEntidad"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Núcleo","codpoblamiento","eiel_t_","getCodINEPoblamiento"));
	}
	
	@Override
	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);
		fields.put("codentidad", codINEEntidad);
		fields.put("codpoblamiento", codINEPoblamiento);		
		return fields;
	}
	
	private String codINEEntidad = null;
	private String codINEPoblamiento = null;
	private String nombreOficial = null;
	
	private Date fechaRevision = null;
	private Integer estadoActualizacion = null;
	private Date fechaAlta = null;
	
	private String observaciones = null;
	
	private VersionEiel version = null;
	
//	public NucleosPoblacionEIEL(){
//		super();
//		this.codINEEntidad ="";
//		this.codINEMunicipio="";
//		this.codINEPoblamiento="";
//		this.codINEProvincia="";
//		this.nombreOficial="";
//	}
	
	public String getCodINEEntidad() {
		return codINEEntidad;
	}
	public void setCodINEEntidad(String codINEEntidad) {
		this.codINEEntidad = codINEEntidad;
	}
	
	public Date getFechaRevision() {
		return fechaRevision;
	}
	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
	}
	public Integer getEstadoActualizacion() {
		return estadoActualizacion;
	}
	public void setEstadoActualizacion(Integer estadoActualizacion) {
		this.estadoActualizacion = estadoActualizacion;
	}
	
	
	public void setNombreOficial(String nombre){
		this.nombreOficial = nombre;
	}
	
	public String getNombreOficial(){
		return this.nombreOficial;
	}

	public String getCodINEPoblamiento() {
		return codINEPoblamiento;
	}

	public void setCodINEPoblamiento(String codINEPoblamiento) {
		this.codINEPoblamiento = codINEPoblamiento;
	}

	public Date getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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
