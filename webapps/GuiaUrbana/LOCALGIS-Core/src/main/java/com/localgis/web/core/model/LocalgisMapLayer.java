/**
 * LocalgisMapLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;


public class LocalgisMapLayer {

    private Integer mapid;

    private Integer layerid;

    private Integer styleid;

    /*
     * Posicion absoluta de la capa dentro del mapa sin tener en cuenta las
     * capas wms. Esta posicion comienza en 0
     */
    private Integer position;

    /*
     * Posicion de la layerfamily a la que pertenece la capa teniendo en cuenta
     * las capas wms, que se consideran como layerfamilies. Esta posicion
     * comienza en 1
     */
    private Integer positionLayerFamily;

    private Boolean visible;
    
    public LocalgisMapLayer() {
    }
    
    public LocalgisMapLayer(Integer mapid, Integer layerid, Integer styleid, Integer position, Integer positionLayerFamily, Boolean visible) {
        super();
        this.mapid = mapid;
        this.layerid = layerid;
        this.styleid = styleid;
        this.position = position;
        this.positionLayerFamily = positionLayerFamily;
        this.visible = visible;
    }
    
    public LocalgisMapLayer(Integer mapid, Integer layerid, Integer styleid, Integer position, Integer positionLayerFamily) {
    	this(mapid, layerid, styleid, position, positionLayerFamily, Boolean.TRUE);
    }

    /**
     * Devuelve el campo mapid
     * @return El campo mapid
     */
    public Integer getMapid() {
        return mapid;
    }

    /**
     * Establece el valor del campo mapid
     * @param mapid El campo mapid a establecer
     */
    public void setMapid(Integer mapid) {
        this.mapid = mapid;
    }

    /**
     * Devuelve el campo layerid
     * @return El campo layerid
     */
    public Integer getLayerid() {
        return layerid;
    }

    /**
     * Establece el valor del campo layerid
     * @param layerid El campo layerid a establecer
     */
    public void setLayerid(Integer layerid) {
        this.layerid = layerid;
    }

    /**
     * Devuelve el campo styleid
     * @return El campo styleid
     */
    public Integer getStyleid() {
        return styleid;
    }

    /**
     * Establece el valor del campo styleid
     * @param styleid El campo styleid a establecer
     */
    public void setStyleid(Integer styleid) {
        this.styleid = styleid;
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
    
	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

}