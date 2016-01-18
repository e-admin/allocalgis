/**
 * WMSSimpleConfigurator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.wms;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.ibatis.dao.client.DaoManager;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.ProxySOCKS5;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.localgis.web.core.ConstantesSQL;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.dao.GeopistaCoverageLayerDAO;
import com.localgis.web.core.dao.LocalgisAttributeDAO;
import com.localgis.web.core.dao.LocalgisLayerDAO;
import com.localgis.web.core.dao.LocalgisMapDAO;
import com.localgis.web.core.dao.LocalgisMapServerLayerDAO;
import com.localgis.web.core.dao.LocalgisMunicipioDAO;
import com.localgis.web.core.dao.LocalgisSpatialRefSysDAO;
import com.localgis.web.core.dao.LocalgisStyleDAO;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.exceptions.LocalgisWMSException;
import com.localgis.web.core.model.BoundingBox;
import com.localgis.web.core.model.GeopistaCoverageLayer;
import com.localgis.web.core.model.GeopistaEntidadSupramunicipal;
import com.localgis.web.core.model.LocalgisAttribute;
import com.localgis.web.core.model.LocalgisLayer;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.core.model.LocalgisMapServerLayer;
import com.localgis.web.core.model.LocalgisSpatialRefSys;
import com.localgis.web.core.model.LocalgisStyle;
import com.localgis.web.core.utils.FileUtils;
import com.vividsolutions.jts.geom.Coordinate;

import edu.umn.gis.mapscript.MS_LAYER_TYPE;
import edu.umn.gis.mapscript.classObj;
import edu.umn.gis.mapscript.labelObj;
import edu.umn.gis.mapscript.layerObj;
import edu.umn.gis.mapscript.lineObj;
import edu.umn.gis.mapscript.mapObj;
import edu.umn.gis.mapscript.outputFormatObj;
import edu.umn.gis.mapscript.pointObj;
import edu.umn.gis.mapscript.symbolObj;

/**
 * Clase que se encarga de la configuración de mapas en MapServer
 * @author albegarcia
 *
 */
public class WMSSimpleConfigurator implements WMSConfigurator {

    /**
     * Logger para las trazas
     */
    private static Logger logger = Logger.getLogger(WMSSimpleConfigurator.class);
    
    /**
     * Constante que contiene la cabecera de un fichero de estilos
     */
    private static final String BEGIN_STYLES = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
    "<sld:StyledLayerDescriptor xmlns:sld=\"http://www.opengis.net/sld\" xmlns:java=\"java\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:wfs=\"http://www.opengis.net/wfs\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:gml=\"http://www.opengis.net/gml\" xmlns:ogc=\"http://www.opengis.net/ogc\" xmlns=\"http://www.opengis.net/sld\" version=\"1.0.0\">\n";

    /**
     * Constante que contiene el final de un fichero de estilos
     */
    private static final String END_STYLES = "</sld:StyledLayerDescriptor>";

    /**
     * Constante del alias de la geometria
     */
    private static final String ALIAS_GEOMETRY = "geometry_localgis";
    
    /**
     * Constante del alias del campo id
     */
    private static final String ALIAS_ID = "id_localgis";
    
    /**
     * Tipo de conexion (postgis u oraclespatial, definidos en WMSConfigurator)
     */
    private int connectionType;

    /**
     * Dao para las coverage layers de geopista
     */
    private GeopistaCoverageLayerDAO geopistaCoverageLayerDAO;

    /**
     * Dao para los atributos de localgis
     */
    private LocalgisAttributeDAO localgisAttributeDAO;
    
    /**
     * Dao para las capas de localgis
     */
    private LocalgisLayerDAO localgisLayerDAO;

    /**
     * Dao para las capas externas de localgis
     */
    private LocalgisMapServerLayerDAO localgisMapServerLayerDAO;

    /**
     * Dao para los municipios de localgis
     */
    private LocalgisMunicipioDAO localgisMunicipioDAO;

    /**
     * Dao para los estilos de localgis
     */
    private LocalgisStyleDAO localgisStyleDAO;

    /**
     * Dao para los mapas de localgis
     */
    private LocalgisMapDAO localgisMapDAO;

    /**
     * Dao para los sistemas de referencia
     */
    private LocalgisSpatialRefSysDAO localgisSpatialRefSysDAO;

    /**
     * Locales para configurar el WMS
     */
    private String locale;
    
    
	Session session = null;

	ChannelSftp sftpChannel;
	ChannelExec ssshChannel;

    /**
     * Constructor a partir de un dao manager y de un bean de configuracion
     * @param daoManager El dao manager
     * @param config El bean de configuracion
     */
    public WMSSimpleConfigurator(DaoManager daoManager, String locale, int connectionType) {
        this.geopistaCoverageLayerDAO = (GeopistaCoverageLayerDAO) daoManager.getDao(GeopistaCoverageLayerDAO.class);
        this.localgisMapDAO = (LocalgisMapDAO) daoManager.getDao(LocalgisMapDAO.class);
        this.localgisLayerDAO = (LocalgisLayerDAO) daoManager.getDao(LocalgisLayerDAO.class);
        this.localgisStyleDAO = (LocalgisStyleDAO) daoManager.getDao(LocalgisStyleDAO.class);
        this.localgisMapServerLayerDAO = (LocalgisMapServerLayerDAO) daoManager.getDao(LocalgisMapServerLayerDAO.class);
        this.localgisMunicipioDAO = (LocalgisMunicipioDAO) daoManager.getDao(LocalgisMunicipioDAO.class);
        this.localgisAttributeDAO = (LocalgisAttributeDAO) daoManager.getDao(LocalgisAttributeDAO.class);
        this.localgisSpatialRefSysDAO = (LocalgisSpatialRefSysDAO) daoManager.getDao(LocalgisSpatialRefSysDAO.class);
        this.locale = locale;
        this.connectionType = connectionType;
    }  
  
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.wms.WMSConfigurator#configureWMSServer(com.localgis.web.core.model.GeopistaMunicipio, java.lang.Boolean)
     */
    public void configureWMSServer(GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal, Boolean publicConfiguration) throws LocalgisWMSException, LocalgisConfigurationException {
        removeMapServerFiles(geopistaEntidadSupramunicipal, publicConfiguration);
        removeStyleFiles(geopistaEntidadSupramunicipal, publicConfiguration);
        String mapServerConnection;
        if (connectionType == WMSConfigurator.ORACLESPATIAL_CONNECTION) {
            mapServerConnection = Configuration.getPropertyString(Configuration.PROPERTY_DB_USERNAME)+"/"+Configuration.getPropertyString(Configuration.PROPERTY_DB_PASSWORD)+"@"+Configuration.getPropertyString(Configuration.PROPERTY_DB_ORACLESERVICE);
        } else {
            mapServerConnection = "user="+Configuration.getPropertyString(Configuration.PROPERTY_DB_USERNAME)+" password="+Configuration.getPropertyString(Configuration.PROPERTY_DB_PASSWORD)+" host="+Configuration.getPropertyString(Configuration.PROPERTY_DB_HOST)+" port="+Configuration.getPropertyString(Configuration.PROPERTY_DB_PORT)+" dbname="+Configuration.getPropertyString(Configuration.PROPERTY_DB_NAME);
        }
        configureMapAndStylesFiles(geopistaEntidadSupramunicipal, publicConfiguration, mapServerConnection);
    }

    /**
     * Método para configurar el fichero de mapas para una entidad supramunicipal y una configuracion (publica o privada) determinada 
     * @param geopistaEntidadSupramunicipal Entidad para la que se desea configurar el servidor de mapas
     * @param publicConfiguration Si se desea configurar para los mapas publicos (true) o privados (false)
     * @param mapServerConnection Conexion del MapServer
     * @throws LocalgisWMSException Si sucede algun error con el servidor WMS
     * @throws LocalgisConfigurationException Si sucede algun error de configuracion
     */
    private void configureMapAndStylesFiles(GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal, Boolean publicConfiguration, String mapServerConnection) throws LocalgisWMSException, LocalgisConfigurationException {
        // Obtenemos todos los mapas
        List maps = localgisMapDAO.selectMapsByIdEntidad(geopistaEntidadSupramunicipal.getIdEntidad(), publicConfiguration, locale);

        /*
         * Obtenemos el BoundingBox de la entidad
         */
        BoundingBox bbox = localgisMunicipioDAO.selectBoundingBoxByIdEntidadAndSRID(geopistaEntidadSupramunicipal.getIdEntidad(), Integer.parseInt(geopistaEntidadSupramunicipal.getSrid()));
        String maxExtentMunicipio = bbox.getMinx() + " " + bbox.getMiny() + " " + bbox.getMaxx() + " " + bbox.getMaxy();

        /*
         * Obtenemos la extension de la ortofoto, en caso de que la tenga
         */
        GeopistaCoverageLayer geopistaCoverageLayer = geopistaCoverageLayerDAO.selectCoverageLayerByIdEntidad(geopistaEntidadSupramunicipal.getIdEntidad());
        String ortofotoExtension = null;
        String ortofotoSrid = null;
        if (geopistaCoverageLayer != null) {
            ortofotoExtension = geopistaCoverageLayer.getExtension();
            ortofotoSrid = geopistaCoverageLayer.getSrs();
        }
        
        /*
         * Map donde guardaremos todas las capas encontradas. La clave será el
         * Id de capa y el valor será un objeto LocalgisLayer
         */
        Map mapLocalgisLayers = new HashMap();
        /*
         * Map donde guardaremos todas las capas externas encontradas. La clave
         * será el Id de la capa y el valor sera un objeto
         * LocalgisMapServerLayer
         */
        Map mapLocalgisMapServerLayers = new HashMap();
        /*
         * Map donde guardaresmos todos los estilos "por defecto" de las capas.
         * La clave será el nombre de la capa y el valor será un objeto
         * LocalgisStyle
         */
        Map mapDefaultLocalgisStyles = new HashMap();
        /*
         * Vamos configurando para cada mapa su fichero de estilos y de maps y a
         * la vez nos guardamos la información necesaria para generar los ficheros globales 
         */
        
        openSFTPSession();
        
        int idMap;
        
        Iterator itMaps = maps.iterator();
        while (itMaps.hasNext()) {
            LocalgisMap map = (LocalgisMap) itMaps.next();
            BoundingBox bboxMapSrid = localgisMunicipioDAO.selectBoundingBoxByIdEntidadAndSRID(geopistaEntidadSupramunicipal.getIdEntidad(), Integer.parseInt(map.getSrid()));
            String maxExtentMunicipioMapSrid = bboxMapSrid.getMinx() + " " + bboxMapSrid.getMiny() + " " + bboxMapSrid.getMaxx() + " " + bboxMapSrid.getMaxy();
         
            //TODO Habria que optimizar lo de no republicar todos los mapas en el mapserver sino solo el que ha cambiado
            logger.info("Configurando mapa en el WMS:"+map.getName());
            configureMapAndStylesFile(geopistaEntidadSupramunicipal, map, publicConfiguration, mapLocalgisLayers, mapLocalgisMapServerLayers, mapDefaultLocalgisStyles, maxExtentMunicipioMapSrid, mapServerConnection, ortofotoExtension, ortofotoSrid);
            
        }
        
        
       
        
        // Escribimos el fichero estatico de mapas
        writeMapFile(geopistaEntidadSupramunicipal, null, null, null, maxExtentMunicipio, Configuration.MAPSERVER_CONFIG_FILE_SKELETON_STATIC, Configuration.MAPSERVER_CONFIG_FILE_SKELETON_EXTRA, mapServerConnection, ortofotoExtension, ortofotoSrid);
        
        // Creamos el fichero global de estilos para la configuracion publica o privada
        writeGlobalStylesFile(geopistaEntidadSupramunicipal, publicConfiguration, mapDefaultLocalgisStyles);
        
        // Creamos el fichero global de mapa para la configuracion publica o privada
        writeGlobalMapFile(geopistaEntidadSupramunicipal, publicConfiguration, maxExtentMunicipio, mapLocalgisLayers, mapLocalgisMapServerLayers, mapServerConnection, ortofotoExtension, ortofotoSrid);
        
      
        
        
        logger.info("Aplicando los estilos al mapa");
        // Aplicamos los estilos al fichero global
        try {
            applyStylesToMap(geopistaEntidadSupramunicipal, null, publicConfiguration);
        } catch (Throwable e) {
            logger.error("Error grave al configurar el sistema", e);
            throw new LocalgisWMSException("Error al configurar el fichero de mapas",e);
        }
        logger.info("Proceso de publicacion finalizado");
        
        closeSFTPSession();
        
        logger.info("Sesion FTP cerrada");

    }


