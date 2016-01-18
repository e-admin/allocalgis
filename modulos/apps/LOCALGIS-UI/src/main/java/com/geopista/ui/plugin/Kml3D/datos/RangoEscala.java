/**
 * RangoEscala.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.Kml3D.datos;

import com.geopista.ui.plugin.Kml3D.Kml3DPlugIn;
import com.vividsolutions.jump.I18N;

/**
 * Clase que representa un rango de escala
 * 
 * @author David Vicente
 *
 */
public class RangoEscala {

	
	private String minEscala = "";
	private String maxEscala = "";
	
	
	
	public RangoEscala ( String minEscala, String maxEscala ) throws NumberFormatException
	{
		valida ( minEscala, maxEscala );
		
		this.minEscala = minEscala;
		this.maxEscala = maxEscala;
	}
	
	
	
	public static void valida ( String min, String max ) throws NumberFormatException
	{
		Double.parseDouble ( min );
		Double.parseDouble ( max );
	}
	
	


	public String getMinEscala() {
		return minEscala;
	}



	public void setMinEscala(String minEscala) {
		this.minEscala = minEscala;
	}



	public String getMaxEscala() {
		return maxEscala;
	}



	public void setMaxEscala(String maxEscala) {
		this.maxEscala = maxEscala;
	}



	public String toString () 	
	{
		String desde = I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoRangosEscala.desde");
		String hasta = I18N.get(Kml3DPlugIn.name,"Kml3D.dialogoRangosEscala.hasta");
		String texto = desde + " " + this.minEscala + " " + hasta + " " + this.maxEscala;
		
		return texto;
	}
	
}
