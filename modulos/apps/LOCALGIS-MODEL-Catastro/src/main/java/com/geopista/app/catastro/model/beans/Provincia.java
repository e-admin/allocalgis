/**
 * Provincia.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.io.Serializable;

public class Provincia implements Serializable {
    /**
     * Identificador de la provincia (dos posiciones)
     */
    private String idProvincia = new String();
    
    /**
     * Nombre oficial de la provincia
     */
    private String nombreOficial=new String();
    
    public Provincia (){
        
    }

    /**
     * @return Returns the idProvincia.
     */
    public String getIdProvincia()
    {
        return idProvincia;
    }

    /**
     * @param idProvincia The idProvincia to set.
     */
    public void setIdProvincia(String idProvincia)
    {
        this.idProvincia = idProvincia;
    }

    /**
     * @return Returns the nombreOficial.
     */
    public String getNombreOficial()
    {
        return nombreOficial;
    }

    /**
     * @param nombreOficial The nombreOficial to set.
     */
    public void setNombreOficial(String nombreOficial)
    {
        this.nombreOficial = nombreOficial;
    }
    
    public boolean equals (Object o)
    {
        if (!(o instanceof Provincia))
            return false;
        
        if (((Provincia)o).getIdProvincia()!=null && 
                ((Provincia)o).getIdProvincia().equalsIgnoreCase(this.idProvincia))
        {
//            setNombreOficial(((Provincia)o).getNombreOficial());
            return true;
        }            
        else 
            return false;
    }    
}
