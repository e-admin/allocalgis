/**
 * ACCoverageLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.administradorCartografia;

import java.io.Serializable;

public class ACCoverageLayer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int id_municipio;
	private String coverage_layer_name = null;
	private String desc_path = null;
	private String srs =null;
	

	public ACCoverageLayer() {
		super();
	}


	public ACCoverageLayer(int id_municipio, String coverage_layer_name, String desc_path, String srs) {
		super();
		this.id_municipio = id_municipio;
		this.coverage_layer_name = coverage_layer_name;
		this.desc_path = desc_path;
		this.srs = srs;
	}
	
	
	public String getCoverage_layer_name() {
		return coverage_layer_name;
	}


	public String getDesc_path() {
		return desc_path;
	}


	public int getId_municipio() {
		return id_municipio;
	}


	public String getSrs() {
		return srs;
	}


	public void setCoverage_layer_name(String coverage_layer_name) {
		this.coverage_layer_name = coverage_layer_name;
	}


	public void setDesc_path(String desc_path) {
		this.desc_path = desc_path;
	}


	public void setId_municipio(int id_municipio) {
		this.id_municipio = id_municipio;
	}


	public void setSrs(String srs) {
		this.srs = srs;
	}

	
	
}
