/**
 * ShowFeatureMapAction.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.localgis.web.gwfst.actions;

import ieci.tecdoc.sgm.catalogo.ws.server.Tramite;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.localgis.ln.ObtenerEntidadMunicipios;
import com.localgis.model.ot.EntidadOT;
import com.localgis.model.ot.MunicipioOT;
import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.LocalgisManagerBuilderGeoWfst;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisInitiationException;
import com.localgis.web.core.gwfst.util.LocalgisManagerBuilderSingletonFeature;
import com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager;
import com.localgis.web.core.manager.LocalgisGeoFeatureManager;
import com.localgis.web.core.manager.LocalgisLayerManager;
import com.localgis.web.core.manager.LocalgisMapManager;
import com.localgis.web.core.manager.LocalgisMapsConfigurationManager;
import com.localgis.web.core.manager.LocalgisUtilsManager;
import com.localgis.web.core.model.BoundingBox;
import com.localgis.web.core.model.GeopistaCoverageLayer;
import com.localgis.web.core.model.GeopistaEntidadSupramunicipal;
import com.localgis.web.core.model.GeopistaMunicipio;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.core.model.ProcedureDefaults;
import com.localgis.web.core.wm.util.LocalgisManagerBuilderSingleton;
import com.localgis.web.gwfst.actionsforms.ShowFeatureMapActionForm;
import com.localgis.web.gwfst.config.LocalgisWebConfiguration;
import com.localgis.web.gwfst.config.ShowFeatureMapConfiguration;
import com.localgis.web.gwfst.ws.geoserver.GeoserverWS;
import com.localgis.web.gwfst.ws.soalocalgis.SOALocalGISWS;
import com.localgis.web.gwfst.filters.LoginFilterFeatures;
import com.localgis.web.gwfst.openlayers.LayerOpenlayers;
import com.localgis.web.gwfst.util.GwsftPublish;
import com.localgis.web.gwfst.util.WebUtils;

/**
 * @author david.caaveiro
 * @company SATEC
 * @date 10-05-2012
 * @version 1.0
 * @ClassComments Acción de recuperación del mapa y de los atributos necesarias para el visor
 */
public class ShowFeatureMapAction extends Action {

    /**
     * Logger
     */
    private static Log log = LogFactory.getLog(ShowFeatureMapAction.class);
	
	/**
	 * Constantes
	 */  
    private static final String SUCCESS_PAGE = "success";
    private static final String ERROR_PAGE = "errorFeature"; 
    private static final String SRID_4230 = "4230";
    //private static final String SRID_WGS84 = "4326";
    //private static final String SRID_WGS84_GOOGLE_MERCATOR = "900913";
          
    /**
     * Variables
     */
    private GeoserverWS geoserverWS = null; 
    
