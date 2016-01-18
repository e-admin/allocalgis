/**
 * ExportLayersFamilyBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ExportLayersFamilyBean implements Serializable {
	private String idLayerFamily;
	private String nameLayerFamily;
	private ArrayList<ExportLayersBean> exportSHPLayers = new ArrayList();

	public String getIdLayerFamily() {
		return idLayerFamily;
	}
	public void setIdLayerFamily(String idLayerFamily) {
		this.idLayerFamily = idLayerFamily;
	}
	public String getNameLayerFamily() {
		return nameLayerFamily;
	}
	public void setNameLayerFamily(String nameLayerFamily) {
		this.nameLayerFamily = nameLayerFamily;
	}
	public ArrayList<ExportLayersBean> getExportSHPLayers() {
		return exportSHPLayers;
	}
	public void setExportSHPLayers(ArrayList<ExportLayersBean> exportSHPLayers) {
		this.exportSHPLayers = exportSHPLayers;
	}
	
}

