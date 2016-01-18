/**
 * CreacionExpedientesResponse.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.util.Date;


public class CreacionExpedientesResponse {

	private Expediente expediente;
	
	private EntidadGeneradora entidadGeneradora;
	
	/**
     * Código de la entidad generadora.
     */
    private int codigoEntidadGeneradora;
    
	private Date fechaGeneracion;

	private Date horaGeneracion;
	
	private String tipo;
	
	private int codigoEnvio;
	
	
	
	public int getCodigoEnvio() {
		return codigoEnvio;
	}

	public void setCodigoEnvio(int codigoEnvio) {
		this.codigoEnvio = codigoEnvio;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getFechaGeneracion() {
		return fechaGeneracion;
	}
	
	public void setFechaGeneracion(Date fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}
	
	public Date getHoraGeneracion() {
		return horaGeneracion;
	}
	
	public void setHoraGeneracion(Date horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}
	public int getCodigoEntidadGeneradora() {
		return codigoEntidadGeneradora;
	}
	
	public void setCodigoEntidadGeneradora(int codigoEntidadGeneradora) {
		this.codigoEntidadGeneradora = codigoEntidadGeneradora;
	}
	
	public Expediente getExpediente() {
		return expediente;
	}
	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}
	
	public EntidadGeneradora getEntidadGeneradora() {
		return entidadGeneradora;
	}
	public void setEntidadGeneradora(EntidadGeneradora entidadGeneradora) {
		this.entidadGeneradora = entidadGeneradora;
	}

	
}
