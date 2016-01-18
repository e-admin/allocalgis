/**
 * V_infraestr_viaria_bean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database.validacion.beans;


public class V_infraestr_viaria_bean {
	
	  String provincia="-";
	  String municipio="-";
	  String entidad="-";
	  String nucleo="-";
	  String tipo_infr="-";
	  String estado="-";
	  int longitud;
	  int superficie;
	  int viv_afecta;
	
	public String getNucleo() {
		return nucleo;
	}
	public void setNucleo(String nucleo) {
		this.nucleo = nucleo;
	}
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public String getMunicipio() {
		return municipio;
	}
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}
	public String getEntidad() {
		return entidad;
	}
	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}
	public String getTipo_infr() {
		return tipo_infr;
	}
	public void setTipo_infr(String tipoInfr) {
		tipo_infr = tipoInfr;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getLongitud() {
		return longitud;
	}
	public void setLongitud(int longitud) {
		this.longitud = longitud;
	}
	public int getSuperficie() {
		return superficie;
	}
	public void setSuperficie(int superficie) {
		this.superficie = superficie;
	}
	public int getViv_afecta() {
		return viv_afecta;
	}
	public void setViv_afecta(int vivAfecta) {
		viv_afecta = vivAfecta;
	}
	

}
