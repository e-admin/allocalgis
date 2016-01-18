package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class Encuestados2EIEL extends WorkflowEIEL implements Serializable, EIELPanel{

	public Encuestados2EIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_nucl_encuest_2","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_nucl_encuest_2","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Entidad","codentidad","eiel_t_nucl_encuest_2","getCodINEEntidad"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Núcleo","codpoblamiento","eiel_t_nucl_encuest_2","getCodINEPoblamiento"));
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
	
	private String disponibilidadCaudal = null;
	private String restriccionesAgua = null;
	private String contadores = null;
	private String tasa = null;
	private String annoInstalacion = null;
	private String hidrantes = null;
	private String estadoHidrantes = null;
	private String valvulas = null;
	private String estadoValvulas = null;
	private String bocasRiego = null;
	private String estadoBocasRiego = null;
	private String cisterna = null;
	private String observaciones=null;
	
	private Date fechaRevision = null;
	private Integer estadoRevision = null;
	

	private VersionEiel version = null;
	

	public String getCodINEMunicipio() {
		return codINEMunicipio;
	}

	public void setCodINEMunicipio(String codINEMunicipio) {
		this.codINEMunicipio = codINEMunicipio;
	}

	public String getCodINEEntidad() {
		return codINEEntidad;
	}

	public void setCodINEEntidad(String codINEEntidad) {
		this.codINEEntidad = codINEEntidad;
	}

	public String getDisponibilidadCaudal() {
		return disponibilidadCaudal;
	}

	public void setDisponibilidadCaudal(String disponibilidadCaudal) {
		this.disponibilidadCaudal = disponibilidadCaudal;
	}

	public String getRestriccionesAgua() {
		return restriccionesAgua;
	}

	public void setRestriccionesAgua(String restriccionesAgua) {
		this.restriccionesAgua = restriccionesAgua;
	}

	public String getContadores() {
		return contadores;
	}

	public void setContadores(String contadores) {
		this.contadores = contadores;
	}

	public String getTasa() {
		return tasa;
	}

	public void setTasa(String tasa) {
		this.tasa = tasa;
	}

	public String getAnnoInstalacion() {
		return annoInstalacion;
	}

	public void setAnnoInstalacion(String annoInstalacion) {
		this.annoInstalacion = annoInstalacion;
	}

	public String getHidrantes() {
		return hidrantes;
	}

	public void setHidrantes(String hidrantes) {
		this.hidrantes = hidrantes;
	}

	public String getEstadoHidrantes() {
		return estadoHidrantes;
	}

	public void setEstadoHidrantes(String estadoHidrantes) {
		this.estadoHidrantes = estadoHidrantes;
	}

	public String getValvulas() {
		return valvulas;
	}

	public void setValvulas(String valvulas) {
		this.valvulas = valvulas;
	}

	public String getEstadoValvulas() {
		return estadoValvulas;
	}

	public void setEstadoValvulas(String estadoValvulas) {
		this.estadoValvulas = estadoValvulas;
	}

	public String getBocasRiego() {
		return bocasRiego;
	}

	public void setBocasRiego(String bocasRiego) {
		this.bocasRiego = bocasRiego;
	}

	public String getEstadoBocasRiego() {
		return estadoBocasRiego;
	}

	public void setEstadoBocasRiego(String estadoBocasRiego) {
		this.estadoBocasRiego = estadoBocasRiego;
	}

	public String getCisterna() {
		return cisterna;
	}

	public void setCisterna(String cisterna) {
		this.cisterna = cisterna;
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
