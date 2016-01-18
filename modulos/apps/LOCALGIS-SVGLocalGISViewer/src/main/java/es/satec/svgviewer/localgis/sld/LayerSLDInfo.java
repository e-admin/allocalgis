/**
 * LayerSLDInfo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.svgviewer.localgis.sld;

public class LayerSLDInfo {

	private String layerName;
	private StyledLayerDescriptor sld;
	private String currentStyleName;
	private boolean active;
	private boolean search;

	public LayerSLDInfo(String layerName, StyledLayerDescriptor sld, String currentStyleName) {
		this(layerName, sld, currentStyleName, false, false);
	}
	
	public LayerSLDInfo(String layerName, StyledLayerDescriptor sld, String currentStyleName, boolean active) {
		this(layerName, sld, currentStyleName, active, false);
	}

	public LayerSLDInfo(String layerName, StyledLayerDescriptor sld, String currentStyleName, boolean active, boolean search) {
		this.layerName = layerName;
		this.sld = sld;
		this.currentStyleName = currentStyleName;
		this.active = active;
		this.search = search;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCurrentStyleName() {
		return currentStyleName;
	}

	public void setCurrentStyleName(String currentStyleName) {
		this.currentStyleName = currentStyleName;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	public StyledLayerDescriptor getSld() {
		return sld;
	}

	public void setSld(StyledLayerDescriptor sld) {
		this.sld = sld;
	}
	
	public boolean isSearch() {
		return search;
	}

	public void setSearch(boolean search) {
		this.search = search;
	}
}
