/**
 * ReglaPintadoGrafico.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.datos;

import java.awt.Color;

/**
 * Representa una regla de pintado para un grafico
 * 
 * @author David Vicente
 *
 */
public class ReglaPintadoGrafico extends ReglaPintado {

	
	public enum TIPO_SIMBOLO { simbolo_predefinido, grafico_externo };
	
	
	private Color color = Color.black;
	
	
	public ReglaPintadoGrafico ()
	{
		this ( "" );
	}
	
	
	
	public ReglaPintadoGrafico ( String nombre)
	{
		this ( nombre, "" );
	}
	
	
	public ReglaPintadoGrafico ( String nombre, String filtro )
	{
		super ( nombre, filtro );
	}



	
	
	
	public Color getColor() {
		return color;
	}



	public void setColor(Color color) {
		this.color = color;
	}
	
	
	
	
	
}
