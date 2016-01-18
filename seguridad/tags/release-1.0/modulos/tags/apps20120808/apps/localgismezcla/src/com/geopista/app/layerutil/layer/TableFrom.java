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


package com.geopista.app.layerutil.layer;


/**
 * Bean que recoge los nombres de las tablas y columnas que se utilizan para la
 * generación de sugencias para consultas SQL de las capas de geopista 
 * 
 * @author cotesa
 *
 */

import java.util.Vector;

public class TableFrom
{
    
    private String nombre1 = null;
    private String nombre2 = null;
    
    Vector condicionesOn = new Vector();
    
    /**
     * Constructor por defecto
     *
     */
    public TableFrom()
    {
        super();
        // TODO Auto-generated constructor stub
    }
    
    /**
     * Constructor de la clase
     * @param nombre1 
     * @param nombre2
     */
    public TableFrom (String nombre1, String nombre2)
    {
        this.nombre1 = nombre1;
        this.nombre2 = nombre2;
        
    }
    
    
    /**
     * @return Returns the condicionesOn.
     */
    public Vector getCondicionesOn()
    {
        return condicionesOn;
    }
    
    
    /**
     * @param condicionesOn The condicionesOn to set.
     */
    public void setCondicionesOn(Vector condicionesOn)
    {
        this.condicionesOn = condicionesOn;
    }
    
    
    /**
     * @return Returns the nombre1.
     */
    public String getNombre1()
    {
        return nombre1;
    }
    
    
    /**
     * @param nombre1 The nombre1 to set.
     */
    public void setNombre1(String nombre1)
    {
        this.nombre1 = nombre1;
    }
    
    
    /**
     * @return Returns the nombre2.
     */
    public String getNombre2()
    {
        return nombre2;
    }
    
    
    /**
     * @param nombre2 The nombre2 to set.
     */
    public void setNombre2(String nombre2)
    {
        this.nombre2 = nombre2;
    }
    
    
    public void addCondicion (String condicion)
    {
        this.condicionesOn.add(condicion);
    }
    
}
