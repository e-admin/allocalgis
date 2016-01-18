/**
 * NumeroOT.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.model.ot;

import java.sql.Date;

public class NumeroOT {

	private Integer codigoINEMunicipio = null;
	private String numero = null;
	private String calificador = null;
	private Integer idVia = null;
	private Date fechaEjecucion = null;
	private Integer idacl = null;
	
	public Integer getCodigoINEMunicipio() {
		return codigoINEMunicipio;
	}
	public void setCodigoINEMunicipio(Integer codigoINEMunicipio) {
		this.codigoINEMunicipio = codigoINEMunicipio;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getCalificador() {
		return calificador;
	}
	public void setCalificador(String calificador) {
		this.calificador = calificador;
	}
	public Integer getIdVia() {
		return idVia;
	}
	public void setIdVia(Integer idVia) {
		this.idVia = idVia;
	}
	public Date getFechaEjecucion() {
		return fechaEjecucion;
	}
	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}
	public Integer getIdacl() {
		return idacl;
	}
	public void setIdacl(Integer idacl) {
		this.idacl = idacl;
	}
	
	
}
