/**
 * ConcesionBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.cementerios;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Clase que implementa un objeto de tipo Concesion
 */
public class ConcesionBean extends ElemCementerioBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int tipo_concesion;
	private Date fecha_ini;
	private Date fecha_fin;
	private Date fecha_ultRenovacion;
	private String descripcion;
	private TarifaBean tarifa;
	private PersonaBean titular;
	private List<PersonaBean> cotitulares;
	private UnidadEnterramientoBean unidad;
	private int estado;
	private String bonificacion;
	private String precio_final;
	private String localizacion;
	private String codigo;
	

	private long id_concesion= -1;
	
	
	public String getLocalizacion() {
		return localizacion;
	}
	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public Date getFecha_ini() {
		return fecha_ini;
	}
	public void setFecha_ini(Date fecha_ini) {
		this.fecha_ini = fecha_ini;
	}
	public Date getFecha_fin() {
		return fecha_fin;
	}
	public void setFecha_fin(Date fecha_fin) {
		this.fecha_fin = fecha_fin;
	}
	public Date getFecha_ultRenovacion() {
		return fecha_ultRenovacion;
	}
	public void setFecha_ultRenovacion(Date fecha_ultRenovacion) {
		this.fecha_ultRenovacion = fecha_ultRenovacion;
	}
	public TarifaBean getTarifa() {
		return tarifa;
	}
	public void setTarifa(TarifaBean tarifa) {
		this.tarifa = tarifa;
	}

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public UnidadEnterramientoBean getUnidad() {
		return unidad;
	}
	public void setUnidad(UnidadEnterramientoBean unidad) {
		this.unidad = unidad;
	}

	public int getTipo_concesion() {
		return tipo_concesion;
	}
	public void setTipo_concesion(int tipo_concesion) {
		this.tipo_concesion = tipo_concesion;
	}
	public PersonaBean getTitular() {
		return titular;
	}
	public void setTitular(PersonaBean titular) {
		this.titular = titular;
	}
	public List<PersonaBean> getCotitulares() {
		return cotitulares;
	}
	public void setCotitulares(List<PersonaBean> cotitulares) {
		this.cotitulares = cotitulares;
	}
	public long getId_concesion() {
		return id_concesion;
	}
	public void setId_concesion(long id_concesion) {
		this.id_concesion = id_concesion;
	}

	public String getBonificacion() {
		return bonificacion;
	}
	public void setBonificacion(String bonificacion) {
		this.bonificacion = bonificacion;
	}
	public String getPrecio_final() {
		return precio_final;
	}
	public void setPrecio_final(String precio_final) {
		this.precio_final = precio_final;
	}

}
