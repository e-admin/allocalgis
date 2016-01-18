/**
 * BloqueBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.cementerios;

import java.io.Serializable;


/**
 * Clase que implementa un objeto de tipo Bloque
 */
public class BloqueBean extends ElemCementerioBean implements Serializable{
	
	private int id_bloque = -1;
	private int tipo_unidades;
	private int numFilas ;
	private int numColumnas;
	private String descripcion;
	private int idElemCementerio;
	private int id_feature;
	
	
	
	public int getId_feature() {
		return id_feature;
	}
	public void setId_feature(int id_feature) {
		this.id_feature = id_feature;
	}
	public int getIdElemCementerio() {
		return idElemCementerio;
	}
	public void setIdElemCementerio(int idElemCementerio) {
		this.idElemCementerio = idElemCementerio;
	}
	public int getId_bloque() {
		return id_bloque;
	}
	public void setId_bloque(int id_bloque) {
		this.id_bloque = id_bloque;
	}
	public int getTipo_unidades() {
		return tipo_unidades;
	}
	public void setTipo_unidades(int tipo_unidades) {
		this.tipo_unidades = tipo_unidades;
	}
	public int getNumFilas() {
		return numFilas;
	}
	public void setNumFilas(int numFilas) {
		this.numFilas = numFilas;
	}
	public int getNumColumnas() {
		return numColumnas;
	}
	public void setNumColumnas(int numColumnas) {
		this.numColumnas = numColumnas;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	

	
		
}