    /**
	 * Recupera el mapa y los atributos necesarias para el visor
	 * @param mapping: Mapeo de la acción
	 * @param form: Formulario asociado a la acción
	 * @param request: Objeto petición de la acción
	 * @param response: Objeto respuesta de la acción
	 * @throws Exception
	 * @return ActionForward: Devuelve la siguiente acción
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	//-----------------NUEVO-------------    
    	ShowFeatureMapActionForm formBean = (ShowFeatureMapActionForm)form;  
        //----------------FIN-NUEVO-------------

    	try{	    	
	        LocalgisManagerBuilder localgisManagerBuilder = LocalgisManagerBuilderSingleton.getInstance();        
	        LocalgisEntidadSupramunicipalManager localgisEntidadSupramunicipalManager = localgisManagerBuilder.getLocalgisEntidadSupramunicipalManager();
	        LocalgisLayerManager localgisLayerManager = localgisManagerBuilder.getLocalgisLayerManager();
	        LocalgisMapManager localgisMapManager = localgisManagerBuilder.getLocalgisMapManager();
	        LocalgisMapsConfigurationManager localgisMapsConfigurationManager = localgisManagerBuilder.getLocalgisMapsConfigurationManager();
	        LocalgisUtilsManager localgisUtilsManager = localgisManagerBuilder.getLocalgisUtilsManager();
	       
	        //-----------------NUEVO------------->  
	        LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
	        LocalgisGeoFeatureManager localgisGeoFeatureManager = localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager();      
	        Integer idEntidad = localgisGeoFeatureManager.getIdEntidadLocalgis(formBean.getIdEntidad()); 
	        
	        
	        //List<Tramite> tramites = SigemCatalogoTramitesWS.getAllTramitesByEntidad("http://sedelectronica.aytonavacerrada.org:8080/", "000");
	        
	       // List<Tramite> tramites = SigemCatalogoTramitesWS.getAllTramitesByEntidad("http://sedelectronica.aytonavacerrada.org:8080/", "000");
	        
	        
	        //List<Tramite> tramites = SigemCatalogoTramitesWS.getTramites("http://sedelectronica.aytonavacerrada.org:8080/", "000");
//	        EntidadOT [] entidades;
//	        try{
//	    	   entidades = SOALocalGISWS.getEntidadMunicipios("http://localhost:8080/",33001);
//	        }
//	        catch(Exception ex){
//	        	System.out.println(ex);
//	        }
	       
	        if(idEntidad!=null){
	        	String layerName = localgisGeoFeatureManager.getProceduresByProcedureType(formBean.getProcedureName()).getLayerName();
	        	String procedureId = localgisGeoFeatureManager.getProceduresByProcedureType(formBean.getProcedureName()).getId();
	        	if(geoserverConfigIni(layerName)){
				        formBean.setIdEntidad(String.valueOf(idEntidad));		   
				       	log.info("idEntidad = " + idEntidad);
				        Integer idMap = null;
				        if(idEntidad!=null && formBean.getProcedureName()!=null){
				        	idMap = GwsftPublish.gwfstPublish(idEntidad, localgisGeoFeatureManager.getProceduresByProcedureType(formBean.getProcedureName()).getId(), localgisGeoFeatureManager.getProceduresByProcedureType(formBean.getProcedureName()).getMapName());
				        }
				        if(idMap!=null){
					        formBean.setIdMap(idMap);
	
					        if(formBean.getIdMunicipio() != null){
					        	request.setAttribute("id_municipio", formBean.getIdMunicipio());
				        	}
				        	else{
					        	request.setAttribute("id_municipio", String.valueOf(((GeopistaMunicipio)localgisEntidadSupramunicipalManager.getMunicipiosByIdEntidad(idEntidad).get(0)).getId()));
					        }
					        request.setAttribute("id_entidad", idEntidad);
					        request.setAttribute("id_feature", formBean.getIdFeature());
					        request.setAttribute("procedure_id", localgisGeoFeatureManager.getProceduresByProcedureType(formBean.getProcedureName()).getId());
					        request.setAttribute("procedure_name", formBean.getProcedureName() != null ? formBean.getProcedureName() : "");
					        request.setAttribute("layer_name", layerName);	
					        request.setAttribute("scheme", formBean.getScheme() != null ? formBean.getScheme() : "");     
					        
					        String sldStyle = localgisGeoFeatureManager.getSldStyle(layerName,idEntidad);
					        request.setAttribute("sld_style",sldStyle);  
					   
					        
					        ProcedureDefaults procedureDefaults = localgisGeoFeatureManager.getProcedureDefaults(procedureId);
					        if(procedureDefaults!=null){
						        request.setAttribute("feature_toolbarName", procedureDefaults.getFeatureToolbarname());
							    
						        String featureName = procedureDefaults.getFeatureName();
						        //Nombre del elemento a mostrar
							    request.setAttribute("feature_name",featureName);
							    //Nombre del elemento a mostrar con la primera letra en mayuscula
							    request.setAttribute("feature_nameFirst",featureName.substring(0, 1).toUpperCase() + featureName.substring(1));	   
							    //Nombre del elemento a mostrar totalmente en mayuscula
							    request.setAttribute("feature_nameUpper",featureName.toUpperCase());
			
							    //Nombre del campo de estilo en la bd de informacion (no en la tabla spacial)
							    request.setAttribute("style_property",procedureDefaults.getStyleProperty());
							    //Nombre del campo de direccion en la bd de informacion (no en la tabla spacial)
							    request.setAttribute("address_property",procedureDefaults.getAddressProperty());
						
							    //Texto a mostrar cuando el elemento no esta seleccionado 
							    request.setAttribute("onFeatureUnselectText",procedureDefaults.getOnfeatureunselecttext());
							    //Texto a mostrar cuando el elemento no tiene informacion 
							    request.setAttribute("onNotFeatureInfoText",procedureDefaults.getOnnotfeatureinfotext());
							    //Texto a mostrar cuando no hay resultados en una busqueda de elementos 
							    request.setAttribute("onNotFeatureSearchText",procedureDefaults.getOnnotfeaturesearchtext());
					        }
						    //URL de la aplicacion GEOSERVER			    
						    request.setAttribute("geoserverUrl",ShowFeatureMapConfiguration.getPropertyString(ShowFeatureMapConfiguration.PROPERTY_GEOSERVER_URL_HOST));			   
						    //URL del servicio de obtención de informacion de BD de LocalGIS				    
						    request.setAttribute("procedureWSUrl",ShowFeatureMapConfiguration.getPropertyString(ShowFeatureMapConfiguration.PROPERTY_PROCEDUREWS_URL_HOST));	
						    //URL del servicio de obtencion	de informacion WFS			    
						    request.setAttribute("localgisWFSG",ShowFeatureMapConfiguration.getPropertyString(ShowFeatureMapConfiguration.PROPERTY_LOCALGISWFSG_URL_HOST));	
				//----------------FIN-NUEVO-------------
					        
					        /*
					         * Si tenemos idMap obtenemos el mapa publicado. Si no tenemos idMap obtenemos el mapa por defecto
					         */
					        LocalgisMap localgisMap;
					        String configurationLocalgisWeb = "features";				 
					        if (formBean.getIdMap() != null && formBean.getIdMap().intValue() >= 0) {
					            localgisMap = localgisMapManager.getPublishedMap(formBean.getIdMap());       
					        } else {
					            /* Comprobacion de que la entidad sea correcto */
					            if (formBean.getIdEntidad() == null || Integer.getInteger(formBean.getIdEntidad()).intValue() <= 0) {
					                log.error("La entidad seleccionada no es correcto");
					                request.setAttribute("errorMessageKey", "showMap.error.entidadNotValid");
					                return mapping.findForward(ERROR_PAGE);
					            }			            
					            localgisMap = localgisMapsConfigurationManager.getDefaultMap(Integer.getInteger(formBean.getIdEntidad()), false);
					        }	  
					        /*
					         * Comprobacion de que el mapa exista
					         */
					        if (localgisMap == null) {
					            log.error("El mapa solicitado no existe");
					            request.setAttribute("errorMessageKey", "showMap.error.mapNotExist");
					            return mapping.findForward(ERROR_PAGE);
					        }
					        List localgisLayers = localgisMapManager.getMapLayers(localgisMap.getMapid(), formBean.getLanguage());
					        List localgisWMSLayers = localgisMapManager.getMapWMSLayers(localgisMap.getMapid());
					        GeopistaCoverageLayer geopistaCoverageLayer = localgisEntidadSupramunicipalManager.getCoverageLayer(localgisMap.getMapidentidad()); 
					        					        
