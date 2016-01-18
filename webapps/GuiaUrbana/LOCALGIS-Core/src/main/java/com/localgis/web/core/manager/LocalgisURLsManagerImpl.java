/**
 * LocalgisURLsManagerImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.manager;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import org.apache.log4j.Logger;

import com.ibatis.dao.client.DaoException;
import com.ibatis.dao.client.DaoManager;
import com.localgis.web.core.ConstantesSQL;
import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.dao.GeopistaCoverageLayerDAO;
import com.localgis.web.core.dao.GeopistaEntidadSupramunicipalDAO;
import com.localgis.web.core.dao.GeopistaMapGenericElementDAO;
import com.localgis.web.core.dao.GeopistaNumeroPoliciaDAO;
import com.localgis.web.core.dao.GeopistaParcelaDAO;
import com.localgis.web.core.dao.GeopistaTableDAO;
import com.localgis.web.core.dao.GeopistaViaDAO;
import com.localgis.web.core.dao.LocalgisMapDAO;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.exceptions.LocalgisMapNotFoundException;
import com.localgis.web.core.model.BoundingBox;
import com.localgis.web.core.model.GeopistaCoverageLayer;
import com.localgis.web.core.model.GeopistaEntidadSupramunicipal;
import com.localgis.web.core.model.GeopistaNumeroPolicia;
import com.localgis.web.core.model.GeopistaParcela;
import com.localgis.web.core.model.GeopistaVia;
import com.localgis.web.core.model.LocalgisLayer;
import com.localgis.web.core.model.LocalgisLayerExt;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.core.model.LocalgisMapServerLayer;
import com.localgis.web.core.model.Point;
import com.localgis.web.core.model.Scale;
import com.localgis.web.core.utils.ChangeCoordinateSystem;
import com.localgis.web.core.utils.LocalgisUtils;
import com.localgis.web.core.wms.WMSConfigurator;

/**
 * Implementación de LocalgisURLsManager
 * 
 * @author albegarcia
 *
 */
public class LocalgisURLsManagerImpl implements LocalgisURLsManager {

    /**
     * Logger para las trazas
     */
    private static Logger logger = Logger.getLogger(LocalgisURLsManagerImpl.class);

    /**
     * Dao para las coverage layers de geopista
     */
    private GeopistaCoverageLayerDAO geopistaCoverageLayerDAO;

    /**
     * Dao para las entidades supramunicipales de geopista
     */
    private GeopistaEntidadSupramunicipalDAO geopistaEntidadSupramunicipalDAO;

    /**
     * Dao para los elementos genericos de un mapa de geopista
     */
    private GeopistaMapGenericElementDAO geopistaMapGenericElementDAO;

    /**
     * Dao para los números de policía de geopista
     */
    private GeopistaNumeroPoliciaDAO geopistaNumeroPoliciaDAO;

    /**
     * Dao para las parcelas de geopista
     */
    private GeopistaParcelaDAO geopistaParcelaDAO;

    /**
     * Dao para las tablas de geopista
     */
    private GeopistaTableDAO geopistaTableDAO;

    /**
     * Dao para las vias de geopista
     */
    private GeopistaViaDAO geopistaViaDAO;

    /**
     * Dao para los mapas de localgis 
     */
    private LocalgisMapDAO localgisMapDAO;

    /**
     * LocalgisManagerBuilder que construyo el objeto
     */
    private LocalgisManagerBuilder localgisManagerBuilder;

    /**
     * Configurador WMS
     */
    private WMSConfigurator wmsConfigurator;
    
