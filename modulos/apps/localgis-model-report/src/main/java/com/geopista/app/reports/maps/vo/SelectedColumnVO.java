/**
 * SelectedColumnVO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.reports.maps.vo;

public class SelectedColumnVO {
	
	private String atributo;
	private int idColumna;
	private String nombre;
	private int idTabla;
	private String nombreTabla;
	
	public int getIdColumna() {
		return idColumna;
	}
	
	public void setIdColumna(int idColumna) {
		this.idColumna = idColumna;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public int getIdTabla() {
		return idTabla;
	}
	
	public void setIdTabla(int idTabla) {
		this.idTabla = idTabla;
	}

	public String getNombreTabla() {
		return nombreTabla;
	}
	
	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	public String toString() {
        return nombre;
    }

	public String getAtributo() {
		return atributo;
	}

	public void setAtributo(String atributo) {
		this.atributo = atributo;
	}
}
