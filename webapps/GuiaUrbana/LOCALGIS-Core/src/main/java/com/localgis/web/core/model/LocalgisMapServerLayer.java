/**
 * LocalgisMapServerLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

import java.math.BigDecimal;

public class LocalgisMapServerLayer {

    private Integer id;

    private String service;

    private String url;

    private String layers;

    private String srs;

    private String format;

    private String version;

    private BigDecimal activa;

    private BigDecimal visible;

    private Integer mapid;

    private Integer position;

    private Integer idgeopista;

    public LocalgisMapServerLayer() {
    }
    
    public LocalgisMapServerLayer(Integer id, String service, String url, String layers, String srs, String format, String version, BigDecimal activa, BigDecimal visible, Integer mapid,
            Integer position, Integer idgeopista) {
        super();
        this.id = id;
        this.service = service;
        this.url = url;
        this.layers = layers;
        this.srs = srs;
        this.format = format;
        this.version = version;
        this.activa = activa;
        this.visible = visible;
        this.mapid = mapid;
        this.position = position;
        this.idgeopista = idgeopista;
    }

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
     * Devuelve el campo url
     * @return El campo url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Establece el valor del campo url
     * @param url El campo url a establecer
     */
    public void setUrl(String url) {
        this.url = url;
    }

   
    /**
     * Devuelve el campo layers
     * @return El campo layers
     */
    public String getLayers() {
        return layers;
    }

    /**
     * Establece el valor del campo layers
     * @param layers El campo layers a establecer
     */
    public void setLayers(String layers) {
        this.layers = layers;
    }

    /**
     * Devuelve el campo srs
     * @return El campo srs
     */
    public String getSrs() {
        return srs;
    }

    /**
     * Establece el valor del campo srs
     * @param srs El campo srs a establecer
     */
    public void setSrs(String srs) {
        this.srs = srs;
    }

    /**
     * Devuelve el campo format
     * @return El campo format
     */
    public String getFormat() {
        return format;
    }

    /**
     * Establece el valor del campo format
     * @param format El campo format a establecer
     */
    public void setFormat(String format) {
        this.format = format;
    }

    /**
     * Devuelve el campo version
     * @return El campo version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Establece el valor del campo version
     * @param version El campo version a establecer
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Devuelve el campo activa
     * @return El campo activa
     */
    public BigDecimal getActiva() {
        return activa;
    }

    /**
     * Establece el valor del campo activa
     * @param activa El campo activa a establecer
     */
    public void setActiva(BigDecimal activa) {
        this.activa = activa;
    }

    /**
     * Devuelve el campo visible
     * @return El campo visible
     */
    public BigDecimal getVisible() {
        return visible;
    }

    /**
     * Establece el valor del campo visible
     * @param visible El campo visible a establecer
     */
    public void setVisible(BigDecimal visible) {
        this.visible = visible;
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
     * Devuelve el campo idgeopista
     * @return El campo idgeopista
     */
    public Integer getIdgeopista() {
        return idgeopista;
    }

    /**
     * Establece el valor del campo idgeopista
     * @param idgeopista El campo idgeopista a establecer
     */
    public void setIdgeopista(Integer idgeopista) {
        this.idgeopista = idgeopista;
    }

    /**
     * Devuelve el nombre interno de la capa en el map server
     * @return El nombre interno de la capa en el map server
     */
    public String getInternalNameMapServer() {
        String name = "";
        if (layers != null) { 
            name = this.layers.split(",")[0];
        }
        name += "_" + this.id + "_remote";
        return name;
    }
}