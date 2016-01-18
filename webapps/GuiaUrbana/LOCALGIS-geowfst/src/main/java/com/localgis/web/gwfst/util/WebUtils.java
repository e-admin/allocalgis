/**
 * WebUtils.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.gwfst.util;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.localgis.web.core.manager.LocalgisLayerManager;
import com.localgis.web.core.model.GeopistaCoverageLayer;
import com.localgis.web.core.model.LocalgisLayerExt;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.core.model.LocalgisMapServerLayer;
import com.localgis.web.gwfst.openlayers.LayerOpenlayers;

/**
 * Clase que proporciona utilidades
 * 
 * @author albegarcia
 * 
 */
public class WebUtils {

    /**
     * Método para juntar y ordenar las capas por posicion
     * 
     * @param layersIncluded
     *            Si se pasa como parametro solo se incluiran en el resultado
     *            final aquellas capas cuyos ids esten en esta lista
     * @param localgisLayers
     *            Lista de capas internas
     * @param localgisWMSLayers
     *            Lista de capas externas
     * @param localgisMap
     *            Mapa al que pertencen las capas
     * @param urlGetMapRequests
     *            URL usada para las peticiones de tipo GetMap
     * @param urlGetFeatureInfoRequests
     *            URL usada para las peticiones de tipo GetFeatureInfo
     * @param urlGetLegendGraphicsRequests
     *            URL usada para las peticiones de tipo GetLegendGraphics
     * @param projection
     *            Proyeccion de las capas
     * @param urlCustomLegend
     *            URL para obtener las leyendas personalizadas de aquellas capas
     *            que hayan sido personalizadas
     * @param localgisLayerManager
     *            Un localgisLayerManager para obtener ciertas propiedades de
     *            las capas.
     * @param format
     *            Formato a usar
     * @return Un conjunto ordenado de capas como objetos LayerOpenlayers
     */
    public static SortedSet joinAndSortLayers(List layersIncluded, List localgisLayers, List localgisWMSLayers, LocalgisMap localgisMap, String urlGetMapRequests, String urlGetFeatureInfoRequests, String urlGetLegendGraphicsRequests,
            String projection, String urlCustomLegend, LocalgisLayerManager localgisLayerManager, int operationType, String format) {
        SortedSet result = new TreeSet();
        if (localgisLayers != null) {
            Iterator itLayers = localgisLayers.iterator();
            while (itLayers.hasNext()) {
                LocalgisLayerExt localgisLayer = (LocalgisLayerExt) itLayers.next();
                result.add(buildLayerOpenlayers(localgisLayer, localgisMap, urlGetMapRequests, urlGetFeatureInfoRequests, urlGetLegendGraphicsRequests, projection, urlCustomLegend, localgisLayerManager.getInternalNameGenericLayer(localgisMap, localgisLayer, operationType), format));
            }
        }
        if (localgisWMSLayers != null) {
            Iterator itWMSLayers = localgisWMSLayers.iterator();
            while (itWMSLayers.hasNext()) {
                LocalgisMapServerLayer localgisMapServerLayer = (LocalgisMapServerLayer) itWMSLayers.next();
                result.add(buildLayerOpenlayers(localgisMapServerLayer));
            }
        }
        if (layersIncluded != null) {
            for (Iterator iterator = result.iterator(); iterator.hasNext();) {
                LayerOpenlayers layer = (LayerOpenlayers) iterator.next();
                if (!layersIncluded.contains(layer.getIdLayer())) {
                    iterator.remove();
                }
            }
        }
        return result;
    }

