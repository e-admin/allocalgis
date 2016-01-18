/**
 * ShowMapActionForm.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.actionsforms;

import org.apache.struts.action.ActionForm;

public class ShowMapActionForm extends ActionForm {

    private Integer idMap;

    private String language;

    private Integer idEntidad;
    
    private Double x;
    
    private Double y;
    
    private Double scale;
    
    private String marker;
    
    /**
     * Devuelve el campo idMap
     * @return El campo idMap
     */
    public Integer getIdMap() {
        return idMap;
    }

    /**
     * Establece el valor del campo idMap
     * @param idMap El campo idMap a establecer
     */
    public void setIdMap(Integer idMap) {
        this.idMap = idMap;
    }

    /**
     * Devuelve el campo language
     * @return El campo language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Establece el valor del campo language
     * @param language El campo language a establecer
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Devuelve el campo idEntidad
     * @return El campo idEntidad
     */
    public Integer getIdEntidad() {
        return idEntidad;
    }

    /**
     * Establece el valor del campo idEntidad
     * @param idEntidad El campo idEntidad a establecer
     */
    public void setIdEntidad(Integer idEntidad) {
        this.idEntidad = idEntidad;
    }

    /**
     * Devuelve el campo x
     * @return El campo x
     */
    public Double getX() {
        return x;
    }

    /**
     * Establece el valor del campo x
     * @param x El campo x a establecer
     */
    public void setX(Double x) {
        this.x = x;
    }

    /**
     * Devuelve el campo y
     * @return El campo y
     */
    public Double getY() {
        return y;
    }

    /**
     * Establece el valor del campo y
     * @param y El campo y a establecer
     */
    public void setY(Double y) {
        this.y = y;
    }

    /**
     * Devuelve el campo scale
     * @return El campo scale
     */
    public Double getScale() {
        return scale;
    }

    /**
     * Establece el valor del campo scale
     * @param scale El campo scale a establecer
     */
    public void setScale(Double scale) {
        this.scale = scale;
    }
    
    /**
     * Devuelve el campo marker
     * @return El campo marker
     */
    public String getMarker() {
    	return marker;
    }

    /**
     * Establece el valor del campo marker
     * @param scale El campo marker a establecer
     */
    public void setMarker(String marker) {
    	this.marker = marker;
    }

}
