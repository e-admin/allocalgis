/**
 * InformacionWSFichero.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.servicioWebCatastro.beans;


public class InformacionWSFichero {
	
	private String horaGenerancion;
	
	private String fechaGeneracion;
	
	private String tipo;

	public String getHoraGenerancion() {
		return horaGenerancion;
	}

	public void setHoraGeneracion(String horaGenerancion) {
		this.horaGenerancion = horaGenerancion;
	}

	public String getFechaGeneracion() {
		return fechaGeneracion;
	}

	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
