/**
 * MapTag.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import org.apache.log4j.Logger;

import com.localgis.web.core.ConstantesSQL;
import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.exceptions.LocalgisMapNotFoundException;
import com.localgis.web.core.exceptions.LocalgisNotAllowedOperationException;
import com.localgis.web.core.manager.LocalgisLayerManager;
import com.localgis.web.core.manager.LocalgisMapManager;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager;
import com.localgis.web.core.manager.LocalgisUtilsManager;
import com.localgis.web.core.model.BoundingBox;
import com.localgis.web.core.model.GeopistaCoverageLayer;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.openlayers.LayerOpenlayers;
import com.localgis.web.util.LocalgisManagerBuilderSingleton;
import com.localgis.web.util.WebUtils;

public class MapTag implements Tag {

    private static final Logger logger = Logger.getLogger(MapTag.class);

    private PageContext pageContext;
    
    private Tag parent;
    
    /*
     * Atributos que vienen en el tag
     */
    private Integer idMap;
    private Boolean publicMap;
    private String language;
    private Boolean showMunicipios;
    private String ortofotoName;
    private String provinciasName;
    private Integer buffer;
    private Boolean singleTile;
    private String idDivMap;
    private String idDivToolbar;
    private String idDivLayerSwitcher;
    private String activeLayer;
    private String getFeatureInfoProxy;
    private String getFeatureInfoCallback;
    

    
    /*
     * Atributos que no vienen en el tag
     */
    private LocalgisMap localgisMap;
    private LayerOpenlayers layerOverviewMap;
    private LayerOpenlayers ortofotoLayer;
    private LayerOpenlayers provinciasLayer;
    private List layersList;
    private BoundingBox boundingBoxSpain;
    
    public void setPageContext(PageContext pageContext) {
        this.pageContext = pageContext;
    }

    public void setParent(Tag parent) {
        this.parent = parent;
    }

    public Tag getParent() {
        return this.parent;
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    public int doStartTag() throws JspException {
        try {
            getInformationMap(idMap);
        } catch (Exception e) {
            logger.error("Error al recuperar la informacion necesaria del mapa", e);
            throw new JspException(e);
        }
        JspWriter out = pageContext.getOut();
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        try {
            out.println("<script type='text/javascript' src='"+request.getContextPath()+"/js/openlayers-2.5/lib/OpenLayers.js'></script>");
            out.println("<script type='text/javascript'>");
            out.println("var map;");
            out.println("");
            out.println("//No mostramos el componente");
            out.println("OpenLayers.Util.onImageLoadError = function() {");
            out.println("    this.style.display = 'none';");
            out.println("}"); 
            out.println("");
            out.println("function initOpenLayers() {");
             
            out.println("    var srid = "+localgisMap.getSrid()+";");
            out.println("    var projectionStr = 'EPSG:'+srid;");
            out.println("    var imgPath = OpenLayers._getScriptLocation() + 'theme/localgis/img';");
            out.println("    var themeStr = OpenLayers._getScriptLocation() + 'theme/localgis/style.css';");
            out.println("");
            out.println("    var minx = "+localgisMap.getMinx()+";");
            out.println("    var miny = "+localgisMap.getMiny()+";");
            out.println("    var maxx = "+localgisMap.getMaxx()+";");
            out.println("    var maxy = "+localgisMap.getMaxy()+";");
            out.println("    var extentEntidad = new OpenLayers.Bounds(minx, miny, maxx, maxy);");
            out.println("    var minScale = " + Configuration.MIN_SCALE_LAYERS + ";");
            out.println("    var numZoomLevels = "+ Configuration.ZOOM_LEVELS + ";");
            out.println("    var maxExtentSpain = new OpenLayers.Bounds("+boundingBoxSpain.getMinx()+", "+boundingBoxSpain.getMiny()+", "+boundingBoxSpain.getMaxx()+", "+boundingBoxSpain.getMaxy()+");");
            out.println("");
            out.println("OpenLayers.ProxyHost = \""+request.getContextPath()+getFeatureInfoProxy+"?language="+language+"&urlWFSServer=\";");
            out.println("");
            out.println("    // Creación y configuracion del mapa");
            out.println("    map = new OpenLayers.MapLocalgis( ");
            out.println("                            $('"+idDivMap+"') ,");
            out.println("                            {controls: [],");
            out.println("                             minScale: minScale,");
            out.println("                             maxExtent: maxExtentSpain, ");
            out.println("                             projection: projectionStr, ");
            out.println("                             maxResolution: 'auto', ");
            out.println("                             numZoomLevels: numZoomLevels, ");
            out.println("                             theme: themeStr, ");
            out.println("                             units: 'm', ");
            out.println("                             extentEntidad: extentEntidad});");
            out.println("");
            if (idDivLayerSwitcher != null) {
                out.println("    // Layer switcher del mapa");
                out.println("    var layerSwitcher = new OpenLayers.Control.LayerSwitcherLocalgis();");
                out.println("    map.addControlWithIdDiv(layerSwitcher, null, '"+idDivLayerSwitcher+"');");
            }
            out.println("");
            out.println("    // Toolbar de localgis");
            out.println("    var toolbar = new OpenLayers.Control.ToolbarLocalgisReduced({}, \""+getFeatureInfoCallback+"\");");
            out.println("    map.addControlWithIdDiv(toolbar, null, '"+idDivToolbar+"');");
            out.println("");
            out.println("    // Barra de escala");
            out.println("    var scaleBar = new OpenLayers.Control.ScaleBarLocalgis();");
            out.println("    scaleBar.displaySystem = 'metric';");
            out.println("    scaleBar.abbreviateLabel = true;");
            out.println("    map.addControl(scaleBar);");
            out.println("");
            out.println("    var layer_base = new OpenLayers.Layer.Image('BaseLayer', imgPath+'/blank.gif', maxExtentSpain, new OpenLayers.Size(1,1), null);");
            out.println("    map.addLayer(layer_base);");
            out.println("");
            if (ortofotoLayer != null) {
            out.println("    var layer_ortofoto_name = '"+ortofotoLayer.getName()+"';");
            out.println("    var layer_ortofoto_preName = '<img src=\"'+imgPath+'/ortofoto.gif\"/>&nbsp;';");
            out.println("    ");
            out.println("    var layer_ortofoto = new OpenLayers.Layer.WMSLocalgis(");
            out.println("                                 layer_ortofoto_name, ");
            out.println("                                 '"+ortofotoLayer.getUrlGetMapRequests()+"', ");
            out.println("                                 {layers: '"+ortofotoLayer.getInternalName()+"', ");
            out.println("                                  request: 'GetMap', ");
            out.println("                                  format: '"+ortofotoLayer.getFormat()+"', ");
            out.println("                                  transparent: false, ");
            out.println("                                  EXCEPTIONS: 'application/vnd.ogc.se_blank'}, ");
            out.println("                                 {gutter: 0, ");
            out.println("                                  buffer: "+buffer+", ");
            out.println("                                  isBaseLayer: false, ");
            out.println("                                  minScale: minScale,");
            out.println("                                  maxExtent: extentEntidad, ");
            out.println("                                  projection: projectionStr, ");
            out.println("                                  maxResolution: 'auto', ");
            out.println("                                  numZoomLevels: numZoomLevels, ");
            out.println("                                  urlLegend: '"+(ortofotoLayer.getUrlGetLegendGraphicsRequests() != null ? ortofotoLayer.getUrlGetLegendGraphicsRequests() : "") +"', ");
            out.println("                                  urlGetFeatureInfo: '"+(ortofotoLayer.getUrlGetFeatureInfoRequests() != null ? ortofotoLayer.getUrlGetFeatureInfoRequests() : "") +"', ");
            out.println("                                  preName: layer_ortofoto_preName,");
            out.println("                                  singleTile: "+singleTile+",");
            out.println("                                  idLayer: '"+ortofotoLayer.getIdLayer()+"'} );");
            out.println("");
            out.println("    map.addLayer(layer_ortofoto);");
            out.println("    layer_ortofoto.setVisibility(false);");
            }
            out.println("");
            out.println("    var layer_provincias_name = 'Provincias';");
            out.println("    var layer_provincias_preName = '<img src=\"'+imgPath+'/layerProvincias.gif\"/>&nbsp;';");
            out.println("    ");
            out.println("    var layer_provincias = new OpenLayers.Layer.WMSLocalgis(");
            out.println("                                   layer_provincias_name, ");
            out.println("                                   '"+provinciasLayer.getUrlGetMapRequests()+"', ");
            out.println("                                   {layers: '"+provinciasLayer.getInternalName()+"', ");
            out.println("                                   request: 'GetMap', ");
            out.println("                                   format: '"+provinciasLayer.getFormat()+"', ");
            out.println("                                   transparent: true, ");
            out.println("                                   EXCEPTIONS: 'application/vnd.ogc.se_blank'},");
            out.println("                                   {gutter: 0, ");
            out.println("                                   buffer: "+buffer+", ");
            out.println("                                   isBaseLayer: false, ");
            out.println("                                   urlLegend: '"+(provinciasLayer.getUrlGetLegendGraphicsRequests() != null ? provinciasLayer.getUrlGetLegendGraphicsRequests() : "") + "', ");
            out.println("                                   urlGetFeatureInfo: '"+(provinciasLayer.getUrlGetFeatureInfoRequests() != null ? provinciasLayer.getUrlGetFeatureInfoRequests() : "") + "', ");
            out.println("                                   preName: layer_provincias_preName,");
            out.println("                                   singleTile: "+singleTile+",");
            out.println("                                   idLayer: '"+provinciasLayer.getIdLayer()+"',");
            out.println("                                   scales: ["+Configuration.MIN_SCALE_LAYERS+", "+Configuration.MAX_SCALE_PROVINCIAS+"]} );");
            out.println("");
            out.println("    map.addLayer(layer_provincias);");
            out.println("    layer_provincias.setVisibility(true);");
            out.println("");
            out.println("    // Creacion y carga de capas dinamicas");
            Iterator itLayers = layersList.iterator();
            int layerIndex = 0;
            while (itLayers.hasNext()) {
                LayerOpenlayers layer = (LayerOpenlayers)itLayers.next();
                out.println("    var layer_"+layerIndex+"_name = '"+layer.getName()+"';");
                if (layer.getExternalLayer() != null && layer.getExternalLayer().booleanValue()) {
                    out.println("    var layer_"+layerIndex+"_preName = '<img src=\"'+imgPath+'/layerWMS.gif\"/>&nbsp;';");
                } else {
                    out.println("    var layer_"+layerIndex+"_preName = '';");
                }
                out.println("");
                out.println("    var layer_"+layerIndex+" = new OpenLayers.Layer.WMSLocalgis(");
                out.println("                                                        layer_"+layerIndex+"_name, ");
                out.println("                                                        '"+layer.getUrlGetMapRequests()+"', ");
                out.println("                                                        {layers: '"+layer.getInternalName()+"', ");
                out.println("                                                         request: 'GetMap', ");
                out.println("                                                         format: '"+layer.getFormat()+"', ");
                out.println("                                                         transparent:true, ");
                out.println("                                                         EXCEPTIONS: 'application/vnd.ogc.se_blank'},");
                out.println("                                                        {gutter: 0,");
                out.println("                                                         buffer: "+buffer+", ");
                out.println("                                                         minScale: minScale,");
                out.println("                                                         maxExtent: extentEntidad, ");
                out.println("                                                         projection: projectionStr, ");
                out.println("                                                         maxResolution: 'auto', ");
                out.println("                                                         isBaseLayer: false, ");
                out.println("                                                         urlLegend: '"+(layer.getUrlGetLegendGraphicsRequests() != null ? layer.getUrlGetLegendGraphicsRequests() : "") +"', ");
                out.println("                                                         urlGetFeatureInfo: '"+(layer.getUrlGetFeatureInfoRequests() != null ? layer.getUrlGetFeatureInfoRequests() : "") +"', ");
                out.println("                                                         preName: layer_"+layerIndex+"_preName,");
                out.println("                                                         singleTile: "+singleTile+",");
                out.println("                                                         projection: '"+layer.getProjection()+"',");
                out.println("                                                         isExternal: "+layer.getExternalLayer()+",");
                out.println("                                                         idLayer: '"+layer.getIdLayer()+"'");
                out.println("                                                         } );");
                out.println("             ");
                out.println("    layer_"+layerIndex+".setOpacity(0.85);");
                out.println("    map.addLayer(layer_"+layerIndex+");");
                out.println("    layer_"+layerIndex+".setVisibility(true);");
                if (layer.getWmcName().equals(activeLayer)) {
                    out.println("map.activeLayer = layer_"+layerIndex+";");
                }
                layerIndex++;
            }
            out.println("    var markersAuxiliar = new OpenLayers.Layer.Markers(\"Markers\",{layers: 'Markers'});");
            out.println("    markersAuxiliar.displayInLayerSwitcher = false;");
            out.println("    map.addMarkersAuxiliarLayer(markersAuxiliar);");
            out.println("");
            if (idDivLayerSwitcher != null) {
                out.println("    //Maximizamos layerSwitcher");
                out.println("    layerSwitcher.maximizeControl();");
            }
            out.println("");
            out.println("// Visualizamos la entidad completa");
            out.println("map.zoomToExtent(extentEntidad);");
            out.println("    ");
            out.println("");
            out.println("    var layer_overview_base = new OpenLayers.Layer.Image(");
            out.println("                            'OverviewBaseLayer', ");
            out.println("                            imgPath+'/blank.gif', ");
            out.println("                            maxExtentSpain, ");
            out.println("                            new OpenLayers.Size(1,1), ");
            out.println("                            {projection: projectionStr, ");
            out.println("                             maxExtent: maxExtentSpain, ");
            out.println("                             maxResolution: 'auto', ");
            out.println("                             numZoomLevels: numZoomLevels, ");
            out.println("                             minScale: "+Configuration.MIN_SCALE_PROVINCIAS+", ");
            out.println("                             units: 'm'});");
            out.println("");
            out.println("    var layer_overview = new OpenLayers.Layer.WMSLocalgis(");
            out.println("                                   'Overview', ");
            out.println("                                   '"+layerOverviewMap.getUrlGetMapRequests()+"', ");
            out.println("                                   {layers: '"+layerOverviewMap.getInternalName()+"', ");
            out.println("                                    request: 'GetMap', ");
            out.println("                                    format: '"+layerOverviewMap.getFormat()+"', ");
            out.println("                                    transparent:true, ");
            out.println("                                    EXCEPTIONS: 'application/vnd.ogc.se_blank'},");
            out.println("                                   {buffer: 0, ");
            out.println("                                    isBaseLayer: false, ");
            out.println("                                    urlLegend: '"+(layerOverviewMap.getUrlGetLegendGraphicsRequests() != null ? layerOverviewMap.getUrlGetLegendGraphicsRequests() : "") +"', ");
            out.println("                                    urlGetFeatureInfo: '"+(layerOverviewMap.getUrlGetFeatureInfoRequests() != null ? layerOverviewMap.getUrlGetFeatureInfoRequests() : "") +"', ");
            out.println("                                    preName: layer_provincias_preName,");
            out.println("                                    singleTile: "+singleTile+",");
            out.println("                                    maxExtent: maxExtentSpain, ");
            out.println("                                    projection: projectionStr, ");
            out.println("                                    maxResolution: 'auto', ");
            out.println("                                    numZoomLevels: numZoomLevels, ");
            out.println("                                    minScale: "+Configuration.MIN_SCALE_PROVINCIAS+",");
            out.println("                                    theme: themeStr, ");
            out.println("                                    units: 'm'} );");
            out.println("    var layersToOverview = [layer_overview_base, layer_overview];");
            out.println("    ");
            out.println("    // Posicion del mouse");
            out.println("    var mousePosition = new OpenLayers.Control.MousePosition();");
            out.println("    map.addControl(mousePosition);");
            out.println("");
            out.println("    // Overview map");
            out.println("    var overviewMap = new OpenLayers.Control.OverviewMapLocalgis({layers: layersToOverview});");
            out.println("    overviewMap.size = new OpenLayers.Size(180,133);");
            out.println("    map.addControl(overviewMap);");
            out.println("");
            out.println("}");
            out.println("</script>");

            out.println("<script type='text/javascript'>");
            out.println("    initOpenLayers();");
            out.println("</script>");
        
        } catch (IOException e) {
            logger.error("Error de I/O al evaluar el tag", e);
            throw new JspException(e);
        }
        return EVAL_BODY_INCLUDE;
    }

    public void release() {
        // No necesitamos liberar nada
    }

    /**
     * Establece el valor del campo idMap
     * @param idMap El campo idMap a establecer
     */
    public void setIdMap(Integer idMap) {
        this.idMap = idMap;
    }

    /**
     * Establece el valor del campo publicMap
     * @param publicMap El campo publicMap a establecer
     */
    public void setPublicMap(Boolean publicMap) {
        this.publicMap = publicMap;
    }

    /**
     * Establece el valor del campo language
     * @param language El campo language a establecer
     */
    public void setLanguage(String language) {
        this.language = language;
    }
    
    /**
     * Establece el valor del campo buffer
     * @param buffer El campo buffer a establecer
     */
    public void setBuffer(Integer buffer) {
        this.buffer = buffer;
    }

    /**
     * Establece el valor del campo singleTile
     * @param singleTile El campo singleTile a establecer
     */
    public void setSingleTile(Boolean singleTile) {
        this.singleTile = singleTile;
    }

    /**
     * Establece el valor del campo idDivMap
     * @param idDivMap El campo idDivMap a establecer
     */
    public void setIdDivMap(String idDivMap) {
        this.idDivMap = idDivMap;
    }

    /**
     * Establece el valor del campo idDivToolbar
     * @param idDivToolbar El campo idDivToolbar a establecer
     */
    public void setIdDivToolbar(String idDivToolbar) {
        this.idDivToolbar = idDivToolbar;
    }

    /**
     * Establece el valor del campo showMunicipios
     * @param showMunicipios El campo showMunicipios a establecer
     */
    public void setShowMunicipios(Boolean showMunicipios) {
        this.showMunicipios = showMunicipios;
    }

    /**
     * Establece el valor del campo ortofotoName
     * @param ortofotoName El campo ortofotoName a establecer
     */
    public void setOrtofotoName(String ortofotoName) {
        this.ortofotoName = ortofotoName;
    }

    /**
     * Establece el valor del campo provinciasName
     * @param provinciasName El campo provinciasName a establecer
     */
    public void setProvinciasName(String provinciasName) {
        this.provinciasName = provinciasName;
    }

    /**
     * Establece el valor del campo idDivLayerSwitcher
     * @param idDivLayerSwitcher El campo idDivLayerSwitcher a establecer
     */
    public void setIdDivLayerSwitcher(String idDivLayerSwitcher) {
        this.idDivLayerSwitcher = idDivLayerSwitcher;
    }

    /**
     * Establece el valor del campo activeLayer
     * @param activeLayer El campo activeLayer a establecer
     */
    public void setActiveLayer(String activeLayer) {
        this.activeLayer = activeLayer;
    }

    
    /**
     * Establece el valor del campo getFeatureInfoProxy
     * @param getFeatureInfoProxy El campo getFeatureInfoProxy a establecer
     */
    public void setGetFeatureInfoProxy(String getFeatureInfoProxy) {
        this.getFeatureInfoProxy = getFeatureInfoProxy;
    }

    /**
     * Establece el valor del campo getFeatureInfoCallback
     * @param getFeatureInfoCallback El campo getFeatureInfoCallback a establecer
     */
    public void setGetFeatureInfoCallback(String getFeatureInfoCallback) {
        this.getFeatureInfoCallback = getFeatureInfoCallback;
    }

    /**
     * 
     * @param idMap
     * @throws LocalgisMapNotFoundException
     * @throws LocalgisNotAllowedOperationException
     * @throws LocalgisConfigurationException
     * @throws LocalgisInitiationException
     * @throws LocalgisDBException
     * @throws LocalgisInvalidParameterException
     */
    private void getInformationMap(Integer idMap) throws LocalgisMapNotFoundException, LocalgisNotAllowedOperationException, LocalgisConfigurationException, LocalgisInitiationException, LocalgisDBException, LocalgisInvalidParameterException{
        /*
         * Obtenemos los managers de localgis
         */
        LocalgisManagerBuilder localgisManagerBuilder = LocalgisManagerBuilderSingleton.getInstance();
        LocalgisEntidadSupramunicipalManager localgisEntidadSupramunicipalManager = localgisManagerBuilder.getLocalgisEntidadSupramunicipalManager();
        LocalgisLayerManager localgisLayerManager = localgisManagerBuilder.getLocalgisLayerManager();
        LocalgisMapManager localgisMapManager = localgisManagerBuilder.getLocalgisMapManager();
        LocalgisUtilsManager localgisUtilsManager = localgisManagerBuilder.getLocalgisUtilsManager();
        
        localgisMap = localgisMapManager.getPublishedMap(idMap);

        /*
         * Comprobacion de que el mapa exista
         */
        if (localgisMap == null) {
            logger.error("El mapa con id \""+idMap+"\" no existe");
            throw new LocalgisMapNotFoundException("El mapa con id \""+idMap+"\" no existe");
        }

        /*
         * Comprobación de que el mapa sea publico o privado dependiendo de lo indicado en el atributo publicMap
         */
        if ((publicMap.booleanValue() && localgisMap.getMappublic().equals(ConstantesSQL.FALSE)) ||
                (!publicMap.booleanValue() && localgisMap.getMappublic().equals(ConstantesSQL.TRUE))) {
            logger.error("No se dispone de permisos para ver el mapa con id \""+idMap+"\"");
            throw new LocalgisNotAllowedOperationException("No se dispone de permisos para ver el mapa con id \""+idMap+"\"");
        }

        List localgisLayers = localgisMapManager.getMapLayers(localgisMap.getMapid(), language);
        List localgisWMSLayers = localgisMapManager.getMapWMSLayers(localgisMap.getMapid());
        GeopistaCoverageLayer geopistaCoverageLayer = localgisEntidadSupramunicipalManager.getCoverageLayer(localgisMap.getMapidentidad()); 
        
        /*
         * URLs necesarias
         */
        String urlGetMapRequests = localgisMapManager.getMapServerURL(localgisMap.getMapidentidad(), localgisMap.getMapid(), publicMap, LocalgisMapManager.GET_MAP_REQUESTS);
        String urlGetLegendGraphicsRequests = localgisMapManager.getMapServerURL(localgisMap.getMapidentidad(), localgisMap.getMapid(), publicMap, LocalgisMapManager.GET_LEGEND_GRAPHICS_REQUESTS);
        String urlGetFeatureInfoRequests = localgisMapManager.getMapServerURL(localgisMap.getMapidentidad(), localgisMap.getMapid(), publicMap, LocalgisMapManager.GET_FEATURE_INFO_REQUESTS);

        /*
         * Obtenemos el bbox de España 
         */
        boundingBoxSpain = localgisUtilsManager.getBoundingBoxSpain(localgisMap.getSrid());

        /*
         * Juntamos las capas wms con las capas normales en un conjunto de
         * objetos LayerOpenlayers ordenado por posicion
         */
        String projection = "EPSG:"+localgisMap.getSrid();
        
        SortedSet layersOpenlayersSorted = WebUtils.joinAndSortLayers(null, localgisLayers, localgisWMSLayers, localgisMap, urlGetMapRequests, urlGetFeatureInfoRequests, urlGetLegendGraphicsRequests, projection, null, localgisLayerManager, LocalgisLayerManager.SHOW_MAP_OPERATION, LayerOpenlayers.FORMAT_PNG);
        layersList = new ArrayList(layersOpenlayersSorted.size());
        layersList.addAll(layersOpenlayersSorted);
        
        /*
         * Creamos la capa de provincias (junto con la de municipios dependiendo de la configuracion)
         */
        provinciasLayer = new LayerOpenlayers();
        provinciasLayer.setIdLayer(new Integer(LayerOpenlayers.ID_LAYER_PROVINCIAS));
        provinciasLayer.setExternalLayer(Boolean.FALSE);
        provinciasLayer.setFormat(LayerOpenlayers.FORMAT_PNG);
        provinciasLayer.setInternalName(localgisLayerManager.getInternalNameLayerMunicipiosAndProvincias(localgisMap, showMunicipios, LocalgisLayerManager.SHOW_MAP_OPERATION));
        provinciasLayer.setWmcName(localgisLayerManager.getInternalNameLayerMunicipiosAndProvincias(localgisMap, showMunicipios, LocalgisLayerManager.SAVE_MAP_OPERATION));
        provinciasLayer.setName(provinciasName);
        provinciasLayer.setPosition(new Integer(0));
        provinciasLayer.setProjection(projection);
        provinciasLayer.setUrlGetLegendGraphicsRequests(null);
        provinciasLayer.setUrlGetMapRequests(urlGetMapRequests);
        provinciasLayer.setUrlGetFeatureInfoRequests(null);
        provinciasLayer.setVersion("1.1.1");
        /*
         * Metemos en la request todo lo necesario
         */
        if (geopistaCoverageLayer != null) {
            ortofotoLayer = WebUtils.buildLayerOpenlayers(geopistaCoverageLayer, localgisMap, urlGetMapRequests, projection, ortofotoName, localgisLayerManager.getInternalNameLayerOrtofoto(localgisMap, LocalgisLayerManager.SHOW_MAP_OPERATION), localgisLayerManager.getInternalNameLayerOrtofoto(localgisMap, LocalgisLayerManager.SAVE_MAP_OPERATION), LayerOpenlayers.FORMAT_JPEG);
            ortofotoLayer.setUrlGetFeatureInfoRequests(null);
            ortofotoLayer.setUrlGetLegendGraphicsRequests(null);
        }
       
        /*
         * Creamos una capa que incluya las capas de provincias y todas las capas de geopista (es decir, las externas no) para el overview map 
         */
        layerOverviewMap = new LayerOpenlayers();
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

    }

}
