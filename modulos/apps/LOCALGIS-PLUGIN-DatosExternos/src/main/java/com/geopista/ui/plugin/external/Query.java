/**
 * Query.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.external;

public class Query {

	private int dataSourceId;
	private int queryId;
	private String name;
	private String text;
	private String internalLayer;
	private String internalAttribute;
	private String externalTable;
	private String externalColumn;
	
	
	public int getDataSourceId() {
		return dataSourceId;
	}
	public void setDataSourceId(int dataSourceId) {
		this.dataSourceId = dataSourceId;
	}
	public int getQueryId() {
		return queryId;
	}
	public void setQueryId(int queryId) {
		this.queryId = queryId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getInternalLayer() {
		return internalLayer;
	}
	public void setInternalLayer(String internalLayer) {
		this.internalLayer = internalLayer;
	}
	public String getInternalAttribute() {
		return internalAttribute;
	}
	public void setInternalAttribute(String internalAttribute) {
		this.internalAttribute = internalAttribute;
	}
	public String getExternalTable() {
		return externalTable;
	}
	public void setExternalTable(String externalTable) {
		this.externalTable = externalTable;
	}
	public String getExternalColumn() {
		return externalColumn;
	}
	public void setExternalColumn(String externalColumn) {
		this.externalColumn = externalColumn;
	}


}