    /**
     * Construye una capa de Openlayers a partir de una GeopistaCoverageLayer y otros parametros
     * @param geopistaCoverageLayer
     * @param localgisMap
     * @param urlGetMapRequests
     * @param projection
     * @param name
     * @param internalName
     * @param wmcName
     * @param format
     * @return Una capa de Openlayers
     */
    public static LayerOpenlayers buildLayerOpenlayers(GeopistaCoverageLayer geopistaCoverageLayer, LocalgisMap localgisMap, String urlGetMapRequests, String projection, String name, String internalName, String wmcName, String format) {
        LayerOpenlayers layerOpenlayers = new LayerOpenlayers();
        layerOpenlayers.setIdLayer(new Integer(LayerOpenlayers.ID_LAYER_ORTOFOTO));
        layerOpenlayers.setName(name);
        layerOpenlayers.setInternalName(internalName);
        layerOpenlayers.setWmcName(wmcName);
        /*
         * La posicion de las ortofotos es siempre la 0 (tanto para la posicion
         * de la layerfamily como para la posicion de la capa dentro de la layer
         * family) para que quede siempre la primera
         */
        layerOpenlayers.setPosition(new Integer(0));
        layerOpenlayers.setPositionLayerFamily(new Integer(0));
        layerOpenlayers.setProjection(projection);
        layerOpenlayers.setFormat(format);
        layerOpenlayers.setVersion("1.1.1");
        layerOpenlayers.setExternalLayer(Boolean.FALSE);
        /*
         * Para las coverage layers no mostramos leyenda ni se hacen peticiones
         * de get feature info. Solo se hacen peticiones GetMap
         */
        layerOpenlayers.setUrlGetMapRequests(urlGetMapRequests);
        layerOpenlayers.setUrlGetLegendGraphicsRequests(null);
        layerOpenlayers.setUrlGetFeatureInfoRequests(null);
        return layerOpenlayers;
    }

    /**
     * Construye una capa de Openlayers a partir de una LocalgisLayer y otros parametros
     * @param localgisLayer
     * @param localgisMap
     * @param urlGetMapRequests
     * @param urlGetFeatureInfoRequests
     * @param urlGetLegendGraphicsRequests
     * @param projection
     * @param urlCustomLegend
     * @param internalName
     * @param format
     * @return Una capa de Openlayers
     */
    public static LayerOpenlayers buildLayerOpenlayers(LocalgisLayerExt localgisLayer, LocalgisMap localgisMap, String urlGetMapRequests, String urlGetFeatureInfoRequests, String urlGetLegendGraphicsRequests, String projection, String urlCustomLegend, String internalName, String format) {
        LayerOpenlayers layerOpenlayers = new LayerOpenlayers();
        layerOpenlayers.setIdLayer(localgisLayer.getLayerid());
        layerOpenlayers.setName(localgisLayer.getNametranslated());
        layerOpenlayers.setInternalName(internalName);
        layerOpenlayers.setWmcName(localgisLayer.getLayername());
        layerOpenlayers.setPosition(localgisLayer.getPosition());
        layerOpenlayers.setPositionLayerFamily(localgisLayer.getPositionLayerFamily());
        layerOpenlayers.setProjection(projection);
        layerOpenlayers.setFormat(format);
        layerOpenlayers.setVersion("1.1.1");
        layerOpenlayers.setExternalLayer(new Boolean(false));
        boolean visible= localgisLayer.getVisible().booleanValue();
        layerOpenlayers.setVisible(visible);
        /*
         * URL para las peticiones GetMap
         */
        layerOpenlayers.setUrlGetMapRequests(urlGetMapRequests);
      
        /*
         * URL para las peticiones GetFeatureInfo
         */
        if (urlGetFeatureInfoRequests.indexOf("?") == -1) {
            urlGetFeatureInfoRequests += "?";
        } else {
            urlGetFeatureInfoRequests += "&";
        }
        urlGetFeatureInfoRequests += "REQUEST=GetFeatureInfo&EXCEPTIONS=application/vnd.ogc.se_xml&INFO_FORMAT=gml&FORMAT="+format+"&QUERY_LAYERS="+localgisLayer.getLayername()+"&LAYERS="+localgisLayer.getLayername()+"&VERSION="+layerOpenlayers.getVersion()+"&SRS="+projection;
        layerOpenlayers.setUrlGetFeatureInfoRequests(urlGetFeatureInfoRequests);
        

        /*
         * URL para las peticiones GetLegendGraphics
         */
        if (localgisLayer.isCustomlegend().booleanValue()) {
            if (urlCustomLegend.indexOf("?") == -1) {
                urlCustomLegend += "?";
            } else {
                urlCustomLegend += "&";
            }
            urlCustomLegend += "idLayerGeopista="+localgisLayer.getLayeridgeopista();
            layerOpenlayers.setUrlGetLegendGraphicsRequests(urlCustomLegend);
        } else {
            if (urlGetLegendGraphicsRequests.indexOf("?") == -1) {
                urlGetLegendGraphicsRequests += "?";
            } else {
                urlGetLegendGraphicsRequests += "&";
            }
            // Las leyendas siempre en PNG
            urlGetLegendGraphicsRequests += "REQUEST=GetLegendGraphic&layer="+localgisLayer.getLayername()+"&VERSION="+layerOpenlayers.getVersion()+"&FORMAT="+LayerOpenlayers.FORMAT_PNG;
            layerOpenlayers.setUrlGetLegendGraphicsRequests(urlGetLegendGraphicsRequests);
        }
        return layerOpenlayers;
    }
    
