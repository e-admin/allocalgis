/**
 * V_nucl_encuestado_1_bean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database.validacion.beans;


public class V_nucl_encuestado_1_bean {
	
	  String provincia="-";
	  String municipio="-";
	  String entidad="-";
	  String nucleo="-";
	  int padron;
	  int pob_estaci;
	  int altitud;
	  int viv_total;
	  int hoteles;
	  int casas_rural;
	  String accesib="-";
	
	
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
	public int getPadron() {
		return padron;
	}
	public void setPadron(int padron) {
		this.padron = padron;
	}
	public int getPob_estaci() {
		return pob_estaci;
	}
	public void setPob_estaci(int pobEstaci) {
		pob_estaci = pobEstaci;
	}
	public int getAltitud() {
		return altitud;
	}
	public void setAltitud(int altitud) {
		this.altitud = altitud;
	}
	public int getViv_total() {
		return viv_total;
	}
	public void setViv_total(int vivTotal) {
		viv_total = vivTotal;
	}
	public int getHoteles() {
		return hoteles;
	}
	public void setHoteles(int hoteles) {
		this.hoteles = hoteles;
	}
	public int getCasas_rural() {
		return casas_rural;
	}
	public void setCasas_rural(int casasRural) {
		casas_rural = casasRural;
	}
	public String getAccesib() {
		return accesib;
	}
	public void setAccesib(String accesib) {
		this.accesib = accesib;
	}
	
	
	
	
}
