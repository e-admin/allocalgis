/**
 * UsoCatastro.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;


import java.io.Serializable;

public class UsoCatastro implements Serializable
{
	
    private String codigoUso;
    private long superficieDestinada;
       
    
    /**
     * Constructor por defecto
     *
     */
    public UsoCatastro(String codigoUso, long superficieDestinada)
    {       
        this.superficieDestinada = superficieDestinada;       
        this.codigoUso = codigoUso;
    }

    public UsoCatastro()
    {
    }

    /**
     * @return Returns the codigoUso.
     */
    public String getCodigoUso()
    {
        return codigoUso;
    }

    /**
     * @param codigoUso The codigoUso to set.
     */
    public void setCodigoUso(String codigoUso)
    {
        this.codigoUso = codigoUso;
    }

    /**
     * @return Returns the superficieDestinada.
     */
    public long getSuperficieDestinada()
    {
        return superficieDestinada;
    }

    /**
     * @param superficieDestinada The superficieDestinada to set.
     */
    public void setSuperficieDestinada(long superficieDestinada)
    {
        this.superficieDestinada = superficieDestinada;
    }
}
