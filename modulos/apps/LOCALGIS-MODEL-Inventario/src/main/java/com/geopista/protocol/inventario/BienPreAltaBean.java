/**
 * BienPreAltaBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.inventario;

import java.io.Serializable;
import java.util.Date;

/**
 * Creado por SATEC.
 * User: angeles
 * Date: 07-sep-2006
 * Time: 15:38:56
 */
public class BienPreAltaBean implements Serializable {
	private static final long	serialVersionUID	= 3546643200656945977L;
	
    private long id=-1;
    private String nombre;
    private String descripcion;
    private long idMunicipio;
    private long tipo;
    private Date fechaAdquisicion;
    private double costeAdquisicion;
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public long getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(long idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public void setTipo(long tipo) {
		this.tipo = tipo;
	}
	public long getTipo() {
		return tipo;
	}
	public Date getFechaAdquisicion() {
		return fechaAdquisicion;
	}
	public void setFechaAdquisicion(Date fechaAdquisicion) {
		this.fechaAdquisicion = fechaAdquisicion;
	}
	public double getCosteAdquisicion() {
		return costeAdquisicion;
	}
	public void setCosteAdquisicion(double costeAdquisicion) {
		this.costeAdquisicion = costeAdquisicion;
	}
    
}
