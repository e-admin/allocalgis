/**
 * PoiOT.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.model.ot;

public class PoiOT {
	
	String tipo = null;
	String subtipo = null;
	int idContenido = 0;
	String urlContenido = null;
	double coordX = 0;
	double coordY = 0;
	String direccion = null;
	int idMunicpio = 0;
	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}
	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	/**
	 * @return the subtipo
	 */
	public String getSubtipo() {
		return subtipo;
	}
	/**
	 * @param subtipo the subtipo to set
	 */
	public void setSubtipo(String subtipo) {
		this.subtipo = subtipo;
	}
	/**
	 * @return the idContenido
	 */
	public int getIdContenido() {
		return idContenido;
	}
	/**
	 * @param idContenido the idContenido to set
	 */
	public void setIdContenido(int idContenido) {
		this.idContenido = idContenido;
	}
	/**
	 * @return the urlContenido
	 */
	public String getUrlContenido() {
		return urlContenido;
	}
	/**
	 * @param urlContenido the urlContenido to set
	 */
	public void setUrlContenido(String urlContenido) {
		this.urlContenido = urlContenido;
	}
	/**
	 * @return the coordX
	 */
	public double getCoordX() {
		return coordX;
	}
	/**
	 * @param coordX the coordX to set
	 */
	public void setCoordX(double coordX) {
		this.coordX = coordX;
	}
	/**
	 * @return the coordY
	 */
	public double getCoordY() {
		return coordY;
	}
	/**
	 * @param coordY the coordY to set
	 */
	public void setCoordY(double coordY) {
		this.coordY = coordY;
	}
	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}
	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	/**
	 * @return the idMunicpio
	 */
	public int getIdMunicpio() {
		return idMunicpio;
	}
	/**
	 * @param idMunicpio the idMunicpio to set
	 */
	public void setIdMunicpio(int idMunicpio) {
		this.idMunicpio = idMunicpio;
	}
	
	

}
