/**
 * The GEOPISTA project is a set of tools and applications to manage
 * geographical data for local administrations.
 *
 * Copyright (C) 2004 INZAMAC-SATEC UTE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * For more information, contact:
 *
 * 
 * www.geopista.com
 *
 */


package com.geopista.app.layerutil.datosexternos;


/**
 * Bean con los datos de un atributo de una capa de geopista
 * 
 * @author cotesa
 *
 */


public class TablaCamposExternosRow{
	
	String origen;
	String nombre;
	String tipo;
	
    /**
     * Constructor por defecto
     *
     */
	public TablaCamposExternosRow()
	{
		super();
	}
	
    /**
     * Constructor de la clase

     */
	public TablaCamposExternosRow(String origen, String nombre, String tipo){
		this.origen = origen;
		this.nombre = nombre;
		this.tipo = tipo;
	}
	
/*
    public int hashCode() 
    {
        if (this.columna!=null)
            return this.columna.getIdColumn();
        else 
            return 0;
    }
*/
    public boolean equals(Object o) {      
        if (!(o instanceof TablaCamposExternosRow)) return false;
        
        if ( ((TablaCamposExternosRow)o).getNombre().equals(this.getNombre())
                || ((TablaCamposExternosRow)o).getNombre()==null)
            return true;
        else
            return false;
        
        
    }

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}
