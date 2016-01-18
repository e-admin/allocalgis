package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class TramosCarreterasEIEL extends WorkflowEIEL implements Serializable, EIELPanel {
	
	public TramosCarreterasEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("IdMunicip","id_municipio","eiel_t_carreteras","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Provincia","codprov","eiel_t_carreteras","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código carretera","cod_carrt","eiel_t_carreteras","getCodCarretera"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Punto kilométrico inicial","pki","eiel_t_carreteras","getPKI"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Punto kilométrico final","pkf","eiel_t_carreteras","getPKF"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Gestor","titular_via","eiel_t_carreteras","getTitularidad"));
	}
	
	@Override
	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();	
		fields.put("id_municipio", Integer.parseInt(codINEMunicipio));
		fields.put("codprov", codINEProvincia);
		fields.put("cod_carrt", codCarretera);
		fields.put("pki", pki);
		fields.put("pkf", pkf);
		fields.put("gestor", titular_via);		
		return fields;
	}
	
	private int id=-1;
	//private int id_municipio=-1;
	//private String codINEProvincia = null;
	private String codCarretera = null;
	private BigDecimal pki = null;
	private BigDecimal pkf = null;
	private String claseVia = null;
	private String denominacion = null;
	private String titular_via = null;
	private String observaciones = null;	
	private Date fecha_revision = null;	
	
	private VersionEiel version = null;
	

	public String getCodCarretera() {
		return codCarretera;
	}	
	public void setCodCarretera(String codCarretera) {
		this.codCarretera = codCarretera;
	}	
	public BigDecimal getPKI() {
		return pki;
	}	
	public void setPKI(BigDecimal pki) {
		this.pki = pki;
	}
	public BigDecimal getPKF() {
		return pkf;
	}	
	public void setPKF(BigDecimal pkf) {
		this.pkf = pkf;
	}
	public String getClaseVia() {
		return claseVia;
	}
	public void setClaseVia(String claseVia) {
		this.claseVia = claseVia;
	}
	public String getDenominacion() {
		return denominacion;
	}
	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}
	public String getTitularidad() {
		return titular_via;
	}
	public void setTitularidad(String titularidad) {
		this.titular_via = titularidad;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public Date getFechaActualizacion() {
		return fecha_revision;
	}
	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fecha_revision = fechaActualizacion;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
