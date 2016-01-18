/**
 * SaveMapContextAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.actions;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.localgis.web.actionsforms.SaveMapContextActionForm;
import com.localgis.web.config.LocalgisWebConfiguration;
import com.localgis.web.core.ConstantesSQL;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager;
import com.localgis.web.core.manager.LocalgisLayerManager;
import com.localgis.web.core.manager.LocalgisMapManager;
import com.localgis.web.core.model.GeopistaCoverageLayer;
import com.localgis.web.core.model.GeopistaEntidadSupramunicipal;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.openlayers.LayerOpenlayers;
import com.localgis.web.util.LocalgisManagerBuilderSingleton;
import com.localgis.web.util.WebUtils;

public class SaveMapContextAction extends Action {
	
	private static Log log = LogFactory.getLog(SaveMapContextAction.class);

    private static String ERROR_PAGE = "error";
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        SaveMapContextActionForm formBean = (SaveMapContextActionForm)form;

        /*
         * Obtenemos el manager de localgis
         */        
        LocalgisMapManager localgisMapManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisMapManager();        
        LocalgisEntidadSupramunicipalManager localgisMunicipioManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisEntidadSupramunicipalManager();
        LocalgisLayerManager localgisLayerManager = LocalgisManagerBuilderSingleton.getInstance().getLocalgisLayerManager();
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
                    (configurationLocalgisWeb.equals("private") && localgisMap.getMappublic().equals(ConstantesSQL.TRUE)) ||
                    (configurationLocalgisWeb.equals("incidencias") && localgisMap.getMappublic().equals(ConstantesSQL.FALSE))) {
                log.error("No se dispone de permisos para ver el mapa");
                request.setAttribute("errorMessageKey", "printMap.error.mapNotAllowed");
                return mapping.findForward(ERROR_PAGE);
            }
        }        
        
        // Obtenemos la lista de capas
        ArrayList layersList = getLayersList(request, formBean, localgisMapManager, localgisMap, localgisLayerManager, localgisMunicipioManager);
        
        // Obtenemos el documento JDOM del Web Map Context
        GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal = localgisMunicipioManager.getEntidadSupramunicipal(localgisMap.getMapidentidad());        
        String nombreMuncipio = geopistaEntidadSupramunicipal.getNombreoficial();        
        Document wmcDocument = generateWebMapContextDocument(formBean, localgisMap, layersList, nombreMuncipio, localgisLayerManager);
        
        // Obtenemos el XML de salida
        Format format = Format.getPrettyFormat();        
        XMLOutputter xmlOutputter = new XMLOutputter(format);
        ByteArrayOutputStream wmcXmlOutputStream = new ByteArrayOutputStream();        
        xmlOutputter.output(wmcDocument, wmcXmlOutputStream);
        byte[] wmcXmlBytes = wmcXmlOutputStream.toByteArray();
                
        response.setContentLength(wmcXmlOutputStream.size());
        response.setContentType("application/x-file-download");
        String filename = localgisMap.getName() + ".xml";
        filename = filename.replace(" ", "_");
        response.setHeader("Content-disposition", "attachment; filename=" + filename);
