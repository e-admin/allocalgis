/**
 * AdscripcionBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.beans.tramite;

public class AdscripcionBean {
	
	private int id;
	private int entidadOrigen;
	private int entidadDestino;
	private double cuantia;
	private String texto;
	private int unidad;
	private int tipo;
	private int tramite;
	
	boolean modificable;
	
	public boolean isModificable() {
		return modificable;
	}
	public void setModificable(boolean modificable) {
		this.modificable = modificable;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getEntidadOrigen() {
		return entidadOrigen;
	}
	public void setEntidadOrigen(int entidadOrigen) {
		this.entidadOrigen = entidadOrigen;
	}
	public int getEntidadDestino() {
		return entidadDestino;
	}
	public void setEntidadDestino(int entidadDestino) {
		this.entidadDestino = entidadDestino;
	}
	public double getCuantia() {
		return cuantia;
	}
	public void setCuantia(double cuantia) {
		this.cuantia = cuantia;
	}
	public String getTexto() {
		return texto;
	}
	public void setTexto(String texto) {
		this.texto = texto;
	}
	public int getUnidad() {
		return unidad;
	}
	public void setUnidad(int unidad) {
		this.unidad = unidad;
	}
	public int getTipo() {
		return tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public int getTramite() {
		return tramite;
	}
	public void setTramite(int tramite) {
		this.tramite = tramite;
	}

}
