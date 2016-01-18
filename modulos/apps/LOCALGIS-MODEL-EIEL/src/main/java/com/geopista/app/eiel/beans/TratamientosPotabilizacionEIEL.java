/**
 * TratamientosPotabilizacionEIEL.java
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

public class TratamientosPotabilizacionEIEL extends WorkflowEIEL implements Serializable, EIELPanel{
	
	public TratamientosPotabilizacionEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código provincia","codprov","eiel_t_abast_tp","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_abast_tp","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_t_abast_tp","getClave"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Orden","orden_tp","eiel_t_abast_tp","getOrdenPotabilizadora"));
	}	

	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);
		fields.put("clave", clave);
		fields.put("orden_tp", ordenPotabilizadora);		
		return fields;
	}

	private String clave = null;
	private String ordenPotabilizadora = null;
	
	private String tipo = null;
	private String ubicacion = null;
	private String soloDesinfeccion = null;
	private String categoriaA1 = null;
	private String categoriaA2 = null;
	private String categoriaA3 = null;
	private String desaladora = null;
	private String otros = null;
	private String metodoDesinfeccion1 = null;
	private String metodoDesinfeccion2 = null;
	private String metodoDesinfeccion3 = null;
	private String perioricidad = null;
	private String organismoControl = null;
	private String estado = null;
	private String observ = null;
	
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

	

	public String getOrdenPotabilizadora() {
		return ordenPotabilizadora;
	}

	public void setOrdenPotabilizadora(String ordenPotabilizadora) {
		this.ordenPotabilizadora = ordenPotabilizadora;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getSoloDesinfeccion() {
		return soloDesinfeccion;
	}

	public void setSoloDesinfeccion(String soloDesinfeccion) {
		this.soloDesinfeccion = soloDesinfeccion;
	}

	public String getCategoriaA1() {
		return categoriaA1;
	}

	public void setCategoriaA1(String categoriaA1) {
		this.categoriaA1 = categoriaA1;
	}

	public String getCategoriaA2() {
		return categoriaA2;
	}

	public void setCategoriaA2(String categoriaA2) {
		this.categoriaA2 = categoriaA2;
	}

	public String getCategoriaA3() {
		return categoriaA3;
	}

	public void setCategoriaA3(String categoriaA3) {
		this.categoriaA3 = categoriaA3;
	}

	public String getDesaladora() {
		return desaladora;
	}

	public void setDesaladora(String desaladora) {
		this.desaladora = desaladora;
	}

	public String getOtros() {
		return otros;
	}

	public void setOtros(String otros) {
		this.otros = otros;
	}

	public String getMetodoDesinfeccion1() {
		return metodoDesinfeccion1;
	}

	public void setMetodoDesinfeccion1(String metodoDesinfeccion1) {
		this.metodoDesinfeccion1 = metodoDesinfeccion1;
	}

	public String getMetodoDesinfeccion2() {
		return metodoDesinfeccion2;
	}

	public void setMetodoDesinfeccion2(String metodoDesinfeccion2) {
		this.metodoDesinfeccion2 = metodoDesinfeccion2;
	}

	public String getMetodoDesinfeccion3() {
		return metodoDesinfeccion3;
	}

	public void setMetodoDesinfeccion3(String metodoDesinfeccion3) {
		this.metodoDesinfeccion3 = metodoDesinfeccion3;
	}

	public String getPerioricidad() {
		return perioricidad;
	}

	public void setPerioricidad(String perioricidad) {
		this.perioricidad = perioricidad;
	}

	public String getOrganismoControl() {
		return organismoControl;
	}

	public void setOrganismoControl(String organismoControl) {
		this.organismoControl = organismoControl;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getObserv() {
		return observ;
	}

	public void setObserv(String observ) {
		this.observ = observ;
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
