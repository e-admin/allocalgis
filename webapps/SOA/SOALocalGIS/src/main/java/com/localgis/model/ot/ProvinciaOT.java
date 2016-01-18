/**
 * ProvinciaOT.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.model.ot;

public class ProvinciaOT {

	private Integer codigoINE = null;
	
	private String nombreOficial = null;
	
	private String nombreCoOficial = null;
	
	private String comunidadAutonoma = null;
	
	public Integer getCodigoINE() {
		return codigoINE;
	}
	public void setCodigoINE(Integer codigoINE) {
		this.codigoINE = codigoINE;
	}
	public String getNombreOficial() {
		return nombreOficial;
	}
	public void setNombreOficial(String nombreOficial) {
		this.nombreOficial = nombreOficial;
	}
	public String getNombreCoOficial() {
		return nombreCoOficial;
	}
	public void setNombreCoOficial(String nombreCoOficial) {
		this.nombreCoOficial = nombreCoOficial;
	}
	public String getComunidadAutonoma() {
		return comunidadAutonoma;
	}
	public void setComunidadAutonoma(String comunidadAutonoma) {
		this.comunidadAutonoma = comunidadAutonoma;
	}
	
	
}
