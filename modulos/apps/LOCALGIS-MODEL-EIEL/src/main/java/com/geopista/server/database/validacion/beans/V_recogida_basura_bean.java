/**
 * V_recogida_basura_bean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database.validacion.beans;

public class V_recogida_basura_bean {


	    String provincia="-";
	    String municipio="-";
	    String entidad="-";
	    String nucleo="-";
	    String tipo_rbas="-";
	    String gestion="-";
	    String periodicid="-";
	    String calidad="-";
	    double produ_basu;
	    int contenedor;
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
	public String getTipo_rbas() {
		return tipo_rbas;
	}
	public void setTipo_rbas(String tipo_rbas) {
		this.tipo_rbas = tipo_rbas;
	}
	public String getGestion() {
		return gestion;
	}
	public void setGestion(String gestion) {
		this.gestion = gestion;
	}
	public String getPeriodicid() {
		return periodicid;
	}
	public void setPeriodicid(String periodicid) {
		this.periodicid = periodicid;
	}
	public String getCalidad() {
		return calidad;
	}
	public void setCalidad(String calidad) {
		this.calidad = calidad;
	}
	public double getProdu_basu() {
		return produ_basu;
	}
	public void setProdu_basu(double produ_basu) {
		this.produ_basu = produ_basu;
	}
	public int getContenedor() {
		return contenedor;
	}
	public void setContenedor(int contenedor) {
		this.contenedor = contenedor;
	}
	 
}
