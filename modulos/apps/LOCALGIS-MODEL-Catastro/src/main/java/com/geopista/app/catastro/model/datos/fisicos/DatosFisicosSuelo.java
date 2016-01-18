/**
 * DatosFisicosSuelo.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.datos.fisicos;

import java.io.Serializable;

public class DatosFisicosSuelo implements Serializable
{    
    
    
    /**
     * Longitud de la fachada en centímetros.
     */
    //private Integer longFachada;
	private Float longFachada;
    
    /**
     * Fondo del elemento de suelo (en metros)
     */
    
    private Integer fondo;
    
    /**
     * Superficie del elemento Suelo en metros cuadarados
     */
    private Long supOcupada;
    /**
     * Puede tomar los valores FA, FD, DR, IZ o SI.
     */
    private String tipoFachada;
    
    
    /**
     * Constructor por defecto
     *
     */
    public DatosFisicosSuelo()
    {
        
    }    
    
    /**
     * @return Returns the fondo.
     */
    public Integer getFondo()
    {
        return fondo;
    }
    
    
    /**
     * @param fondo The fondo to set.
     */
    public void setFondo(Integer fondo)
    {
        this.fondo = fondo;
    }
    
    
    /**
     * @return Returns the longFachada.
     */
    //public Integer getLongFachada()
    public Float getLongFachada()
    {
        return longFachada;
    }
    
    
    /**
     * @param longFachada The longFachada to set.
     */
    //public void setLongFachada(Integer longFachada)
    public void setLongFachada(Float longFachada)
    {
        this.longFachada = longFachada;
    }
    
    
    /**
     * @return Returns the supOcupada.
     */
    public Long getSupOcupada()
    {
        return supOcupada;
    }
    
    
    /**
     * @param supOcupada The supOcupada to set.
     */
    public void setSupOcupada(Long supOcupada)
    {
        this.supOcupada = supOcupada;
    }
    
    
    /**
     * @return Returns the tipoFachada.
     */
    public String getTipoFachada()
    {
        return tipoFachada;
    }
    
    
    /**
     * @param tipoFachada The tipoFachada to set.
     */
    public void setTipoFachada(String tipoFachada)
    {
        this.tipoFachada = tipoFachada;
    }
    
    /**
     * Serializa el objeto 
     * 
     * @return Cadena con el XML
     */
    public String toXML ()
    {
        return null;
    }
    
    
    
}
