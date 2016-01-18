/**
 * DocumentoPanelBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.beans.panels.tramite;

public class DocumentoPanelBean {

	private int id;
	private int documentoid;
	private int determinacion;
	private String nombre;
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getDocumentoid() {
		return documentoid;
	}
	
	public void setDocumentoid(int documentoid) {
		this.documentoid = documentoid;
	}
	
	public int getDeterminacion() {
		return determinacion;
	}
	
	public void setDeterminacion(int determinacion) {
		this.determinacion = determinacion;
	}
}
