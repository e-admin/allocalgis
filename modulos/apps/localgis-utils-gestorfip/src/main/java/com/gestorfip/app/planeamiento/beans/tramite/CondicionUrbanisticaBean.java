/**
 * CondicionUrbanisticaBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.beans.tramite;

public class CondicionUrbanisticaBean {
	
	 private int id; 
	 private int codigoentidad_entidadid; 
	 private int codigodeterminacion_determinacionid; 
	 private int tramite;
	 
	 private CondicionUrbanisticaCasoBean[] lstCondicionUrbanisticaCasoBean;
	 
	public CondicionUrbanisticaCasoBean[] getLstCondicionUrbanisticaCasoBean() {
		return lstCondicionUrbanisticaCasoBean;
	}

	public void setLstCondicionUrbanisticaCasoBean(
			CondicionUrbanisticaCasoBean[] lstCondicionUrbanisticaCasoBean) {
		this.lstCondicionUrbanisticaCasoBean = lstCondicionUrbanisticaCasoBean;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getCodigoentidad_entidadid() {
		return codigoentidad_entidadid;
	}
	
	public void setCodigoentidad_entidadid(int codigoentidadEntidadid) {
		codigoentidad_entidadid = codigoentidadEntidadid;
	}
	
	public int getCodigodeterminacion_determinacionid() {
		return codigodeterminacion_determinacionid;
	}
	
	public void setCodigodeterminacion_determinacionid(
			int codigodeterminacionDeterminacionid) {
		codigodeterminacion_determinacionid = codigodeterminacionDeterminacionid;
	}
	
	public int getTramite() {
		return tramite;
	}
	
	public void setTramite(int tramite) {
		this.tramite = tramite;
	}

}
