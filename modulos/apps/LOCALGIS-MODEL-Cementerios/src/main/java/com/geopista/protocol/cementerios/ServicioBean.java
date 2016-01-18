/**
 * ServicioBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.cementerios;

import java.io.Serializable;
import java.util.Date;


/**
 * Clase que implementa un objeto de tipo Servicio
 */
public class ServicioBean extends ElemCementerioBean implements Serializable{
	
	private String consideracion;
	private Date plazo_concesion;
	private int cuota;
	private int canon_mantenimiento;
	private int estado;
	private Date fecha_modificado;
	
	
	public String getConsideracion() {
		return consideracion;
	}
	public void setConsideracion(String consideracion) {
		this.consideracion = consideracion;
	}
	public Date getPlazo_concesion() {
		return plazo_concesion;
	}
	public void setPlazo_concesion(Date plazo_concesion) {
		this.plazo_concesion = plazo_concesion;
	}
	public int getCuota() {
		return cuota;
	}
	public void setCuota(int cuota) {
		this.cuota = cuota;
	}
	public int getCanon_mantenimiento() {
		return canon_mantenimiento;
	}
	public void setCanon_mantenimiento(int canon_mantenimiento) {
		this.canon_mantenimiento = canon_mantenimiento;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public Date getFecha_modificado() {
		return fecha_modificado;
	}
	public void setFecha_modificado(Date fecha_modificado) {
		this.fecha_modificado = fecha_modificado;
	}

}
