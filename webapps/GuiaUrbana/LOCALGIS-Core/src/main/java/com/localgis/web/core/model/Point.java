/**
 * Point.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

public class Point {

    private Double x;

    private Double y;

    public Point() {
    }

    public Point(Double x, Double y) {
        super();
        this.x = x;
        this.y = y;
    }

    /**
     * Devuelve el campo x
     * 
     * @return El campo x
     */
    public Double getX() {
        return x;
    }

    /**
     * Establece el valor del campo x
     * 
     * @param x
     *            El campo x a establecer
     */
    public void setX(Double x) {
        this.x = x;
    }

    /**
     * Devuelve el campo y
     * 
     * @return El campo y
     */
    public Double getY() {
        return y;
    }

    /**
     * Establece el valor del campo y
     * 
     * @param y
     *            El campo y a establecer
     */
    public void setY(Double y) {
        this.y = y;
    }

}
