/**
 * RegistroPropiedad.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.io.Serializable;

public class RegistroPropiedad implements Serializable{
	
	/**
	 * Código de la provincia.
	 */
	private String codigoProvincia;
	
	/**
	 * Código del Registro de la propiedad.
	 */
	private String codigoRegistroPropiedad;
	
	/**
	 * Registro de la propiedad = codigoProvincia + codigoRegistroPropiedad
	 */
	private String registroPropiedad;
	
	
	
	
	public RegistroPropiedad(String registroPropiedad) {
		this.registroPropiedad = registroPropiedad;
		this.codigoProvincia = registroPropiedad.substring(0,2);
		this.codigoRegistroPropiedad = registroPropiedad.substring(2,5);
	}
	
	

	public RegistroPropiedad(String codigoProvincia, String codigoRegistroPropiedad) {
		this.codigoProvincia = codigoProvincia;
		this.codigoRegistroPropiedad = codigoRegistroPropiedad;
		this.registroPropiedad = codigoProvincia + codigoRegistroPropiedad;
	}



	/**
	 * Devuelve el registro de la propiedad.
	 * @return String con el registro de la propiedad.
	 */
	public String getRegistroPropiedad() {
		return registroPropiedad;
	}

	/**
	 * Guarda el registro de la propiedad.
	 * @param registroPropiedad String con el registro de la propiedad a cargar.
	 */
	public void setRegistroPropiedad(String registroPropiedad) {
		this.registroPropiedad = registroPropiedad;
	}

	/**
	 * Devuelve el código de la provincia.
	 * @return String con el código de la provincia.
	 */
	public String getCodigoProvincia() {
		return codigoProvincia;
	}

	/**
	 * Guarda el código de la provincia.
	 * @param codigoProvincia  String con el código de la provincia.
	 */
	public void setCodigoProvincia(String codigoProvincia) {
		this.codigoProvincia = codigoProvincia;
	}

	/**
	 * Devuelve el código del registro de la propiedad.
	 * @return String con el código del registro de la propiedad.
	 */
	public String getCodigoRegistroPropiedad() {
		return codigoRegistroPropiedad;
	}

	/**
	 * Guarda el código del registro de la propiedad.
	 * @param codigoRegistroPropiedad String con el registro de la propiedad a cargar.
	 */
	public void setCodigoRegistroPropiedad(String codigoRegistroPropiedad) {
		this.codigoRegistroPropiedad = codigoRegistroPropiedad;
	}
	
	

}
