/**
 * V_deposito_enc_m50_bean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database.validacion.beans;


public class V_deposito_enc_m50_bean {

	   String clave="-";
	   String provincia="-";
	   String municipio="-";
	   String orden_depo="-";
	   String ubicacion="-";
	   String titular="-";
	   String gestion="-";
	   int capacidad;
	   String estado="-";
	   String proteccion="-";
	   int limpieza;
	   String contador;
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
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
	public String getOrden_depo() {
		return orden_depo;
	}
	public void setOrden_depo(String orden_depo) {
		this.orden_depo = orden_depo;
	}
	public String getUbicacion() {
		return ubicacion;
	}
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
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
	public int getCapacidad() {
		return capacidad;
	}
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getProteccion() {
		return proteccion;
	}
	public void setProteccion(String proteccion) {
		this.proteccion = proteccion;
	}
	public int getLimpieza() {
		return limpieza;
	}
	public void setLimpieza(int limpieza) {
		this.limpieza = limpieza;
	}
	public String getContador() {
		return contador;
	}
	public void setContador(String contador) {
		this.contador = contador;
	}

}
