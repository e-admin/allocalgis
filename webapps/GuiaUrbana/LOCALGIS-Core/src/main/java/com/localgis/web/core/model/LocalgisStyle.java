/**
 * LocalgisStyle.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.model;

public class LocalgisStyle {

    private Integer styleid;

    private String stylename;

    private String styletitle;

    private String styleabstract;

    private String featuretypestyles;
    
    public LocalgisStyle() {
    }
    
    public LocalgisStyle(Integer styleid, String stylename, String styletitle, String styleabstract, String featuretypestyles) {
        super();
        this.styleid = styleid;
        this.stylename = stylename;
        this.styletitle = styletitle;
        this.styleabstract = styleabstract;
        this.featuretypestyles = featuretypestyles;
    }

    /**
     * Devuelve el campo styleid
     * 
     * @return El campo styleid
     */
    public Integer getStyleid() {
        return styleid;
    }

    /**
     * Establece el valor del campo styleid
     * 
     * @param styleid
     *            El campo styleid a establecer
     */
    public void setStyleid(Integer styleid) {
        this.styleid = styleid;
    }

    /**
     * Devuelve el campo stylename
     * 
     * @return El campo stylename
     */
    public String getStylename() {
        return stylename;
    }

    /**
     * Establece el valor del campo stylename
     * 
     * @param stylename
     *            El campo stylename a establecer
     */
    public void setStylename(String stylename) {
        this.stylename = stylename;
    }

    /**
     * Devuelve el campo styletitle
     * 
     * @return El campo styletitle
     */
    public String getStyletitle() {
        return styletitle;
    }

    /**
     * Establece el valor del campo styletitle
     * 
     * @param styletitle
     *            El campo styletitle a establecer
     */
    public void setStyletitle(String styletitle) {
        this.styletitle = styletitle;
    }

    /**
     * Devuelve el campo styleabstract
     * 
     * @return El campo styleabstract
     */
    public String getStyleabstract() {
        return styleabstract;
    }

    /**
     * Establece el valor del campo styleabstract
     * 
     * @param styleabstract
     *            El campo styleabstract a establecer
     */
    public void setStyleabstract(String styleabstract) {
        this.styleabstract = styleabstract;
    }

    /**
     * Devuelve el campo featuretypestyles
     * 
     * @return El campo featuretypestyles
     */
    public String getFeaturetypestyles() {
        return featuretypestyles;
    }

    /**
     * Establece el valor del campo featuretypestyles
     * 
     * @param featuretypestyles
     *            El campo featuretypestyles a establecer
     */
    public void setFeaturetypestyles(String featuretypestyles) {
        this.featuretypestyles = featuretypestyles;
    }

}