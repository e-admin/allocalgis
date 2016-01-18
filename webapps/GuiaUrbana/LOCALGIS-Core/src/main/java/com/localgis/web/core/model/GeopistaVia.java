/**
 * GeopistaVia.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

/**
 * Clase que representa una via de Geopista
 * 
 * @author albegarcia
 * 
 */
public class GeopistaVia {

    /**
     * Identificador del mapa
     */
    private Integer id;

    /**
     * X de centrado
     */
    private double x;

    /**
     * Y de centrado
     */
    private double y;

    /**
     * Devuelve el campo id
     * @return El campo id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el valor del campo id
     * @param id El campo id a establecer
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Devuelve el campo x
     * @return El campo x
     */
    public double getX() {
        return x;
    }

    /**
     * Establece el valor del campo x
     * @param x El campo x a establecer
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Devuelve el campo y
     * @return El campo y
     */
    public double getY() {
        return y;
    }

    /**
     * Establece el valor del campo y
     * @param y El campo y a establecer
     */
    public void setY(double y) {
        this.y = y;
    }

}
