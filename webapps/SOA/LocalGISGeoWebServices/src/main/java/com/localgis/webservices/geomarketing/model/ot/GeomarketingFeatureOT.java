/**
 * GeomarketingFeatureOT.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.geomarketing.model.ot;

public class GeomarketingFeatureOT {
	private String wktGeometry;
	private Integer srid;
	public Integer getSrid() {
		return srid;
	}
	public void setSrid(Integer srid) {
		this.srid = srid;
	}
	private String attributeName;
	private String municipio;
	private Integer[] id;
	public String getWktGeometry() {
		return wktGeometry;
	}
	public void setWktGeometry(String wktGeometry) {
		this.wktGeometry = wktGeometry;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public void setMunicipio(String int1) {
		this.municipio = int1;
		
	}
	public String getMunicipio(){
		return this.municipio;
	}
	public Integer[] getId() {
		return this.id;
	}
	public void setId(Integer[] id){
		this.id = id;
	}
}
