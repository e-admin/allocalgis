/**
 * LayerInfo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.dialogs.beans;

public class LayerInfo {
	
	private String name;
	private String systemId;
	private int geometryType;
	
	public LayerInfo() {
		super();
	}
	public LayerInfo(String name, String systemId, int geometryType) {
		super();
		this.name = name;
		this.systemId = systemId;
		this.geometryType = geometryType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSystemId() {
		return systemId;
	}
	public void setSystemId(String systemId) {
		this.systemId = systemId;
	}
	public int getGeometryType() {
		return geometryType;
	}
	public void setGeometryType(int geometryType) {
		this.geometryType = geometryType;
	}
}
