/**
 * OperacionEntidadBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.beans.tramite;

import com.gestorfip.app.planeamiento.beans.panels.tramite.EntidadPanelBean;

public class OperacionEntidadBean {
	
	  private int id; 
	  private int tipo; 
	  private long orden;
	  private String texto;
	  private int operadora_entidadid; 
	  private EntidadPanelBean operada_entidad;
	  private double propiedadesadscripcion_cuantia;
	  private String propiedadesadscripcion_texto;
	  private int propiedadesadscripcion_unidad_determinacionid;
	  private int propiedadesadscripcion_tipo_determinacionid;
	  private int tramite;
	  
	public int getTramite() {
		return tramite;
	}

	public void setTramite(int tramite) {
		this.tramite = tramite;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public int getTipo() {
		return tipo;
	}
	
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	
	public long getOrden() {
		return orden;
	}
	
	public void setOrden(long orden) {
		this.orden = orden;
	}
	
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public int getOperadora_entidadid() {
		return operadora_entidadid;
	}
	
	public void setOperadora_entidadid(int operadoraEntidadid) {
		operadora_entidadid = operadoraEntidadid;
	}
	
	public EntidadPanelBean getOperada_entidad() {
		return operada_entidad;
	}
	
	public void setOperada_entidad(EntidadPanelBean operadaEntidad) {
		operada_entidad = operadaEntidad;
	}
	
	public double getPropiedadesadscripcion_cuantia() {
		return propiedadesadscripcion_cuantia;
	}
	
	public void setPropiedadesadscripcion_cuantia(
			double propiedadesadscripcionCuantia) {
		propiedadesadscripcion_cuantia = propiedadesadscripcionCuantia;
	}
	
	public String getPropiedadesadscripcion_texto() {
		return propiedadesadscripcion_texto;
	}
	
	public void setPropiedadesadscripcion_texto(String propiedadesadscripcionTexto) {
		propiedadesadscripcion_texto = propiedadesadscripcionTexto;
	}
	
	public int getPropiedadesadscripcion_unidad_determinacionid() {
		return propiedadesadscripcion_unidad_determinacionid;
	}
	
	public void setPropiedadesadscripcion_unidad_determinacionid(
			int propiedadesadscripcionUnidadDeterminacionid) {
		propiedadesadscripcion_unidad_determinacionid = propiedadesadscripcionUnidadDeterminacionid;
	}
	
	public int getPropiedadesadscripcion_tipo_determinacionid() {
		return propiedadesadscripcion_tipo_determinacionid;
	}
	
	public void setPropiedadesadscripcion_tipo_determinacionid(
			int propiedadesadscripcionTipoDeterminacionid) {
		propiedadesadscripcion_tipo_determinacionid = propiedadesadscripcionTipoDeterminacionid;
	}

}
