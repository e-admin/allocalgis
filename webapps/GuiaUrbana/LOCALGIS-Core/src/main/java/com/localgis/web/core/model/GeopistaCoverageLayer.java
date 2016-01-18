/**
 * GeopistaCoverageLayer.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

public class GeopistaCoverageLayer {

    private Integer idMunicipio;

    private Integer idName;

    private String descPath;

    private String srs;

    private String extension;

    /**
     * Devuelve el campo idMunicipio
     * @return El campo idMunicipio
     */
    public Integer getIdMunicipio() {
        return idMunicipio;
    }

    /**
     * Establece el valor del campo idMunicipio
     * @param idMunicipio El campo idMunicipio a establecer
     */
    public void setIdMunicipio(Integer idMunicipio) {
        this.idMunicipio = idMunicipio;
    }

    /**
     * Devuelve el campo idName
     * @return El campo idName
     */
    public Integer getIdName() {
        return idName;
    }

    /**
     * Establece el valor del campo idName
     * @param idName El campo idName a establecer
     */
    public void setIdName(Integer idName) {
        this.idName = idName;
    }

    /**
     * Devuelve el campo descPath
     * @return El campo descPath
     */
    public String getDescPath() {
        return descPath;
    }

    /**
     * Establece el valor del campo descPath
     * @param descPath El campo descPath a establecer
     */
    public void setDescPath(String descPath) {
        this.descPath = descPath;
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
     * Devuelve el campo extension
     * @return El campo extension
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Establece el valor del campo extension
     * @param extension El campo extension a establecer
     */
    public void setExtension(String extension) {
        this.extension = extension;
    }
    
}