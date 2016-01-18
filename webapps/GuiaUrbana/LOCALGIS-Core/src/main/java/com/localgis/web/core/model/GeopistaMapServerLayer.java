/**
 * GeopistaMapServerLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

import java.math.BigDecimal;

public class GeopistaMapServerLayer {

    private Integer id;

    private String service;

    private String url;

    private String params;

    private String srs;

    private String format;

    private String version;

    private BigDecimal activa;

    private BigDecimal visible;

    private Integer position;

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
     * Devuelve el campo params
     * @return El campo params
     */
    public String getParams() {
        return params;
    }

    /**
     * Establece el valor del campo params
     * @param params El campo params a establecer
     */
    public void setParams(String params) {
        this.params = params;
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

}