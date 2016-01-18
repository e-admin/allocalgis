/**
 * PrintRutasAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.localgis.web.actionsforms.PrintMapActionForm;
import com.localgis.web.config.LocalgisWebConfiguration;
import com.localgis.web.core.ConstantesSQL;
import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager;
import com.localgis.web.core.manager.LocalgisLayerManager;
import com.localgis.web.core.manager.LocalgisMapManager;
import com.localgis.web.core.manager.LocalgisUtilsManager;
import com.localgis.web.core.model.BoundingBox;
import com.localgis.web.core.model.GeopistaCoverageLayer;
import com.localgis.web.core.model.GeopistaEntidadSupramunicipal;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.filters.LoginFilter;
import com.localgis.web.openlayers.LayerOpenlayers;
import com.localgis.web.util.LocalgisManagerBuilderSingleton;
import com.localgis.web.util.MarkersUtils;
import com.localgis.web.util.WebUtils;

public class PrintRutasAction extends Action {

    private static Log log = LogFactory.getLog(PrintMapAction.class);
    
    private static String SUCCESS_PAGE = "success";
    private static String ERROR_PAGE = "error";
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintMapActionForm formBean = (PrintMapActionForm)form;
        
        String[] layersIds = formBean.getLayers();
        ArrayList layersIncluded = new ArrayList();
        for (int i = 0; i < layersIds.length; i++) {
        	String[] temp = layersIds[i].split(",");
        	for (int z = 0; z < temp.length; z++) {
 
        		try{
        			Integer layerID = new Integer(temp[z]);
        			if (layerID>0)
        			    layersIncluded.add(layerID);
        			
        		}
        		catch(NumberFormatException ne){
        			//System.out.println("No es un numero:"+temp[z]);
        		}
        		
        	}
            
        }
        /*
         * Obtenemos los managers de localgis
         */
        LocalgisManagerBuilder localgisManagerBuilder = LocalgisManagerBuilderSingleton.getInstance();
        LocalgisLayerManager localgisLayerManager = localgisManagerBuilder.getLocalgisLayerManager();
        LocalgisEntidadSupramunicipalManager localgisEntidadSupramunicipalManager = localgisManagerBuilder.getLocalgisEntidadSupramunicipalManager();
        LocalgisMapManager localgisMapManager = localgisManagerBuilder.getLocalgisMapManager();
        LocalgisUtilsManager localgisUtilsManager = localgisManagerBuilder.getLocalgisUtilsManager();
        
        LocalgisMap localgisMap = localgisMapManager.getPublishedMap(formBean.getIdMap());
        String configurationLocalgisWeb = (String)request.getAttribute("configurationLocalgisWeb");
        
        /*
         * Comprobacion de que el mapa exista
         */
        if (localgisMap == null) {
            log.error("El mapa no existe");
            request.setAttribute("errorMessageKey", "printMap.error.mapNotExist");
            return mapping.findForward(ERROR_PAGE);
        } else {
            /*
             * Comprobación de que el mapa sea publico o privado dependiendo de la
             * configuracion de la aplicación 
             */
            if ((configurationLocalgisWeb.equals("public") && localgisMap.getMappublic().equals(ConstantesSQL.FALSE)) ||
                    (configurationLocalgisWeb.equals("private") && localgisMap.getMappublic().equals(ConstantesSQL.TRUE))||
                    (configurationLocalgisWeb.equals("incidencias") && localgisMap.getMappublic().equals(ConstantesSQL.FALSE))) {
                log.error("No se dispone de permisos para ver el mapa");
                request.setAttribute("errorMessageKey", "printMap.error.mapNotAllowed");
                return mapping.findForward(ERROR_PAGE);
            }
        }
        
        List layers = localgisMapManager.getMapLayers(formBean.getIdMap(), formBean.getLanguage());
        List wmsLayers = localgisMapManager.getMapWMSLayers(formBean.getIdMap());

        //Obtenemos la hoja de estilos, si la hubiera
        String customCSS = localgisEntidadSupramunicipalManager.getCSS(localgisMap.getMapidentidad());

        //Obtenemos la entidad
        GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal = localgisEntidadSupramunicipalManager.getEntidadSupramunicipal(localgisMap.getMapidentidad());

        /*
         * Obtenemos las URLs necesarias del servidor de mapas dependidendo de la configuracion
         */
        Boolean publicMap;
        if (configurationLocalgisWeb.equals("public")||configurationLocalgisWeb.equals("incidencias")) {
            publicMap = Boolean.TRUE;
        } else {
            publicMap = Boolean.FALSE;
        }
        String urlAllRequests = localgisMapManager.getMapServerURL(localgisMap.getMapidentidad(), localgisMap.getMapid(), publicMap, LocalgisMapManager.ALL_REQUESTS);
        String urlGetLegendGraphicsRequests = localgisMapManager.getMapServerURL(localgisMap.getMapidentidad(), localgisMap.getMapid(), publicMap, LocalgisMapManager.GET_LEGEND_GRAPHICS_REQUESTS);
        String urlGetFeatureInfoRequests = localgisMapManager.getMapServerURL(localgisMap.getMapidentidad(), localgisMap.getMapid(), publicMap, LocalgisMapManager.GET_FEATURE_INFO_REQUESTS);

        /*
         * Obtenemos el bbox de España 
         */
        BoundingBox boundingBoxSpain = localgisUtilsManager.getBoundingBoxSpain(localgisMap.getSrid());

        String projection = "EPSG:"+localgisMap.getSrid();

        // Variable donde vamos guardando las capas a mostrar en la pantalla de impresion
        String layersParam = "";
        
        /*
         * La primera capa es la de la ortofoto (si la hay y si se selecciono)
         */
        GeopistaCoverageLayer geopistaCoverageLayer = null;
        if (layersIncluded.contains(new Integer(LayerOpenlayers.ID_LAYER_ORTOFOTO))) {
            geopistaCoverageLayer = localgisEntidadSupramunicipalManager.getCoverageLayer(localgisMap.getMapidentidad()); 
            if (geopistaCoverageLayer != null) {
                LayerOpenlayers ortofotoOpenlayers = WebUtils.buildLayerOpenlayers(geopistaCoverageLayer, localgisMap, urlAllRequests, projection, LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_ORTOFOTO_NAME), localgisLayerManager.getInternalNameLayerOrtofoto(localgisMap, LocalgisLayerManager.PRINT_MAP_OPERATION), localgisLayerManager.getInternalNameLayerOrtofoto(localgisMap, LocalgisLayerManager.SAVE_MAP_OPERATION), LayerOpenlayers.FORMAT_JPEG);
                layersParam = ortofotoOpenlayers.getInternalName()+",";
                request.setAttribute("ortofotoLayer", ortofotoOpenlayers);
            }
        }

        /*
         * Creacion de la capa de provincias (junto con la de municipios dependiendo de la configuracion) si estaba seleccionada
         */
        if (layersIncluded.contains(new Integer(LayerOpenlayers.ID_LAYER_PROVINCIAS))) {
            Boolean showMunicipios = new Boolean(LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_MUNICIPIOS_VISIBLE));
            String internalNameProvincias = localgisLayerManager.getInternalNameLayerMunicipiosAndProvincias(localgisMap, showMunicipios, LocalgisLayerManager.PRINT_MAP_OPERATION);
            layersParam += internalNameProvincias + ",";
            LayerOpenlayers provinciasLayer = new LayerOpenlayers();
            provinciasLayer.setIdLayer(new Integer(-1));
            provinciasLayer.setExternalLayer(Boolean.FALSE);
            provinciasLayer.setFormat("image/png");
            provinciasLayer.setInternalName(internalNameProvincias);
            provinciasLayer.setName(LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_ORTOFOTO_NAME));
            provinciasLayer.setPosition(new Integer(0));
            provinciasLayer.setProjection(projection);
            provinciasLayer.setUrlGetLegendGraphicsRequests(null);
            provinciasLayer.setUrlGetMapRequests(urlAllRequests);
            provinciasLayer.setUrlGetFeatureInfoRequests(null);
            provinciasLayer.setVersion("1.1.1");
            request.setAttribute("provinciasLayer", provinciasLayer);
        }

        /*
         * Juntamos las capas wms con las capas normales en un conjunto de
         * objetos LayerOpenlayers ordenado por posicion
         */
        String urlCustomLegend = "http";
        if (request.isSecure()) {
            urlCustomLegend += "s";
        }
        urlCustomLegend += "://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"+configurationLocalgisWeb+"/getLegend.do?idEntidad="+localgisMap.getMapidentidad();
        SortedSet layersOpenlayersSorted = WebUtils.joinAndSortLayers(layersIncluded, layers, wmsLayers, localgisMap, urlAllRequests, urlGetFeatureInfoRequests, urlGetLegendGraphicsRequests, projection, urlCustomLegend, localgisLayerManager, LocalgisLayerManager.PRINT_MAP_OPERATION, LayerOpenlayers.FORMAT_JPEG);
        ArrayList layersList = new ArrayList(layersOpenlayersSorted.size());
        layersList.addAll(layersOpenlayersSorted);
                
        request.setAttribute("localgisMap", localgisMap);
        request.setAttribute("layers", layersList);
        
        Iterator it = layersList.iterator();
        int i = 0;
        while (it.hasNext()) {
            LayerOpenlayers layer = (LayerOpenlayers) it.next();
            if (i != 0) {
                layersParam += ",";
            }
            if (layer.getExternalLayer().booleanValue()) {
                layersParam += layer.getInternalNameMapServer();
            } else {
                layersParam += layer.getInternalName();
            }
            i++;
        }

        if (layersParam.endsWith(",")) {
            layersParam = layersParam.substring(0, layersParam.length()-1);
        }
        
        /*
         * Creamos la capa de openlayers a imprimir, que será una capa union de todas las capas seleccionadas
         */
        LayerOpenlayers layerToPrint = new LayerOpenlayers();
        layerToPrint.setIdLayer(new Integer(-1));
        layerToPrint.setExternalLayer(new Boolean(false));
        layerToPrint.setFormat(LayerOpenlayers.FORMAT_JPEG);
        layerToPrint.setInternalName(layersParam);
        layerToPrint.setName("Todo");
        layerToPrint.setPosition(new Integer(0));
        layerToPrint.setProjection(projection);
        layerToPrint.setUrlGetLegendGraphicsRequests(null);
        layerToPrint.setUrlGetMapRequests(urlAllRequests);
        layerToPrint.setUrlGetFeatureInfoRequests(null);
        
        request.setAttribute("layerToPrint", layerToPrint);
        if (customCSS != null) {
            request.setAttribute("customCSS", customCSS);
        }
        request.setAttribute("language", formBean.getLanguage());
        request.setAttribute("printMap", formBean );
        request.setAttribute("nombreMapa", geopistaEntidadSupramunicipal.getNombreoficial() + " - " + localgisMap.getName());
        request.setAttribute("singleTile", new Boolean(LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_OPENLAYERS_SINGLE_TILE)));
        request.setAttribute("boundingBoxSpain", boundingBoxSpain);

        if (formBean.getShowMarkers().booleanValue()) {
            List markers;
            if (configurationLocalgisWeb.equals("public")||configurationLocalgisWeb.equals("incidencias")) {
                Cookie cookie = MarkersUtils.getMarkerCookie(request, localgisMap.getMapid());
                if (cookie != null) {
                    markers = MarkersUtils.getMarkersFromCookie(cookie);
                } else {
                    cookie = MarkersUtils.createMarkerCookie(localgisMap.getMapid());
                    cookie.setPath(request.getContextPath());
                    response.addCookie(cookie);
                    markers = new ArrayList();
                }
            } else {
                /*
                 * Obtenemos las marcas de posicion para el usuario y para el mapa
                 */
                String username = (String)request.getSession().getAttribute(LoginFilter.LOGIN_ATTRIBUTE);
                markers = localgisMapManager.getMarkers(localgisMap.getMapid(), username);
            }
            request.setAttribute("markers", markers);
        }
        
        return mapping.findForward(SUCCESS_PAGE);
    }

}
