/**
 * GeopistaLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

public class GeopistaLayer {

    private Integer idLayer;

    private String name;

    private String selectQuery;

    private Integer idStyle;

    private String xmlStyle;

    private String nameStyle;
    
    private Integer position;

    private Integer positionLayerFamily;

    private Boolean visible;

    public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}
	
    /**
     * Devuelve el campo idLayer
     * 
     * @return El campo idLayer
     */
    public Integer getIdLayer() {
        return idLayer;
    }

    /**
     * Establece el valor del campo idLayer
     * 
     * @param idLayer
     *            El campo idLayer a establecer
     */
    public void setIdLayer(Integer idLayer) {
        this.idLayer = idLayer;
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

    /**
     * Devuelve el campo selectQuery
     * 
     * @return El campo selectQuery
     */
    public String getSelectQuery() {
        return selectQuery;
    }

    /**
     * Establece el valor del campo selectQuery
     * 
     * @param selectQuery
     *            El campo selectQuery a establecer
     */
    public void setSelectQuery(String selectQuery) {
        this.selectQuery = selectQuery;
    }

    /**
     * Devuelve el campo idStyle
     * 
     * @return El campo idStyle
     */
    public Integer getIdStyle() {
        return idStyle;
    }

    /**
     * Establece el valor del campo idStyle
     * 
     * @param idStyle
     *            El campo idStyle a establecer
     */
    public void setIdStyle(Integer idStyle) {
        this.idStyle = idStyle;
    }

    /**
     * Devuelve el campo xmlStyle
     * 
     * @return El campo xmlStyle
     */
    public String getXmlStyle() {
        return xmlStyle;
    }

    /**
     * Establece el valor del campo xmlStyle
     * 
     * @param xmlStyle
     *            El campo xmlStyle a establecer
     */
    public void setXmlStyle(String xmlStyle) {
        this.xmlStyle = xmlStyle;
    }

    /**
     * Devuelve el campo nameStyle
     * 
     * @return El campo nameStyle
     */
    public String getNameStyle() {
        return nameStyle;
    }

    /**
     * Establece el valor del campo nameStyle
     * 
     * @param nameStyle
     *            El campo nameStyle a establecer
     */
    public void setNameStyle(String nameStyle) {
        this.nameStyle = nameStyle;
    }

    /**
     * Devuelve el campo position
     * @return El campo position
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * Establece el valor del campo position
     * @param position El campo position a establecer
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * Devuelve el campo positionLayerFamily
     * @return El campo positionLayerFamily
     */
    public Integer getPositionLayerFamily() {
        return positionLayerFamily;
    }

    /**
     * Establece el valor del campo positionLayerFamily
     * @param positionLayerFamily El campo positionLayerFamily a establecer
     */
    public void setPositionLayerFamily(Integer positionLayerFamily) {
        this.positionLayerFamily = positionLayerFamily;
    }

}