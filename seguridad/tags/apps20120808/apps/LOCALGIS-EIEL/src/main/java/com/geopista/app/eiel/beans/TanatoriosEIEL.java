 package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class TanatoriosEIEL extends WorkflowEIEL  implements Serializable, EIELPanel {
	
	public TanatoriosEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código provincia","codprov","eiel_t_ta","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_ta","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_t_ta","getClave"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Entidad","codentidad","eiel_t_ta","getCodINEEntidad"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Núcleo","codpoblamiento","eiel_t_ta","getCodINEPoblamiento"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Orden","orden_ta","eiel_t_ta","getCodOrden"));
	}
	
	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);
		fields.put("clave", clave);
		fields.put("codpoblamiento", codINEPoblamiento);
		fields.put("codentidad", codINEEntidad);
		fields.put("orden_ta", codOrden);		
		return fields;
	}
	
	private String clave = null;
	private String codINEEntidad = null;
	private String codINEPoblamiento = null;
	private String codOrden = null;
	
	private String nombre = null;	
	private String titularidad = null;
	private String gestion = null;
	private String estado = null;
	private String observaciones = null;
	
	private String acceso_s_ruedas = null;
	private String obra_ejec = null;
	
	private Integer supCubierta = null;
	private Integer supLibre = null;
	private Integer supSolar = null;
	private Integer salas = null;
	private Integer estadoRevision = null;
	
	private Date fechaInstalacion = null;
	private Date fechaRevision = null;
	
	
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
	public String getCodOrden() {
		return codOrden;
	}
	public void setCodOrden(String codOrden) {
		this.codOrden = codOrden;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}	
	public String getTitularidad() {
		return titularidad;
	}
	public void setTitularidad(String titularidad) {
		this.titularidad = titularidad;
	}
	public String getGestion() {
		return gestion;
	}
	public void setGestion(String gestion) {
		this.gestion = gestion;
	}	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}	
	public Integer getSupCubierta() {
		return supCubierta;
	}
	public void setSupCubierta(Integer supCubierta) {
		this.supCubierta = supCubierta;
	}
	public Integer getSupLibre() {
		return supLibre;
	}
	public void setSupLibre(Integer supLibre) {
		this.supLibre = supLibre;
	}
	public Integer getSupSolar() {
		return supSolar;
	}
	public void setSupSolar(Integer supSolar) {
		this.supSolar = supSolar;
	}
	public Integer getSalas() {
		return salas;
	}
	public void setSalas(Integer salas) {
		this.salas = salas;
	}
	public Integer getEstadoRevision() {
		return estadoRevision;
	}
	public void setEstadoRevision(Integer estadoRevision) {
		this.estadoRevision = estadoRevision;
	}
	public Date getFechaInstalacion() {
		return fechaInstalacion;
	}
	public void setFechaInstalacion(Date fechaInstalacion) {
		this.fechaInstalacion = fechaInstalacion;
	}
	public Date getFechaRevision() {
		return fechaRevision;
	}
	public void setFechaRevision(Date fechaRevision) {
		this.fechaRevision = fechaRevision;
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
