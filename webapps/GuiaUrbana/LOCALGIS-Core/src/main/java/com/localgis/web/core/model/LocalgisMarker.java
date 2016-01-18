/**
 * LocalgisMarker.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LocalgisMarker {

    private Integer markerid;

    private Integer mapid;

    private String username;

    private Double x;

    private Double y;

    private Double scale;

    private String markname;

    private String marktext;

    public LocalgisMarker() {
    }
    
    public LocalgisMarker(Integer markerid, Integer mapid, String username, Double x, Double y, Double scale, String markname, String marktext) {
        super();
        this.markerid = markerid;
        this.mapid = mapid;
        this.username = username;
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.markname = markname;
        this.marktext = marktext;
    }

    /**
     * Devuelve el campo markerid
     * @return El campo markerid
     */
    public Integer getMarkerid() {
        return markerid;
    }

    /**
     * Establece el valor del campo markerid
     * @param markerid El campo markerid a establecer
     */
    public void setMarkerid(Integer markerid) {
        this.markerid = markerid;
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
     * Devuelve el campo username
     * @return El campo username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el valor del campo username
     * @param username El campo username a establecer
     */
    public void setUsername(String username) {
        this.username = username;
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
     * Devuelve el campo markname
     * @return El campo markname
     */
    public String getMarkname() {
        return markname;
    }

    /**
     * Devuelve el campo markname codificado
     * @return El campo markname codificado
     */
    public String getMarknameEncoded() {
        try {
            return URLEncoder.encode(markname, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * Establece el valor del campo markname
     * @param markname El campo markname a establecer
     */
    public void setMarkname(String markname) {
        this.markname = markname;
    }

    /**
     * Devuelve el campo marktext
     * @return El campo marktext
     */
    public String getMarktext() {
        return marktext;
    }

    /**
     * Devuelve el campo marktext codificado
     * @return El campo marktext codificado
     */
    public String getMarktextEncoded() {
        try {
            return URLEncoder.encode(marktext, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    /**
     * Establece el valor del campo marktext
     * @param marktext El campo marktext a establecer
     */
    public void setMarktext(String marktext) {
        this.marktext = marktext;
    }

    
}