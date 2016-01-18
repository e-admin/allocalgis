package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class NucleoEncuestado7EIEL extends WorkflowEIEL implements Serializable, EIELPanel{

	public NucleoEncuestado7EIEL(){
		//REVISAR TABLA eiel_t_?
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_inf_ttmm","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_inf_ttmm","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Núcleo","codpoblamiento","eiel_t_inf_ttmm","getCodINEPoblamiento"));
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
	
	private Integer viviendasDeficitariasAlumbrado = null;
	private Integer longitudDeficitariaAlumbrado = null;

	
	private String tvAntena = null;
	private String tvCable = null;
	private String calidadGSM = null;
	private String calidadUMTS= null;
	private String calidadGPRS= null;
	private String correos = null;
	private String rdsi = null;
	private String adsl = null;
	private String wifi = null;
	private String internetTV = null;
	private String internetRed = null;
	private String internetSatelite = null;
	private String internetPublico = null;
	private String calidadElectricidad = null;
	private String calidadGas = null;
	private String observaciones = null;
	
	private Date fechaRevision = null;
	private Integer estadoRevision = null;
	

	private VersionEiel version = null;
	
	

	public String getCodINEEntidad() {
		return codINEEntidad;
	}

	public void setCodINEEntidad(String codINEEntidad) {
		this.codINEEntidad = codINEEntidad;
	}


	public Integer getViviendasDeficitariasAlumbrado() {
		return viviendasDeficitariasAlumbrado;
	}

	public void setViviendasDeficitariasAlumbrado(
			Integer viviendasDeficitariasAlumbrado) {
		this.viviendasDeficitariasAlumbrado = viviendasDeficitariasAlumbrado;
	}

	public Integer getLongitudDeficitariaAlumbrado() {
		return longitudDeficitariaAlumbrado;
	}

	public void setLongitudDeficitariaAlumbrado(Integer longitudDeficitariaAlumbrado) {
		this.longitudDeficitariaAlumbrado = longitudDeficitariaAlumbrado;
	}

	public String getTvAntena() {
		return tvAntena;
	}

	public void setTvAntena(String tvAntena) {
		this.tvAntena = tvAntena;
	}

	public String getTvCable() {
		return tvCable;
	}

	public void setTvCable(String tvCable) {
		this.tvCable = tvCable;
	}

	public String getCalidadGSM() {
		return calidadGSM;
	}

	public void setCalidadGSM(String calidadGSM) {
		this.calidadGSM = calidadGSM;
	}

	public String getCalidadUMTS() {
		return calidadUMTS;
	}

	public void setCalidadUMTS(String calidadUMTS) {
		this.calidadUMTS = calidadUMTS;
	}

	public String getCorreos() {
		return correos;
	}

	public void setCorreos(String correos) {
		this.correos = correos;
	}

	public String getRdsi() {
		return rdsi;
	}

	public void setRdsi(String rdsi) {
		this.rdsi = rdsi;
	}

	public String getAdsl() {
		return adsl;
	}

	public void setAdsl(String adsl) {
		this.adsl = adsl;
	}

	public String getWifi() {
		return wifi;
	}

	public void setWifi(String wifi) {
		this.wifi = wifi;
	}

	public String getInternetTV() {
		return internetTV;
	}

	public void setInternetTV(String internetTV) {
		this.internetTV = internetTV;
	}

	public String getInternetRed() {
		return internetRed;
	}

	public void setInternetRed(String internetRed) {
		this.internetRed = internetRed;
	}

	public String getInternetSatelite() {
		return internetSatelite;
	}

	public void setInternetSatelite(String internetSatelite) {
		this.internetSatelite = internetSatelite;
	}

	public String getInternetPublico() {
		return internetPublico;
	}

	public void setInternetPublico(String internetPublico) {
		this.internetPublico = internetPublico;
	}

	public String getCalidadElectricidad() {
		return calidadElectricidad;
	}

	public void setCalidadElectricidad(String calidadElectricidad) {
		this.calidadElectricidad = calidadElectricidad;
	}

	public String getCalidadGas() {
		return calidadGas;
	}

	public void setCalidadGas(String calidadGas) {
		this.calidadGas = calidadGas;
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

	public String getCalidadGPRS() {
		return calidadGPRS;
	}

	public void setCalidadGPRS(String calidadGPRS) {
		this.calidadGPRS = calidadGPRS;
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
