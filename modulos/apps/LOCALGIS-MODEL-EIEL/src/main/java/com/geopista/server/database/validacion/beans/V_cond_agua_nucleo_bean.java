/**
 * V_cond_agua_nucleo_bean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database.validacion.beans;


public class V_cond_agua_nucleo_bean {
	
	   String provincia="-";
	   String municipio="-";
	   String entidad="-";
	   String nucleo="-";
	   String clave="-";
	   String cond_provi="-";
	   String cond_munic="-";
	   String orden_cond="-";
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
	public String getNucleo() {
		return nucleo;
	}
	public void setNucleo(String nucleo) {
		this.nucleo = nucleo;
	}
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public String getCond_provi() {
		return cond_provi;
	}
	public void setCond_provi(String cond_provi) {
		this.cond_provi = cond_provi;
	}
	public String getCond_munic() {
		return cond_munic;
	}
	public void setCond_munic(String cond_munic) {
		this.cond_munic = cond_munic;
	}
	public String getOrden_cond() {
		return orden_cond;
	}
	public void setOrden_cond(String orden_cond) {
		this.orden_cond = orden_cond;
	}
	

}
