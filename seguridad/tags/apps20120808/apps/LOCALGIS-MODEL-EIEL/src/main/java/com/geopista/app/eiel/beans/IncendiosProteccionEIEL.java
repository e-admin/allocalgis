package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class IncendiosProteccionEIEL extends WorkflowEIEL  implements Serializable, EIELPanel{

	public IncendiosProteccionEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código provincia","codprov","eiel_t_ip","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_ip","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_t_ip","getClave"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Entidad","codentidad","eiel_t_ip","getCodINEEntidad"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Núcleo","codpoblamiento","eiel_t_ip","getCodINEPoblamiento"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Orden","orden_ip","eiel_t_ip","getOrden"));
	}
	
	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);
		fields.put("clave", clave);
		fields.put("codpoblamiento", codINEPoblamiento);
		fields.put("codentidad", codINEEntidad);
		fields.put("orden_ip", orden);		
		return fields;
	}
	
	private String clave = null;
	private String codINEEntidad = null;
	private String codINEPoblamiento = null;
	private String orden = null;
	
	private Integer superficieCubierta = null;
	private Integer superficieAireLibre = null;
	private Integer superficieSolar = null;
	private Integer plantillaProfesionales = null;
	private Integer plantillaVoluntarios = null;
	private Integer vechiculosIncendios = null;
	private Integer vechiculosRescate = null;
	private Integer ambulancias = null;
	private Integer mediosAereos = null;
	private Integer otrosVehiculos = null;
	private Integer quitanieves = null;
	private Integer SistemasDeteccionIncencios = null;
	private Integer otros = null;
	
	private String nombre = null;
	private String tipo = null;
	private String titular = null;
	private String gestor = null;
	private String ambito = null;
	private String estado = null;
	
	private String acceso_s_ruedas = null;
	private String obra_ejec = null;
	
	private String observaciones = null;
	
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

	public Integer getSuperficieCubierta() {
		return superficieCubierta;
	}

	public void setSuperficieCubierta(Integer superficieCubierta) {
		this.superficieCubierta = superficieCubierta;
	}

	public Integer getSuperficieAireLibre() {
		return superficieAireLibre;
	}

	public void setSuperficieAireLibre(Integer superficieAireLibre) {
		this.superficieAireLibre = superficieAireLibre;
	}

	public Integer getSuperficieSolar() {
		return superficieSolar;
	}

	public void setSuperficieSolar(Integer superficieSolar) {
		this.superficieSolar = superficieSolar;
	}

	public Integer getPlantillaProfesionales() {
		return plantillaProfesionales;
	}

	public void setPlantillaProfesionales(Integer plantillaProfesionales) {
		this.plantillaProfesionales = plantillaProfesionales;
	}

	public Integer getPlantillaVoluntarios() {
		return plantillaVoluntarios;
	}

	public void setPlantillaVoluntarios(Integer plantillaVoluntarios) {
		this.plantillaVoluntarios = plantillaVoluntarios;
	}

	public Integer getVechiculosIncendios() {
		return vechiculosIncendios;
	}

	public void setVechiculosIncendios(Integer vechiculosIncendios) {
		this.vechiculosIncendios = vechiculosIncendios;
	}

	public Integer getVechiculosRescate() {
		return vechiculosRescate;
	}

	public void setVechiculosRescate(Integer vechiculosRescate) {
		this.vechiculosRescate = vechiculosRescate;
	}

	public Integer getAmbulancias() {
		return ambulancias;
	}

	public void setAmbulancias(Integer ambulancias) {
		this.ambulancias = ambulancias;
	}

	public Integer getMediosAereos() {
		return mediosAereos;
	}

	public void setMediosAereos(Integer mediosAereos) {
		this.mediosAereos = mediosAereos;
	}

	public Integer getOtrosVehiculos() {
		return otrosVehiculos;
	}

	public void setOtrosVehiculos(Integer otrosVehiculos) {
		this.otrosVehiculos = otrosVehiculos;
	}

	public Integer getQuitanieves() {
		return quitanieves;
	}

	public void setQuitanieves(Integer quitanieves) {
		this.quitanieves = quitanieves;
	}

	public Integer getSistemasDeteccionIncencios() {
		return SistemasDeteccionIncencios;
	}

	public void setSistemasDeteccionIncencios(Integer sistemasDeteccionIncencios) {
		SistemasDeteccionIncencios = sistemasDeteccionIncencios;
	}

	public Integer getOtros() {
		return otros;
	}

	public void setOtros(Integer otros) {
		this.otros = otros;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}


	public String getAmbito() {
		return ambito;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
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

	public String getGestor() {
		return gestor;
	}

	public void setGestor(String gestor) {
		this.gestor = gestor;
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
