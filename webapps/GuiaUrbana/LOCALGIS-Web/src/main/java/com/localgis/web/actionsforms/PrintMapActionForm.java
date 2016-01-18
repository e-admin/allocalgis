/**
 * PrintMapActionForm.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.actionsforms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class PrintMapActionForm extends ActionForm {

    private Integer idMap;

    private String language;

    private String x;
    
    private String y;
    
    private Integer zoom;
    
    private String[] layers;
    private String desc; // descripcion para añadir a la pagina
    private String overlay; // GML con las geometrias
    private Boolean showMarkers;
    private Boolean esRuta;
    private Boolean esArea;
    private String markerRouteRequest;
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors actionErrors = new ActionErrors();
        if (idMap == null || idMap.intValue() < 0) {
            actionErrors.add("idMap", new ActionMessage("printMap.error.idMap"));
        }
        if (language == null) {
            actionErrors.add("language", new ActionMessage("printMap.error.language"));
        }
        if (x == null) {
            actionErrors.add("language", new ActionMessage("printMap.error.x"));
        }
        if (y == null) {
            actionErrors.add("language", new ActionMessage("printMap.error.y"));
        }
        if (zoom == null) {
        	actionErrors.add("language", new ActionMessage("printMap.error.zoom"));
        }
        if (layers == null) {
            actionErrors.add("language", new ActionMessage("printMap.error.layers"));
        }
        if (showMarkers == null) {
            actionErrors.add("language", new ActionMessage("printMap.error.showMarkers"));
        }

        return actionErrors;
    }

    /**
     * Devuelve el campo idMap
     * @return El campo idMap
     */
    public Integer getIdMap() {
        return idMap;
    }

    /**
     * Establece el valor del campo idMap
     * @param idMap El campo idMap a establecer
     */
    public void setIdMap(Integer idMap) {
        this.idMap = idMap;
    }

    /**
     * Devuelve el campo language
     * @return El campo language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Establece el valor del campo language
     * @param language El campo language a establecer
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Devuelve el campo x
     * @return El campo x
     */
    public String getX() {
        return x;
    }

    /**
     * Establece el valor del campo x
     * @param x El campo x a establecer
     */
    public void setX(String x) {
        this.x = x;
    }

    /**
     * Devuelve el campo y
     * @return El campo y
     */
    public String getY() {
        return y;
    }

    /**
     * Establece el valor del campo y
     * @param y El campo y a establecer
     */
    public void setY(String y) {
        this.y = y;
    }

    /**
     * Devuelve el campo showMarkers
     * @return El campo showMarkers
     */
    public Boolean getShowMarkers() {
        return showMarkers;
    }

    /**
     * Establece el valor del campo showMarkers
     * @param showMarkers El campo showMarkers a establecer
     */
    public void setShowMarkers(Boolean showMarkers) {
        this.showMarkers = showMarkers;
    }

    /**
     * Devuelve el campo zoom
     * @return El campo zoom
     */
    public Integer getZoom() {
        return zoom;
    }

    /**
     * Establece el valor del campo zoom
     * @param zoom El campo zoom a establecer
     */
    public void setZoom(Integer zoom) {
        this.zoom = zoom;
    }

    /**
     * Devuelve el campo layers
     * @return El campo layers
     */
    public String[] getLayers() {
        return layers;
    }

    /**
     * Establece el valor del campo layers
     * @param layers El campo layers a establecer
     */
    public void setLayers(String[] layers) {
        this.layers = layers;
    }

    public Boolean getEsRuta()
    {
        return esRuta;
    }

    public void setEsRuta(Boolean esRuta)
    {
        this.esRuta = esRuta;
    }

    public Boolean getEsArea()
    {
        return esArea;
    }

    public void setEsArea(Boolean esArea)
    {
        this.esArea = esArea;
    }

    public String getMarkerRouteRequest()
    {
        return markerRouteRequest;
    }

    public void setMarkerRouteRequest(String markerRouteRequest)
    {
        this.markerRouteRequest = markerRouteRequest;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public String getOverlay()
    {
        return overlay;
    }

    public void setOverlay(String overlay)
    {
        this.overlay = overlay;
    }
    
}
