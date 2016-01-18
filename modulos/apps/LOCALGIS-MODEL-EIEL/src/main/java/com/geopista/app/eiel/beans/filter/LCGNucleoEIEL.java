/**
 * LCGNucleoEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans.filter;

import java.io.Serializable;

public class LCGNucleoEIEL implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2398878193852911999L;
	private String codprov;
	private String codentidad;
	private String codMunicipio;
	private String codpoblamiento;
	private String denominacion;
	public String getCodentidad() {
		return codentidad;
	}
	public void setCodentidad(String codentidad) {
		this.codentidad = codentidad;
	}
	public String getCodpoblamiento() {
		return codpoblamiento;
	}
	public void setCodpoblamiento(String codpoblamiento) {
		this.codpoblamiento = codpoblamiento;
	}
	public String getDenominacion() {
		return denominacion;
	}
	public void setDenominacion(String denominacion) {
		this.denominacion = denominacion;
	}
	@Override
	public String toString() {
		return "LCGNucleoIEL [codentidad=" + codentidad + ", codpoblamiento="
				+ codpoblamiento + ", denominacion=" + denominacion + "]";
	}
	public String getCodMunicipio() {
		return codMunicipio;
	}
	public void setCodMunicipio(String codMunicipio) {
		this.codMunicipio = codMunicipio;
	}
	public String getCodProvincia() {
		return codprov;
	}
	public void setCodProvincia(String codprov) {
		this.codprov = codprov;
	}
	

}