					        //Obtenemos la hoja de estilos, si la hubiera
					        String customCSS = localgisEntidadSupramunicipalManager.getCSS(localgisMap.getMapidentidad());
					        
					        //Obtenemos el municipio
					        GeopistaEntidadSupramunicipal geopistaEntidad = localgisEntidadSupramunicipalManager.getEntidadSupramunicipal(localgisMap.getMapidentidad());
			
					        Boolean publicMap = Boolean.FALSE;
					    
					        /*
					         * URLs necesarias
					         */
					        String urlGetMapRequests = localgisMapManager.getMapServerURL(localgisMap.getMapidentidad(), localgisMap.getMapid(), publicMap, LocalgisMapManager.GET_MAP_REQUESTS);
					        String urlGetLegendGraphicsRequests = localgisMapManager.getMapServerURL(localgisMap.getMapidentidad(), localgisMap.getMapid(), publicMap, LocalgisMapManager.GET_LEGEND_GRAPHICS_REQUESTS);
					        String urlGetFeatureInfoRequests = localgisMapManager.getMapServerURL(localgisMap.getMapidentidad(), localgisMap.getMapid(), publicMap, LocalgisMapManager.GET_FEATURE_INFO_REQUESTS);
				
					        /*
					         * Obtenemos el bbox de España 
					         */
					        BoundingBox boundingBoxSpain = localgisUtilsManager.getBoundingBoxSpain(localgisMap.getSrid());
					       // BoundingBox boundingBoxSpain = localgisUtilsManager.getBoundingBoxSpain(SRID_WGS84);
					        //BoundingBox boundingBoxSpain = localgisUtilsManager.getBoundingBoxSpain(SRID_WGS84_GOOGLE_MERCATOR);
					        //boundingBoxSpain = localgisUtilsManager.getBoundingBoxSpain(localgisMap.getSrid());
		
					        /*
					         * Juntamos las capas wms con las capas normales en un conjunto de
					         * objetos LayerOpenlayers ordenado por posicion
					         */
					        String projection = "EPSG:"+localgisMap.getSrid();
					        String urlApplicationServer = "http";
					        if (request.isSecure()) {
					            urlApplicationServer += "s";
					        }
					        urlApplicationServer += "://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/"+configurationLocalgisWeb;
					        String urlCustomLegend = urlApplicationServer + "/getLegend.do?idEntidad="+localgisMap.getMapidentidad();
					        
