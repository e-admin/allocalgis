/**
 * ReglaPintado.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.datos;

/**
 * Representa una regla de pintado, y establece unos atributos comunes
 * 
 * @author David Vicente
 *
 */
public abstract class ReglaPintado {

	
	protected String nombre = "";
	
	protected String filtro = "";
	
	
	
	public ReglaPintado ( String nombre )
	{
		this ( nombre, "" );
	}
	
	
	
	public ReglaPintado ( String nombre, String filtro )
	{
		this.nombre = nombre;
		this.filtro = filtro;
	}

	


	public String getNombre() {
		return nombre;
	}



	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public String getFiltro() {
		return filtro;
	}



	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}
	
	

}
