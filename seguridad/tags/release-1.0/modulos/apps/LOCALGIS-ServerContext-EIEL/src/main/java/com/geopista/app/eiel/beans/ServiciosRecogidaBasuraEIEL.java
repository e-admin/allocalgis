 package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class ServiciosRecogidaBasuraEIEL extends WorkflowEIEL implements Serializable, EIELPanel {

	public ServiciosRecogidaBasuraEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_rb_serv","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_rb_serv","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Entidad","codentidad","eiel_t_rb_serv","getCodINEEntidad"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Núcleo","codpoblamiento","eiel_t_rb_serv","getCodINEPoblamiento"));
	}
	
	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);
		fields.put("codpoblamiento", codINEPoblamiento);
		fields.put("codentidad", codINEEntidad);		
		return fields;
	}
	
	private String codINEEntidad = null;
	private String codINEPoblamiento = null;
	
	private String servLimpCalles = null;
	private String observaciones = null;
	
	private Date fechaRevision = null;
	
	private Integer vivSinServicio = null;
	private Integer poblResSinServicio = null;
	private Integer poblEstSinServicio = null;
	private Integer plantilla = null;
	private Integer estadoRevision = null;

	private VersionEiel version = null;
	
	
	public String getCodINEEntidad() {
		return codINEEntidad;
	}
	public void setCodINEEntidad(String codINEEntidad) {
		this.codINEEntidad = codINEEntidad;
	}
	public String getServLimpCalles() {
		return servLimpCalles;
	}
	public void setServLimpCalles(String servLimpCalles) {
		this.servLimpCalles = servLimpCalles;
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
	public Integer getVivSinServicio() {
		return vivSinServicio;
	}
	public void setVivSinServicio(Integer vivSinServicio) {
		this.vivSinServicio = vivSinServicio;
	}
	public Integer getPoblResSinServicio() {
		return poblResSinServicio;
	}
	public void setPoblResSinServicio(Integer poblResSinServicio) {
		this.poblResSinServicio = poblResSinServicio;
	}
	public Integer getPoblEstSinServicio() {
		return poblEstSinServicio;
	}
	public void setPoblEstSinServicio(Integer poblEstSinServicio) {
		this.poblEstSinServicio = poblEstSinServicio;
	}
	public Integer getPlantilla() {
		return plantilla;
	}
	public void setPlantilla(Integer plantilla) {
		this.plantilla = plantilla;
	}	
	public Integer getEstadoRevision() {
		return estadoRevision;
	}
	public void setEstadoRevision(Integer estadoRevision) {
		this.estadoRevision = estadoRevision;
	}

	
	public String getCodINEPoblamiento() {
		return codINEPoblamiento;
	}
	public void setCodINEPoblamiento(String codINEPoblamiento) {
		this.codINEPoblamiento = codINEPoblamiento;
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
