/**
 * LCGCampoCapaTablaEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans.filter;

import java.io.Serializable;

public class LCGCampoCapaTablaEIEL implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1651606207113289269L;
	private String campoCapa;
	private String campoBD;
	private String tabla;
	private String method;
	
	
	public LCGCampoCapaTablaEIEL() {
		super();
	}
	public LCGCampoCapaTablaEIEL(String campoCapa, String campoBD,
			String tabla, String method) {
		super();
		this.campoCapa = campoCapa;
		this.campoBD = campoBD;
		this.tabla = tabla;
		this.method = method;
	}
	public String getCampoCapa() {
		return campoCapa;
	}
	public void setCampoCapa(String campoCapa) {
		this.campoCapa = campoCapa;
	}
	public String getCampoBD() {
		return campoBD;
	}
	public void setCampoBD(String campoBD) {
		this.campoBD = campoBD;
	}
	
	public String getTabla() {
		return tabla;
	}
	public void setTabla(String tabla) {
		this.tabla = tabla;
	}

	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}

	
	
}
