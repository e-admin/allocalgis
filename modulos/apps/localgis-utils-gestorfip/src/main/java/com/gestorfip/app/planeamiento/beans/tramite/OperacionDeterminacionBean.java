/**
 * OperacionDeterminacionBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.beans.tramite;

import com.gestorfip.app.planeamiento.beans.panels.tramite.DeterminacionPanelBean;

public class OperacionDeterminacionBean {
	
	  private int id; 
	  private int tipo;
	  private long orden;
	  private String  texto;
	  private int operadora_determinacionid;
	  private DeterminacionPanelBean operada_determinacion;
	  private int tramite;
	  
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
	
	public int getOperadora_determinacionid() {
		return operadora_determinacionid;
	}
	
	public void setOperadora_determinacionid(int operadoraDeterminacionid) {
		operadora_determinacionid = operadoraDeterminacionid;
	}
	
	public DeterminacionPanelBean getOperada_determinacion() {
		return operada_determinacion;
	}
	
	public void setOperada_determinacion(DeterminacionPanelBean operadaDeterminacion) {
		operada_determinacion = operadaDeterminacion;
	}
	
	public int getTramite() {
		return tramite;
	}
	
	public void setTramite(int tramite) {
		this.tramite = tramite;
	}

}