    /**
     * Construye una capa de Openlayers a partir de una LocalgisMapServerLayer
     * @param localgisMapServerLayer
     * @return Una capa de Openlayers
     */
    public static LayerOpenlayers buildLayerOpenlayers(LocalgisMapServerLayer localgisMapServerLayer) {
        LayerOpenlayers layerOpenlayers = new LayerOpenlayers();
        layerOpenlayers.setIdLayer(localgisMapServerLayer.getId());
        String name;
        if (localgisMapServerLayer.getLayers().split(",") != null) {
            name = localgisMapServerLayer.getLayers().split(",")[0];
        } else {
            name = localgisMapServerLayer.getLayers();
        }
        layerOpenlayers.setName(name);
        layerOpenlayers.setInternalName(localgisMapServerLayer.getLayers());
        layerOpenlayers.setWmcName(localgisMapServerLayer.getLayers());
        boolean visible=Integer.parseInt(localgisMapServerLayer.getVisible().toString())==0?false:true;
        layerOpenlayers.setVisible(visible);
        /*
         * Para las capas WMS, la posicion de la layerfamily será la que
         * contenga la propia capa. La posición absoluta de la capa es la 0 pues
         * estas capas son especiales y no se tiene la posicion absoluta dentro
         * del mapa
         */
        layerOpenlayers.setPositionLayerFamily(localgisMapServerLayer.getPosition());
        layerOpenlayers.setPosition(new Integer(0));
        layerOpenlayers.setProjection(localgisMapServerLayer.getSrs());
        layerOpenlayers.setFormat(localgisMapServerLayer.getFormat());
        layerOpenlayers.setVersion(localgisMapServerLayer.getVersion());
        layerOpenlayers.setExternalLayer(new Boolean(true));
        layerOpenlayers.setInternalNameMapServer(layerOpenlayers.getName()+"_"+localgisMapServerLayer.getId()+"_remote");
        /*
         *  Para las capas externas no mostramos leyenda
         */
        layerOpenlayers.setUrlGetMapRequests(localgisMapServerLayer.getUrl());
        layerOpenlayers.setUrlGetLegendGraphicsRequests(null);
        /*
         * URL para las peticiones GetFeatureInfo
         */
        String urlGetFeatureInfoRequests = localgisMapServerLayer.getUrl();
        if (urlGetFeatureInfoRequests.indexOf("?") == -1) {
            urlGetFeatureInfoRequests += "?";
        } else {
            urlGetFeatureInfoRequests += "&";
        }
        urlGetFeatureInfoRequests += "REQUEST=GetFeatureInfo&EXCEPTIONS=application/vnd.ogc.se_xml&INFO_FORMAT=gml&FORMAT="+localgisMapServerLayer.getFormat()+"&QUERY_LAYERS="+localgisMapServerLayer.getLayers()+"&LAYERS="+localgisMapServerLayer.getLayers()+"&VERSION="+layerOpenlayers.getVersion()+"&SRS="+localgisMapServerLayer.getSrs();
        layerOpenlayers.setUrlGetFeatureInfoRequests(urlGetFeatureInfoRequests);

        return layerOpenlayers;
    }

}
