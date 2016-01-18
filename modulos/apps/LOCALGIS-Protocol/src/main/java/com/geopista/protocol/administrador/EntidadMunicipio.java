/**
 * EntidadMunicipio.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.geopista.protocol.administrador;


/**
 * @author seilagamo
 *
 */
public class EntidadMunicipio {
    
    private String id_entidad;
    private String id_municipio;
    private int srid;
    
    public EntidadMunicipio(){        
    }
    
    public EntidadMunicipio (String id_entidad, String id_municipio) {
        this.id_entidad = id_entidad;
        this.id_municipio = id_municipio;
    }

    public EntidadMunicipio (String id_entidad, String id_municipio, int srid) {
        this.id_entidad = id_entidad;
        this.id_municipio = id_municipio;
        this.srid = srid;
    }
    
    /**
     * @return the id_entidad
     */
    public String getId_entidad() {
        return id_entidad;
    }

    
    /**
     * @param id_entidad the id_entidad to set
     */
    public void setId_entidad(String id_entidad) {
        this.id_entidad = id_entidad;
    }

    
    /**
     * @return the id_municipio
     */
    public String getId_municipio() {
        return id_municipio;
    }

    
    /**
     * @param id_municipio the id_municipio to set
     */
    public void setId_municipio(String id_municipio) {
        this.id_municipio = id_municipio;
    }

    
    /**
     * @return the srid
     */
    public int getSrid() {
        return srid;
    }

    
    /**
     * @param srid the srid to set
     */
    public void setSrid(int srid) {
        this.srid = srid;
    }
}