					        SortedSet layersOpenlayersSorted = WebUtils.joinAndSortLayers(null, localgisLayers, localgisWMSLayers, localgisMap, urlGetMapRequests, urlGetFeatureInfoRequests, urlGetLegendGraphicsRequests, projection, urlCustomLegend, localgisLayerManager, LocalgisLayerManager.SHOW_MAP_OPERATION, LayerOpenlayers.FORMAT_PNG);
					        ArrayList layersList = new ArrayList(layersOpenlayersSorted.size());
					        layersList.addAll(layersOpenlayersSorted);
					        
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
					        
					        //NUEVO
					       // geoserverConfigIni(new String [] {"provincias", "municipios"}, idEntidad, idMap);	       
					        
					        //geoserverConfigIni(layersList.toArray(), idEntidad, idMap);
					        //FIN NUEVO
					        
					        /*
					         * Metemos en la request todo lo necesario
					         */
					        request.setAttribute("localgisMap", localgisMap);
					        request.setAttribute("layers", layersList);
					        request.setAttribute("nombreMapa", geopistaEntidad.getNombreoficial() + " - " + localgisMap.getName());
					        if (geopistaCoverageLayer != null) {
					            request.setAttribute("ortofotoLayer", WebUtils.buildLayerOpenlayers(geopistaCoverageLayer, localgisMap, urlGetMapRequests, projection, LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_ORTOFOTO_NAME), localgisLayerManager.getInternalNameLayerOrtofoto(localgisMap, LocalgisLayerManager.SHOW_MAP_OPERATION), localgisLayerManager.getInternalNameLayerOrtofoto(localgisMap, LocalgisLayerManager.SAVE_MAP_OPERATION), LayerOpenlayers.FORMAT_JPEG));
					        }
					        request.setAttribute("provinciasLayer", provinciasLayer);
					        if (customCSS != null) {
					            request.setAttribute("customCSS", customCSS);
					        }
					        if (formBean.getLanguage() != null) {
					            request.setAttribute("language", formBean.getLanguage());
					        } else {
					            request.setAttribute("language", localgisManagerBuilder.getDefaultLocale());
					        }
					        // Para que busque las calles solo del municipio seleccionado:
					        log.debug("ID ENTIDAD: "+geopistaEntidad.getIdEntidad());
					        request.setAttribute("id_entidad",geopistaEntidad.getIdEntidad());
					   
					        request.setAttribute("entidad", geopistaEntidad.getNombreoficial());
					        request.setAttribute("streetInfoService", LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_STREET_INFO_SERVICE));
					        request.setAttribute("buffer", new Integer(LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_OPENLAYERS_BUFFER)));
					        request.setAttribute("singleTile", new Boolean(LocalgisWebConfiguration.getPropertyString(LocalgisWebConfiguration.PROPERTY_OPENLAYERS_SINGLE_TILE)));
					        request.setAttribute("boundingBoxSpain", boundingBoxSpain);
					    
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
					    
					        String username = (String)request.getSession().getAttribute(LoginFilterFeatures.LOGIN_ATTRIBUTE);
					        List markers = localgisMapManager.getMarkers(localgisMap.getMapid(), username);	
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
					   
					        //log.info("FIN");				        
				        }
				        else{
					    	request.setAttribute("errorMessageKey", "Error en la relación con el mapa publicado."); 
					    	return mapping.findForward(ERROR_PAGE);
				        }
	        	}
			    else{
			        request.setAttribute("errorMessageKey", "Error en la comprobación inicial de Geoserver. "); 
				    return mapping.findForward(ERROR_PAGE);
			    }
			}
		    else{
		    	request.setAttribute("errorMessageKey", "Error: La entidad de SIGM no está relacionada con la entidad de LocalGIS. Para relacionarla vaya al apartado Entidades del Administrador de Usuarios de LocalGIS."); 
		    	return mapping.findForward(ERROR_PAGE);
		    }
    	}catch(Exception ex){
    		request.setAttribute("errorMessageKey", "Error: " + ex.getMessage()); 
        	return mapping.findForward(ERROR_PAGE);
        }                	    
	    return mapping.findForward(SUCCESS_PAGE);
    }
        
    private boolean geoserverConfigIni(Object [] layerNames, Integer idEntidad, Integer idMap) throws MalformedURLException, LocalgisConfigurationException, LocalgisInitiationException{
    	if(geoserverDataSourceConfigIni()){    	
    		LocalgisManagerBuilderGeoWfst localgisManagerBuilderGeoWfst = LocalgisManagerBuilderSingletonFeature.getInstance();
  	        LocalgisGeoFeatureManager localgisGeoFeatureManager = localgisManagerBuilderGeoWfst.getLocalgisGeoFeatureManager();      
  	        for(int i=0;i<layerNames.length;i++){    	
  	        	String layerName;
  	        	if(layerNames[i] instanceof LayerOpenlayers){
  	        		LayerOpenlayers layerOpenlayers = (LayerOpenlayers) layerNames[i];
  	        		layerName = layerOpenlayers.getWmcName();
  	        	}
  	        	else{
  	        		layerName = (String) layerNames[i];
  	        	}
    			if(!geoserverLayerConfigIni(layerName, localgisGeoFeatureManager.getSldStyle(layerName, idEntidad), idMap)){    				
    				log.error("Error al publicar la capa " + layerName);
    			}
    		}
    		return true;
    	}
    	return false;
    }    

    private boolean geoserverConfigIni(String layerName) throws MalformedURLException, LocalgisConfigurationException{
    	if(geoserverDataSourceConfigIni()){
    		return geoserverLayerConfigIni(layerName, null, null);
    	}
    	return false;
    }
        
