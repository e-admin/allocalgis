/**
 * ShowMapAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.actions;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.localgis.web.actionsforms.ShowMapActionForm;
import com.localgis.web.comparator.LayerComparator;
import com.localgis.web.config.LocalgisWebConfiguration;
import com.localgis.web.core.ConstantesSQL;
import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager;
import com.localgis.web.core.manager.LocalgisLayerManager;
import com.localgis.web.core.manager.LocalgisMapManager;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
import com.localgis.web.core.manager.LocalgisUtilsManager;
import com.localgis.web.core.model.BoundingBox;
import com.localgis.web.core.model.GeopistaCoverageLayer;
import com.localgis.web.core.model.GeopistaEntidadSupramunicipal;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.core.utils.ChangeCoordinateSystem;
import com.localgis.web.filters.LoginFilter;
import com.localgis.web.openlayers.LayerOpenlayers;
import com.localgis.web.util.LocalgisManagerBuilderSingleton;
import com.localgis.web.util.MarkersUtils;
import com.localgis.web.util.WFSGUtils;
import com.localgis.web.util.WebUtils;

public class ShowMapAction extends Action {

    private static Log log = LogFactory.getLog(ShowMapAction.class);
    
    private static String SUCCESS_PAGE = "success";
    private static String ERROR_PAGE = "error";
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ShowMapActionForm formBean = (ShowMapActionForm)form;
        
        /*
         * Obtenemos los managers de localgis
         */
        LocalgisManagerBuilder localgisManagerBuilder = LocalgisManagerBuilderSingleton.getInstance();
        LocalgisEntidadSupramunicipalManager localgisEntidadSupramunicipalManager = localgisManagerBuilder.getLocalgisEntidadSupramunicipalManager();
        LocalgisLayerManager localgisLayerManager = localgisManagerBuilder.getLocalgisLayerManager();
        LocalgisMapManager localgisMapManager = localgisManagerBuilder.getLocalgisMapManager();
        LocalgisMapsConfigurationManager localgisMapsConfigurationManager = localgisManagerBuilder.getLocalgisMapsConfigurationManager();
        LocalgisUtilsManager localgisUtilsManager = localgisManagerBuilder.getLocalgisUtilsManager();
        
        /*
         * Si tenemos idMap obtenemos el mapa publicado. Si no tenemos idMap obtenemos el mapa por defecto
         */
        LocalgisMap localgisMap;
        String configurationLocalgisWeb = (String)request.getAttribute("configurationLocalgisWeb");
        if (formBean.getIdMap() != null && formBean.getIdMap().intValue() >= 0) {
            localgisMap = localgisMapManager.getPublishedMap(formBean.getIdMap());
        } else {
            /* Comprobacion de que la entidad sea correcto */
            if (formBean.getIdEntidad() == null || formBean.getIdEntidad().intValue() <= 0) {
                log.error("La entidad seleccionada no es correcto");
                request.setAttribute("errorMessageKey", "showMap.error.entidadNotValid");
                return mapping.findForward(ERROR_PAGE);
            }
            localgisMap = localgisMapsConfigurationManager.getDefaultMap(formBean.getIdEntidad(), new Boolean(configurationLocalgisWeb.equals("public")));
        }

        /*
         * Comprobacion de que el mapa exista
         */
        if (localgisMap == null) {
            log.error("El mapa solicitado no existe");
            request.setAttribute("errorMessageKey", "showMap.error.mapNotExist");
            return mapping.findForward(ERROR_PAGE);
        }
        

        /*
         * Comprobación de que el mapa sea publico o privado dependiendo de la
         * configuracion de la aplicación 
         */
        if ((configurationLocalgisWeb.equals("public") && localgisMap.getMappublic().equals(ConstantesSQL.FALSE)) ||
                (configurationLocalgisWeb.equals("private") && localgisMap.getMappublic().equals(ConstantesSQL.TRUE)) ||
                (configurationLocalgisWeb.equals("incidencias") && localgisMap.getMappublic().equals(ConstantesSQL.FALSE))) {
            log.error("No se dispone de permisos para ver el mapa");
            request.setAttribute("errorMessageKey", "showMap.error.mapNotAllowed");
            return mapping.findForward(ERROR_PAGE);
        }

        List localgisLayers = localgisMapManager.getMapLayers(localgisMap.getMapid(), formBean.getLanguage());
        List localgisWMSLayers = localgisMapManager.getMapWMSLayers(localgisMap.getMapid());
        GeopistaCoverageLayer geopistaCoverageLayer = localgisEntidadSupramunicipalManager.getCoverageLayer(localgisMap.getMapidentidad()); 
        
        //Obtenemos la hoja de estilos, si la hubiera
        String customCSS = localgisEntidadSupramunicipalManager.getCSS(localgisMap.getMapidentidad());
        
        //Obtenemos el municipio
        GeopistaEntidadSupramunicipal geopistaEntidad = localgisEntidadSupramunicipalManager.getEntidadSupramunicipal(localgisMap.getMapidentidad());
        
        Boolean publicMap;
        if (configurationLocalgisWeb.equals("public")||configurationLocalgisWeb.equals("incidencias")) {
            publicMap = Boolean.TRUE;
        } else {
            publicMap = Boolean.FALSE;
        }
        
        /*
         * URLs necesarias
         */
        String urlGetMapRequests = localgisMapManager.getMapServerURL(localgisMap.getMapidentidad(), localgisMap.getMapid(), publicMap, LocalgisMapManager.GET_MAP_REQUESTS);
        String urlGetLegendGraphicsRequests = localgisMapManager.getMapServerURL(localgisMap.getMapidentidad(), localgisMap.getMapid(), publicMap, LocalgisMapManager.GET_LEGEND_GRAPHICS_REQUESTS);
        String urlGetFeatureInfoRequests = localgisMapManager.getMapServerURLInternal(localgisMap.getMapidentidad(), localgisMap.getMapid(), publicMap, LocalgisMapManager.GET_FEATURE_INFO_REQUESTS);

        /*
         * Obtenemos el bbox de España 
         */
        
       //Conversion de coordenadas
        String srid=localgisMap.getSrid();
        localgisMap.setOriginalSrid(srid);
        try {
        	if (request.getParameter("srid")!=null){
        		srid=request.getParameter("srid");
        	}
        	else{        	
        		srid=Configuration.getPropertyString(Configuration.PROPERTY_DISPLAYPROJECTION);
        	}
			double[] transformedCoordinates=ChangeCoordinateSystem.transform(localgisMap.getSrid(),srid,new double[] {localgisMap.getMinx(),localgisMap.getMiny()});
			double[] transformedCoordinates1=ChangeCoordinateSystem.transform(localgisMap.getSrid(),srid,new double[] {localgisMap.getMaxx(),localgisMap.getMaxy()});

			localgisMap.setMinx(transformedCoordinates[0]);
			localgisMap.setMiny(transformedCoordinates[1]);
			localgisMap.setMaxx(transformedCoordinates1[0]);
			localgisMap.setMaxy(transformedCoordinates1[1]);
			
			localgisMap.setSrid(srid);
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
        
        BoundingBox boundingBoxSpain = localgisUtilsManager.getBoundingBoxSpain(srid);
        BoundingBox boundingBoxComunidad = localgisUtilsManager.getBoundingBoxComunidad(srid);
        //BoundingBox boundingBoxSpain = localgisUtilsManager.getBoundingBoxSpain("4326");

        /*
         * Juntamos las capas wms con las capas normales en un conjunto de
         * objetos LayerOpenlayers ordenado por posicion
         */
        String projection = "EPSG:"+srid;

        String urlApplicationServer = "http";
        if (request.isSecure()) {
            urlApplicationServer += "s";
        }
        urlApplicationServer += "://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"+configurationLocalgisWeb;
        String urlCustomLegend = urlApplicationServer + "/getLegend.do?idEntidad="+localgisMap.getMapidentidad();
        
        SortedSet layersOpenlayersSorted = WebUtils.joinAndSortLayers(null, localgisLayers, localgisWMSLayers, localgisMap, urlGetMapRequests, urlGetFeatureInfoRequests, urlGetLegendGraphicsRequests, projection, urlCustomLegend, localgisLayerManager, LocalgisLayerManager.SHOW_MAP_OPERATION, LayerOpenlayers.FORMAT_PNG);
       
        Set newSet = new TreeSet(new LayerComparator());  
        newSet.addAll(layersOpenlayersSorted);  
        
        ArrayList layersList = new ArrayList(newSet.size());
        layersList.addAll(newSet);
        
        /*
         * Creamos la capa de provincias (junto con la de municipios dependiendo de la configuracion)
         */
        Boolean showMunicipios = new Boolean(LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_MUNICIPIOS_VISIBLE));
        LayerOpenlayers provinciasLayer = new LayerOpenlayers();
        provinciasLayer.setIdLayer(new Integer(LayerOpenlayers.ID_LAYER_PROVINCIAS));
        provinciasLayer.setExternalLayer(Boolean.FALSE);
        provinciasLayer.setFormat(LayerOpenlayers.FORMAT_PNG);
        provinciasLayer.setInternalName(localgisLayerManager.getInternalNameLayerMunicipiosAndProvincias(localgisMap, showMunicipios, LocalgisLayerManager.SHOW_MAP_OPERATION));
        provinciasLayer.setWmcName(localgisLayerManager.getInternalNameLayerMunicipiosAndProvincias(localgisMap, showMunicipios, LocalgisLayerManager.SAVE_MAP_OPERATION));
        provinciasLayer.setName(LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_PROVINCIAS_NAME));
        provinciasLayer.setPosition(new Integer(0));
        provinciasLayer.setProjection(projection);
        provinciasLayer.setUrlGetLegendGraphicsRequests(null);
        provinciasLayer.setUrlGetMapRequests(urlGetMapRequests);
        provinciasLayer.setUrlGetFeatureInfoRequests(null);
        provinciasLayer.setVersion("1.1.1");
        /*
         * Metemos en la request todo lo necesario
         */
        
        request.setAttribute("localgisMap", localgisMap);
        request.setAttribute("layers", layersList);
        
        //Fijamos una serie de valores en sesion. Se utilizan por ejemplo en el Reporte
        //srid
        //identidad       
        try {
			request.getSession().setAttribute("srid",srid);
			request.getSession().setAttribute("idEntidad",localgisMap.getMapidentidad());
			request.getSession().setAttribute("idMap",formBean.getIdMap());
			
		} catch (Exception e) {
		}
        
        
        //Tipo de Visor
        String tipoVisor="Visor público";
        try {
			tipoVisor = (String)request.getSession().getAttribute("tipoVisor");	
			if (tipoVisor==null)
				tipoVisor="Visor público";
		} catch (Exception e) {
		}
        
        request.setAttribute("nombreMapa", tipoVisor+" "+geopistaEntidad.getNombreoficial() + " - " + localgisMap.getName());
        
        
        if (geopistaCoverageLayer != null) {
            request.setAttribute("ortofotoLayer", WebUtils.buildLayerOpenlayers(geopistaCoverageLayer, localgisMap, urlGetMapRequests, projection, LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_ORTOFOTO_NAME), localgisLayerManager.getInternalNameLayerOrtofoto(localgisMap, LocalgisLayerManager.SHOW_MAP_OPERATION), localgisLayerManager.getInternalNameLayerOrtofoto(localgisMap, LocalgisLayerManager.SAVE_MAP_OPERATION), LayerOpenlayers.FORMAT_JPEG));
        }
        request.setAttribute("provinciasLayer", provinciasLayer);
        if (customCSS != null) {
        	//Quitamos las dobles comillas porque da problemas al parsear los estilos
        	customCSS=customCSS.replaceAll("\"","");
            request.setAttribute("customCSS", customCSS);
        }
        
        if (formBean.getLanguage() != null) {
            request.setAttribute("language", formBean.getLanguage());
            request.getSession().setAttribute("locale",formBean.getLanguage());
        } else {
            request.setAttribute("language", localgisManagerBuilder.getDefaultLocale());
            request.getSession().setAttribute("locale",localgisManagerBuilder.getDefaultLocale());
        }
        
        
        
        // Para que busque las calles solo del municipio seleccionado:
        log.debug("ID ENTIDAD: "+geopistaEntidad.getIdEntidad());
        request.setAttribute("id_entidad",geopistaEntidad.getIdEntidad());
   
        request.setAttribute("entidad", geopistaEntidad.getNombreoficial());
        request.setAttribute("streetInfoService", LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_STREET_INFO_SERVICE));
        request.setAttribute("buffer", new Integer(LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_OPENLAYERS_BUFFER)));
        request.setAttribute("singleTile", new Boolean(LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_OPENLAYERS_SINGLE_TILE)));
        request.setAttribute("boundingBoxSpain", boundingBoxSpain);
        request.setAttribute("boundingBoxComunidad", boundingBoxComunidad);
        
        /*
         * Comprobamos si nos han pasado una x, una y y una escala para que situemos el mapa en esa posicion
         */
        if (formBean.getX() != null && formBean.getY() != null && formBean.getScale() != null) {
            request.setAttribute("x", formBean.getX());
            request.setAttribute("y", formBean.getY());
            // Obtenemos la escala normalizada, es decir, numerador entre denominador
            double scaleNormalized = (double) 1/formBean.getScale().doubleValue();
            // Obtenemos la resolucion a partir de la escala, las pulgadas por unidad (metros de momento) y los puntos por pulgada
            double resolution = 1.0 / (scaleNormalized * Configuration.INCHES_PER_UNIT * Configuration.DOTS_PER_INCH);
            request.setAttribute("resolution", new Double(resolution));
            // Se indica el marker usado
            if (formBean.getMarker() != null) {
            	request.setAttribute("marker", formBean.getMarker());
            }
        }
        
        List markers;
        if ((configurationLocalgisWeb.equals("public")) || (configurationLocalgisWeb.equals("incidencias"))) {
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
        
        /*
         * Creamos una capa que incluya las capas de provincias y todas las capas de geopista (es decir, las externas no) para el overview map 
         */
        LayerOpenlayers layerOverviewMap = new LayerOpenlayers();
        layerOverviewMap.setIdLayer(new Integer(-1));
        layerOverviewMap.setExternalLayer(Boolean.FALSE);
        layerOverviewMap.setFormat(LayerOpenlayers.FORMAT_PNG);
        layerOverviewMap.setInternalName(localgisLayerManager.getInternalNameLayerOverview(localgisMap, localgisLayers, showMunicipios, LocalgisLayerManager.SHOW_MAP_OPERATION));
        layerOverviewMap.setName("overview");
        layerOverviewMap.setPosition(new Integer(0));
        layerOverviewMap.setProjection(projection);
        layerOverviewMap.setUrlGetMapRequests(urlGetMapRequests);
        layerOverviewMap.setUrlGetFeatureInfoRequests(urlGetFeatureInfoRequests);
        layerOverviewMap.setUrlGetLegendGraphicsRequests(null);
        layerOverviewMap.setVersion("1.1.1");
        request.setAttribute("layerOverviewMap", layerOverviewMap);        
        String urlWpsRutas = Configuration.getPropertyString(Configuration.PROPERTY_URL_WPS_RUTAS);
        request.setAttribute("urlWpsRutas", urlWpsRutas);
   
        
        String escudoEntidad = "img/escudos/" + formBean.getIdEntidad() + ".png";
        String ruta = "/usr/local/LocalGIS/webapps/localgis-guiaurbana/" + escudoEntidad;
        //if (!new File(ruta).exists())
        //  escudoEntidad = "img/banner_left.gif";
        request.setAttribute("escudoEntidad", escudoEntidad);
        
        
        //Pasamos los atributos de gravedad de incidencia y tipo de incidencia
        try{
           String gravedadIncidencia=localgisMapManager.getDomain("MDL_Gravedad_Incidencia",localgisMap.getMapidentidad());
           String tipoIncidencia=localgisMapManager.getDomain("MDL_Tipo_Incidencia",localgisMap.getMapidentidad());
           if (gravedadIncidencia==null || gravedadIncidencia.equals("")){
        	   request.setAttribute("gravedadIncidencia", "m:Media;a:Alta;b:Baja");
           }
           else
        	   request.setAttribute("gravedadIncidencia", gravedadIncidencia);
           if (tipoIncidencia==null || tipoIncidencia.equals("")){
        	   request.setAttribute("tipoIncidencia", "o:Otros;mu:Mobiliario Urbano;fa:Farola;vp:Vía Pública");
           }
           else
        	   request.setAttribute("tipoIncidencia", tipoIncidencia);
	     
	       //request.setAttribute("gravedadIncidencia", "g0:G0;g1:G1;g2:G2;g3:G3;g4:G4");
	       //request.setAttribute("tipoIncidencia", "t1:T1;t2:T2;t3:T3");
        }
        catch (Throwable e){
        	request.setAttribute("gravedadIncidencia", "m:Media;a:Alta;b:Baja");
   	       	request.setAttribute("tipoIncidencia", "o:Otros;mu:Mobiliario Urbano;fa:Farola;vp:Vía Pública");
        	log.error("Exception"+e);
        }      

        
        return mapping.findForward(SUCCESS_PAGE);
        
        
    }

    public static void main(String args[]){
    	double coordinates[]={700612.27,4805253.96};
    	double[] coord2=ChangeCoordinateSystem.transform("23029","4230",coordinates);
    	System.out.println("Coordenadas X:"+coord2[0]);
    	System.out.println("Coordenadas Y:"+coord2[1]);
    }
}
