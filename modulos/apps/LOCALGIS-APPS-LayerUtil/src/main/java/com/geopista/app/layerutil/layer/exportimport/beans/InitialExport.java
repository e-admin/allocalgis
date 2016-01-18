/**
 * InitialExport.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.layerutil.layer.exportimport.beans;

import java.util.List;

public class InitialExport {

	private List<String> Tables;
	private List<String> Domains;
	private List<String> Layers;
	
	public List<String> getTables() {
		return Tables;
	}
	
	public void setTables(List<String> tables) {
		Tables = tables;
	}
	
	public List<String> getDomains() {
		return Domains;
	}
	
	public void setDomains(List<String> domains) {
		Domains = domains;
	}
	
	public List<String> getLayers() {
		return Layers;
	}
	
	public void setLayers(List<String> layers) {
		Layers = layers;
	}	
}
