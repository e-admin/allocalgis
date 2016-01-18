package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class CementeriosEIEL extends WorkflowEIEL  implements Serializable, EIELPanel{

	public CementeriosEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código provincia","codprov","eiel_t_ce","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_ce","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_t_ce","getClave"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Entidad","codentidad","eiel_t_ce","getCodINEEntidad"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Núcleo","codpoblamiento","eiel_t_ce","getCodINEPoblamiento"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Orden","orden_ce","eiel_t_ce","getOrden"));
	}
	
	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);
		fields.put("clave", clave);
		fields.put("codpoblamiento", codINEPoblamiento);
		fields.put("codentidad", codINEEntidad);
		fields.put("orden_ce", orden);		
		return fields;
	}
	
	private String clave = null;
	private String codINEEntidad = null;
	private String codINEPoblamiento = null;
	private String orden = null;
	
	private Float distancia = null;
	private Float saturacion = null;
	private Integer superficie = null;
	
	private String nombre = null;
	private String titular = null;
	private String acceso = null;
	private String capilla = null;
	private String depositoCadaveres = null;
	private String ampliacion = null;
	private String crematorio = null;
	private String observaciones = null;
	
	private String acceso_s_ruedas = null;
	private String obra_ejec = null;
	
	private Date fechaRevision = null;
	private Date fechaInstalacion = null;
	private Integer estadoRevision = null;
	

	private VersionEiel version = null;
	
	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getCodINEEntidad() {
		return codINEEntidad;
	}

	public void setCodINEEntidad(String codINEEntidad) {
		this.codINEEntidad = codINEEntidad;
	}


	public String getOrden() {
		return orden;
	}

	public void setOrden(String orden) {
		this.orden = orden;
	}

	public Float getDistancia() {
		return distancia;
	}

	public void setDistancia(Float distancia) {
		this.distancia = distancia;
	}

	public Float getSaturacion() {
		return saturacion;
	}

	public void setSaturacion(Float saturacion) {
		this.saturacion = saturacion;
	}

	public Integer getSuperficie() {
		return superficie;
	}

	public void setSuperficie(Integer superficie) {
		this.superficie = superficie;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	public String getAcceso() {
		return acceso;
	}

	public void setAcceso(String acceso) {
		this.acceso = acceso;
	}

	public String getCapilla() {
		return capilla;
	}

	public void setCapilla(String capilla) {
		this.capilla = capilla;
	}

	public String getDepositoCadaveres() {
		return depositoCadaveres;
	}

	public void setDepositoCadaveres(String depositoCadaveres) {
		this.depositoCadaveres = depositoCadaveres;
	}

	public String getAmpliacion() {
		return ampliacion;
	}

	public void setAmpliacion(String ampliacion) {
		this.ampliacion = ampliacion;
	}

	public String getCrematorio() {
		return crematorio;
	}

	public void setCrematorio(String crematorio) {
		this.crematorio = crematorio;
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

	public Date getFechaInstalacion() {
		return fechaInstalacion;
	}

	public void setFechaInstalacion(Date fechaInstalacion) {
		this.fechaInstalacion = fechaInstalacion;
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

	public String getAcceso_s_ruedas() {
		return acceso_s_ruedas;
	}

	public void setAcceso_s_ruedas(String acceso_s_ruedas) {
		this.acceso_s_ruedas = acceso_s_ruedas;
	}

	public String getObra_ejec() {
		return obra_ejec;
	}

	public void setObra_ejec(String obra_ejec) {
		this.obra_ejec = obra_ejec;
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
