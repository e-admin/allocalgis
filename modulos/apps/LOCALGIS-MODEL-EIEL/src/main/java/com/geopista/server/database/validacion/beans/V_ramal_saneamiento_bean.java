/**
 * V_ramal_saneamiento_bean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database.validacion.beans;

public class V_ramal_saneamiento_bean {

	    String provincia="-";
	    String municipio="-";
	    String entidad="-";
	    String nucleo="-";
	    String tipo_rama="-";
	    String sist_trans="-";
	    String estado="-";
	    String tipo_red="-";
	    String titular="-";
	    String gestion="-";
	    int longit_ram;
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
	public String getTipo_rama() {
		return tipo_rama;
	}
	public void setTipo_rama(String tipo_rama) {
		this.tipo_rama = tipo_rama;
	}
	public String getSist_trans() {
		return sist_trans;
	}
	public void setSist_trans(String sist_trans) {
		this.sist_trans = sist_trans;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getTipo_red() {
		return tipo_red;
	}
	public void setTipo_red(String tipo_red) {
		this.tipo_red = tipo_red;
	}
	public String getTitular() {
		return titular;
	}
	public void setTitular(String titular) {
		this.titular = titular;
	}
	public String getGestion() {
		return gestion;
	}
	public void setGestion(String gestion) {
		this.gestion = gestion;
	}
	public int getLongit_ram() {
		return longit_ram;
	}
	public void setLongit_ram(int longit_ram) {
		this.longit_ram = longit_ram;
	}
	 
}
