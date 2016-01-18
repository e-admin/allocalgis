/**
 * ExportSHPBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.util.exportshp.beans;

import java.io.Serializable;
import java.util.ArrayList;

import com.geopista.model.ExportMapBean;
import com.geopista.protocol.administrador.Entidad;

public class ExportSHPBean{
	
	private ExportMapBean exportMap = new ExportMapBean();
	private Entidad entidad = new Entidad();
	
	public ExportSHPBean() {

	}
	
	public ExportMapBean getExportMap() {
		return exportMap;
	}
	public void setExportMap(ExportMapBean exportMap) {
		this.exportMap = exportMap;
	}
	public Entidad getEntidad() {
		return entidad;
	}
	public void setEntidad(Entidad entidad) {
		this.entidad = entidad;
	}
	
	
}

