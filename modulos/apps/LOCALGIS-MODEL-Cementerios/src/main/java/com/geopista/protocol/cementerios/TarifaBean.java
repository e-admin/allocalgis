/**
 * TarifaBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.cementerios;

import java.io.Serializable;

/**
 * Clase que implementa un objeto de tipo Tarifa
 */
public class TarifaBean extends ElemCementerioBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int tipo_unidad = -1;
	private String tipo_calculo;
	private String concepto;
	private String tipo_tarifa;
	private String precio;
	private int id_tarifa = -1;
	

	public int getId_tarifa() {
		return id_tarifa;
	}
	public void setId_tarifa(int id_tarifa) {
		this.id_tarifa = id_tarifa;
	}
	public String getTipo_calculo() {
		return tipo_calculo;
	}
	public void setTipo_calculo(String tipo_calculo) {
		this.tipo_calculo = tipo_calculo;
	}
	public String getConcepto() {
		return concepto;
	}
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getPrecio() {
		return precio;
	}
	public void setPrecio(String precio) {
		this.precio = precio;
	}
	public int getTipo_unidad() {
		return tipo_unidad;
	}
	public void setTipo_unidad(int tipo_unidad) {
		this.tipo_unidad = tipo_unidad;
	}
	public String getTipo_tarifa() {
		return tipo_tarifa;
	}
	public void setTipo_tarifa(String tipo_tarifa) {
		this.tipo_tarifa = tipo_tarifa;
	}
	
	
}
