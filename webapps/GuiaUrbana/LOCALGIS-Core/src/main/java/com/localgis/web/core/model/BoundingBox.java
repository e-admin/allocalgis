/**
 * BoundingBox.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

public class BoundingBox {
    
    private Double minx;

    private Double miny;

    private Double maxx;

    private Double maxy;

    public BoundingBox() {
    }

    public BoundingBox(Double minx, Double miny, Double maxx, Double maxy) {
        super();
        this.minx = minx;
        this.miny = miny;
        this.maxx = maxx;
        this.maxy = maxy;
    }

    /**
     * Devuelve el campo minx
     * @return El campo minx
     */
    public Double getMinx() {
        return minx;
    }

    /**
     * Establece el valor del campo minx
     * @param minx El campo minx a establecer
     */
    public void setMinx(Double minx) {
        this.minx = minx;
    }

    /**
     * Devuelve el campo miny
     * @return El campo miny
     */
    public Double getMiny() {
        return miny;
    }

    /**
     * Establece el valor del campo miny
     * @param miny El campo miny a establecer
     */
    public void setMiny(Double miny) {
        this.miny = miny;
    }

    /**
     * Devuelve el campo maxx
     * @return El campo maxx
     */
    public Double getMaxx() {
        return maxx;
    }

    /**
     * Establece el valor del campo maxx
     * @param maxx El campo maxx a establecer
     */
    public void setMaxx(Double maxx) {
        this.maxx = maxx;
    }

    /**
     * Devuelve el campo maxy
     * @return El campo maxy
     */
    public Double getMaxy() {
        return maxy;
    }

    /**
     * Establece el valor del campo maxy
     * @param maxy El campo maxy a establecer
     */
    public void setMaxy(Double maxy) {
        this.maxy = maxy;
    }

}
