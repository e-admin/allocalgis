/**
 * TipologiaLocal.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

/**
 * Tipo de tipología de local para mostrar en los desplegables
 * 
 * @author cotesa
 *
 */
public class TipologiaLocal extends EstructuraDB
{
    /**
     * Identificador de la tipología del local
     */
    private int id;
    
    /**
     * Año de las normas origen de la tipología
     */
    private int anioNormas;
    
    public TipologiaLocal()
    {        
    }

   
    /**
     * @return Returns the id.
     */
    public int getId()
    {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return Returns the anioNormas.
     */
    public int getAnioNormas()
    {
        return anioNormas;
    }

    /**
     * @param anioNormas The anioNormas to set.
     */
    public void setAnioNormas(int anioNormas)
    {
        this.anioNormas = anioNormas;
    }    
}
