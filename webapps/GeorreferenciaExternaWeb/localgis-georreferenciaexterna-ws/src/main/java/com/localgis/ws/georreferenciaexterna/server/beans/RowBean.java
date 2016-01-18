/**
 * RowBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.ws.georreferenciaexterna.server.beans;

public class RowBean {
	public ColumnsBean[] getColumns() {
		return columns;
	}
	public void setColumns(ColumnsBean[] columns) {
		this.columns = columns;
	}
	public String getGeometryWkt() {
		return geometryWkt;
	}
	public void setGeometryWkt(String geometryWkt) {
		this.geometryWkt = geometryWkt;
	}
	
	public String getSrid() {
		return srid;
	}
	public void setSrid(String srid) {
		this.srid = srid;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	public String getKey() {
		return key;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getTotal() {
		return total;
	}
	private ColumnsBean[] columns;
	private String geometryWkt;
	private String key;
	private int total;
	private String srid;
}
