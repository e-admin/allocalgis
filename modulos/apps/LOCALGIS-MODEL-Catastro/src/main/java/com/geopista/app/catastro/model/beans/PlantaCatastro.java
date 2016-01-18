/**
 * PlantaCatastro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;


import java.io.Serializable;
import java.util.ArrayList;

public class PlantaCatastro implements Serializable
{
	
    private String nombre;
    private int numPlantasReales;
    private ArrayList lstUsos = new ArrayList();
       
    
    /**
     * Constructor por defecto
     *
     */
    public PlantaCatastro(String nombre, int numPlantasReales)
    {       
        this.numPlantasReales = numPlantasReales;       
        this.nombre = nombre;
    }

    public PlantaCatastro()
    {
    }

    /**
     * @return Returns the codigoUso.
     */
    public String getNombre()
    {
        return nombre;
    }

    /**
     * @param codigoUso The codigoUso to set.
     */
    public void setNombre(String codigoUso)
    {
        this.nombre = codigoUso;
    }

    /**
     * @return Returns the superficieDestinada.
     */
    public int getNumPlantasReales()
    {
        return numPlantasReales;
    }

    /**
     * @param superficieDestinada The superficieDestinada to set.
     */
    public void setNumPlantasReales(int numplantas)
    {
        this.numPlantasReales = numplantas;
    }

    /**
     * @return Returns the lstUsos.
     */
    public ArrayList getLstUsos()
    {
        return lstUsos;
    }

    /**
     * @param lstUsos The lstUsos to set.
     */
    public void setLstUsos(ArrayList lstUsos)
    {
        this.lstUsos = lstUsos;
    }
    
}

