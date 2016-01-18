package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class LimpiezaCallesEIEL extends WorkflowEIEL  implements Serializable, EIELPanel {

	public LimpiezaCallesEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_id","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_id","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Entidad","codentidad","eiel_t_id","getCodINEEntidad"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Núcleo","codpoblamiento","eiel_t_id","getCodINEPoblamiento"));
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
	
	private String srb_viviendas_afec = null;
	private String srb_pob_res_afect = null;
	private String srb_pob_est_afect = null;
	private String serv_limp_calles = null;
	
	private Float plantilla_serv_limp = null;
	private String observaciones = null;
	
	private Date fecharevision = null;
	private Integer estadoRevision = null;

	
	private VersionEiel version = null;
	
	
	public String getCodINEEntidad() {
		return codINEEntidad;
	}
	public void setCodINEEntidad(String codINEEntidad) {
		this.codINEEntidad = codINEEntidad;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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
	public Date getFecharevision() {
		return fecharevision;
	}
	public void setFecharevision(Date fecharevision) {
		this.fecharevision = fecharevision;
	}
	public String getSrb_viviendas_afec() {
		return srb_viviendas_afec;
	}
	public void setSrb_viviendas_afec(String srb_viviendas_afec) {
		this.srb_viviendas_afec = srb_viviendas_afec;
	}
	public String getSrb_pob_res_afect() {
		return srb_pob_res_afect;
	}
	public void setSrb_pob_res_afect(String srb_pob_res_afect) {
		this.srb_pob_res_afect = srb_pob_res_afect;
	}
	public String getSrb_pob_est_afect() {
		return srb_pob_est_afect;
	}
	public void setSrb_pob_est_afect(String srb_pob_est_afect) {
		this.srb_pob_est_afect = srb_pob_est_afect;
	}
	public String getServ_limp_calles() {
		return serv_limp_calles;
	}
	public void setServ_limp_calles(String serv_limp_calles) {
		this.serv_limp_calles = serv_limp_calles;
	}
	public Float getPlantilla_serv_limp() {
		return plantilla_serv_limp;
	}
	public void setPlantilla_serv_limp(Float plantilla_serv_limp) {
		this.plantilla_serv_limp = plantilla_serv_limp;
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
