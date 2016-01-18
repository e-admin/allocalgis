/**
 * FechaInventario.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.inventario.component;
import java.text.DateFormat;

public class FechaInventario {
	java.util.Date fecha;
	java.text.DateFormat df;
	public FechaInventario(java.util.Date fecha){
		this.fecha=fecha;
	}
	public FechaInventario(java.sql.Timestamp fecha){
		this.fecha=fecha;
	}
	public FechaInventario(java.util.Date fecha, DateFormat df){
		this.fecha=fecha;
		this.df=df;
	}
	public FechaInventario(java.sql.Timestamp fecha, DateFormat df){
		this.fecha=fecha;
		this.df=df;
	}
	
	public java.util.Date getFecha(){
		return fecha;
	}
	
	public String toString(){
		if (fecha==null) return "";
		if (df==null)
			return com.geopista.app.inventario.Constantes.df.format(fecha);
		else
			return df.format(fecha);
	
	}

}
