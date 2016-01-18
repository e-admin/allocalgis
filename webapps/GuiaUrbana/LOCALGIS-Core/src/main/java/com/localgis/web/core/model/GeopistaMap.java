/**
 * GeopistaMap.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

/**
 * Clase que representa un mapa de geopista
 * 
 * @author albegarcia
 * 
 */
public class GeopistaMap {

    /**
     * Identificador del mapa
     */
    private Integer idMap;

    /**
     * Identificador de la entidad del mapa
     */
    private Integer idEntidad;

    /**
     * XML asociado al mapa
     */
    private String xml;

    /**
     * Nombre del mapa
     */
    private String name;

    /**
     * Devuelve el campo idMap
     * 
     * @return El campo idMap
     */
    public Integer getIdMap() {
        return idMap;
    }

    /**
     * Establece el valor del campo idMap
     * 
     * @param idMap
     *            El campo idMap a establecer
     */
    public void setIdMap(Integer idMap) {
        this.idMap = idMap;
    }

    /**
     * Devuelve el campo idEntidad
     * 
     * @return El campo idEntidad
     */
    public Integer getIdEntidad() {
        return idEntidad;
    }

    /**
     * Establece el valor del campo idEntidad
     * 
     * @param idEntidad
     *            El campo idEntidad a establecer
     */
    public void setIdEntidad(Integer idEntidad) {
        this.idEntidad = idEntidad;
    }

    /**
     * Devuelve el campo xml
     * 
     * @return El campo xml
     */
    public String getXml() {
        return xml;
    }

    /**
     * Establece el valor del campo xml
     * 
     * @param xml
     *            El campo xml a establecer
     */
    public void setXml(String xml) {
        this.xml = xml;
    }

    /**
     * Devuelve el campo name
     * 
     * @return El campo name
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el valor del campo name
     * 
     * @param name
     *            El campo name a establecer
     */
    public void setName(String name) {
        this.name = name;
    }

}