    /**
     * Reemplaza las variables del fichero base (skeleton) del servidor de mapas
     * @param skeleton Skeleton sin variables reemplazadas
     * @param srid SRID asociado al mapa
     * @param maxExtent Maxima extensión del mapa 
     * @param layers Configuración de las capas que forman el mapa
     * @param urlMapServer URL del servidor de mapas
     * @param idMunicipio Identificador de municipio asociado al mapa
     * @param mapServerConnection Conexión del map server
     * @param ortofotoExtension Extension de la ortofoto
     * @param ortofotoSrid SRID asociado a la ortofoto
     * @return El skeleton con las variables reemplazadas
     * @throws LocalgisConfigurationException Si ocurre algún error de configuración
     */
    private String replaceSkeletonVariables(String skeleton, String srid, String maxExtent, String layers, String urlMapServer, String idMunicipio, String mapServerConnection, String ortofotoExtension, String ortofotoSrid) throws LocalgisConfigurationException {
    	// Obtencion de datos sobre los sistemas de referencia
    	LocalgisSpatialRefSys spatialRefSys = localgisSpatialRefSysDAO.selectSpatialRefSysBySrid(Integer.parseInt(srid));
    	// Comprobamos si se han definido parametros adicionales para reproyección en localgis.properties
    	String addProjection="";
    	
    	/*addProjection=Configuration.getPropertyString("mapprojection."+srid);
    	if (addProjection!=null && addProjection.length()>0) {
    	   logger.debug("Añadiendo parametros proj4text "+addProjection);
    	   spatialRefSys.setProj4Text(spatialRefSys.getProj4Text()+addProjection);
    	}*/
    	spatialRefSys.setProj4Text("init=epsg:"+srid);
    	
    	LocalgisSpatialRefSys spatialRefSysOrtofoto = null;
    	if (ortofotoSrid != null && !ortofotoSrid.trim().equals("")) {
    		spatialRefSysOrtofoto = localgisSpatialRefSysDAO.selectSpatialRefSysBySrid(Integer.parseInt(ortofotoSrid));
    	}

    	// Lo primero es reemplazar las variables de queries porque suelen tener nuevas variables a expandir
        if (connectionType == WMSConfigurator.ORACLESPATIAL_CONNECTION) {
            skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_MUNICIPIOS_DATA, Configuration.MAPSERVER_CONFIG_FILE_MUNICIPIOS_DATA_ORACLESPATIAL);
            skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_DATA, Configuration.MAPSERVER_CONFIG_FILE_PROVINCIAS_DATA_ORACLESPATIAL);
            skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PARCELAS_DATA, Configuration.MAPSERVER_CONFIG_FILE_PARCELAS_DATA_ORACLESPATIAL);
        } else {
            skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_MUNICIPIOS_DATA, Configuration.MAPSERVER_CONFIG_FILE_MUNICIPIOS_DATA_POSTGIS);
            skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_DATA, Configuration.MAPSERVER_CONFIG_FILE_PROVINCIAS_DATA_POSTGIS);
            skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PARCELAS_DATA, Configuration.MAPSERVER_CONFIG_FILE_PARCELAS_DATA_POSTGIS);
        }
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_ENCODING, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_ENCODING));
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROJECTION, spatialRefSys.getProj4Text());
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_SRID, srid);
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_MAX_EXTENT, maxExtent);

        layers = layers.replaceAll(Configuration.VAR_MAPSERVER_SRID, srid);
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_LAYERS, layers);
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_ONLINE_RESOURCE, urlMapServer);
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_ID_MUNICIPIO, idMunicipio);
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_CONNECTION, mapServerConnection);
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_CONNECTIONTYPE, Configuration.getPropertyString(Configuration.PROPERTY_DB_CONNECTIONTYPE));
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_ORTOFOTOS_DIRECTORY, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_ORTOFOTOS_DIRECTORY));
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_ORTOFOTO_LAYER_NAME, Configuration.MAPSERVER_CONFIG_FILE_ORTOFOTO_LAYER_NAME);
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_ORTOFOTO_EXTENSION, ortofotoExtension != null ? ortofotoExtension : "");
        /*
         * Si aun no hay ortofoto y el parametro ortofotoSrid es null (o vacio) ponemos la proyeccion del mapa
         */
        String ortofotoProjectionTranslated;
        String ortofotoSridTranslated;
        if (ortofotoSrid == null || ortofotoSrid.trim().equals("")) {
            ortofotoProjectionTranslated = spatialRefSys.getProj4Text();
            ortofotoSridTranslated = srid;
        } else {
            ortofotoProjectionTranslated = spatialRefSysOrtofoto.getProj4Text();
            ortofotoSridTranslated = ortofotoSrid;
        }
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_ORTOFOTO_PROJECTION, ortofotoProjectionTranslated);
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_ORTOFOTO_SRID, ortofotoSridTranslated);
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_LAYER_NAME, Configuration.MAPSERVER_CONFIG_FILE_PROVINCIAS_LAYER_NAME);
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_OUTLINECOLOR, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_PROVINCIAS_OUTLINECOLOR));
        String provinciasMaxExtent;
        try {
            BoundingBox boundingBoxSpain = getBoundingBoxSpain(srid);
            provinciasMaxExtent = boundingBoxSpain.getMinx() + " " + boundingBoxSpain.getMiny() + " " + boundingBoxSpain.getMaxx() + " " + boundingBoxSpain.getMaxy();
        } catch (LocalgisConfigurationException e) {
            provinciasMaxExtent = Configuration.DEFAULT_SPAIN_MINX + " " + Configuration.DEFAULT_SPAIN_MINY + " " + Configuration.DEFAULT_SPAIN_MAXX + " " + Configuration.DEFAULT_SPAIN_MAXY;        
        }
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_MAX_EXTENT, provinciasMaxExtent);

        String mapserverCompatibility = Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_COMPATIBILITY);
        String varProvinciasName = Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_PROVINCIAS_NAME_ATTRIBUTE);
        if(mapserverCompatibility != null && mapserverCompatibility.equalsIgnoreCase("on"))
        	varProvinciasName = "'[" + varProvinciasName + "]'";
        else
        	varProvinciasName = "([" + varProvinciasName + "])";
        
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_NAME_ATTRIBUTE, varProvinciasName);        
        
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_NAME_COLOR, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_PROVINCIAS_NAME_COLOR));
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_NAME_SIZE, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_PROVINCIAS_NAME_SIZE));
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_MUNICIPIOS_LAYER_NAME, Configuration.MAPSERVER_CONFIG_FILE_MUNICIPIOS_LAYER_NAME);
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_MUNICIPIOS_OUTLINECOLOR, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_MUNICIPIOS_OUTLINECOLOR));

        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_POLYGON_REPORTS_LAYER_NAME, Configuration.MAPSERVER_CONFIG_FILE_POLYGON_REPORTS_LAYER_NAME);
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_LINE_REPORTS_LAYER_NAME, Configuration.MAPSERVER_CONFIG_FILE_LINE_REPORTS_LAYER_NAME);
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_POINT_REPORTS_LAYER_NAME, Configuration.MAPSERVER_CONFIG_FILE_POINT_REPORTS_LAYER_NAME);
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_REPORTS_TABLE_NAME, Configuration.MAPSERVER_CONFIG_FILE_REPORTS_TABLE_NAME);
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_REPORTS_IDENTIFIER_COLUMN_NAME, Configuration.MAPSERVER_CONFIG_FILE_REPORTS_IDENTIFIER_COLUMN_NAME);
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_REPORTS_IDENTIFIER_VALUE, Configuration.MAPSERVER_CONFIG_FILE_REPORTS_IDENTIFIER_VALUE);
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_REPORTS_POLYGON_SELECTED_OUTLINE_COLOR, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_REPORTS_POLYGON_SELECTED_OUTLINE_COLOR));
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_REPORTS_POLYGON_SELECTED_FILL_COLOR, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_REPORTS_POLYGON_SELECTED_FILL_COLOR));
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_REPORTS_LINE_SELECTED_COLOR, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_REPORTS_LINE_SELECTED_COLOR));
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_REPORTS_POINT_SELECTED_COLOR, Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_CONFIG_FILE_REPORTS_POINT_SELECTED_COLOR));
        
        // Sumamos uno para evitar problemas con los redondeos
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_MUNICIPIOS_MIN_SCALE, Double.toString(Configuration.MIN_SCALE_MUNICIPIOS + 1));
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_MUNICIPIOS_MAX_SCALE, Double.toString(Configuration.MAX_SCALE_MUNICIPIOS - 1));
        // Sumamos uno para evitar problemas con los redondeos
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_MIN_SCALE, Double.toString(Configuration.MIN_SCALE_PROVINCIAS + 1));
        skeleton = skeleton.replaceAll(Configuration.VAR_MAPSERVER_PROVINCIAS_MAX_SCALE, Double.toString(Configuration.MAX_SCALE_PROVINCIAS - 1));
        return skeleton;
    }
    
    /**
     * Método para eliminar los ficheros de estilos existentes para una entidad supramunicipal y una configuracion (publica o privada) determinada
     * @param geopistaEntidadSupramunicipal Entidad supramunicipal para la que se desea eliminar los ficheros de estilos del servidor de mapas
     * @param publicConfiguration Si se desea eliminar la configuracion publica (true) o privada (false)
     * @throws LocalgisConfigurationException Si sucede algun error de configuracion
     */
    private void removeStyleFiles(GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal, Boolean publicConfiguration) throws LocalgisConfigurationException {
        File directory = new File(Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_DIRECTORY)+Configuration.MAPSERVER_STYLES_FILE_DIRECTORY);
        if (!directory.isDirectory() || !directory.canWrite()) {
            throw new LocalgisConfigurationException("El directorio \""+directory.getAbsolutePath()+"\" no existe o no se puede escribir");
        }
        final String prefixStylesFilesToRemove;
        if (publicConfiguration.booleanValue()) {
            prefixStylesFilesToRemove = Configuration.MAPSERVER_STYLES_FILE_PREFIX_PUBLIC+geopistaEntidadSupramunicipal.getIdEntidad() + "_";
        } else {
            prefixStylesFilesToRemove = Configuration.MAPSERVER_STYLES_FILE_PREFIX_PRIVATE+geopistaEntidadSupramunicipal.getIdEntidad() + "_";
        }
        File[] filesToRemove = directory.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name != null && name.startsWith(prefixStylesFilesToRemove)) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        for (int i = 0; i < filesToRemove.length; i++) {
            if (!filesToRemove[i].delete()) {
                logger.error("El fichero \""+filesToRemove[i].getAbsolutePath()+"\" no se ha podido eliminar. Es posible que el sistema no quede correctamente configurado.");
            }
        }
    }

    /**
     * Método para eliminar los ficheros de mapas existentes para una entidad supramunicipal y una configuracion (publica o privada) determinada
     * @param geopistaEntidadSupramunicipal Entidad supramunicipal para la que se desea eliminar los ficheros de mapas del servidor de mapas
     * @param publicConfiguration Si se desea eliminar la configuracion publica (true) o privada (false)
     * @throws LocalgisConfigurationException Si sucede algun error de configuracion
     */
    private void removeMapServerFiles(GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal, Boolean publicConfiguration) throws LocalgisConfigurationException {
        File directory = new File(Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_DIRECTORY)+Configuration.MAPSERVER_CONFIG_FILE_DIRECTORY);
        if (!directory.isDirectory() || !directory.canWrite()) {
            throw new LocalgisConfigurationException("El directorio \""+directory.getAbsolutePath()+"\" no existe o no se puede escribir");
        }
        final String prefixFilesToRemove;
        if (publicConfiguration.booleanValue()) {
            prefixFilesToRemove = Configuration.MAPSERVER_CONFIG_FILE_PREFIX_PUBLIC+geopistaEntidadSupramunicipal.getIdEntidad() + "_";
        } else {
            prefixFilesToRemove = Configuration.MAPSERVER_CONFIG_FILE_PREFIX_PRIVATE+geopistaEntidadSupramunicipal.getIdEntidad() + "_";
        }
        File[] filesToRemove = directory.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                if (name != null && name.startsWith(prefixFilesToRemove)) {
                    return true;
                } else {
                    return false;
                }
            }
        });
        for (int i = 0; i < filesToRemove.length; i++) {
            if (!filesToRemove[i].delete()) {
                logger.error("El fichero \""+filesToRemove[i].getAbsolutePath()+"\" no se ha podido eliminar. Es posible que el sistema no quede correctamente configurado.");
            }
        }
    }
    
    /**
     * Método para configurar el fichero de estilos para una entidad supramunicipal, un mapa y una configuracion (publica o privada) determinada
     * @param geopistaEntidadSupramunicipal Entidad para la que se desea configurar el servidor de mapas
     * @param localgisMap Mapa para el que se desea configurar el servidor de mapas
     * @param publicConfiguration Si se desea configurar para los mapas publicos (true) o privados (false)
     * @param mapLocalgisLayers Map donde vamos guardando todas las capas para luego generar un fichero de mapa global
     * @param mapLocalgisMapServerLayers Map donde vamos guardando todas las capas externas para luego generar un fichero de mapa global
     * @param mapDefaultLocalgisStyles Map donde vamos guardando los estilos por defecto de todas las capas
     * @param mapServerConnection Conexion del MapServer
     * @param ortofotoExtension Extension de la ortofoto
     * @param ortofotoSrid SRID asociado a la ortofoto
     * @throws LocalgisWMSException Si sucede algun error con el servidor WMS
     * @throws LocalgisConfigurationException Si sucede algun error de configuracion
     */
    private void configureMapAndStylesFile(GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal, LocalgisMap localgisMap, Boolean publicConfiguration, Map mapLocalgisLayers, Map mapLocalgisMapServerLayers, Map mapDefaultLocalgisStyles, String maxExtentMunicipio, String mapServerConnection, String ortofotoExtension, String ortofotoSrid) throws LocalgisWMSException, LocalgisConfigurationException {
        // Creamos un StringBuffer para ir almacenando toda la configuracion de los estilos
        StringBuffer stylesConfiguration = new StringBuffer();
        
        // Creamos un StringBuffer para ir almacenando toda la configuracion del mapa
        StringBuffer mapConfiguration = new StringBuffer();
        
        // Escribimos el inicio del fichero de estilos
        stylesConfiguration.append(BEGIN_STYLES);
        //Obtenemos todas las capas del mapa
        List layers = localgisLayerDAO.selectLayersByIdMap(localgisMap.getMapid(), locale);
        Iterator itLayers = layers.iterator();
        while (itLayers.hasNext()   ) {
            LocalgisLayer localgisLayer = (LocalgisLayer)itLayers.next();
            
            boolean gestionarVersionables=true;
            if (localgisLayer.isVersionable() && gestionarVersionables) {
	        	localgisLayer.setColumnTime("time");
	        	localgisLayer.setTime("2010-01-01/3000-12-31");
	        	
	        	StringBuffer query = new StringBuffer(localgisLayer.getLayerquery());
	        	int index = query.toString().toUpperCase().indexOf(" FROM");
	        	//si creamos una vista que ya internamente tiene la revision actual y la revision expirada y obtenemos los datos como "*"
	        	//no tenemos que pedir los datos individualmente o el proceso fallara. Para eso introducimos al principio de la query esta 
	        	//cadena  SELECT '' as lcg_view_v_ce_ct, Si la cadena lcg_view viene en la query no metemos ni la revision actual ni la 
	        	//expirada. Este proceso se utiliza por ejemplo en las queries de las capas tipo CE CT.
	        	
	        	String tableBase=getTableName(query.toString());
	        	if (tableBase!=null)
	        		tableBase+=".";
	        	else
	        		tableBase="";
	        	
	        	if (query.toString().contains("lcg_view"))
	        		query.insert(index, ",to_timestamp('%TIME%','yyyy-MM-dd HH24:MI:ss') as time ");
	        	else
	        		if (query.toString().contains("revision_actual"))
		        		query.insert(index, ",to_timestamp('%TIME%','yyyy-MM-dd HH24:MI:ss') as time ");
	        		else
	        			query.insert(index, ", "+tableBase+"revision_actual, "+tableBase+"revision_expirada ,to_timestamp('%TIME%','yyyy-MM-dd HH24:MI:ss') as time ");
	        	
	        	
	        	//Solucion del problema de bloqueo del mapserver porque la query no estaba optimizada.
	        	/*
	        	query = query.append(" and "+tableBase+"revision_actual<=(select coalesce(max(revision),0) from versiones v, tables t, attributes a, columns c, layers l ");
	        	query = query.append(" where ");
	        	query = query.append(" ("+tableBase+"revision_actual<>-1 or "+tableBase+"revision_actual<>-2) ");
	        	query = query.append(" and a.id_column=c.id and c.id_table=v.id_table_versionada and a.id_layer=l.id_layer and l.name='");
	        	*/
	        	query = query.append(" and ("+tableBase+"revision_actual<>-1 or "+tableBase+"revision_actual<>-2) ");
	        	query = query.append(" and "+tableBase+"revision_actual<=(select coalesce(max(v.revision),0) from versiones v where id_table_versionada=(select max(id_table) ");
	        	query = query.append("from columns where id=(select max(id_column) from attributes where id_layer=(select id_layer from layers where name='"+localgisLayer.getLayername()+"'))) ");
	        	query = query.append("and fecha<=CASE position('-' in '%TIME%') WHEN 5 THEN to_timestamp('%TIME%','yyyy-MM-dd HH24:MI:ss') ELSE localtimestamp END) ");

	        	query = query.append(" and "+tableBase+"revision_expirada>(select coalesce(max(v.revision),0) from versiones v where id_table_versionada=(select max(id_table) ");
	        	query = query.append("from columns where id=(select max(id_column) from attributes where id_layer=(select id_layer from layers where name='"+localgisLayer.getLayername()+"'))) ");
	        	query = query.append("and fecha<=CASE position('-' in '%TIME%') WHEN 5 THEN to_timestamp('%TIME%','yyyy-MM-dd HH24:MI:ss') ELSE localtimestamp END) ");
	               	/*
	        	query = query.append(" and "+tableBase+"revision_actual<=(select coalesce(max(revision),0) from versiones v, tables t, attributes a, columns c, layers l ");
	        	query = query.append(" where ");
	        	query = query.append(" ("+tableBase+"revision_actual<>-1 or "+tableBase+"revision_actual<>-2) ");
	        	query = query.append(" and a.id_column=c.id and c.id_table=v.id_table_versionada and a.id_layer=l.id_layer and l.name='");
	        	
	        	query = query.append(localgisLayer.getLayername());
	        	query = query.append("' and fecha<=CASE position('-' in '%TIME%') WHEN 5 THEN to_timestamp('%TIME%','yyyy-MM-dd HH24:MI:ss') ELSE localtimestamp END) ");
	        	query = query.append(" and "+tableBase+"revision_expirada>(select coalesce(max(revision),0) from versiones v, tables t, attributes a, columns c, layers l ");
	        	query = query.append(" where a.id_column=c.id and c.id_table=v.id_table_versionada and a.id_layer=l.id_layer and l.name='");
	        	query = query.append(localgisLayer.getLayername());
	        	query = query.append("' and fecha<=CASE position('-' in '%TIME%') WHEN 5 THEN to_timestamp('%TIME%','yyyy-MM-dd HH24:MI:ss') ELSE localtimestamp END) ");
	        	*/
	        	//logger.error(query.toString());
	        	//String queryAConvertir=query.toString();
	        	//queryAConvertir=queryAConvertir.replaceAll("'", "\"");
	        	//System.out.println("QUERY:"+localgisLayer.getLayername()+":"+query.toString());
	        	localgisLayer.setLayerquery(query.toString());
            }
            else if (localgisLayer.isVersionable2()) {
            	int a=1;
            	/*
            	String tableBase=getTableName(localgisLayer.getLayerquery());
	        	if (tableBase!=null)
	        		tableBase+=".";
	        	else
	        		tableBase="";
	        	
            	StringBuffer query = new StringBuffer(localgisLayer.getLayerquery());
            	query = query.append(" and ("+tableBase+"revision_actual<>-1 || ("+tableBase+"revision_actual<>-2)");
	        	localgisLayer.setLayerquery(query.toString());
	        	//System.out.println("QUERY2:"+localgisLayer.getLayername()+":"+query.toString());*/
	
            }
            else{
            	String tableBase=getTableName(localgisLayer.getLayerquery());
	        	if (tableBase!=null)
	        		tableBase+=".";
	        	else
	        		tableBase="";
	        	
            	StringBuffer query = new StringBuffer(localgisLayer.getLayerquery());
            	//query = query.append(" and ("+tableBase+"revision_actual<>-1 or ("+tableBase+"revision_actual<>-2)");
            	//query = query.append(" and ("+tableBase+"revision_expirada=9999999999)");
	        	localgisLayer.setLayerquery(query.toString());
   
            }
            
            //Configuramos los estilos
            configureLayerStyle(stylesConfiguration, localgisLayer, localgisMap.getMapid(), mapDefaultLocalgisStyles);
            configureLayer(mapConfiguration, localgisMap, localgisLayer, geopistaEntidadSupramunicipal, maxExtentMunicipio, publicConfiguration, mapServerConnection,localgisMap.getMapid());
            
            localgisLayer.setMapId(localgisMap.getMapid());
            mapLocalgisLayers.put(localgisLayer.getLayerid(), localgisLayer);
        }
        // Escribimos el final del fichero de estilos
        stylesConfiguration.append(END_STYLES);
        
        //Obtenemos todas las capas externas del mapa
        List serverLayers = localgisMapServerLayerDAO.selectMapServerLayersByIdMap(localgisMap.getMapid());
        Iterator itMapServerLayers = serverLayers.iterator();
        while (itMapServerLayers.hasNext()   ) {
            LocalgisMapServerLayer localgisMapServerLayer = (LocalgisMapServerLayer)itMapServerLayers.next();
            configureLayer(mapConfiguration, localgisMapServerLayer, geopistaEntidadSupramunicipal, maxExtentMunicipio);
            mapLocalgisMapServerLayers.put(localgisMapServerLayer.getMapid(), localgisMapServerLayer);
        }

        // Escribimos el fichero de estilos
        writeStylesFile(geopistaEntidadSupramunicipal, localgisMap, publicConfiguration, stylesConfiguration.toString());

        // Escribimos el fichero de map
        writeMapFile(geopistaEntidadSupramunicipal, localgisMap, publicConfiguration, mapConfiguration.toString(), maxExtentMunicipio, Configuration.MAPSERVER_CONFIG_FILE_SKELETON, null, mapServerConnection, ortofotoExtension, ortofotoSrid);
        
        // Aplicamos los estilos al fichero .map
        try {
        	
            applyStylesToMap(geopistaEntidadSupramunicipal, localgisMap, publicConfiguration);
        } catch (Throwable e) {
            logger.error("Error grave al configurar el sistema", e);
            throw new LocalgisWMSException("Error al configurar el fichero de estilos",e);
        }
    }
    
    /**
     * Obtiene el valor de la tabla de la query
     * @param layerquery
     * @return
     */
    private String getTableName(String layerquery){
    	
    	String resultado=null;
    	try {					
			String posiciontabla="transform(";
			int posicionInicial=layerquery.indexOf(posiciontabla);
			//Problema si la tabla tiene un punto en su nombre.
			int posicionFinal=layerquery.indexOf(".",posicionInicial);
			resultado=layerquery.substring(posicionInicial+posiciontabla.length(),posicionFinal).trim();
			//System.out.println("Resultado:<"+resultado+">");
			
			
		} catch (Exception e) {
		}
    	return resultado;
    }
    
    public static void main(String args[]){
    	String cadena="SELECT \"eiel_c_ce\".oid as id_localgis, transform(\"eiel_c_ce\".\"GEOMETRY\" , $SRID$) as ";
    	//String resultado=getTableName(cadena);
    	//System.out.println("Resultado:"+resultado);
    }

    /**
     * Escribe el fichero global de estilos para una entidad  y una
     * configuracion publica o privada a partir de un map donde estan
     * almacenados los estilos
     * 
     * @param geopistaEntidadSupramunicipal Entidad
     * @param publicConfiguration Configuracion publica o privada
     * @param mapLocalgisStyles Map donde estan los estilos. Las claves serán los nombres de las capas y los valores serán objetos LocalgisStyle
     * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
     * @throws LocalgisWMSException Si sucede algun error con el servidor WMS
     */
    private void writeGlobalStylesFile(GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal, Boolean publicConfiguration, Map mapLocalgisStyles) throws LocalgisConfigurationException, LocalgisWMSException {
        // Creamos un StringBuffer para ir almacenando toda la configuracion de los estilos
        StringBuffer stylesConfiguration = new StringBuffer();
        
        // Escribimos el inicio del fichero de estilos
        stylesConfiguration.append(BEGIN_STYLES);

        Iterator itKeys = mapLocalgisStyles.keySet().iterator();
        while (itKeys.hasNext()) {
            String layerName = (String) itKeys.next();
            LocalgisStyle localgisStyle = (LocalgisStyle) mapLocalgisStyles.get(layerName);
            configureStyle(stylesConfiguration, layerName, localgisStyle);
        }

        // Escribimos el final del fichero de estilos
        stylesConfiguration.append(END_STYLES);

        String fileNameStyles = getStylesFilename(geopistaEntidadSupramunicipal.getIdEntidad(), null, publicConfiguration);
        try {
            FileUtils.writeContentToFile(stylesConfiguration.toString(), fileNameStyles, "ISO-8859-1");
                       
            sendSFTPFile(fileNameStyles,"mapserver.ftp.styles.directory");
        } catch (FileNotFoundException e) {
            logger.error("El fichero de estilos del servidor de mapas no existe o no se puede escribir.", e);
            throw new LocalgisWMSException("El fichero de estilos del servidor de mapas no existe o no se puede escribir.");
        } catch (IOException e) {
            logger.error("Error de entrada/salida al escribir el fichero de estilos del servidor de mapas.", e);
            throw new LocalgisWMSException("Error de entrada/salida al escribir el fichero de estilos del servidor de mapas.");
        }
    }

    /**
     * Escribe un fichero de estilos
     * @param geopistaEntidadSupramunicipal Entidad a la que pertenece
     * @param localgisMap Mapa al que pertenece
     * @param publicConfiguration Configuracion publica o privada del mapa
     * @param stylesConfiguration Configuracion de los estilos
     * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
     * @throws LocalgisWMSException Si sucede algun error con el servidor WMS
     */
    private void writeStylesFile(GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal, LocalgisMap localgisMap, Boolean publicConfiguration, String stylesConfiguration) throws LocalgisConfigurationException, LocalgisWMSException {
        String fileNameStyles = getStylesFilename(geopistaEntidadSupramunicipal.getIdEntidad(), localgisMap.getMapid(), publicConfiguration);
        try {
            FileUtils.writeContentToFile(stylesConfiguration.toString(), fileNameStyles, "ISO-8859-1");
            
			
            sendSFTPFile(fileNameStyles,"mapserver.ftp.styles.directory");
        } catch (FileNotFoundException e) {
            logger.error("El fichero de estilos del servidor de mapas no existe o no se puede escribir.", e);
            throw new LocalgisWMSException("El fichero de estilos del servidor de mapas no existe o no se puede escribir.");
        } catch (IOException e) {
            logger.error("Error de entrada/salida al escribir el fichero de estilos del servidor de mapas.", e);
            throw new LocalgisWMSException("Error de entrada/salida al escribir el fichero de estilos del servidor de mapas.");
        }
    }

    /**
     * Escribe el fichero global de mapas (el que contiene todas las capas de una 
     * entidad supramunicipal determinada, para una configuracion publica o privada)
     * @param geopistaEntidadSupramunicipal Entidad supramunicipal
     * @param localgisMap Mapa a escribir
     * @param publicConfiguration Configuracion publica o privada
     * @param maxExtentMunicipio Maxima extensión del municipio
     * @param mapLocalgisLayers Mapa de capas de localgis
     * @param mapLocalgisMapServerLayers Mapa de capas externas
     * @param mapServerConnection Conexion del MapServer
     * @param ortofotoExtension Extension de la ortofoto
     * @param ortofotoSrid SRID asociado a la ortofoto
     * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
     * @throws LocalgisWMSException Si sucede algun error con el servidor WMS
     */
    private void writeGlobalMapFile(GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal, Boolean publicConfiguration, String maxExtentMunicipio, Map mapLocalgisLayers, Map mapLocalgisMapServerLayers, String mapServerConnection, String ortofotoExtension, String ortofotoSrid) throws LocalgisWMSException, LocalgisConfigurationException {
        StringBuffer mapConfiguration = new StringBuffer();
        //Iteramos sobre las capas normales 
        Iterator itLocalgisLayers = mapLocalgisLayers.values().iterator();
        while (itLocalgisLayers.hasNext()) {
            LocalgisLayer localgisLayer = (LocalgisLayer)itLocalgisLayers.next();
            configureLayer(mapConfiguration, null, localgisLayer, geopistaEntidadSupramunicipal, maxExtentMunicipio, publicConfiguration, mapServerConnection,localgisLayer.getMapId());
        }

        // Iteramos sobre las capas externas
        Iterator itLocalgisMapServerLayers = mapLocalgisMapServerLayers.values().iterator();
        while (itLocalgisMapServerLayers.hasNext()) {
            LocalgisMapServerLayer localgisMapServerLayer = (LocalgisMapServerLayer)itLocalgisMapServerLayers.next();
            configureLayer(mapConfiguration, localgisMapServerLayer, geopistaEntidadSupramunicipal, maxExtentMunicipio);
        }

        writeMapFile(geopistaEntidadSupramunicipal, null, publicConfiguration, mapConfiguration.toString(), maxExtentMunicipio, Configuration.MAPSERVER_CONFIG_FILE_SKELETON, null, mapServerConnection, ortofotoExtension, ortofotoSrid);
    }

    /**
     * Escribe un fichero de mapas
     * @param geopistaEntidadSupramunicipal Entidad a la que pertenece 
     * @param localgisMap Mapa a escribir
     * @param publicConfiguration Configuracion publica o privada del mapa
     * @param mapConfiguration Configuracion del mapa
     * @param maxExtentMunicipio Maxima extension del municipio
     * @param nameResourceConfig Nombre del recurso a partir del que se genera la configuracion (fichero skeleton.map)
     * @param nameResourceExtraConfig Nombre del fichero extra donde hay mas capas (fichero extra.map) 
     * @param mapServerConnection Conexion del MapServer
     * @param ortofotoExtension Extension de la ortofoto
     * @param ortofotoSrid SRID asociado a la ortofoto
     * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
     * @throws LocalgisWMSException Si sucede algun error con el servidor WMS
     */
    private void writeMapFile(GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal, LocalgisMap localgisMap, Boolean publicConfiguration, String mapConfiguration, String maxExtentMunicipio, String nameResourceConfig, String nameResourceExtraConfig, String mapServerConnection, String ortofotoExtension, String ortofotoSrid) throws LocalgisWMSException, LocalgisConfigurationException {
        // Escribimos al fichero la configuracion a partir del skeleton y el fichero extra de capas si lo hubiera
        String skeletonString = null;
        try {
            skeletonString = FileUtils.readContentFromResource(nameResourceConfig);
        } catch (FileNotFoundException e) {
            logger.error("El fichero skeleton del servidor de mapas no existe o no se puede leer.", e);
            throw new LocalgisWMSException("El fichero skeleton del servidor de mapas no existe o no se puede escribir.");
        } catch (IOException e) {
            logger.error("Error al leer el fichero skeleton de configuracion.", e);
            throw new LocalgisWMSException("Error al leer el fichero skeleton de configuracion.");
        }

        String urlMapServer = Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_SERVER)+Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_DIRECTORY)+"/"+geopistaEntidadSupramunicipal.getIdEntidad()+"/"; 
        if (localgisMap != null) {
            urlMapServer += localgisMap.getMapid()+ "/";
        }
        if (publicConfiguration != null) {
            if (publicConfiguration.booleanValue()) {
                urlMapServer += "public";
            } else {
                urlMapServer += "private";
            }
            urlMapServer += "/";
        }
        urlMapServer += Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_FILE);

        String srid = null;
        if (localgisMap != null) {
        	srid = localgisMap.getSrid();
        }
        if (srid == null || srid.equals("")) {
        	srid = geopistaEntidadSupramunicipal.getSrid();
        }

        String skeletonExtraString = null;
        if (nameResourceExtraConfig != null) {
            try {
                skeletonExtraString = FileUtils.readContentFromResource(nameResourceExtraConfig);
                skeletonExtraString = replaceSkeletonVariables(skeletonExtraString, srid, maxExtentMunicipio,  mapConfiguration != null ? mapConfiguration : "", urlMapServer, geopistaEntidadSupramunicipal.getIdEntidad().toString(), mapServerConnection, ortofotoExtension, ortofotoSrid);
                
                if (mapConfiguration != null) {
                    mapConfiguration += "\n" + skeletonExtraString;
                } else {
                    mapConfiguration = skeletonExtraString;
                }
            } catch (FileNotFoundException e) {
                logger.info("El fichero skeleton extra del servidor de mapas no existe o no se puede leer.", e);
            } catch (IOException e) {
                logger.error("Error al leer el fichero skeleton de configuracion.", e);
            }
        }
        // Si tenemos cosas definidos en el skeletonExtra (debería haber capas), las concatenamos en la configuracion
        
        String fileName = getMapFilename(geopistaEntidadSupramunicipal.getIdEntidad(), localgisMap != null ? localgisMap.getMapid() : null, publicConfiguration);
        
        // Reemplazo de las variables del skeleton
        skeletonString = replaceSkeletonVariables(skeletonString, srid, maxExtentMunicipio, mapConfiguration != null ? mapConfiguration : "", urlMapServer, geopistaEntidadSupramunicipal.getIdEntidad().toString(), mapServerConnection, ortofotoExtension, ortofotoSrid);
        
        try {
            FileUtils.writeContentToFile(skeletonString, fileName, null);
            
            //Ademas de escribirlo intentamos enviar por FTP 
            //Para depuraciones en local es interesante porque normalemente no tenemos un mapserver en local.
            sendSFTPFile(fileName,"mapserver.ftp.map.directory");
        } catch (FileNotFoundException e) {
            logger.error("El fichero de configuracion del servidor de mapas no existe o no se puede escribir.", e);
            throw new LocalgisWMSException("El fichero de configuracion del servidor de mapas no existe o no se puede escribir.");
        } catch (IOException e) {
            logger.error("Error de entrada/salida al escribir el fichero de configuracion del servidor de mapas.", e);
           throw new LocalgisWMSException("Error de entrada/salida al escribir el fichero de configuracion del servidor de mapas.");
        }

    }

    /**
     * Aplica los estilos SLD a un fichero .map y lo salva. El localgisMap podra ser nulo para aplicar los estilos al fichero global de mapas
     * @param geopistaEntidadSupramunicipal Entidad a la que pertenece el mapa
     * @param localgisMap Mapa al que se desean aplicar los estilos
     * @param publicConfiguration Si el mapa es publico o privado
     * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
     */
    private void applyStylesToMap(GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal, LocalgisMap localgisMap, Boolean publicConfiguration) throws LocalgisConfigurationException {
    	Integer mapId = localgisMap != null ? localgisMap.getMapid() : null;
        String filenameMap = getMapFilename(geopistaEntidadSupramunicipal.getIdEntidad(), mapId, publicConfiguration);
        mapObj mapObj = new mapObj(filenameMap);
        
        // Si es un Mapserver6 tenemos que corregir el OUTPUTFORMAT
        String mapserverCompatibility = Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_COMPATIBILITY);
        if(mapserverCompatibility != null && mapserverCompatibility.equalsIgnoreCase("on")){
        
        	if(!mapObj.getOutputformat().getDriver().equals("AGG/PNG"))
        		mapObj.getOutputformat().setDriver("AGG/PNG");
        }
        
        /*
         * Obtenemos los tipos de las capas antes de aplicar los estilos para
         * evitar que se nos modifiquen los tipos, ya que se ha detectado que a
         * veces se modifica el tipo y pasa a ser annotation (concretamente
         * cuando tenemos un estilo de siempre visible y otro estilo en una
         * escala de 1 a 1000, por ejemplo)
         */
        int numLayers = mapObj.getNumlayers();
        MS_LAYER_TYPE[] typeLayers = new MS_LAYER_TYPE[numLayers];
        for (int i = 0; i < numLayers; i++) {
            typeLayers[i] = mapObj.getLayer(i).getType();
        }
        String stylesURL = getStylesURL(geopistaEntidadSupramunicipal.getIdEntidad(), mapId, publicConfiguration);
        mapObj.applySLDURL(stylesURL);
       
        //Si utilizamos Mapserver6 una vez aplicados los estilos se deben arreglar los symbol invalidos
        HashMap<String, int[]> patternToApplyList = new HashMap<String, int[]>();
        if(mapserverCompatibility != null && mapserverCompatibility.equalsIgnoreCase("on")){
	        for (int i=0; i < mapObj.getSymbolset().getNumsymbols(); i++){
	    		symbolObj symbol = mapObj.getSymbolset().getSymbol(i);  
	    		
	    		//En MapServer6 los symbol no pueden tener GAP
	    		symbol.setGap(0);
	    		
	    		//En MapServer6 los symbol Tipo Vector, para dibujar un + o x pintan dos 
	    		//lineas y la separacion antes era un punto (-1,-1) ahora es (-99, -99)
	    		String symbolName = symbol.getName();
	    		if(symbol.getType() == 1001 && symbolName!= null){
	    			if(symbolName.contains("_cross") || symbolName.contains("_x")){    			
	    				lineObj lineaPuntos = symbol.getPoints();
	    				lineaPuntos.set(2, new pointObj(-99, -99, 0));
	    				symbol.getPoints().delete();
	    				symbol.setPoints(lineaPuntos);
	    			}
	    		}
	    		
	    		/* En MapServer6 los symbol no pueden tener el patron
	    		 * por lo que se copia el patron para aplicarlo mas adelante 
	    		 * y se elimina del symbol
	    		*/
	    		if(symbol.getPatternlength() > 0){
	    			int[] oldPattern = symbol.getPattern();
	    			int patternToApplyLength = symbol.getPatternlength();
	    			int[] patternToApply = new int[patternToApplyLength];
	    			/* Solo copio los elementos de la longitud del patron ya que 
	    			 * puede venir con mas valores que no son validos
	    			*/
	    			for(int z=0; z < patternToApplyLength; z++){
	    				patternToApply[z] = oldPattern[z];
	    			}
	    			String nameSymbolRef = symbol.getName();
	    			patternToApplyList.put(nameSymbolRef ,patternToApply);
	    			
	    			symbol.setPatternlength(0);
	    		}
	    	}
        }
        
        /*
         * Para cada capa reestablecemos el tipos para evitar modificaciones y ordenamos los class
         * segun las escalas, siguiendo estas reglas:
         */
        for (int i = 0; i < numLayers; i++) {
            layerObj layer = mapObj.getLayer(i);
            layer.setType(typeLayers[i]); 
            sortClasses(layer);
            
            if(mapserverCompatibility != null && mapserverCompatibility.equalsIgnoreCase("on")){	
       
            	for (int j=0 ; j < layer.getNumclasses(); j++){
            		classObj layerClass = layer.getClass(j);
            		
            		/*
                     * Si el class de la layer tiene un estilo donde utiliza
                     * un symbol con un patron anteriormente corregido, es necesario 
                     * marcarlo para poder corregir ese estilo añadiendo el patron
                     * y eliminando el symbol
                     */
            		for(int z=0; z < layerClass.getNumstyles(); z++){
            			String nameSymbol = layerClass.getStyle(z).getSymbolname();
            			if(patternToApplyList.containsKey(nameSymbol))
            				/* Convertimos en variable el nombre del symbol 
            				 * dentro del estilo para poder modificarlo
            				 */
            				layerClass.getStyle(z).setSymbolname("%" +nameSymbol+ "%");
            		}
            		
            		/*
                     * Para cada capa se debe modificar la expresion de TEXT si existe.
                     * En MapServer6 cambia el texto de ([texto]) a 
                     * expresiones como '[texto]' o como "[texto]"
                     *
                     */
            		if (layerClass.getTextString() != null && layerClass.getTextString().startsWith("([")){
            			String newLayerClassText = layerClass.getTextString().replace('(', '"');
            			newLayerClassText = newLayerClassText.replace(')', '"');	
            			layer.getClass(j).setText(newLayerClassText);
            		}
            		
            		/*
                     * Si la capa es de lineas y tiene textos como por ejemplo las calles donde
                     * se muestran los nombres de la calle, es necesario unir el LABEL dentro del CLASS
                     */
            		if(layer.getType() == MS_LAYER_TYPE.MS_LAYER_LINE){
            			labelObj layerClassLabel= layerClass.getLabel();
            			if(layerClassLabel != null && layerClassLabel.getFont() != null){
            				if(layer.getNumclasses() > 1){
	            				cloneLabel(layer.getClass(j-1).getLabel(), layerClassLabel);
	            				layer.getClass(j-1).setText(layerClass.getTextString());
	            				layer.removeClass(j);
            				}
            			}
            		}
            	}
            }
            else{
            	//NUEVO
            	//Si el estilo de texto es "Elemento Temporal" o "Elemento Publicable" 
            	//lo ponemos distinto a si se utiliza una variable
            	
            	for (int j=0 ; j < layer.getNumclasses(); j++){
            		classObj layerClass = layer.getClass(j);            		            		
            		/*
                     * Para cada capa se debe modificar la expresion de TEXT si existe.
                     * En MapServer6 cambia el texto de ([texto]) a 
                     * expresiones como '[texto]' o como "[texto]"
                     *
                     */
            		if (layerClass.getTextString() != null && layerClass.getTextString().startsWith("([")){
            			if (layerClass.getTextString().equals("([Elemento Temporal])")){ 
            				layer.removeClass(j);
            				//layer.getClass(j).setText(("Elemento Temporal"));
            			}            			
            		}
            	}
            	
            	//Lo hacemos dos veces para temporal y publicable porque cuando borras el class se mueve la posicion y no
            	//pilla la siguiente
            	
            	for (int j=0 ; j < layer.getNumclasses(); j++){
            		classObj layerClass = layer.getClass(j);            		            		
            		/*
                     * Para cada capa se debe modificar la expresion de TEXT si existe.
                     * En MapServer6 cambia el texto de ([texto]) a 
                     * expresiones como '[texto]' o como "[texto]"
                     *
                     */
            		if (layerClass.getTextString() != null && layerClass.getTextString().startsWith("([")){
            			if (layerClass.getTextString().equals("([Elemento Publicable])")){       
            				layer.removeClass(j);
            				//layer.getClass(j).setText(("Elemento Publicable"));
            			}
            		}
            	}
            	
            	for (int j=0 ; j < layer.getNumclasses(); j++){
            		classObj layerClass = layer.getClass(j);            		            		
            		/*
                     * Para cada capa se debe modificar la expresion de TEXT si existe.
                     * En MapServer6 cambia el texto de ([texto]) a 
                     * expresiones como '[texto]' o como "[texto]"
                     *
                     */
            		if (layerClass.getTextString() != null && layerClass.getTextString().startsWith("([")){
            			if (layerClass.getTextString().equals("([Elemento Borrable])")){       
            				layer.removeClass(j);
            				//layer.getClass(j).setText(("Elemento Borrable"));
            			}
            		}
            	}
            }
        }
        
        mapObj.save(filenameMap);
        
        if(mapserverCompatibility != null && mapserverCompatibility.equalsIgnoreCase("on")){
	        if(!patternToApplyList.isEmpty())
	        	try{
			        String mapFileContent = FileUtils.readContentFromFile(filenameMap);
			       
			        //Si existen patrones por aplicar, se deben recorrer todas las variables creadas y reemplazar su valor
			        for(Iterator it = patternToApplyList.keySet().iterator(); it.hasNext();){
			        	String nameSymbolKey = (String) it.next();
			        	int[] pattern = patternToApplyList.get(nameSymbolKey);
			        	
			        	String symbolToReplace = "SYMBOL \"%"+nameSymbolKey+"%\"";
			        	String patternValue = "PATTERN ";
			        	for(int i=0; i <pattern.length; i++ ){
			        		patternValue = patternValue + pattern[i] +" "; 
			        	}
			        	patternValue = patternValue +"END";
			        	mapFileContent = mapFileContent.replaceAll(symbolToReplace, patternValue);
			        }
			        
			        FileUtils.writeContentToFile(mapFileContent, filenameMap, null);
		        }
		        catch(Exception e){
		        	logger.error("Error al intentar reemplazar los symbol por los patrones correspondientes");
		        	e.printStackTrace();
		        }
        }
        
        
        //Lo enviamos por FTP para que lo tengamos tambien
        generateRemoteStyleFile(filenameMap,stylesURL);
   
        
        //Eliminamos el fichero de estilos
        File stylesFile = new File(getStylesFilename(geopistaEntidadSupramunicipal.getIdEntidad(), mapId, publicConfiguration));
        if (stylesFile.exists() && stylesFile.canWrite()) {
        	
        	//Lo borramos de FTP por si acaso
        	removeSFTPFile(getStylesFilename(geopistaEntidadSupramunicipal.getIdEntidad(), mapId, publicConfiguration),"mapserver.ftp.styles.directory");
        	
            if (stylesFile.delete()) {
                logger.debug("Fichero de estilos \""+stylesFile+"\" eliminado correctamente");
            } else {
                logger.error("Error al eliminar el fichero de estilos \""+stylesFile+"\" borrado correctamente");
            }
        } else {
            logger.error("El fichero de estilos \""+stylesFile+"\" no existe o no se puede escribir");
        }
    }
    
    private void cloneLabel(labelObj labelDestino, labelObj labelOrigen){
    	labelDestino.setAngle(labelOrigen.getAngle());
    	labelDestino.setAntialias(labelOrigen.getAntialias());
    	labelDestino.setAutoangle(labelOrigen.getAutoangle());
    	labelDestino.setAutofollow(labelOrigen.getAutofollow());
    	labelDestino.setAutominfeaturesize(labelOrigen.getAutominfeaturesize());
    	labelDestino.setBackgroundcolor(labelOrigen.getBackgroundcolor());
    	labelDestino.setBackgroundshadowcolor(labelOrigen.getBackgroundshadowcolor());
    	labelDestino.setBackgroundshadowsizex(labelOrigen.getBackgroundshadowsizex());
    	labelDestino.setBackgroundshadowsizey(labelOrigen.getBackgroundshadowsizey());
    	labelDestino.setBuffer(labelOrigen.getBuffer());
    	labelDestino.setColor(labelOrigen.getColor());
    	labelDestino.setEncoding(labelOrigen.getEncoding());
    	labelDestino.setFont(labelOrigen.getFont());
    	labelDestino.setForce(labelOrigen.getForce());
    	labelDestino.setMaxsize(labelOrigen.getMaxsize());
    	labelDestino.setMindistance(labelOrigen.getMindistance());
    	labelDestino.setMinfeaturesize(labelOrigen.getMinfeaturesize());
    	labelDestino.setMinsize(labelOrigen.getMinsize());
    	labelDestino.setOffsetx(labelOrigen.getOffsetx());
    	labelDestino.setOffsety(labelOrigen.getOffsety());
    	labelDestino.setOutlinecolor(labelOrigen.getOutlinecolor());
    	labelDestino.setPartials(labelOrigen.getPartials());
    	labelDestino.setPosition(labelOrigen.getPosition());
    	labelDestino.setPriority(labelOrigen.getPriority());
    	labelDestino.setShadowcolor(labelOrigen.getShadowcolor());
    	labelDestino.setShadowsizex(labelOrigen.getShadowsizex());
    	labelDestino.setShadowsizey(labelOrigen.getShadowsizey());
    	labelDestino.setSize(labelOrigen.getSize());
    	labelDestino.setType(labelOrigen.getType());
    	labelDestino.setWrap(labelOrigen.getWrap());
    }
    
    /**
     * Método para ordenar los class de una capa para que el mapserver se
     * comporte igual que el editor gis. La forma de ordenar es la siguiente:<br>
     * 1º por MAXSCALEDENOM: Irá primero la que menor maxscaledenom tenga.<br>
     * 2º por MINSCALEDENOM: En caso de igualdad en maxscaledenom, irá primero
     * el que mayor minscaledenom tenga.<br>
     * 3º por expresion: En caso de igualdad en minscaledenom y de
     * maxscaledenom, irá primero el que tenga una expresion<br>
     * Ordenamos según el algoritmo "bubble sort" porque tenemos un metodo en el
     * objeto layer que justamente hace un swap entre dos posiciones
     * consecutivas (método moveClassDown)
     * 
     * @param layer
     *            Capa a ordenar
     */
    private void sortClasses(layerObj layer) {
        int numClasses = layer.getNumclasses();
        boolean swapped;
        int i = numClasses;
        do {
            swapped = false;
            i = i - 1;
            for (int j = 0; j < i; j++) {
                classObj class1 = layer.getClass(j);
                classObj class2 = layer.getClass(j+1);
                if ((class2.getMaxscaledenom() < class1.getMaxscaledenom()) ||
                    (class1.getMaxscaledenom() == class2.getMaxscaledenom() && class2.getMinscaledenom() > class1.getMinscaledenom()) ||
                    (class1.getMaxscaledenom() == class2.getMaxscaledenom() && class1.getMinscaledenom() == class2.getMinscaledenom() && class2.getExpressionString() != null && class1.getExpressionString() == null)) {
                    layer.moveClassDown(j);
                    swapped = true;
                }
            }
        } while (swapped);
    }
    /**
     * Configura una capa y la almacena en un StringBuffer
     * @param configuration StringBuffer donde se va almacenando la configuracion
     * @param localgisLayer Capa a configurar
     * @param geopistaEntidadSupramunicipal Entidad que estamos configurando
     * @param maxExtents Extension del municipio como String
     * @param publicConfiguration Si se desea configurar para los mapas publicos (true) o privados (false)
     * @param mapServerConnection Conexion del MapServer
     * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
     */
    private void configureLayer(StringBuffer configuration, LocalgisMap localgisMap, LocalgisLayer localgisLayer, GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal, String maxExtents, Boolean publicConfiguration, String mapServerConnection,Integer mapId) throws LocalgisConfigurationException {
        // Establecer el srid del mapa o de la entidad
    	String srid = null;
        if (localgisMap != null) {
        	srid = localgisMap.getSrid();
        }
        if (srid == null || srid.equals("")) {
        	srid = geopistaEntidadSupramunicipal.getSrid();
        }
        
        configuration.append("LAYER\n");
        configuration.append("\tNAME \"").append(localgisLayer.getLayername()).append("\"\n");   
        configuration.append("\tSTATUS OFF\n");   
        configuration.append("\t#DEBUG OFF\n");    
        configuration.append("\tDEBUG 5\n");    
        configuration.append("\tTOLERANCE 13\n");    
        configuration.append("\tDUMP true\n");  
        String geometrytype = localgisLayer.getGeometrytype();
        if (geometrytype != null && (geometrytype.equals("gml:PointPropertyType") || geometrytype.equals("gml:MultiPointPropertyType"))) {
            configuration.append("\tTYPE POINT\n");   
        } else if (geometrytype != null && (geometrytype.equals("gml:PolygonPropertyType") || geometrytype.equals("gml:MultiPolygonPropertyType"))){
            configuration.append("\tTYPE POLYGON\n");   
        } else if (geometrytype != null && (geometrytype.equals("gml:LineStringPropertyType") || geometrytype.equals("gml:MultiLineStringPropertyType"))) { 
            configuration.append("\tTYPE LINE\n");   
        } else {
            configuration.append("\tTYPE POLYGON\n");   
        }
        configuration.append("\tCONNECTIONTYPE "+Configuration.getPropertyString(Configuration.PROPERTY_DB_CONNECTIONTYPE)+"\n");   
        configuration.append("\tCONNECTION \""+mapServerConnection+"\"\n");
        /*
         * Escapamos las comillas simples. Para ello tenemos que poner en el
         * string dos backslashes para que cuando se vuelquen al fichero se
         * mantenga una de ellos. Al tratar con expresiones regulares habrá que
         * poner cuatro backslashes en la expresion a reemplazar para conseguir
         * uno en el resultado
         */
        String mapserverCompatibility = Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_COMPATIBILITY);
        String layerQuery = null;
        if(mapserverCompatibility != null && mapserverCompatibility.equalsIgnoreCase("on"))
        	layerQuery = localgisLayer.getLayerquery();        
        else
        	layerQuery = localgisLayer.getLayerquery().replaceAll("'", "&#39;").replaceAll("\"", "&#34;");
        
        if (connectionType == WMSConfigurator.ORACLESPATIAL_CONNECTION) {
            configuration.append("\tDATA 'GEOMETRY from (").append(layerQuery).append(" ) USING UNIQUE "+ALIAS_ID+" SRID ").append(srid).append("'\n");
        } else {
            configuration.append("\tDATA '").append(ALIAS_GEOMETRY).append(" from (").append(layerQuery).append(" ) AS ").append(localgisLayer.getLayername()).append(" USING UNIQUE ").append(ALIAS_ID).append(" USING SRID=").append(srid).append("'\n");
        }
       configuration.append("\tMETADATA\n"); 
        configuration.append("\t\t\"wms_featureinfoformat\" \"gml\"\n");    
        configuration.append("\t\t\"wms_title\" \"").append(localgisLayer.getLayername()).append("\"\n");    
        //configuration.append("\t\t\"wms_srs\" \"").append(srid).append("\"\n");
        configuration.append("\t\t\"wms_srs\" \"EPSG:4230 EPSG:4258 EPSG:4326 EPSG:25828 EPSG:25829 EPSG:25830 EPSG:25831 EPSG:23028 EPSG:23029 EPSG:23030 EPSG:23031 EPSG:900913\"");
        if (localgisLayer.isVersionable()) {
            configuration.append("\t\t\"wms_timeextent\" \"").append(localgisLayer.getTime()).append("\"\n");;
            configuration.append("\t\t\"wms_timeitem\" \"").append(localgisLayer.getColumnTime()).append("\"\n");            
        }
        // Obtenemos los atributos de la capa
        Short mapPublic = ConstantesSQL.FALSE;
        if (publicConfiguration.booleanValue()) {
            mapPublic = ConstantesSQL.TRUE;
        }
        Collection attributes = localgisAttributeDAO.selectAttributesByLayer(localgisLayer.getLayerid(),mapId);
        if (attributes != null && attributes.size() > 0) {
            String includeItems = null;
            Iterator itAttributes = attributes.iterator();
            while (itAttributes.hasNext()) {
                LocalgisAttribute attribute = (LocalgisAttribute) itAttributes.next();
                if (includeItems == null) {
                    includeItems = attribute.getAttributename();
                } else {
                    includeItems += ","+attribute.getAttributename();
                }
            }
            configuration.append("\t\t\"gml_include_items\" \"").append(includeItems).append("\"\n");
        } else {
            configuration.append("\t\t\"gml_include_items\" \"all\"\n");
        }
        // Obtenemos los atributos restringidos
        Collection restrictedAttributes = localgisAttributeDAO.selectRestrictedAttributesByIdLayerGeopistaAndMapPublic(localgisLayer.getLayeridgeopista(), mapPublic,geopistaEntidadSupramunicipal.getIdEntidad());
        // Siempre restringimos el ID_LOCALGIS, que es el rowid o el oid
        String excludeItems = ALIAS_ID;
        if (restrictedAttributes != null && restrictedAttributes.size() > 0) {
            Iterator itRestrictedAttributes = restrictedAttributes.iterator();
            while (itRestrictedAttributes.hasNext()) {
                LocalgisAttribute attribute = (LocalgisAttribute) itRestrictedAttributes.next();
                excludeItems += ","+attribute.getAttributename();
            }
        }
        configuration.append("\t\t\"gml_exclude_items\" \"").append(excludeItems).append("\"\n");
        configuration.append("\tEND\n");  
        // Ponemos un simbolo por defecto por si acaso aunque en principio todo
        // los estilos se generan a traves de un SLD especifico.
        configuration.append("\tCLASS\n");
        configuration.append("\t\tCOLOR 0 0 0\n");
        configuration.append("\t\tOUTLINECOLOR 0 0 0\n");
        configuration.append("\tEND\n");
        configuration.append("\tEXTENT ").append(maxExtents).append("\n");
        configuration.append("\tTRANSPARENCY 85\n");
        configuration.append("\tTEMPLATE \"templates/feature.html\"\n");
        configuration.append("\tHEADER \"templates/header_feature.html\"\n");            
        configuration.append("\tFOOTER \"templates/footer_feature.html\"\n");
        configuration.append("\tLABELCACHE ON\n");  
        configuration.append("\tPOSTLABELCACHE FALSE\n");  
        configuration.append("END\n\n");  
    }

    /**
     * Configura una capa y la almacena en un StringBuffer
     * @param configuration StringBuffer donde se va almacenando la configuracion
     * @param localgisMapServerLayer Capa a configurar
     * @param geopistaEntidadSupramunicipal Entidad que estamos configurando
     * @param maxExtentMunicipio Extension del municipio como String
     * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
     */
    private void configureLayer(StringBuffer configuration, LocalgisMapServerLayer localgisMapServerLayer, GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal, String maxExtentMunicipio) throws LocalgisConfigurationException {
        configuration.append("LAYER\n");
        configuration.append("\tNAME \"").append(localgisMapServerLayer.getInternalNameMapServer()).append("\"\n");   
        configuration.append("\tSTATUS OFF\n");   
        configuration.append("\t#DEBUG OFF\n");    
        configuration.append("\tDEBUG 5\n"); 
        configuration.append("\tTOLERANCE 13\n");    
        configuration.append("\tDUMP true\n");  
        configuration.append("\tTYPE RASTER\n");   
        configuration.append("\tGROUP \"").append(localgisMapServerLayer.getInternalNameMapServer()).append("\"\n");   
        configuration.append("\tCONNECTIONTYPE WMS \n");   
        configuration.append("\tCONNECTION \"").append(localgisMapServerLayer.getUrl()).append("\"\n");
        configuration.append("\tMETADATA\n"); 
        //configuration.append("\t\t\"wms_srs\" \"").append(localgisMapServerLayer.getSrs()).append("\"\n");
        configuration.append("\t\t\"wms_srs\" \"EPSG:4230 EPSG:4258 EPSG:4326 EPSG:25828 EPSG:25829 EPSG:25830 EPSG:25831 EPSG:23028 EPSG:23029 EPSG:23030 EPSG:23031 EPSG:900913\"");

        configuration.append("\t\t\"wms_name\" \"").append(localgisMapServerLayer.getLayers()).append("\"\n");    
        configuration.append("\t\t\"wms_server_version\" \"").append(localgisMapServerLayer.getVersion()).append("\"\n");    
        configuration.append("\t\t\"wms_format\" \"").append(localgisMapServerLayer.getFormat()).append("\"\n");    
        configuration.append("\t\t\"wms_featureinfoformat\" \"text/html\"\n");    
        configuration.append("\t\t\"wms_title\" \"").append(localgisMapServerLayer.getInternalNameMapServer()).append("\"\n");    
        configuration.append("\t\t\"wms_group_title\" \"").append(localgisMapServerLayer.getInternalNameMapServer()).append("\"\n");    
        configuration.append("\t\t\"wms_connectiontimeout\" \"60\"");
        configuration.append("\tEND\n");  
        configuration.append("\tEXTENT ").append(maxExtentMunicipio).append("\n");
        configuration.append("\tTRANSPARENCY 85\n");
        configuration.append("END\n\n");  
    }

    /**
     * Configura el estilo de una capa de un mapa y la almacena en un
     * StringBuffer y, en caso de que sea un estilo por defecto lo almacena en
     * el map pasado como parametro con clave el LocalgisLayer
     * 
     * @param stylesConfiguration
     *            StringBuffer donde se va almacenando la configuracion
     * @param localgisLayer
     *            Capa cuyo estilo se desea a configurar
     * @param idMap
     *            Id del mapa al que pertenece la capa
     * @param mapDefaultLocalgisStyles Map donde vamos guardando los estilos por defecto de cada capa
     * @throws LocalgisConfigurationException
     *             Si ocurre algun error de configuracion
     */
    private void configureLayerStyle(StringBuffer stylesConfiguration, LocalgisLayer localgisLayer, Integer idMap, Map mapDefaultLocalgisStyles) throws LocalgisConfigurationException, LocalgisWMSException {
        LocalgisStyle localgisStyle = localgisStyleDAO.selectStyleByIdLayerAndIdMap(localgisLayer.getLayerid(), idMap);
        /*
         * TODO: Hay que comprobar que esto es correcto cuando se arregle el bug
         * que hay en el editor al modificar estilos por defecto
         * 
         * Si el estilo tiene un nombre del tipo default:nombreCapa lo metemos
         * siempre en el Map y sobreescribimos el que hubiera. Sin embargo, si
         * el estilo tiene un nombre del tipo nombreCapa:_:default:nombreCapa
         * solo lo metemos en el map en el caso de que no hubiera ya un estilo
         * asociado a la capa. En otro caso no hacemos nada
         */
        String layerName = localgisLayer.getLayername();
        String styleName = localgisStyle.getStylename();
        if (styleName.equals("default:"+layerName)) {
            mapDefaultLocalgisStyles.put(layerName, localgisStyle);
        } else if (styleName.equals(layerName + ":_:default:"+layerName)) {
            if (!mapDefaultLocalgisStyles.containsKey(styleName)) {
                mapDefaultLocalgisStyles.put(layerName, localgisStyle);
            }
        }
        configureStyle(stylesConfiguration, localgisLayer.getLayername(), localgisStyle);
    }

    /**
     * Configura el estilo de una capa de un mapa y la almacena en un
     * StringBuffer
     * 
     * @param stylesConfiguration
     *            StringBuffer donde se va almacenando la configuracion
     * @param layerName
     *            Nombre de la capa
     * @param localgisStyle
     *            Estilo a configurar
     * @throws LocalgisConfigurationException
     *             Si ocurre algun error de configuracion
     */
    private void configureStyle(StringBuffer stylesConfiguration, String layerName, LocalgisStyle localgisStyle) throws LocalgisConfigurationException, LocalgisWMSException {
        stylesConfiguration.append("<NamedLayer>\n");
        stylesConfiguration.append("<Name>"+layerName+"</Name>\n");
        stylesConfiguration.append("<UserStyle>\n");
        stylesConfiguration.append(localgisStyle.getFeaturetypestyles());
        stylesConfiguration.append("</UserStyle>\n");
        stylesConfiguration.append("</NamedLayer>\n");
    }

    /**
     * Devuelve el nombre del fichero de mapas en funcion del id de la entidad supramunicipal,
     * del mapa y de si el mapa es publico o privado. Tanto el id del mapa como
     * si el mapa es publico o privado pueden ser nulos
     * 
     * @param idEntidad Id de la entidad
     * @param idMap Id del mapa
     * @param publicMap Si el mapa es publico o privado
     * @return El nombre del fichero de mapas
     * @throws LocalgisConfigurationException
     */
    private String getMapFilename(Integer idEntidad, Integer idMap, Boolean publicMap) throws LocalgisConfigurationException {
        StringBuffer filename = new StringBuffer(Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_DIRECTORY));
        filename.append(Configuration.MAPSERVER_CONFIG_FILE_DIRECTORY).append("/"); 
        if (publicMap != null) {
            if (publicMap.booleanValue()) {
                filename.append(Configuration.MAPSERVER_CONFIG_FILE_PREFIX_PUBLIC);
            } else {
                filename.append(Configuration.MAPSERVER_CONFIG_FILE_PREFIX_PRIVATE);
            }
        } else {
            filename.append(Configuration.MAPSERVER_CONFIG_FILE_PREFIX);
        }
        filename.append(idEntidad);
        if (idMap != null) {
            filename.append("_").append(idMap);
        }
        filename.append(".").append(Configuration.MAPSERVER_CONFIG_FILE_EXTENSION);
        return filename.toString();
    }

    /**
     * Devuelve el nombre del fichero de estilos en funcion del id de la entidad
     * supramunicipal, del mapa y de si el mapa es publico o privado. El id del
     * mapa puede ser nulo por si se trata del fichero de estilos global
     * 
     * @param idEntidad
     *            Id de la entidad
     * @param idMap
     *            Id del mapa
     * @param publicMap
     *            Si el mapa es publico o privado
     * @return El nombre del fichero de estilos
     * @throws LocalgisConfigurationException
     */
    private String getStylesFilename(Integer idEntidad, Integer idMap, Boolean publicMap) throws LocalgisConfigurationException {
        String fileNameStyles = idEntidad.toString();
        if (idMap != null) {
            fileNameStyles += "_" + idMap;
        }
        fileNameStyles += "."+Configuration.MAPSERVER_STYLES_FILE_EXTENSION;
        if (publicMap.booleanValue()) {
            fileNameStyles = Configuration.MAPSERVER_STYLES_FILE_PREFIX_PUBLIC + fileNameStyles;
        } else {
            fileNameStyles = Configuration.MAPSERVER_STYLES_FILE_PREFIX_PRIVATE + fileNameStyles;
        }
        fileNameStyles = Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_DIRECTORY)+Configuration.MAPSERVER_STYLES_FILE_DIRECTORY+File.separator + fileNameStyles;
        return fileNameStyles;
    }
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.wms.WMSConfigurator#getStylesURL(java.lang.Integer, java.lang.Integer, java.lang.Boolean)
     */
    private String getStylesURL(Integer idEntidad, Integer idMap, Boolean publicMap) throws LocalgisConfigurationException {
        String stylesURL = Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_SERVER) + Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_STYLES_DIRECTORY) + "/";
        if (publicMap != null && publicMap.booleanValue()) {
            stylesURL += Configuration.MAPSERVER_STYLES_FILE_PREFIX_PUBLIC;
        } else {
            stylesURL += Configuration.MAPSERVER_STYLES_FILE_PREFIX_PRIVATE;
        }
        stylesURL += idEntidad;
        if (idMap != null) {
            stylesURL += "_" + idMap;
        }
        stylesURL += "." + Configuration.MAPSERVER_STYLES_FILE_EXTENSION;
        return stylesURL;
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.wms.WMSConfigurator#getAliasGeometry()
     */
    public String getAliasGeometry() {
        return ALIAS_GEOMETRY;
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.wms.WMSConfigurator#getAliasId()
     */
    public String getAliasId() {
        return ALIAS_ID;
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.wms.WMSConfigurator#getInternalNameGenericLayer(com.localgis.web.core.model.LocalgisMap, com.localgis.web.core.model.LocalgisLayer, int)
     */
    public String getInternalNameGenericLayer(LocalgisMap localgisMap, LocalgisLayer localgisLayer, int operationType) {
        return localgisLayer.getLayername();
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.wms.WMSConfigurator#getInternalNameOrtofotoLayer(com.localgis.web.core.model.LocalgisMap, int)
     */
    public String getInternalNameOrtofotoLayer(LocalgisMap localgisMap, int operationType) throws LocalgisConfigurationException {
        return Configuration.MAPSERVER_CONFIG_FILE_ORTOFOTO_LAYER_NAME;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.wms.WMSConfigurator#getInternalNameMunicipiosAndProvinciasLayer(com.localgis.web.core.model.LocalgisMap, java.lang.Boolean, int)
     */
    public String getInternalNameMunicipiosAndProvinciasLayer(LocalgisMap localgisMap, Boolean showMunicipios, int operationType) throws LocalgisConfigurationException {
        String name = "";
        if (showMunicipios != null && showMunicipios.booleanValue()) {
            name = Configuration.MAPSERVER_CONFIG_FILE_MUNICIPIOS_LAYER_NAME + ",";
        }
        name += Configuration.MAPSERVER_CONFIG_FILE_PROVINCIAS_LAYER_NAME;
        return name;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.wms.WMSConfigurator#getInternalNameOverviewLayer(com.localgis.web.core.model.LocalgisMap, java.util.List, java.lang.Boolean, int)
     */
    public String getInternalNameOverviewLayer(LocalgisMap localgisMap, List layers, Boolean showMunicipios, int operationType) throws LocalgisConfigurationException {
        String name = getInternalNameMunicipiosAndProvinciasLayer(localgisMap, showMunicipios, operationType);
        Iterator iteratorLayers = layers.iterator();
        while (iteratorLayers.hasNext()) {
            LocalgisLayer localgisLayer = (LocalgisLayer) iteratorLayers.next();
            name += "," + localgisLayer.getLayername();
        }
        return name;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.wms.WMSConfigurator#getMapServerURL(java.lang.Integer, java.lang.Integer, java.lang.Boolean, int)
     */
    public String getMapServerURL(Integer idEntidad, Integer idMap, Boolean publicMap, int requestType) throws LocalgisInvalidParameterException, LocalgisConfigurationException {
        StringBuffer urlMapServer = new StringBuffer();
        urlMapServer.append(Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_SERVER));
        urlMapServer.append(Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_DIRECTORY));
        urlMapServer.append("/").append(idEntidad).append("/");
        
        // Si no se ha pasado el idmap o el publicMap entonces es como si quisieramos acceder al mapa estatico del municipio
        if (idMap != null && publicMap != null) {
            urlMapServer.append(idMap);
            if (publicMap != null && publicMap.booleanValue()) {
                urlMapServer.append("/public/");
            } else {
                urlMapServer.append("/private/");
            }
        }
        urlMapServer.append(Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_FILE));
        return urlMapServer.toString();
    }
    
    public String getMapServerURLInternal(Integer idEntidad, Integer idMap, Boolean publicMap, int requestType) throws LocalgisInvalidParameterException, LocalgisConfigurationException {
        StringBuffer urlMapServer = new StringBuffer();
        
        String strUrl=null;
		try {
			strUrl = Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_SERVER_INTERNAL);
		} catch (Exception e) {}
        
		if (strUrl==null)
			strUrl=Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_SERVER);
		
        urlMapServer.append(strUrl);
        urlMapServer.append(Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_DIRECTORY));
        urlMapServer.append("/").append(idEntidad).append("/");
        
        // Si no se ha pasado el idmap o el publicMap entonces es como si quisieramos acceder al mapa estatico del municipio
        if (idMap != null && publicMap != null) {
            urlMapServer.append(idMap);
            if (publicMap != null && publicMap.booleanValue()) {
                urlMapServer.append("/public/");
            } else {
                urlMapServer.append("/private/");
            }
        }
        urlMapServer.append(Configuration.getPropertyString(Configuration.PROPERTY_MAPSERVER_URL_FILE));
        return urlMapServer.toString();
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.wms.WMSConfigurator#getBBoxSpain(java.lang.String)
     */
    public BoundingBox getBoundingBoxSpain(String srid) throws LocalgisConfigurationException {
    	BoundingBox result = new BoundingBox();
    	String sourceSrid = Configuration.getPropertyString(Configuration.PROPERTY_SPAIN_BBOX_EPSG_CODE);
    	Double minX = new Double(Configuration.getPropertyString(Configuration.PROPERTY_SPAIN_MINX));
    	Double minY = new Double(Configuration.getPropertyString(Configuration.PROPERTY_SPAIN_MINY));
    	Double maxX = new Double(Configuration.getPropertyString(Configuration.PROPERTY_SPAIN_MAXX));
    	Double maxY = new Double(Configuration.getPropertyString(Configuration.PROPERTY_SPAIN_MAXY));
    	try {
    		logger.debug("Transformando coordenadas de bounding box de España... srid origen: " + sourceSrid + ", srid destino: " + srid);
    		CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:" + sourceSrid, true);
    		CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:" + srid, true);
    		MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
    		Coordinate coordSourceMin = new Coordinate(minX, minY);
    		Coordinate coordSourceMax = new Coordinate(maxX, maxY);
    		Coordinate coordTargetMin = new Coordinate();
    		Coordinate coordTargetMax = new Coordinate();
    		JTS.transform(coordSourceMin, coordTargetMin, transform);
    		JTS.transform(coordSourceMax, coordTargetMax, transform);
    		logger.debug("Coordenadas origen: " + coordSourceMin + " - " + coordSourceMax);
    		logger.debug("Coordenadas destino: " + coordTargetMin + " - " + coordTargetMax);
    		result.setMinx(coordTargetMin.x);
    		result.setMiny(coordTargetMin.y);
    		result.setMaxx(coordTargetMax.x);
    		result.setMaxy(coordTargetMax.y);
    	} catch (Exception e) {
    		logger.error("Error al transformar coordenadas de bounding box de España", e);
    		result.setMinx(minX);
    		result.setMiny(minY);
    		result.setMaxx(maxX);
    		result.setMaxy(maxY);
    	}
        return result;
    }

    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.wms.WMSConfigurator#getBBoxSpain(java.lang.String)
     */
    public BoundingBox getBoundingBoxComunidad(String srid) throws LocalgisConfigurationException {
    	BoundingBox result = new BoundingBox();
    	String sourceSrid = Configuration.getPropertyString(Configuration.PROPERTY_COMUNIDAD_BBOX_EPSG_CODE);
    	Double minX = new Double(Configuration.getPropertyString(Configuration.PROPERTY_COMUNIDAD_MINX));
    	Double minY = new Double(Configuration.getPropertyString(Configuration.PROPERTY_COMUNIDAD_MINY));
    	Double maxX = new Double(Configuration.getPropertyString(Configuration.PROPERTY_COMUNIDAD_MAXX));
    	Double maxY = new Double(Configuration.getPropertyString(Configuration.PROPERTY_COMUNIDAD_MAXY));
    	try {
    		logger.debug("Transformando coordenadas de bounding box de España... srid origen: " + sourceSrid + ", srid destino: " + srid);
    		CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:" + sourceSrid, true);
    		CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:" + srid, true);
    		MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS);
    		Coordinate coordSourceMin = new Coordinate(minX, minY);
    		Coordinate coordSourceMax = new Coordinate(maxX, maxY);
    		Coordinate coordTargetMin = new Coordinate();
    		Coordinate coordTargetMax = new Coordinate();
    		JTS.transform(coordSourceMin, coordTargetMin, transform);
    		JTS.transform(coordSourceMax, coordTargetMax, transform);
    		logger.debug("Coordenadas origen: " + coordSourceMin + " - " + coordSourceMax);
    		logger.debug("Coordenadas destino: " + coordTargetMin + " - " + coordTargetMax);
    		result.setMinx(coordTargetMin.x);
    		result.setMiny(coordTargetMin.y);
    		result.setMaxx(coordTargetMax.x);
    		result.setMaxy(coordTargetMax.y);
    	} catch (Exception e) {
    		logger.error("Error al transformar coordenadas de bounding box de Comunidad", e);
    		result.setMinx(minX);
    		result.setMiny(minY);
    		result.setMaxx(maxX);
    		result.setMaxy(maxY);
    	}
        return result;
    }
    
    /**
	 * Abre la conexion para enviar
	 * 
	 * @throws JSchException
	 */
	private void openSFTPSession() {

		try {
			String activar=Configuration.getPropertyString("mapserver.ftp.activar");
			if ((activar!=null) && (activar.equals("0")))
				return;

			String userName=Configuration.getPropertyString("mapserver.ftp.user");
			String password=Configuration.getPropertyString("mapserver.ftp.password");
			String host=Configuration.getPropertyString("mapserver.ftp.host");
			String PROXY_HOST=Configuration.getPropertyString("mapserver.ftp.proxy.host");
			String PROXY_PORT=Configuration.getPropertyString("mapserver.ftp.proxy.port");
					
			int port = 22;

			JSch jsch = new JSch();
		
			session = jsch.getSession(userName, host, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.setPassword(password);

			// Por si acaso a traves de SOCKS

			if (PROXY_HOST!=null && !PROXY_HOST.equals("")) {
				logger.info("Realizando la conexion por proxy:" + PROXY_HOST + ":"+ PROXY_PORT);
				logger.info("Host Destino:" + host + ":" + port);
				session.setProxy(new ProxySOCKS5(PROXY_HOST, Integer.parseInt(PROXY_PORT)));
			} else {
				logger.info("Realizando la conexion sin proxy");
				logger.info("Host Destino:" + host + ":" + port);
			}

			session.connect();
			logger.info("Sesion conectada");
			Channel channel = session.openChannel("sftp");
			channel.connect();
			sftpChannel = (ChannelSftp) channel;
		} catch (NumberFormatException e) {
		} catch (LocalgisConfigurationException e) {
		} catch (JSchException e) {
			e.printStackTrace();
		}
		catch (Throwable e){
			e.printStackTrace();
		}

	}
	
	private void sendSFTPFile(String fichero,String remoteDirectory){

		try {
			if (sftpChannel==null)
				return;
			File origen=new File(fichero);

			String directory=Configuration.getPropertyString(remoteDirectory);
			
			crearSubdirectorios(directory,sftpChannel);
			
			sftpChannel.put(origen.getAbsolutePath(), directory, null);
			//System.out.println("Enviado fichero:"+fichero+" a:"+directory);
			
		}  catch (SftpException e) {			
		} catch (Exception e) {	
			e.printStackTrace();
		}
	}
	
	private void removeSFTPFile(String fichero,String remoteDirectory){

		try {
			if (sftpChannel==null)
				return;
			
			File origen=new File(fichero);

			String directory=Configuration.getPropertyString(remoteDirectory);
			
			crearSubdirectorios(directory,sftpChannel);
			sftpChannel.rm(directory+origen.getAbsolutePath());
			
		}  catch (SftpException e) {			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Cierra la conexion
	 * 
	 * @throws JSchException
	 */
	private void closeSFTPSession() {
		try {
			if (sftpChannel != null)
				sftpChannel.exit();
			if (session != null)
				session.disconnect();
		} catch (Exception e) {
		}
	}
	
	private void crearSubdirectorios(String destino, ChannelSftp sftpChannel2){

		if (sftpChannel==null)
			return;
		//Con 3 niveles es suficiente.
		String nivel1=destino.subSequence(0,destino.lastIndexOf("/")).toString();
		String nivel2=destino.subSequence(0,nivel1.lastIndexOf("/")).toString();
		String nivel3=destino.subSequence(0,nivel2.lastIndexOf("/")).toString();
		
		try{			
			sftpChannel.mkdir(nivel3);
		}
		catch (Exception e){}

		try{			
			sftpChannel.mkdir(nivel2);
		}
		catch (Exception e){}
		
		try{			
			sftpChannel.mkdir(nivel1);
		}
		catch (Exception e){}
		
	}
	
	private void generateRemoteStyleFile(String mapFile,String url){
		
		try {
			if (sftpChannel==null)
				return;
			
			File origen=new File(mapFile);

			String directory=Configuration.getPropertyString("mapserver.ftp.map.directory");
			
			// Backup de Directorios
			ssshChannel = (ChannelExec) session.openChannel("exec");

			String command="/usr/local/LocalGIS.III/mapserver/apps/bin/GenerateMapFile.sh "+directory+"/"+origen.getName()+" "+url;
			logger.info("Ejecutando el comando de generacion de fichero:"+command);
			ssshChannel.setCommand(command);
			ssshChannel.connect();
			ssshChannel.disconnect();
			//System.out.println("Generado:"+mapFile);
		} catch (Exception e) {
			System.out.println("Error al enviar:"+mapFile);
			e.printStackTrace();
		}
	}
}