//        response.setHeader("Cache-Control",
//                 "max-age=" + TIMEOUT);
        
        ServletOutputStream outStream = response.getOutputStream();

        outStream.write(wmcXmlBytes);
        outStream.flush();
        
        return null;
    }
    
    private ArrayList getLayersList(HttpServletRequest request, SaveMapContextActionForm formBean, LocalgisMapManager localgisMapManager, LocalgisMap localgisMap, LocalgisLayerManager localgisLayerManager, LocalgisEntidadSupramunicipalManager localgisMunicipioManager) throws Exception{
    	String configurationLocalgisWeb = (String)request.getAttribute("configurationLocalgisWeb");
    	
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
        
        String urlCustomLegend = "http";
        if (request.isSecure()) {
            urlCustomLegend += "s";
        }
        urlCustomLegend += "://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"+configurationLocalgisWeb+"/getLegend.do?idEntidad="+localgisMap.getMapidentidad();
        
        // Obtenemos la lista de capas
        List wmsLayers = localgisMapManager.getMapWMSLayers(formBean.getIdMap());
        List layers = localgisMapManager.getMapLayers(formBean.getIdMap());
        
        String[] layersIds = formBean.getLayersIds();
        ArrayList layersIncluded = new ArrayList();
        for (int i = 0; i < layersIds.length; i++) {
            layersIncluded.add(new Integer(layersIds[i]));
        }
        
        String projection = "EPSG:" + localgisMap.getSrid();
        SortedSet layersOpenlayersSorted = WebUtils.joinAndSortLayers(layersIncluded, layers, wmsLayers, localgisMap, urlAllRequests, urlGetFeatureInfoRequests, urlGetLegendGraphicsRequests, projection, urlCustomLegend, localgisLayerManager, LocalgisLayerManager.SAVE_MAP_OPERATION, LayerOpenlayers.FORMAT_PNG);
        ArrayList layersList = new ArrayList();
        layersList.addAll(layersOpenlayersSorted);

        // A las capas de geopista (wms y normales) añadimos la ortofoto y la de provincias, siempre que se seleccionaran
        /*
         * Creacion de la capa de provincias (junto con la de municipios dependiendo de la configuracion) si estaba seleccionada
         */
        if (layersIncluded.contains(new Integer(LayerOpenlayers.ID_LAYER_PROVINCIAS))) {
            String internalNameProvincias = localgisLayerManager.getInternalNameLayerMunicipiosAndProvincias(localgisMap, new Boolean(LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_MUNICIPIOS_VISIBLE)), LocalgisLayerManager.SAVE_MAP_OPERATION);
            LayerOpenlayers provinciasLayer = new LayerOpenlayers();
            provinciasLayer.setIdLayer(new Integer(-1));
            provinciasLayer.setExternalLayer(Boolean.FALSE);
            provinciasLayer.setFormat("image/png");
            provinciasLayer.setInternalName(internalNameProvincias);
            provinciasLayer.setWmcName(internalNameProvincias);
            provinciasLayer.setName(LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_PROVINCIAS_NAME));
            provinciasLayer.setPosition(new Integer(0));
            provinciasLayer.setProjection(projection);
            provinciasLayer.setUrlGetLegendGraphicsRequests(null);
            provinciasLayer.setUrlGetMapRequests(urlAllRequests);
            provinciasLayer.setUrlGetFeatureInfoRequests(null);
            provinciasLayer.setVersion("1.1.1");
            layersList.add(0, provinciasLayer);
        }
        /*
         * La primera capa es la de la ortofoto (si la hay y si se selecciono)
         */
        GeopistaCoverageLayer geopistaCoverageLayer = null;
        if (layersIncluded.contains(new Integer(LayerOpenlayers.ID_LAYER_ORTOFOTO))) {
            geopistaCoverageLayer = localgisMunicipioManager.getCoverageLayer(localgisMap.getMapidentidad()); 
            if (geopistaCoverageLayer != null) {
                LayerOpenlayers ortofotoOpenlayers = WebUtils.buildLayerOpenlayers(geopistaCoverageLayer, localgisMap, urlAllRequests, projection, LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_ORTOFOTO_NAME), localgisLayerManager.getInternalNameLayerOrtofoto(localgisMap, LocalgisLayerManager.SAVE_MAP_OPERATION), localgisLayerManager.getInternalNameLayerOrtofoto(localgisMap, LocalgisLayerManager.SAVE_MAP_OPERATION), LayerOpenlayers.FORMAT_JPEG);
                layersList.add(0, ortofotoOpenlayers);
            }
        }

        
        return layersList;
    }
    
    private Document generateWebMapContextDocument(SaveMapContextActionForm formBean,
    		LocalgisMap localgisMap, ArrayList layersList, String nombreMunicipio, LocalgisLayerManager localgisLayerManager) throws LocalgisConfigurationException {       
        // Componemos el XML de respuesta del servidor
        Document wmcDocument = new Document();
        Namespace contextNamespace = Namespace.getNamespace("http://www.opengeospatial.net/context");
        Namespace xlinkNamespace = Namespace.getNamespace("xlink", "http://www.w3.org/1999/xlink");
        Namespace xsiNamespace = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance"); 
        
        // ViewContext Element
        Element viewContext = new Element("ViewContext");
        wmcDocument.setRootElement(viewContext);
        viewContext.setAttribute("version", "1.1.0");        
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(new Date());        
        viewContext.setAttribute("id", nombreMunicipio + "_" + localgisMap.getName()
        		+ "_" + gc.get(Calendar.YEAR) + gc.get(Calendar.MONTH) + gc.get(Calendar.DAY_OF_MONTH));
        viewContext.setNamespace(contextNamespace);
        viewContext.addNamespaceDeclaration(xlinkNamespace);
        viewContext.addNamespaceDeclaration(xsiNamespace);
        viewContext.setAttribute("schemaLocation", "http://schemas.opengis.net/context/1.1.0/context.xsd", xsiNamespace);
        
        // General Element
        Element general = new Element("General", contextNamespace);
        viewContext.addContent(general);
        
        // General -> Window (Es opcional)
        Element window = new Element("Window", contextNamespace);
        general.addContent(window);
        window.setAttribute("width", formBean.getWindowWidth());
        window.setAttribute("height", formBean.getWindowHeight());
        
        // General -> BoundingBox
        Element boundingBox = new Element("BoundingBox", contextNamespace);
        general.addContent(boundingBox);        
        boundingBox.setAttribute("SRS", "EPSG:" + localgisMap.getSrid());
        boundingBox.setAttribute("minx", formBean.getMinx()); 
        boundingBox.setAttribute("miny", formBean.getMiny());
        boundingBox.setAttribute("maxx", formBean.getMaxx());
        boundingBox.setAttribute("maxy", formBean.getMaxy());
        
        // General -> Title
        Element title = new Element("Title", contextNamespace);
        general.addContent(title);
        title.setText(nombreMunicipio + " - " + localgisMap.getName());
        
        // General -> KeyWordList (Es opcional)
        // TODO: Preguntar al usuario por una descripcion en el cliente?
        
        // General -> Abstract (Es opcional)
        // TODO: Preguntar al usuario por una descripcion en el cliente?
        
        // General -> Contact Information (Es opcional)
        // TODO: Preguntar si hay que solicitar al usuario la información de contacto
        
        // LayerList
        Element layerList = new Element("LayerList", contextNamespace);
        viewContext.addContent(layerList);
        
        // LayerList -> Layers
        for (int i = 0; i < layersList.size(); i++){
        	LayerOpenlayers currentLayer = (LayerOpenlayers) layersList.get(i);
        	
        	// LayersList -> Layer
        	Element layer = new Element("Layer", contextNamespace);
        	layerList.addContent(layer);
        	if (formBean.getLayersVisibility()[i]){
        		layer.setAttribute("hidden", "0");
        	}
        	else {
        		layer.setAttribute("hidden", "1");
        	}        	
        	if (currentLayer.getInternalName().equals(localgisLayerManager.getInternalNameLayerOrtofoto(localgisMap, LocalgisLayerManager.SAVE_MAP_OPERATION))){
        		layer.setAttribute("queryable", "0");
        	}
        	else {
        		layer.setAttribute("queryable", "1");
        	}
        	        	
        	// Layer -> Server
        	Element server = new Element("Server", contextNamespace);
        	layer.addContent(server);
        	server.setAttribute("service", "OGC:WMS");
        	server.setAttribute("version", currentLayer.getVersion() );
        	
        	// Server -> OnlineResource
        	Element onlineResource = new Element("OnlineResource", contextNamespace);
        	server.addContent(onlineResource);
        	onlineResource.setAttribute("type", "simple", xlinkNamespace);
        	onlineResource.setAttribute("href", currentLayer.getUrlGetMapRequests(), xlinkNamespace);
        	
        	// Layer -> Name
        	Element name = new Element("Name", contextNamespace);
        	layer.addContent(name);
        	name.setText(currentLayer.getWmcName());
        	
        	// Layer -> Title
        	Element layerTitle = new Element("Title", contextNamespace);
        	layer.addContent(layerTitle);
        	layerTitle.setText(currentLayer.getName());
       	
        	// Layer -> SRS
        	Element layerSRS = new Element("SRS", contextNamespace);
        	layer.addContent(layerSRS);
        	layerSRS.setText(currentLayer.getProjection());
        	
        	// Layer -> FormatList
        	Element formatList = new Element("FormatList", contextNamespace);
        	layer.addContent(formatList);
        	
        	// FormatList -> Format        	
        	Element format = new Element("Format", contextNamespace);
        	formatList.addContent(format);
        	format.setAttribute("current", "1");
        	format.setText(currentLayer.getFormat());
        	
            // Si la capa no tiene leyenda no metemos el estilo
            if (currentLayer.getUrlGetLegendGraphicsRequests() != null) { 
                // Layer -> StyleList
                Element styleList = new Element("StyleList", contextNamespace);
                layer.addContent(styleList);
        	
                // StyleList -> Styles        	
                Element style = new Element("Style", contextNamespace);
                styleList.addContent(style);
                Element styleName = new Element("Name", contextNamespace);
                style.addContent(styleName);
                // Map Server solo admite default como nombre de estilo
                styleName.setText("default");
                Element styleTitle = new Element("Title", contextNamespace);
                style.addContent(styleTitle);
                styleTitle.setText(currentLayer.getName());
        	
                // Style -> LegendURL
                Element styleLegendUrl = new Element("LegendURL", contextNamespace);
                style.addContent(styleLegendUrl);
                Element styleOnlineResource = new Element("OnlineResource", contextNamespace);
                styleLegendUrl.addContent(styleOnlineResource);
                styleOnlineResource.setAttribute("type", "simple", xlinkNamespace);
                styleOnlineResource.setAttribute("href", currentLayer.getUrlGetLegendGraphicsRequests(), xlinkNamespace);
        	}        	
        } //Fin for recorrido de capas
        
        return wmcDocument;
    }
    
}