//    private boolean localgisConfigIni(String featureType) throws LocalgisConfigurationException, MalformedURLException{    	
//   	 String localgisUrlHost = ShowFeatureMapConfiguration.getPropertyString(ShowFeatureMapConfiguration.PROPERTY_GEOSERVER_URL_HOST);
//     String localgisUrlUser = ShowFeatureMapConfiguration.getPropertyString(ShowFeatureMapConfiguration.PROPERTY_GEOSERVER_URL_USER);
//     String localgisUrlPassword = ShowFeatureMapConfiguration.getPropertyString(ShowFeatureMapConfiguration.PROPERTY_GEOSERVER_URL_PASSWORD);
//     LocalgisLayerManagerWs localgisLayerManagerWs = new LocalgisLayerManagerWs();
//        
//     localgisLayerManagerWs.createTable(featureType);
//        
//     return true;
//   }
    
    private boolean geoserverLayerConfigIni(String layerName, String layerSldStyle, Integer idMap) throws LocalgisConfigurationException, MalformedURLException{       	
    	 return getGeoserverWs().publishLayer(ShowFeatureMapConfiguration.getPropertyString(ShowFeatureMapConfiguration.PROPERTY_GEOSERVER_DATASOURCE_NAME), layerName, SRID_4230, layerSldStyle, idMap);
    }
    
    private boolean geoserverDataSourceConfigIni() throws LocalgisConfigurationException, MalformedURLException{ 
         String datasourceName = ShowFeatureMapConfiguration.getPropertyString(ShowFeatureMapConfiguration.PROPERTY_GEOSERVER_DATASOURCE_NAME);
         String datasourceHost = Configuration.getPropertyString(Configuration.PROPERTY_DB_HOST);
         Integer datasourcePort = Integer.valueOf(Configuration.getPropertyString(Configuration.PROPERTY_DB_PORT));
         String datasourceDatabase = Configuration.getPropertyString(Configuration.PROPERTY_DB_NAME);
         String datasourceSchema = ShowFeatureMapConfiguration.getPropertyString(ShowFeatureMapConfiguration.PROPERTY_GEOSERVER_DATASOURCE_SCHEMA);
         String datasourceUser = Configuration.getPropertyString(Configuration.PROPERTY_DB_USERNAME);
         String datasourcePassword = Configuration.getPropertyString(Configuration.PROPERTY_DB_PASSWORD);
         return getGeoserverWs().addDataStore(datasourceName, datasourceHost, datasourcePort, datasourceDatabase, datasourceSchema, datasourceUser, datasourcePassword);
    }
    
    private GeoserverWS getGeoserverWs() throws LocalgisConfigurationException, MalformedURLException{
    	if(geoserverWS == null){
	    	String geoserverUrlHost = ShowFeatureMapConfiguration.getPropertyString(ShowFeatureMapConfiguration.PROPERTY_GEOSERVER_URL_HOST);
	        String geoserverUrlUser = ShowFeatureMapConfiguration.getPropertyString(ShowFeatureMapConfiguration.PROPERTY_GEOSERVER_URL_USER);
	        String geoserverUrlPassword = ShowFeatureMapConfiguration.getPropertyString(ShowFeatureMapConfiguration.PROPERTY_GEOSERVER_URL_PASSWORD);
	        geoserverWS = new GeoserverWS(geoserverUrlHost, geoserverUrlUser, geoserverUrlPassword);
    	}
    	return geoserverWS;
    }
    
}
