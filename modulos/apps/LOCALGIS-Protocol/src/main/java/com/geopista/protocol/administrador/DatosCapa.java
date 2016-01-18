/**
 * DatosCapa.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.administrador;

public class DatosCapa implements java.io.Serializable, Cloneable {

	private static final long serialVersionUID = 506106939850148891L;
	private Integer idTabla;
	private String nombreTabla;
	private Integer idCapa;
	private String nombreCapa;
	private String traduccionCapa;
	private int versionable;
	
	
	public DatosCapa() {}
	
	
	public DatosCapa(String nombreCapa) {
		super();
		this.nombreCapa = nombreCapa;
	}



	public Integer getIdTabla() {
		return idTabla;
	}
	public void setIdTabla(Integer idTabla) {
		this.idTabla = idTabla;
	}
	public String getNombreTabla() {
		return nombreTabla;
	}
	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}
	public Integer getIdCapa() {
		return idCapa;
	}
	public void setIdCapa(Integer idCapa) {
		this.idCapa = idCapa;
	}
	public String getNombreCapa() {
		return nombreCapa;
	}
	public void setNombreCapa(String nombreCapa) {
		this.nombreCapa = nombreCapa;
	}
	public String getTraduccionCapa() {
		return traduccionCapa;
	}
	public void setTraduccionCapa(String traduccionCapa) {
		this.traduccionCapa = traduccionCapa;
	}
	public int getVersionable() {
		return versionable;
	}
	public void setVersionable(int versionable) {
		this.versionable = versionable;
	}
	
	public Object clone() {
		DatosCapa obj = null;
		try {
			obj = (DatosCapa) super.clone();
		} 
		catch (CloneNotSupportedException ex) {
			System.out.println("Error al clonar el objeto DatosCapa: "+ex.toString());
		}
		return obj;
	}
	
}
