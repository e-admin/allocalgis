/**
 * ReglaPintadoPunto.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.datos;

/**
 * Representa una regla de pintado para un punto
 * 
 * @author David Vicente
 *
 */
public class ReglaPintadoPunto extends ReglaPintado {

	
	public enum TIPO_SIMBOLO { simbolo_predefinido, grafico_externo };
	
	
	public ReglaPintadoPunto ( String nombre)
	{
		this ( nombre, "" );
	}
	
	
	public ReglaPintadoPunto ( String nombre, String filtro )
	{
		super ( nombre, filtro );
	}
	
	
}
