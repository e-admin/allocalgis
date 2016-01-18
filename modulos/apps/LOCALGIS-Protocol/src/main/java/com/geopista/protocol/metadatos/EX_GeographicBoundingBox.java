/**
 * EX_GeographicBoundingBox.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.protocol.metadatos;


/**
 * Created by IntelliJ IDEA.
 * User: angeles
 * Date: 24-ago-2004
 * Time: 15:52:11
 */
public class EX_GeographicBoundingBox {
    String id;
    boolean extenttypecode;
    float west;
    float east;
    float south;
    float north;

    public EX_GeographicBoundingBox() {
    }

    public float getEast() {
        return east;
    }

    public void setEast(float east) {
        this.east = east;
    }

    public boolean isExtenttypecode() {
        return extenttypecode;
    }

    public void setExtenttypecode(boolean extenttypecode) {
        this.extenttypecode = extenttypecode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getNorth() {
        return north;
    }

    public void setNorth(float north) {
        this.north = north;
    }

    public float getSouth() {
        return south;
    }

    public void setSouth(float south) {
        this.south = south;
    }

    public float getWest() {
        return west;
    }

    public void setWest(float west) {
        this.west = west;
    }
}
