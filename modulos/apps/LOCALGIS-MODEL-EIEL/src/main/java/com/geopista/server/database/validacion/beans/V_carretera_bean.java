/**
 * V_carretera_bean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database.validacion.beans;


public class V_carretera_bean {

	   String provincia="-";
	   String cod_carrt="-";
	   String denominaci="-";
	
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getCod_carrt() {
		return cod_carrt;
	}
	public void setCod_carrt(String cod_carrt) {
		this.cod_carrt = cod_carrt;
	}
	public String getDenominaci() {
		return denominaci;
	}
	public void setDenominaci(String denominaci) {
		this.denominaci = denominaci;
	}
	
	
	
}
