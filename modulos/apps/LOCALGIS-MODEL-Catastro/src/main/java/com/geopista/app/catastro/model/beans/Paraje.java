/**
 * Paraje.java
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
public class Paraje extends EstructuraDB
{
    /**
     * código del paraje
     */
    private String codigo;
    
    /**
     * nombre del paraje
     */
    private String nombre;
    
       
    /**
     * Año de las normas origen de la tipología
     */
    
    public Paraje()
    {        
    }

   
    /**
     * @return Returns the codigo.
     */
    public String getCodigo()
    {
        return codigo;
    }

    /**
     * @param codigo The codigo to set.
     */
    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }

    /**
     * @return Returns the codigo.
     */
    public String getNombre()
    {
        return nombre;
    }

    /**
     * @param codigo The codigo to set.
     */
    public void setNombre(String nombre)
    {
        this.nombre = nombre;
    }

    
}
