/**
 * DatosFisicosUC.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.datos.fisicos;

import java.io.Serializable;

public class DatosFisicosUC implements Serializable
{   
    /**
     * Año de construcción
     */
    private Integer anioConstruccion;
    
    /**
     * Exactitud en el año de construcción
     */
    private String indExactitud;
    
    
    
    /**
     * Longitud de la fachada.
     * 
     */
    private Float longFachada;
    
    /**
     * Superficie del suelo ocupada por la unidad constructiva
     */
    private Long supOcupada;
    
    
    /**
     * Constructor por defecto 
     *
     */
    public DatosFisicosUC()
    {
        
    }
    
    
    /**
     * @return Returns the anioConstruccion.
     */
    public Integer getAnioConstruccion()
    {
        return anioConstruccion;
    }
    
    
    /**
     * @param anioConstruccion The anioConstruccion to set.
     */
    public void setAnioConstruccion(Integer anioConstruccion)
    {
        this.anioConstruccion = anioConstruccion;
    }
    
    
    /**
     * @return Returns the indExactitud.
     */
    public String getIndExactitud()
    {
        return indExactitud;
    }
    
    
    /**
     * @param indExactitud The indExactitud to set.
     */
    public void setIndExactitud(String indExactitud)
    {
        this.indExactitud = indExactitud;
    }
    
    
    /**
     * @return Returns the longFachada.
     */
    public Float getLongFachada()
    {
        return longFachada;
    }
    
    
    /**
     * @param longFachada The longFachada to set.
     */
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
     * Serializa el objeto 
     * 
     * @return Cadena con el XML
     */
    public String toXML ()
    {
        return null;
    }
    
    
}
