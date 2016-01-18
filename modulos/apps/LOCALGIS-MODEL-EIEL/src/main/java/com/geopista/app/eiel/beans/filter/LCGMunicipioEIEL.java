/**
 * LCGMunicipioEIEL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.eiel.beans.filter;

import java.io.Serializable;

public class LCGMunicipioEIEL implements Serializable{

	private int idMunicipio;
	private String nombreOficial;
	
	
	public LCGMunicipioEIEL(int idMunicipio, String nombreOficial) {
		super();
		this.idMunicipio = idMunicipio;
		this.nombreOficial = nombreOficial;
	}
	public LCGMunicipioEIEL() {
		// TODO Auto-generated constructor stub
	}
	public int getIdMunicipio() {
		return idMunicipio;
	}
	public void setIdMunicipio(int idMunicipio) {
		this.idMunicipio = idMunicipio;
	}
	public String getNombreOficial() {
		return nombreOficial;
	}
	public void setNombreOficial(String nombreOficial) {
		this.nombreOficial = nombreOficial;
	}

	@Override
	public String toString() {
		return "LCGMunicipioEIEL [idMunicipio=" + idMunicipio
				+ ", nombreOficial=" + nombreOficial + "]";
	}

}
