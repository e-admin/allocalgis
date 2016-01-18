/**
 * CaptacionesEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import com.geopista.app.eiel.beans.filter.LCGCampoCapaTablaEIEL;
import com.geopista.util.EIELPanel;
import java.lang.reflect.Method;

public class CaptacionesEIEL extends WorkflowEIEL implements Serializable, EIELPanel  {
	
	public CaptacionesEIEL(){
		relacionFields = new ArrayList<LCGCampoCapaTablaEIEL>();
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código provincia","codprov","eiel_t_abast_ca","getCodINEProvincia"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Municipio","codmunic","eiel_t_abast_ca","getCodINEMunicipio"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Clave","clave","eiel_t_abast_ca","getClave"));
		relacionFields.add(new LCGCampoCapaTablaEIEL("Código Orden","orden_ca","eiel_t_abast_ca","getCodOrden"));
	}
	
	public Hashtable getIdentifyFields() {
		Hashtable fields = new Hashtable();		
		fields.put("codprov", codINEProvincia);
		fields.put("codmunic", codINEMunicipio);
		fields.put("clave", clave);
		fields.put("orden_ca", codOrden);		
		return fields;
	}
	
	private String clave = null;

	private String codOrden = null;
	
	private String nombre = null;
	private String tipo = null;
	private String titularidad = null;
	private String gestor = null;
	private String sist_impulsion = null;
	private String estado = null;
	private String uso = null;
	private String proteccion = null;
	private String contador = null;
	private String observaciones = null;
	
	private Date fechaRevision = null;
	private Date fechaInst = null;
	private Integer estadoRevision = null;
	
	private String cuenca=null;
	private String  n_expediente=null;
	private String  n_inventario=null;
	private Integer  cota ;
	private Integer  profundidad ;
	private Double max_consumo;
		
	private VersionEiel version = null;

	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
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
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getTitularidad() {
		return titularidad;
	}
	public void setTitularidad(String titularidad) {
		this.titularidad = titularidad;
	}
	public String getTitular() {
		return titularidad;
	}
	public void setTitular(String titularidad) {
		this.titularidad = titularidad;
	}
	public String getGestion() {
		return gestor;
	}
	public void setGestion(String gestion) {
		this.gestor = gestion;
	}
	public String getSistema() {
		return sist_impulsion;
	}
	public void setSistema(String sistema) {
		this.sist_impulsion = sistema;
	}
	
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getTipoUso() {
		return uso;
	}
	public void setTipoUso(String tipoUso) {
		this.uso = tipoUso;
	}
	public String getProteccion() {
		return proteccion;
	}
	public void setProteccion(String proteccion) {
		this.proteccion = proteccion;
	}
	public String getContador() {
		return contador;
	}
	public void setContador(String contador) {
		this.contador = contador;
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
	public Date getFechaInst() {
		return fechaInst;
	}
	public void setFechaInst(Date fechaInst) {
		this.fechaInst = fechaInst;
	}
	public Integer getEstadoRevision() {
		return estadoRevision;
	}
	public void setEstadoRevision(Integer estadoRevision) {
		this.estadoRevision = estadoRevision;
	}
	
	public String getGestor() {
		return gestor;
	}
	public void setGestor(String gestor) {
		this.gestor = gestor;
	}
	public String getUso() {
		return uso;
	}
	public void setUso(String uso) {
		this.uso = uso;
	}
	public String getCuenca() {
		return cuenca;
	}
	public void setCuenca(String cuenca) {
		this.cuenca = cuenca;
	}
	public String getN_expediente() {
		return n_expediente;
	}
	public void setN_expediente(String n_expediente) {
		this.n_expediente = n_expediente;
	}
	public String getN_inventario() {
		return n_inventario;
	}
	public void setN_inventario(String n_inventario) {
		this.n_inventario = n_inventario;
	}
	public Integer getCota() {
		return cota;
	}
	public void setCota(Integer cota) {
		this.cota = cota;
	}
	public Integer getProfundidad() {
		return profundidad;
	}
	public void setProfundidad(Integer profundidad) {
		this.profundidad = profundidad;
	}
	public Double getMax_consumo() {
		return max_consumo;
	}
	public void setMax_consumo(Double max_consumo) {
		this.max_consumo = max_consumo;
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
