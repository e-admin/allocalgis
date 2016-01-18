/**
 * ExportMapBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.model;

import java.util.Hashtable;

import com.geopista.protocol.administrador.Entidad;


public class ExportMapBean implements java.io.Serializable{
	
	private String name;
	private int srid;
	private String id_map;
	private Entidad entidad;
	private Hashtable<String, ExportLayersFamilyBean> htLayers = new Hashtable();

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId_map() {
		return id_map;
	}
	public void setId_map(String id_map) {
		this.id_map = id_map;
	}
	public Entidad getEntidad() {
		return entidad;
	}
	public void setEntidad(Entidad entidad) {
		this.entidad = entidad;
	}
	public Hashtable<String, ExportLayersFamilyBean> getHtLayers() {
		return htLayers;
	}
	public void setHtLayers(Hashtable<String, ExportLayersFamilyBean> htLayers) {
		this.htLayers = htLayers;
	}
	public int getSrid() {
		return srid;
	}
	public void setSrid(int srid) {
		this.srid = srid;
	}

	
}
