/**
 * ValidacionesMPTBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database.validacion.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class ValidacionesMPTBean implements Serializable{
	
	private int id;
	private String nombre;
	private String tabla;
	private ArrayList lstvalidacuadros = new ArrayList();
	
	
	public ArrayList getLstvalidacuadros() {
		return lstvalidacuadros;
	}
	public void setLstvalidacuadros(ArrayList lstvalidacuadros) {
		this.lstvalidacuadros = lstvalidacuadros;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getTabla() {
		return tabla;
	}
	public void setTabla(String tabla) {
		this.tabla = tabla;
	}
	
	
}
