/**
 * UnidadEnterramientoBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.cementerios;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class UnidadEnterramientoBean extends ElemCementerioBean implements Serializable{
//public class UnidadEnterramientoBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long idUe= -1;
	private int tipo_unidad;
	private int columna;
	private int fila;
	private int estado;
	private String descripcion;
	private Date fecha_construccion;
	private Date fecha_UltimaRef;
	private Date fecha_UltimaMod;
	private String codigo;

	private ArrayList<PlazaBean> plazas;
	private LocalizacionBean localizacion;
	
	private int numMin_Plazas = 0;
	

    
	public long getIdUe() {
		return idUe;
	}
	public void setIdUe(long idUe) {
		this.idUe = idUe;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public LocalizacionBean getLocalizacion() {
		return localizacion;
	}
	public void setLocalizacion(LocalizacionBean localizacion) {
		this.localizacion = localizacion;
	}

	public int getColumna() {
		return columna;
	}
	public void setColumna(int columna) {
		this.columna = columna;
	}
	public int getFila() {
		return fila;
	}
	public void setFila(int fila) {
		this.fila = fila;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Date getFecha_construccion() {
		return fecha_construccion;
	}
	public void setFecha_construccion(Date fecha_construccion) {
		this.fecha_construccion = fecha_construccion;
	}
	public int getTipo_unidad() {
		return tipo_unidad;
	}
	public void setTipo_unidad(int tipo_unidad) {
		this.tipo_unidad = tipo_unidad;
	}

	public int getNumMin_Plazas() {
		return numMin_Plazas;
	}
	public void setNumMin_Plazas(int numMin_Plazas) {
		this.numMin_Plazas = numMin_Plazas;
	}
	public Date getFecha_UltimaRef() {
		return fecha_UltimaRef;
	}
	public void setFecha_UltimaRef(Date fecha_UltimaRef) {
		this.fecha_UltimaRef = fecha_UltimaRef;
	}
	public Date getFecha_UltimaMod() {
		return fecha_UltimaMod;
	}
	public void setFecha_UltimaMod(Date fecha_UltimaMod) {
		this.fecha_UltimaMod = fecha_UltimaMod;
	}
	public ArrayList<PlazaBean> getPlazas() {
		return plazas;
	}
	public void setPlazas(ArrayList<PlazaBean> plazas) {
		this.plazas = plazas;
	}

}