    /**
     * Constructor a partir de un DAOManager, un configurador de wms y un LocalgisManagerBuilder
     */
    public LocalgisURLsManagerImpl(DaoManager daoManager, WMSConfigurator wmsConfigurator, LocalgisManagerBuilder localgisManagerBuilder) {
        this.geopistaCoverageLayerDAO = (GeopistaCoverageLayerDAO) daoManager.getDao(GeopistaCoverageLayerDAO.class);
        this.geopistaEntidadSupramunicipalDAO = (GeopistaEntidadSupramunicipalDAO) daoManager.getDao(GeopistaEntidadSupramunicipalDAO.class);
        this.geopistaMapGenericElementDAO = (GeopistaMapGenericElementDAO) daoManager.getDao(GeopistaMapGenericElementDAO.class);
        this.geopistaNumeroPoliciaDAO = (GeopistaNumeroPoliciaDAO) daoManager.getDao(GeopistaNumeroPoliciaDAO.class);
        this.geopistaParcelaDAO = (GeopistaParcelaDAO) daoManager.getDao(GeopistaParcelaDAO.class);
        this.geopistaTableDAO = (GeopistaTableDAO) daoManager.getDao(GeopistaTableDAO.class);
        this.geopistaViaDAO = (GeopistaViaDAO) daoManager.getDao(GeopistaViaDAO.class);
        this.localgisMapDAO = (LocalgisMapDAO) daoManager.getDao(LocalgisMapDAO.class);

        this.wmsConfigurator = wmsConfigurator;
        
        this.localgisManagerBuilder = localgisManagerBuilder;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisURLsManager#getURLMapByXAndY(int, double, double, com.localgis.web.core.model.Scale, int, int)
     */
    public String[] getURLMapByXAndY (int idMap, double x, double y, Scale scale, int width, int height) throws LocalgisInvalidParameterException, LocalgisConfigurationException, LocalgisMapNotFoundException, LocalgisDBException {
    	return getURLMapByXAndY(idMap, x, y, scale, width, height, null);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisURLsManager#getURLMapByXAndY(int, double, double, com.localgis.web.core.model.Scale, int, int, String)
     */
    public String[] getURLMapByXAndY (int idMap, double x, double y, Scale scale, int width, int height, String imageName) throws LocalgisInvalidParameterException, LocalgisConfigurationException, LocalgisMapNotFoundException, LocalgisDBException {

        // Obtenemos el mapa para comprobar que realmente existe
        LocalgisMap localgisMap;
        try {
            localgisMap = localgisMapDAO.selectMapById(new Integer(idMap), localgisManagerBuilder.getDefaultLocale());
        } catch (DaoException e) {
            logger.error("Error al obtener el mapa", e);
            throw new LocalgisDBException("Error al obtener el mapa",e);
        }
        if (localgisMap == null) {
        	logger.error("El mapa con id \""+idMap+"\" no existe.");
            throw new LocalgisMapNotFoundException("El mapa con id \""+idMap+"\" no existe.");
        }
        
        // Obtenemos las capas del mapa (tanto las capas de map server como las wms externas) 
        List layers = localgisManagerBuilder.getLocalgisMapManager().getMapLayers(new Integer(idMap), localgisManagerBuilder.getDefaultLocale());
        List wmsLayers = localgisManagerBuilder.getLocalgisMapManager().getMapWMSLayers(new Integer(idMap));
        
        // Ordenamos las capas
        SortedSet sortedLayers = LocalgisUtils.joinAndSortLayers(layers, wmsLayers);
        
        /*
         * Obtenemos la url del servidor de mapas dependidendo de la configuracion del mapa (publica o privada)
         */
        Boolean publicMap;
        if (localgisMap.getMappublic().equals(ConstantesSQL.TRUE)) {
            publicMap = Boolean.TRUE;
        } else {
            publicMap = Boolean.FALSE;
        }
        String urlMapServer = wmsConfigurator.getMapServerURL(localgisMap.getMapidentidad(), localgisMap.getMapid(), publicMap, WMSConfigurator.ALL_REQUESTS);

        /*
         * Componemos la lista de capas wms con todas las capas, incluida la ortofoto si existe
         */
        GeopistaCoverageLayer geopistaCoverageLayer = geopistaCoverageLayerDAO.selectCoverageLayerByIdEntidad(localgisMap.getMapidentidad());
        StringBuffer layersStr = new StringBuffer();
        if (geopistaCoverageLayer != null) {
            layersStr.append(Configuration.MAPSERVER_CONFIG_FILE_ORTOFOTO_LAYER_NAME);
            layersStr.append(",");
        }
        
        /*
         * Incluimos la capa de de municipios y de provincias
         */
        layersStr.append(Configuration.MAPSERVER_CONFIG_FILE_MUNICIPIOS_LAYER_NAME);
        layersStr.append(",");
        layersStr.append(Configuration.MAPSERVER_CONFIG_FILE_PROVINCIAS_LAYER_NAME);
        
        if (sortedLayers.size() > 0) {
            layersStr.append(",");
        }
        
        Iterator it = sortedLayers.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (i != 0) {
                layersStr.append(",");
            }
            Object layer = (Object) it.next();
            if (layer instanceof LocalgisLayer) {
                layersStr.append(((LocalgisLayerExt)layer).getLayername());
            } else if (layer instanceof LocalgisMapServerLayer) {
                layersStr.append(((LocalgisMapServerLayer)layer).getInternalNameMapServer());
            }
            i++;
        }

        //Conversion de coordenadas
        String srid=localgisMap.getSrid();
        try {
			srid=Configuration.getPropertyString(Configuration.PROPERTY_DISPLAYPROJECTION);
			double[] transformedCoordinates=ChangeCoordinateSystem.transform(localgisMap.getSrid(),srid,new double[] {x,y});
			x=transformedCoordinates[0];
			y=transformedCoordinates[1];
			
		} catch (Throwable e) {
			e.printStackTrace();
		}
        
        BoundingBox boundingBox = LocalgisUtils.getBoundingBox(scale, x, y, width, height);

        String urlMapServerStr = getURLMapServer(urlMapServer, null, layersStr.toString(),srid, boundingBox, width, height);
        
        /*
         * Obtenemos la URL de la guia urbana
         */
        StringBuffer urlGuiaUrbana = new StringBuffer();
        if (localgisMap.getMappublic().equals(ConstantesSQL.TRUE)) {
            urlGuiaUrbana.append(Configuration.getPropertyString(Configuration.PROPERTY_GUIAURBANA_PUBLIC_MAP));
        } else {
            urlGuiaUrbana.append(Configuration.getPropertyString(Configuration.PROPERTY_GUIAURBANA_PRIVATE_MAP));
        }
        String urlGuiaUrbanaStr = getURLGuiaUrbana(urlGuiaUrbana.toString(), idMap, x, y, scale, imageName);
        //Para que el servicio lo devuelva con CDATA (escapado)
        urlGuiaUrbanaStr+="&INFO=GuiaUrbana&VERSION=1.4&Author=MITyC";
        /*
         * Componemos el resultado
         */
        String[] result = new String[2];
        result[0] = urlMapServerStr;
        result[1] = urlGuiaUrbanaStr;
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisURLsManager#getURLMapByIdNumeroPolicia(int, int, com.localgis.web.core.model.Scale, int, int)
     */
    public String[] getURLMapByIdNumeroPolicia (int idMap, int idNumeroPolicia, Scale scale, int width, int height) throws LocalgisInvalidParameterException, LocalgisConfigurationException, LocalgisMapNotFoundException, LocalgisDBException {
        LocalgisMap localgisMap = localgisMapDAO.selectMapById(new Integer(idMap), localgisManagerBuilder.getDefaultLocale());
       
        GeopistaNumeroPolicia geopistaNumeroPolicia = geopistaNumeroPoliciaDAO.selectNumeroPoliciaById(new Integer(idNumeroPolicia), new Integer(localgisMap.getSrid()));
        if (geopistaNumeroPolicia == null) {
            logger.error("El número de policía con id \""+idNumeroPolicia+"\" no existe");
            throw new LocalgisDBException("El número de policía con id \""+idNumeroPolicia+"\" no existe");
        }
        return getURLMapByXAndY(idMap, geopistaNumeroPolicia.getX(), geopistaNumeroPolicia.getY(), scale, width, height);
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisURLsManager#getURLMapByIdVia(int, int, com.localgis.web.core.model.Scale, int, int)
     */
    public String[] getURLMapByIdVia (int idMap, int idVia, Scale scale, int width, int height) throws LocalgisInvalidParameterException, LocalgisConfigurationException, LocalgisMapNotFoundException, LocalgisDBException {
       LocalgisMap localgisMap = localgisMapDAO.selectMapById(new Integer(idMap), localgisManagerBuilder.getDefaultLocale());
       
        GeopistaVia geopistaVia = geopistaViaDAO.selectViaById(new Integer(idVia), new Integer(localgisMap.getSrid()));
        if (geopistaVia == null) {
            logger.error("La vía con id \""+idVia+"\" no existe");
            throw new LocalgisDBException("La vía con id \""+idVia+"\" no existe");
        }
        return getURLMapByXAndY(idMap, geopistaVia.getX(), geopistaVia.getY(), scale, width, height);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisURLsManager#getURLMapByReferenciaCatastral(int, java.lang.String, com.localgis.web.core.model.Scale, int, int)
     */
    public String[] getURLMapByReferenciaCatastral (int idMap, String referenciaCatastral, Scale scale, int width, int height) throws LocalgisInvalidParameterException, LocalgisConfigurationException, LocalgisMapNotFoundException, LocalgisDBException {
        LocalgisMap localgisMap = localgisMapDAO.selectMapById(new Integer(idMap), localgisManagerBuilder.getDefaultLocale());
       
        GeopistaParcela geopistaParcela = geopistaParcelaDAO.selectParcelaByReferenciaCatastral(referenciaCatastral, new Integer(localgisMap.getSrid()));
        if (geopistaParcela == null) {
            logger.error("La parcela con referencia catastral \""+referenciaCatastral+"\" no existe");
            throw new LocalgisDBException("La parcela con referencia catastral \""+referenciaCatastral+"\" no existe");
        }
        return getURLMapByXAndY(idMap, geopistaParcela.getX(), geopistaParcela.getY(), scale, width, height);
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisURLsManager#getURLReportMap(java.lang.Integer, java.lang.String, java.lang.String, java.lang.Object, int, boolean, java.lang.String, com.localgis.web.core.model.Scale, int, int)
     */
    public String getURLReportMap (Integer idEntidad, String tableName, String identifierColumnName, Object identifierValue, LocalgisMap localgisMap, boolean publicMap, String layers, Scale scale, int width, int height) throws LocalgisInvalidParameterException, LocalgisConfigurationException, LocalgisDBException {
        
    	Integer srid = null;
    	try {
    		srid = Integer.valueOf(localgisMap.getSrid());
    	} catch (Exception e) {
    		logger.error("Error al obtener el srid del mapa", e);
    		throw new LocalgisDBException(e);
    	}
    	
    	System.out.println("DATOS:"+tableName+" "+identifierColumnName+" "+identifierValue+" "+srid);
    	logger.info("DATOS:"+tableName+" "+identifierColumnName+" "+identifierValue+" "+srid);
        Point centeredPoint = geopistaMapGenericElementDAO.selectCenteredPointMapGenericElement(tableName, identifierColumnName, identifierValue, srid);

        // Obtenemos la entidad para comprobar que realmente existe
        GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal;
        try {
            geopistaEntidadSupramunicipal = geopistaEntidadSupramunicipalDAO.selectByPrimaryKey(idEntidad);
        } catch (DaoException e) {
            logger.error("Error al obtener la entidad supramunicipal", e);
            throw new LocalgisDBException(e);
        }
        if (geopistaEntidadSupramunicipal == null) {
            throw new LocalgisDBException("La entidad con id \""+idEntidad+"\" asociado al elemento no existe.");
        }

        /*
         * Ademas de las capas que se solicitan, se pone una capa fija para los
         * informes dependiendo de si la tabla consultada es de tipo poligono,
         * lineas o puntos
         */
        //Importante no meter una coma si no se pidio ninguna capa
        
        //Si en la peticion ya viene alguna capa de marcado de reports no hacemos nada y no incluimos la 
        //que tendriamos que incluir dependiendo del tipo de geometria
        if (!layers.contains("_reports")){
	        if (layers == null || layers.trim().equals("")) {
	            layers = "";
	        } else {
	            layers += ",";
	        }
	        String geometryType = geopistaTableDAO.selectGMLGeometryTypeByTableName(tableName);
	        if (geometryType != null && (geometryType.equals("gml:PointPropertyType") || geometryType.equals("gml:MultiPointPropertyType"))) {
	            layers += Configuration.MAPSERVER_CONFIG_FILE_POINT_REPORTS_LAYER_NAME;
	        } else if (geometryType != null && (geometryType.equals("gml:PolygonPropertyType") || geometryType.equals("gml:MultiPolygonPropertyType"))) {
	            layers += Configuration.MAPSERVER_CONFIG_FILE_POLYGON_REPORTS_LAYER_NAME;
	        } else if (geometryType != null && (geometryType.equals("gml:LineStringPropertyType") || geometryType.equals("gml:MultiLineStringPropertyType"))) { 
	            layers += Configuration.MAPSERVER_CONFIG_FILE_LINE_REPORTS_LAYER_NAME;
	        } else {
	            layers += Configuration.MAPSERVER_CONFIG_FILE_POLYGON_REPORTS_LAYER_NAME;
	        }
        }
        
        /*
         * Como parametros extra metemos ciertas variables para que se consulte
         * el elemento seleccionado de forma correcta, ya que la capa que
         * permite pintar el elemento seleccionado recibe ciertos parámetros
         */
        StringBuffer extraParams = new StringBuffer();
        extraParams.append(Configuration.MAPSERVER_CONFIG_FILE_REPORTS_TABLE_NAME).append("=").append(tableName).append("&");
        extraParams.append(Configuration.MAPSERVER_CONFIG_FILE_REPORTS_IDENTIFIER_COLUMN_NAME).append("=").append(identifierColumnName).append("&");
        extraParams.append(Configuration.MAPSERVER_CONFIG_FILE_REPORTS_IDENTIFIER_VALUE).append("=").append(identifierValue);
        
        /*
         * Obtenemos la url del servidor de mapas
         */
        String urlMapServer = wmsConfigurator.getMapServerURL(idEntidad, localgisMap.getMapid(), new Boolean(publicMap), WMSConfigurator.ALL_REQUESTS);

        System.out.println("URL MAP SERVER:"+urlMapServer);
        logger.info("URL MAP SERVER:"+urlMapServer);
        //System.out.println("CENTERED:"+centeredPoint);
        BoundingBox boundingBox = LocalgisUtils.getBoundingBox(scale, centeredPoint.getX().doubleValue(), centeredPoint.getY().doubleValue(), width, height);
        String result = getURLMapServer(urlMapServer, extraParams.toString(), layers, localgisMap.getSrid(), boundingBox, width, height);
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisURLsManager#getURLReportMap(java.lang.Integer, java.lang.String, java.lang.String, java.lang.Object, int, boolean, java.lang.String, java.lang.String, com.localgis.web.core.model.Scale, int, int)
     */
    public String getURLReportMap(Integer idEntidad, String tableName, String identifierColumnName, Object identifierValue, LocalgisMap localgisMap, boolean publicMap, String layers, String style, Scale scale, int width, int height) throws LocalgisInvalidParameterException, LocalgisConfigurationException, LocalgisDBException {
        return getURLReportMap(idEntidad, tableName, identifierColumnName, identifierValue, localgisMap, publicMap, layers, scale, width, height);
    }
    
    /**
     * Devuelve la url de la guia urbana a partir de la url donde está la guia
     * urbana (protocol://host:port), el id del mapa, una x, una y y una escala
     * 
     * @param urlGuiaUrbana URL donde esta la guia urbana (protocol://host:port)
     * @param idMap Identificador del mapa
     * @param x X de centrado
     * @param y Y de centrado
     * @param scale Escala en la que se desea mostrar
     * @param imageName Nombre de la imagen a mostrar en pantalla en el punto dado
     * @return La URL que permite ver la guia urbana
     */
    private String getURLGuiaUrbana(String urlGuiaUrbana, int idMap, double x, double y, Scale scale, String imageName) {
        StringBuffer result = new StringBuffer();
        result.append(urlGuiaUrbana);
        result.append("?idMap=");
        result.append(idMap);
        result.append("&x=");
        result.append(x);
        result.append("&y=");
        result.append(y);
        result.append("&scale=");
        result.append(scale.getDenominator());
        if (imageName != null && !imageName.equals("")) {
        	result.append("&marker=" + imageName);
        }
        return result.toString();
    }

    /**
     * Devuelve la url del map server a partir de la url donde está el map server
     * (protocol://host:port), los parametros extra, una lista de capas, un formato, una proyeccion, un bounding box a mostrar y, un ancho y un alto
     * 
     * @param urlMapServer URL donde esta el map server (protocol://host:port/file)
     * @param extraParams Parametros extras a pasar
     * @param layers Capas a mostrar
     * @param format Formato en el que se desea mostrar
     * @param srid Proyeccion
     * @param boundingBox Bounding box a mostrar
     * @param width Ancho de la imagen en pixeles
     * @param height Alto de la imagen en pixeles
     * @return La URL que permite ver la imagen del map server
     */
    private String getURLMapServer(String urlMapServer, String extraParams, String layers, String srid, BoundingBox boundingBox, int width, int height) {
        StringBuffer result = new StringBuffer();
        result.append(urlMapServer);
        result.append("?LAYERS=");
        result.append(layers);
        result.append("&REQUEST=GetMap&FORMAT=image%2Fpng%3B+mode%3D24bit&TRANSPARENT=true&EXCEPTIONS=application%2Fvnd.ogc.se_blank&SERVICE=WMS&VERSION=1.1.1&STYLES=&SRS=EPSG%3A");
        result.append(srid);
        result.append("&BBOX=");
        result.append(boundingBox.getMinx());
        result.append(",");
        result.append(boundingBox.getMiny());
        result.append(",");
        result.append(boundingBox.getMaxx());
        result.append(",");
        result.append(boundingBox.getMaxy()); 
        result.append("&WIDTH=");
        result.append(width);
        result.append("&HEIGHT=");
        result.append(height);
        if (extraParams != null && !extraParams.equals("")) {
            result.append("&").append(extraParams);
        }
        return result.toString();
    }
}
