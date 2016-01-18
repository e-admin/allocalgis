/**
 * ReglaTematicaPintado.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.datos;

import java.util.ArrayList;
import java.util.List;


/**
 * Clase que representa una regla tematica, de la que herederán el resto
 * de reglas tematicas
 * 
 * @author David Vicente
 *
 */
public class ReglaTematicaPintado {

	protected boolean porRangos = false;
	protected String propiedad = "";
		
	protected List<ValorTematico> valores = new ArrayList<ValorTematico>();
	
	
	
	public ReglaTematicaPintado()
	{
		
	}

	
	

	public boolean isPorRangos() {
		return porRangos;
	}


	public void setPorRangos(boolean porRangos) {
		this.porRangos = porRangos;
	}


	public String getPropiedad() {
		return propiedad;
	}


	public void setPropiedad(String propiedad) {
		this.propiedad = propiedad;
	}


	public List<ValorTematico> getValores() {
		return valores;
	}


	public void setValores(List<ValorTematico> valores) {
		this.valores = valores;
	}
	
	
	
	
}
