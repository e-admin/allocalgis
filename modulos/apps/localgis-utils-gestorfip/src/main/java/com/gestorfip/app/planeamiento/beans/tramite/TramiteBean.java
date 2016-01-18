/**
 * TramiteBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.beans.tramite;

import com.gestorfip.app.planeamiento.beans.diccionario.TipoTramiteBean;



public class TramiteBean {

	
	private TipoTramiteBean tipoTramite;
	private String codigo;
	private String texto;
	private int id;
	//se utiliza para recoger el nombre del tramtite encargado
	private String nombre;

	public TipoTramiteBean getTipoTramite() {
		return tipoTramite;
	}
	
	public void setTipoTramite(TipoTramiteBean tipoTramite) {
		this.tipoTramite = tipoTramite;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getTexto() {
		return texto;
	}
	
	public void setTexto(String texto) {
		this.texto = texto;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
