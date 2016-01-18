/**
 * AdscripcionesPanelBean.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.gestorfip.app.planeamiento.beans.panels.tramite;

public class AdscripcionesPanelBean {
	
	private int id;
	private int entidadOrigen = -1;
	private int entidadDestino = -1;
	private double cuantia;
	private String texto;
	private int unidad = -1;
	private int tramiteUnidad;
	private int tipo = -1;
	private int tramiteTipo;
	private int tramite;
	
	private String nombreEntidadOrigen;
	private String nombreEntidadDestino;
	
	// indica a que plan pertenece, CS- catalogo sistematizado, PV - planeamiento Vigente, PE - Planeamiento Encargado
	private String nombreTramite;
	
	private boolean modificada;
	private boolean nueva;
	private boolean eliminada;
	
	public boolean isModificada() {
		return modificada;
	}
	
	public void setModificada(boolean modificada) {
		this.modificada = modificada;
	}
	
	public boolean isNueva() {
		return nueva;
	}
	
	public void setNueva(boolean nueva) {
		this.nueva = nueva;
	}
	
	public boolean isEliminada() {
		return eliminada;
	}
	
	public void setEliminada(boolean eliminada) {
		this.eliminada = eliminada;
	}
	
	public String getNombreTramite() {
		return nombreTramite;
	}
	
	public void setNombreTramite(String nombreTramite) {
		this.nombreTramite = nombreTramite;
	}
	
	public String getNombreEntidadOrigen() {
		return nombreEntidadOrigen;
	}
	
	public void setNombreEntidadOrigen(String nombreEntidadOrigen) {
		this.nombreEntidadOrigen = nombreEntidadOrigen;
	}
	
	public String getNombreEntidadDestino() {
		return nombreEntidadDestino;
	}
	
	public void setNombreEntidadDestino(String nombreEntidadDestino) {
		this.nombreEntidadDestino = nombreEntidadDestino;
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
	
	public int getTramiteUnidad() {
		return tramiteUnidad;
	}
	
	public void setTramiteUnidad(int tramiteUnidad) {
		this.tramiteUnidad = tramiteUnidad;
	}
	
	public int getTramiteTipo() {
		return tramiteTipo;
	}
	
	public void setTramiteTipo(int tramiteTipo) {
		this.tramiteTipo = tramiteTipo;
	}

}
