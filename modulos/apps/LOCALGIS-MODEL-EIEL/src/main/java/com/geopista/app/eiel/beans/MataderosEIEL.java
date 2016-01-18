/**
 * MataderosEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;

public class MataderosEIEL extends WorkflowEIEL  implements Serializable, EIELPanel{

	public MataderosEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código provincia","codprov","eiel_t_mt","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_mt","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_t_mt","getClave"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Entidad","codentidad","eiel_t_mt","getCodINEEntidad"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código INE Núcleo","codpoblamiento","eiel_t_mt","getCodINEPoblamiento"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Orden","orden_mt","eiel_t_mt","getOrden"));
	}
	
	public Hashtable getIdentifyFields() {		
		Hashtable fields = new Hashtable();
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);
		fields.put("clave", clave);
		fields.put("codpoblamiento", codINEPoblamiento);
		fields.put("codentidad", codINEEntidad);
		fields.put("orden_mt", orden);		
		return fields;
	}
	
	private String clave = null;
	private String codINEEntidad = null;
	private String codINEPoblamiento = null;
	private String orden = null;

	private Integer superficieCubierta = null;
	private Integer superficieAireLibre = null;
	private Integer superficieSolar = null;
	private Integer capacidadMax = null;
	private Integer capacidadUtilizada = null;
	
	private String nombre = null;
	private String clase = null;
	private String titular = null;
	private String gestion = null;
	private String estado = null;
	private String tunel = null;
	private String bovino = null;
	private String ovino = null;
	private String porcino = null;
	private String otros = null;
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

	public Integer getCapacidadMax() {
		return capacidadMax;
	}

	public void setCapacidadMax(Integer capacidadMax) {
		this.capacidadMax = capacidadMax;
	}

	public Integer getCapacidadUtilizada() {
		return capacidadUtilizada;
	}

	public void setCapacidadUtilizada(Integer capacidadUtilizada) {
		this.capacidadUtilizada = capacidadUtilizada;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
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

	public String getTunel() {
		return tunel;
	}

	public void setTunel(String tunel) {
		this.tunel = tunel;
	}

	public String getBovino() {
		return bovino;
	}

	public void setBovino(String bovino) {
		this.bovino = bovino;
	}

	public String getOvino() {
		return ovino;
	}

	public void setOvino(String ovino) {
		this.ovino = ovino;
	}

	public String getPorcino() {
		return porcino;
	}

	public void setPorcino(String porcino) {
		this.porcino = porcino;
	}

	public String getOtros() {
		return otros;
	}

	public void setOtros(String otros) {
		this.otros = otros;
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
