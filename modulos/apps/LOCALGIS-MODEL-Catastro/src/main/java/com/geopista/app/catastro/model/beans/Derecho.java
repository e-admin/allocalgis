/**
 * Derecho.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro.model.beans;

import java.io.Serializable;


/**
 * Datos extraídos del fichero FINURB-DGC
 * @version 1.0
 * @created 27-ene-2007 16:42:52
 */
public class Derecho implements Serializable {
    
    /**
     * Identificador del bien inmueble 
     */
    private IdBienInmueble idBienInmueble;
    
    /**
     * Código del derecho
     */
    private String codDerecho;
    /**
     * Porcentaje de derecho
     */
    private float porcentajeDerecho;
    /**
     * Ordinal del derecho (número secuencial del derecho del titular sobre el bien)
     */
    private int ordinalDerecho;
    
    
    /**
     * Constructor por defecto
     *
     */
    public Derecho(){
        
    }	
    
    /**
     * @return Returns the codDerecho.
     */
    public String getCodDerecho() {
        return codDerecho;
    }
    
    /**
     * @param codDerecho The codDerecho to set.
     */
    public void setCodDerecho(String codDerecho) {
        this.codDerecho = codDerecho;
    }
    
    /**
     * @return Returns the idBienInmueble.
     */
    public IdBienInmueble getIdBienInmueble() {
        return idBienInmueble;
    }
    
    /**
     * @param idBienInmueble The idBienInmueble to set.
     */
    public void setIdBienInmueble(IdBienInmueble idBienInmueble) {
        this.idBienInmueble = idBienInmueble;
    }
    
    /**
     * @return Returns the ordinalDerecho.
     */
    public int getOrdinalDerecho() {
        return ordinalDerecho;
    }
    
    /**
     * @param ordinalDerecho The ordinalDerecho to set.
     */
    public void setOrdinalDerecho(int ordinalDerecho) {
        this.ordinalDerecho = ordinalDerecho;
    }
    
    /**
     * @return Returns the porcentajeDerecho.
     */
    public float getPorcentajeDerecho() {
        return porcentajeDerecho;
    }
    
    /**
     * @param porcentajeDerecho The porcentajeDerecho to set.
     */
    public void setPorcentajeDerecho(float porcentajeDerecho) {
        this.porcentajeDerecho = porcentajeDerecho;
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
