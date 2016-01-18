/**
 * PlaceNameInfoService.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.beans;

public class PlaceNameInfoService {

    public static int SERVICE_WFS_MNE = 0;
    public static int SERVICE_WFS_G = 1;
    
    private String name;
    
    private String service;

    private int type;

    private String featureType;
    
    /**
     * Devuelve el campo name
     * @return El campo name
     */
    public String getName() {
        return name;
    }

    /**
     * Establece el valor del campo name
     * @param name El campo name a establecer
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Devuelve el campo service
     * @return El campo service
     */
    public String getService() {
        return service;
    }

    /**
     * Establece el valor del campo service
     * @param service El campo service a establecer
     */
    public void setService(String service) {
        this.service = service;
    }

    /**
     * Devuelve el campo type
     * @return El campo type
     */
    public int getType() {
        return type;
    }

    /**
     * Establece el valor del campo type
     * @param type El campo type a establecer
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * Devuelve el campo featureType
     * @return El campo featureType
     */
    public String getFeatureType() {
        return featureType;
    }

    /**
     * Establece el valor del campo featureType
     * @param featureType El campo featureType a establecer
     */
    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }
    
    
}
