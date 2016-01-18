 package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class ServiciosSaneamientoEIEL extends WorkflowEIEL implements Serializable, EIELPanel {
		
	public ServiciosSaneamientoEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_saneam_serv","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_saneam_serv","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Entidad","codentidad","eiel_t_saneam_serv","getCodINEEntidad"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Núcleo","codpoblamiento","eiel_t_saneam_serv","getCodINEPoblamiento"));
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

	private String pozos = null;
	private String sumideros = null;
	private String alivAcumulacion = null;
	private String alivSinAcumulacion = null;
	private String calidad = null;

	private Integer vivNoConectadas = null;
	private Integer vivConectadas = null;
	private Integer longDeficitaria = null;
	private Integer vivvDeficitarias = null;
	private Integer poblResDeficitaria = null;
	private Integer poblEstDeficitaria = null;
	private Integer caudalTotal = null;
	private Integer caudalTratado = null;
	private Integer caudalUrbano = null;
	private Integer caudalRustico = null;
	private Integer caudalIndustrial = null;
	private Integer estadoRevision = null;
	
	private String observaciones = null;
	
	private Date fechaRevision = null;
	
	private VersionEiel version = null;
	
	
	public String getCodINEEntidad() {
		return codINEEntidad;
	}
	public void setCodINEEntidad(String codINEEntidad) {
		this.codINEEntidad = codINEEntidad;
	}
	
	public String getPozos() {
		return pozos;
	}
	public void setPozos(String pozos) {
		this.pozos = pozos;
	}	
	public String getSumideros() {
		return sumideros;
	}
	public void setSumideros(String sumideros) {
		this.sumideros = sumideros;
	}	
	public String getAlivAcumulacion() {
		return alivAcumulacion;
	}
	public void setAlivAcumulacion(String alivAcumulacion) {
		this.alivAcumulacion = alivAcumulacion;
	}	
	public String getAlivSinAcumulacion() {
		return alivSinAcumulacion;
	}
	public void setAlivSinAcumulacion(String alivSinAcumulacion) {
		this.alivSinAcumulacion = alivSinAcumulacion;
	}	
	public String getCalidad() {
		return calidad;
	}
	public void setCalidad(String calidad) {
		this.calidad = calidad;
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
	
	public Integer getVivNoConectadas() {
		return vivNoConectadas;
	}
	public void setVivNoConectadas(Integer vivNoConectadas) {
		this.vivNoConectadas = vivNoConectadas;
	}
	public Integer getVivConectadas() {
		return vivConectadas;
	}
	public void setVivConectadas(Integer vivConectadas) {
		this.vivConectadas = vivConectadas;
	}
	public Integer getLongDeficitaria() {
		return longDeficitaria;
	}
	public void setLongDeficitaria(Integer longDeficitaria) {
		this.longDeficitaria = longDeficitaria;
	}
	public Integer getVivDeficitarias() {
		return vivvDeficitarias;
	}
	public void setVivDeficitarias(Integer vivvDeficitarias) {
		this.vivvDeficitarias = vivvDeficitarias;
	}
	public Integer getPoblResDeficitaria() {
		return poblResDeficitaria;
	}
	public void setPoblResDeficitaria(Integer poblResDeficitaria) {
		this.poblResDeficitaria = poblResDeficitaria;
	}
	public Integer getPoblEstDeficitaria() {
		return poblEstDeficitaria;
	}
	public void setPoblEstDeficitaria(Integer poblEstDeficitaria) {
		this.poblEstDeficitaria = poblEstDeficitaria;
	}
	
	public Integer getcCaudalTratado() {
		return caudalTratado;
	}
	public void setCaudalTratado(Integer caudalTratado) {
		this.caudalTratado = caudalTratado;
	}
	public Integer getCaudalUrbano() {
		return caudalUrbano;
	}
	public void setCaudalUrbano(Integer caudalUrbano) {
		this.caudalUrbano = caudalUrbano;
	}
	public Integer getCaudalRustico() {
		return caudalRustico;
	}
	public void setCaudalRustico(Integer caudalRustico) {
		this.caudalRustico = caudalRustico;
	}
	public Integer getCaudalIndustrial() {
		return caudalIndustrial;
	}
	public void setCaudalIndustrial(Integer caudalIndustrial) {
		this.caudalIndustrial = caudalIndustrial;
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
	public Integer getVivvDeficitarias() {
		return vivvDeficitarias;
	}
	public void setVivvDeficitarias(Integer vivvDeficitarias) {
		this.vivvDeficitarias = vivvDeficitarias;
	}
	public Integer getCaudalTotal() {
		return caudalTotal;
	}
	public void setCaudalTotal(Integer caudalTotal) {
		this.caudalTotal = caudalTotal;
	}
	public Integer getCaudalTratado() {
		return caudalTratado;
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
