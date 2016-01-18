/**
 * EstructuraDB.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

public class EstructuraDB
{
    /**
     * Patrón alfanumérico único
     */
    private String patron=new String();
    /**
     * Descripción del patrón
     */
    private String descripcion = new String();
    
    public EstructuraDB()
    {
        
    }

    /**
     * @return Returns the descripcion.
     */
    public String getDescripcion()
    {
        return descripcion;
    }

    /**
     * @param descripcion The descripcion to set.
     */
    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }

    /**
     * @return Returns the patron.
     */
    public String getPatron()
    {
        return patron;
    }

    /**
     * @param patron The patron to set.
     */
    public void setPatron(String patron)
    {
        this.patron = patron;
    }
    
    public boolean equals (Object o)
    {
        if (!(o instanceof EstructuraDB))
            return false;
        
        if (((EstructuraDB)o).getPatron().equalsIgnoreCase(this.patron))
        {
            setDescripcion(((EstructuraDB)o).getDescripcion());
            return true;
        }            
        else 
            return false;
    }    
}
