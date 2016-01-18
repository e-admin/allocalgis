/**
 * GeopistaLayersStyles.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;


public class GeopistaLayersStyles {

	private String layerName;
	
	private Integer IdEntidad;

	private String xmlStyle;

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}
	
	public Integer getIdEntidad() {
		return IdEntidad;
	}

	public void setIdEntidad(Integer idEntidad) {
		IdEntidad = idEntidad;
	}

	public String getXmlStyle() {
		return xmlStyle;
	}

	public void setXmlStyle(String xmlStyle) {
		this.xmlStyle = xmlStyle;
	}

}