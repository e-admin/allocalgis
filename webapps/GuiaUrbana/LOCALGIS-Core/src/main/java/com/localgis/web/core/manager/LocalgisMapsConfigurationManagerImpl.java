/**
 * LocalgisMapsConfigurationManagerImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.manager;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.ibatis.dao.client.DaoException;
import com.ibatis.dao.client.DaoManager;
import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.coordsys.CoordinateSystem;
import com.localgis.web.core.coordsys.CoordinateSystemRegistry;
import com.localgis.web.core.dao.GeopistaColumnDAO;
import com.localgis.web.core.dao.GeopistaEntidadSupramunicipalDAO;
import com.localgis.web.core.dao.GeopistaLayerDAO;
import com.localgis.web.core.dao.GeopistaMapDAO;
import com.localgis.web.core.dao.GeopistaMapServerLayerDAO;
import com.localgis.web.core.dao.GeopistaMunicipioDAO;
import com.localgis.web.core.dao.LocalgisAttributeDAO;
import com.localgis.web.core.dao.LocalgisLayerDAO;
import com.localgis.web.core.dao.LocalgisMapDAO;
import com.localgis.web.core.dao.LocalgisMapLayerDAO;
import com.localgis.web.core.dao.LocalgisMapServerLayerDAO;
import com.localgis.web.core.dao.LocalgisMunicipioDAO;
import com.localgis.web.core.dao.LocalgisStyleDAO;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.exceptions.LocalgisWMSException;
import com.localgis.web.core.exceptions.NoResourceAvailableException;
import com.localgis.web.core.model.BoundingBox;
import com.localgis.web.core.model.GeopistaColumn;
import com.localgis.web.core.model.GeopistaEntidadSupramunicipal;
import com.localgis.web.core.model.GeopistaLayer;
import com.localgis.web.core.model.GeopistaMap;
import com.localgis.web.core.model.GeopistaMapServerLayer;
import com.localgis.web.core.model.GeopistaMunicipio;
import com.localgis.web.core.model.LocalgisAttribute;
import com.localgis.web.core.model.LocalgisLayer;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.core.model.LocalgisMapLayer;
import com.localgis.web.core.model.LocalgisMapServerLayer;
import com.localgis.web.core.model.LocalgisStyle;
import com.localgis.web.core.utils.LocalgisUtils;
import com.localgis.web.core.wms.WMSConfigurator;

/**
 * Implementación de LocalgisMapsConfigurationManager
 * 
 * @author albegarcia
 *
 */
public class LocalgisMapsConfigurationManagerImpl implements LocalgisMapsConfigurationManager {

    /**
     * Logger para las trazas
     */
    private static Logger logger = Logger.getLogger(LocalgisMapsConfigurationManagerImpl.class);

    /**
     * Dao manager para el acceso a datos
     */
    private DaoManager daoManager;

    /**
     * Dao para las columnas de geopista
     */
    private GeopistaColumnDAO geopistaColumnDAO;

    /**
     * Dao para las entidades supramunicipales de geopista
     */
    private GeopistaEntidadSupramunicipalDAO geopistaEntidadSupramunicipalDAO;

    /**
     * Dao para las capas de geopista
     */
    private GeopistaLayerDAO geopistaLayerDAO;

    /**
     * Dao para los mapas de geopista
     */
    private GeopistaMapDAO geopistaMapDAO;

    /**
     * Dao para las capas wms de geopista
     */
    private GeopistaMapServerLayerDAO geopistaMapServerLayerDAO;

    /**
     * Dao para los municipios de geopista
     */
    private GeopistaMunicipioDAO geopistaMunicipioDAO;

    /**
     * Dao para los atributos de localgis
     */
    private LocalgisAttributeDAO localgisAttributeDAO;

    /**
     * Dao para las capas de localgis
     */
    private LocalgisLayerDAO localgisLayerDAO;

    /**
     * Dao para los mapas de localgis 
     */
    private LocalgisMapDAO localgisMapDAO;

    /**
     * Dao para las relaciones entre mapas y capas de localgis
     */
    private LocalgisMapLayerDAO localgisMapLayerDAO;

    /**
     * Dao para las capas wms de localgis
     */
    private LocalgisMapServerLayerDAO localgisMapServerLayerDAO;
    
    /**
     * Dao para los municipios de localgis 
     */
    private LocalgisMunicipioDAO localgisMunicipioDAO;

    /**
     * Dao para las capas de localgis
     */
    private LocalgisStyleDAO localgisStyleDAO;

    /**
     * LocalgisManagerBuilder que construyo el objeto
     */
    private LocalgisManagerBuilder localgisManagerBuilder;

    /**
     * Configurador del servidor de mapas
     */
    private WMSConfigurator wmsConfigurator;
    
    /**
     * Constructor a partir de un DAOManager, un configurador de wms y un LocalgisManagerBuilder
     */
    public LocalgisMapsConfigurationManagerImpl(DaoManager daoManager, WMSConfigurator wmsConfigurator, LocalgisManagerBuilder localgisManagerBuilder) {
        this.daoManager = daoManager;
        
        this.geopistaColumnDAO = (GeopistaColumnDAO) daoManager.getDao(GeopistaColumnDAO.class);
        this.geopistaEntidadSupramunicipalDAO = (GeopistaEntidadSupramunicipalDAO) daoManager.getDao(GeopistaEntidadSupramunicipalDAO.class);
        this.geopistaLayerDAO = (GeopistaLayerDAO) daoManager.getDao(GeopistaLayerDAO.class);
        this.geopistaMapServerLayerDAO = (GeopistaMapServerLayerDAO) daoManager.getDao(GeopistaMapServerLayerDAO.class);
        this.geopistaMapDAO = (GeopistaMapDAO) daoManager.getDao(GeopistaMapDAO.class);
        this.geopistaMunicipioDAO = (GeopistaMunicipioDAO) daoManager.getDao(GeopistaMunicipioDAO.class);

        this.localgisAttributeDAO = (LocalgisAttributeDAO) daoManager.getDao(LocalgisAttributeDAO.class);
        this.localgisLayerDAO = (LocalgisLayerDAO) daoManager.getDao(LocalgisLayerDAO.class);
        this.localgisMapDAO = (LocalgisMapDAO) daoManager.getDao(LocalgisMapDAO.class);
        this.localgisMapLayerDAO = (LocalgisMapLayerDAO) daoManager.getDao(LocalgisMapLayerDAO.class);
        this.localgisMapServerLayerDAO = (LocalgisMapServerLayerDAO) daoManager.getDao(LocalgisMapServerLayerDAO.class);
        this.localgisMunicipioDAO = (LocalgisMunicipioDAO) daoManager.getDao(LocalgisMunicipioDAO.class);
        this.localgisStyleDAO = (LocalgisStyleDAO) daoManager.getDao(LocalgisStyleDAO.class);

        this.wmsConfigurator = wmsConfigurator;
        this.localgisManagerBuilder = localgisManagerBuilder;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapsConfigurationManager#getMunicipios(java.lang.Integer)
     */
    public List getEntidadesSupramunicipales() throws LocalgisDBException {
        try {
            List listAllEntidades = geopistaEntidadSupramunicipalDAO.selectAll();
            return listAllEntidades;
        } catch (DaoException e) {
            logger.error("Error al obtener las entidades", e);
            throw new LocalgisDBException("Error al obtener las entidades",e);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapsConfigurationManager#getAvailableMaps(java.lang.Integer)
     */
    public List getAvailableMaps(Integer idEntidad) throws LocalgisInvalidParameterException, LocalgisDBException {
        /*
         * Comprobacion de los parametros de entrada
         */
        boolean error = false;
        String message = null;
        if (idEntidad == null) {
            message = "El id de la entidad no puede ser nulo";
            error = true;
        }
        if (error) {
        	logger.error(message);
            throw new LocalgisInvalidParameterException(message);
        }
        try {
            return geopistaMapDAO.selectMapsByIdEntidad(idEntidad, localgisManagerBuilder.getDefaultLocale());
        } catch (DaoException e) {
            logger.error("Error al obtener los mapas disponibles para la entidad ["+idEntidad+"]", e);
            throw new LocalgisDBException("Error al obtener los mapas disponibles para la entidad ["+idEntidad+"]",e);
        }
    }
 
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapsConfigurationManager#getAvailableMap(java.lang.Integer, java.lang.Integer)
     */
    public GeopistaMap getAvailableMap(Integer idEntidad,Integer idMap) throws LocalgisInvalidParameterException, LocalgisDBException {
        /*
         * Comprobacion de los parametros de entrada
         */
        boolean error = false;
        String message = null;
        if (idEntidad == null) {
            message = "El id de la entidad no puede ser nulo";
            error = true;
        }
        if (error) {
        	logger.error(message);
            throw new LocalgisInvalidParameterException(message);
        }
        try {
            return geopistaMapDAO.selectMapById(idEntidad,idMap, localgisManagerBuilder.getDefaultLocale());
        } catch (DaoException e) {
            logger.error("Error al obtener el mapas disponible para la entidad ["+idEntidad+"]", e);
            throw new LocalgisDBException("Error al obtener el mapas disponible para la entidad ["+idEntidad+"]",e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapsConfigurationManager#getPublishedMaps(java.lang.Integer)
     */
    public List getPublishedMaps(Integer idEntidad) throws LocalgisInvalidParameterException, LocalgisDBException {
        /*
         * Comprobacion de los parametros de entrada
         */
        boolean error = false;
        String message = null;
        if (idEntidad == null) {
            message = "El id de la entidad no puede ser nulo";
            error = true;
        }
        if (error) {
        	logger.error(message);
            throw new LocalgisInvalidParameterException(message);
        }
        
        try {
            return localgisMapDAO.selectMapsByIdEntidad(idEntidad, localgisManagerBuilder.getDefaultLocale());        
        } catch (DaoException e) {
            logger.error("Error al obtener los mapas publicados para la entidad ["+idEntidad+"]", e);
            throw new LocalgisDBException("Error al obtener los mapas publicados para la entidad ["+idEntidad+"]",e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapsConfigurationManager#getPublishedMaps(java.lang.Integer, java.lang.Boolean)
     */
    public List getPublishedMaps(Integer idEntidad, Boolean mapasPublicos) throws LocalgisInvalidParameterException, LocalgisDBException {
        /*
         * Comprobacion de los parametros de entrada
         */
        boolean error = false;
        String message = null;
        if (idEntidad == null) {
            message = "El id de la entidad no puede ser nulo";
            error = true;
        }
        if (error) {
        	logger.error(message);
            throw new LocalgisInvalidParameterException(message);
        }
        
        try {
            return localgisMapDAO.selectMapsByIdEntidad(idEntidad, mapasPublicos, localgisManagerBuilder.getDefaultLocale());        
        } catch (DaoException e) {
            logger.error("Error al obtener los mapas publicados para la entidad ["+idEntidad+"] (mapasPublicos = ["+mapasPublicos+"])", e);
            throw new LocalgisDBException("Error al obtener los mapas publicados para la entidad ["+idEntidad+"] (mapasPublicos = ["+mapasPublicos+"])",e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapsConfigurationManager#publishAndRemoveMapsFromWMSServer(com.localgis.web.core.model.GeopistaMap[], com.localgis.web.core.model.LocalgisMap[], com.localgis.web.core.model.LocalgisMap[], java.lang.Integer, java.lang.Boolean, java.lang.Integer)
     */
    public void publishAndRemoveMapsFromWMSServer(GeopistaMap [] mapsToPublish, LocalgisMap[] mapsToRemove, LocalgisMap[] mapsToRepublish, Integer idEntidad, Boolean publicMaps, Integer idMapGeopistaDefault,boolean publicarTodasEntidades ) throws LocalgisWMSException, LocalgisConfigurationException, LocalgisInvalidParameterException, LocalgisDBException {
        
    	
    	logger.info("Comenzando la publicacion de mapas");
        String message = new String();
        boolean error = false;
        if (idEntidad == null) {
            message = "El id de la entidad del mapa no puede ser nulo";
            error = true;
        } else if (publicMaps == null) {
            message = "El valor que determina si los mapas son publicos o privados no puede ser nulo";
            error = true;
        }
        if (error) {
        	logger.error(message);
            throw new LocalgisInvalidParameterException(message);
        }
        GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal = geopistaEntidadSupramunicipalDAO.selectByPrimaryKey(idEntidad);
        if (geopistaEntidadSupramunicipal == null || geopistaEntidadSupramunicipal.getSrid() == null) {
        	logger.error("El id de entidad supramunicipal no es válido");
            throw new LocalgisConfigurationException("El id de entidad supramunicipal no es válido");
        }
        /*
         * Obtenemos los municipios de la entidad y creamos una coleccion de los ids
         */
        List municipios = geopistaMunicipioDAO.selectMunicipiosByIdEntidad(idEntidad);
        Collection<Integer> idsMunicipios = new ArrayList<Integer>();
        Iterator itMunicipios = municipios.iterator();
        while (itMunicipios.hasNext()) {
			GeopistaMunicipio municipio = (GeopistaMunicipio) itMunicipios.next();
			idsMunicipios.add(municipio.getId());
		}
        
        try {
            daoManager.startTransaction();
            if (mapsToRemove != null) {
                for (int i = 0; i < mapsToRemove.length; i++) {
                    LocalgisMap mapToRemove = mapsToRemove[i];
                    if (publicarTodasEntidades){              
                        Integer idLocalGisMapToDelete=localgisMapDAO.getIdMapByIdGeopista(mapToRemove.getMapidgeopista(), idEntidad, localgisManagerBuilder.getDefaultLocale(), publicMaps);
                        if (idLocalGisMapToDelete!=null){
	                        int rows=localgisMapDAO.deleteByIdMapAndPublicMap(idLocalGisMapToDelete, publicMaps);
	                        logger.info("Numero de filas borradas:"+rows);
                        }
                        else{
                        	logger.info("El mapa no existe para la entidad:"+geopistaEntidadSupramunicipal.getIdEntidad());
                        }
                    }
                    else{
                    	logger.error("Borrando mapas publicados");
                        int rows=localgisMapDAO.deleteByIdMapAndPublicMap(mapToRemove.getMapid(), publicMaps);
                        logger.info("Numero de filas borradas:"+rows);
                    }
                        
                }
            }
            if (mapsToPublish != null) {
                for (int i = 0; i < mapsToPublish.length; i++) {       
                	
                    GeopistaMap geopistaMapToPublish = geopistaMapDAO.selectMapById(geopistaEntidadSupramunicipal.getIdEntidad(), mapsToPublish[i].getIdMap(), localgisManagerBuilder.getDefaultLocale());

                    //Si deseamos publicar en todas las entidades, primero hay que borrar el mapa
                    if (publicarTodasEntidades){
                       Integer idMapToRemove = localgisMapDAO.getIdMapByIdGeopista(geopistaMapToPublish.getIdMap(),idEntidad, localgisManagerBuilder.getDefaultLocale(),publicMaps);
                        if (idMapToRemove!=null)
                        	localgisMapDAO.deleteByIdMapAndPublicMap(idMapToRemove, publicMaps);
                    }
                    
                    String srid = getMapSRID(geopistaEntidadSupramunicipal, geopistaMapToPublish);
                    BoundingBox bboxEntidad = localgisMunicipioDAO.selectBoundingBoxByIdEntidadAndSRID(idEntidad, Integer.parseInt(srid));
                    /*
                     * Insertamos el mapa en la base de datos de localgis
                     */
                    logger.info("Insertando el mapa en la BD de LocalGIS");

                    LocalgisMap localgisMap = new LocalgisMap();
                    localgisMap.setMapidgeopista(geopistaMapToPublish.getIdMap());
                    localgisMap.setMapidentidad(idEntidad);
                    localgisMap.setMinx(bboxEntidad.getMinx());
                    localgisMap.setMiny(bboxEntidad.getMiny());
                    localgisMap.setMaxx(bboxEntidad.getMaxx());
                    localgisMap.setMaxy(bboxEntidad.getMaxy());
                    localgisMap.setMappublicBoolean(publicMaps);
                    localgisMap.setSrid(srid);
                    localgisMap.setMapdefaultBoolean(new Boolean(false));
                    Integer idMap = localgisMapDAO.insert(localgisMap);
                    
                    /*
                     * Obtenemos las capas de features del mapa de la base de datos de geopista y las insertamos en localgis
                     */
                    List listlayers = geopistaLayerDAO.selectLayersByIdMapAndIdEntidad(geopistaMapToPublish.getIdMap(), idEntidad);
                    Iterator itLayers = listlayers.iterator();
                    
                    StringBuffer errorRecursos=new StringBuffer();
                    boolean bErrorRecursos=false;
                    while (itLayers.hasNext()) {
                        GeopistaLayer geopistaLayer = (GeopistaLayer) itLayers.next();                       
                        try{
                        	logger.info("Insertando la capa:"+geopistaLayer.getName()+" el mapa en la BD de LocalGIS");
                        	insertLayer(geopistaLayer, idMap, idEntidad, idsMunicipios, publicMaps, srid);
                        }
                        catch (NoResourceAvailableException e){  
                        	logger.error("Falta el recurso:"+e.getMessage()+" en la capa:"+geopistaLayer.getName());
                        	errorRecursos.append("("+geopistaLayer.getName()+")->"+e.getMessage());
                        	bErrorRecursos=true;
                        	
                        }
                    }
                    if (bErrorRecursos){
                    	String messageError="El mapa "+geopistaMapToPublish.getName()+" no se puede publicar porque faltan la siguientes recursos:\n"+errorRecursos;
                    	throw new NoResourceAvailableException(messageError);
                    }
        
                    /*
                     * Insertamos las capas WMS del mapa
                     */
                    logger.info("Insertando las capas WMS en la BD de LocalGIS");

                    insertWMSLayers(geopistaMapToPublish.getIdMap(), idEntidad, idMap);
                }
            }
            
            if (mapsToRepublish != null) {
            	logger.info("Republicando mapas");
            	/*
            	 * Obtenemos el bbox del municipio ya que ha podido cambiar en los mapas que ya estaban publicados
            	 */
                for (int i = 0; i < mapsToRepublish.length; i++) {
                    LocalgisMap mapToRepublish = mapsToRepublish[i];
                    
                    if (mapToRepublish.getMapid() == null) {
                       logger.error("El mapa con indice \""+i+"\" dentro del array de mapas a republicar es nulo");
                       continue;
                    }
                    /*
                     * Obtenemos el mapa de la base de datos para evitar posibles ataques
                     */
                    Integer id=mapToRepublish.getMapid();
                    mapToRepublish = localgisMapDAO.selectMapById(mapToRepublish.getMapid(), localgisManagerBuilder.getDefaultLocale());
                    if (mapToRepublish == null) {
                        logger.error("El mapa con id \""+id+"\" a republicar no existe.");
                        continue;
                    } else if (!mapToRepublish.getMapidentidad().equals(idEntidad)){
                        logger.error("El mapa con id \""+mapToRepublish.getMapid()+"\" a republicar no pertenece al municipio \""+idEntidad+"\".");
                        continue;
                    } 
                    
                    /* Obtener el srid del mapa */
                    GeopistaMap geopistaMapToRepublish = geopistaMapDAO.selectMapById(geopistaEntidadSupramunicipal.getIdEntidad(), mapToRepublish.getMapidgeopista(), localgisManagerBuilder.getDefaultLocale());
                    String srid = getMapSRID(geopistaEntidadSupramunicipal, geopistaMapToRepublish);
                    /*
                     * Actualizamos el mapa, ya que ha podido cambiar su proyeccion y por tanto sus extents
                     */
                    BoundingBox bboxMunicipio = localgisMunicipioDAO.selectBoundingBoxByIdEntidadAndSRID(idEntidad, Integer.parseInt(srid));
                    mapToRepublish.setMinx(bboxMunicipio.getMinx());
                    mapToRepublish.setMiny(bboxMunicipio.getMiny());
                    mapToRepublish.setMaxx(bboxMunicipio.getMaxx());
                    mapToRepublish.setMaxy(bboxMunicipio.getMaxy());
                    mapToRepublish.setSrid(srid);
                    localgisMapDAO.updateMapByIdMap(mapToRepublish);
                    
                    /*
                     * Borramos todas las capas de geopista de la base de datos de localgis para el mapa
                     */
                    localgisMapLayerDAO.deleteLayersByIdMap(mapToRepublish.getMapid());

                    /*
                     * Borramos todas las capas wms de la base de datos de localgis para el mapa
                     */
                    localgisMapServerLayerDAO.deleteMapServerLayersByIdMap(mapToRepublish.getMapid());

                    /*
                     * Obtenemos las capas de features del mapa de la base de datos de geopista y las insertamos en localgis
                     */
                    List listlayers = geopistaLayerDAO.selectLayersByIdMapAndIdEntidad(mapToRepublish.getMapidgeopista(), idEntidad);
                    Iterator itLayers = listlayers.iterator();
                    
                    StringBuffer errorRecursos=new StringBuffer();
                    boolean bErrorRecursos=false;
                    while (itLayers.hasNext()) {
                        GeopistaLayer geopistaLayer = (GeopistaLayer) itLayers.next();
                        try{
                        	insertLayer(geopistaLayer, mapToRepublish.getMapid(), idEntidad, idsMunicipios, publicMaps, srid);
                        }
                        catch (NoResourceAvailableException e){  
                        	logger.error("Falta el recurso:"+e.getMessage()+" en la capa:"+geopistaLayer.getName());
                        	errorRecursos.append(e.getMessage()+"\n");
                        	bErrorRecursos=true;
                        }
                    }
                    
                    if (bErrorRecursos){
                    	String messageError="El mapa "+mapToRepublish.getName()+" no se puede republicar porque faltan la siguientes recursos:\n"+errorRecursos;
                    	throw new NoResourceAvailableException(messageError);
                    }
                    
                    

                    /*
                     * Insertamos las capas WMS del mapa
                     */
                    insertWMSLayers(mapToRepublish.getMapidgeopista(), idEntidad, mapToRepublish.getMapid());

                }
            }

            /*
             * Limpiamos la base de datos para que no se queden capas o estilos no referenciadas 
             */
            cleanUnusedLayersAndStyles();

            /*
             * Guardamos el mapa por defecto, siempre que se haya enviado,
             * porque puede ser que venga a null ya que no hay mapas publicados
             */
            if (idMapGeopistaDefault != null) {
                localgisMapDAO.setDefaultMap(idMapGeopistaDefault, publicMaps, idEntidad);
            }
            logger.info("Configurando el WMS Server");
            wmsConfigurator.configureWMSServer(geopistaEntidadSupramunicipal, publicMaps);
            
            daoManager.commitTransaction();
        } catch (DaoException e) {
            logger.error("Error al publicar y eliminar mapas", e);
            String mesg=e.getCause().getMessage();
            if(mesg.contains("java.sql.SQLException: ERROR: duplicate key violates unique constraint \"unique_map\""))
            	 throw new LocalgisDBException("El mapa que desea publicar ya ha sido previamente publicado por otro usuario",e);
            throw new LocalgisDBException("Error al publicar y eliminar mapas",e);
        } catch (NoResourceAvailableException e){
        	throw new LocalgisConfigurationException(e.getMessage());
        }
        catch (Throwable e){        
        	  logger.error("Error al publicar y eliminar mapas", e);
        	  throw new LocalgisDBException("Error al publicar y eliminar mapas",e);
        }
        finally {
            daoManager.endTransaction();
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapsConfigurationManager#addMapToWMSServer(com.localgis.web.core.model.GeopistaMap, java.lang.Integer, java.lang.Boolean)
     */
    public void addMapToWMSServer(GeopistaMap geopistaMap, Integer idEntidad, Boolean publicMap) throws LocalgisConfigurationException, LocalgisInvalidParameterException, LocalgisDBException, LocalgisWMSException {
        GeopistaMap [] mapsToPublish = new GeopistaMap[1];
        mapsToPublish[0] = geopistaMap;
        publishAndRemoveMapsFromWMSServer(mapsToPublish, null, null, idEntidad, publicMap, geopistaMap.getIdMap(),false);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapsConfigurationManager#getDefaultMap(java.lang.Integer, java.lang.Boolean)
     */
    public LocalgisMap getDefaultMap(Integer idEntidad, Boolean mapPublic) throws LocalgisInvalidParameterException, LocalgisDBException {
        String message = new String();
        boolean error = false;
        if (idEntidad == null) {
            message = "El id de la entidad del mapa no puede ser nulo";
            error = true;
        } else if (mapPublic == null) {
            message = "El valor que determina si el mapa es publico o privado no puede ser nulo";
            error = true;
        }
        if (error) {
        	logger.error(message);
            throw new LocalgisInvalidParameterException(message);
        }
        
        try {
            return localgisMapDAO.getDefaltMap(mapPublic, idEntidad, localgisManagerBuilder.getDefaultLocale());
        } catch (DaoException e) {
            logger.error("Error al obtener el mapa por defecto para la entidad ["+ idEntidad + "] y mapPublic ["+mapPublic+"]");
            throw new LocalgisDBException("Error al obtener el mapa por defecto para la entidad ["+ idEntidad + "] y mapPublic ["+mapPublic+"]",e);
        }

    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapsConfigurationManager#reconfigureWMSServer()
     */
    public void reconfigureWMSServer() throws LocalgisConfigurationException, LocalgisDBException, LocalgisWMSException {
        /*
         * Obtenemos todos las entidades y configuramos el servidor de mapas
         * para cada uno de los municipios para los mapas publicos y privados
         */
        List entidades = getEntidadesSupramunicipales();
        Iterator itEntidades = entidades.iterator();
        while (itEntidades.hasNext()) {
            GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal = (GeopistaEntidadSupramunicipal) itEntidades.next();
            wmsConfigurator.configureWMSServer(geopistaEntidadSupramunicipal, new Boolean(true));
            wmsConfigurator.configureWMSServer(geopistaEntidadSupramunicipal, new Boolean(false));
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapsConfigurationManager#reconfigureWMSServer(java.lang.Integer, java.lang.Boolean)
     */
    public void reconfigureWMSServer(Integer idEntidad, Boolean publicMaps) throws LocalgisInvalidParameterException, LocalgisConfigurationException, LocalgisDBException, LocalgisWMSException {
        String message = new String();
        boolean error = false;
        if (idEntidad == null) {
            message = "El id de la entidad del mapa no puede ser nulo";
            error = true;
        } else if (publicMaps == null) {
            message = "El valor que determina si se desea reconfigurar la configuracion publica o privada";
            error = true;
        }
        if (error) {
        	logger.error(message);
            throw new LocalgisInvalidParameterException(message);
        }
        /*
         * Obtenemos la entidad y configuramos el servidor de mapas
         */
        GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal = geopistaEntidadSupramunicipalDAO.selectByPrimaryKey(idEntidad);
        wmsConfigurator.configureWMSServer(geopistaEntidadSupramunicipal, publicMaps);
    }
    
    /**
     * Método para añadir una capa de geopista a un mapa determinado de localgis
     * 
     * @param geopistaLayer Capa de geopista a añadir
     * @param idMap Identificador del mapa de localgis
     * @param idEntidad Identificador de la entidad
     * @param idsMunicipios Coleccion de municipios que contiene la entidad
     * @param publicMap Si el mapa es publico o no
     * @param srid SRID de la entidad
     * @throws LocalgisConfigurationException Si ocurre algún error de configuración
     */
    private void insertLayer(GeopistaLayer geopistaLayer, Integer idMap, Integer idEntidad, Collection<Integer> idsMunicipios, Boolean publicMap, String srid) throws LocalgisConfigurationException,NoResourceAvailableException,NoResourceAvailableException,Exception {
        /*
         * Si ya existe una capa con el mismo id geopista, no se inserta, se
         * actualiza. La misma capa puede estar para un mismo municpio y para
         * una configuracion publica o privada
         */
        LocalgisLayer localgisLayer = localgisLayerDAO.selectLayerByIdEntidadAndIdGeopista(idEntidad, geopistaLayer.getIdLayer(), publicMap);
        Integer idLayer;
        boolean layerUpdated = false;
        if (localgisLayer != null) {
            localgisLayer.setLayerquery(LocalgisUtils.changeSelectQuery(geopistaLayer.getSelectQuery(), idsMunicipios));
            localgisLayerDAO.updateByPrimaryKey(localgisLayer);
            idLayer = localgisLayer.getLayerid();
            layerUpdated = true;
        } else {
            localgisLayer = new LocalgisLayer();
            localgisLayer.setLayeridgeopista(geopistaLayer.getIdLayer());
            localgisLayer.setLayerquery(LocalgisUtils.changeSelectQuery(geopistaLayer.getSelectQuery(), idsMunicipios));
            localgisLayer.setLayername(geopistaLayer.getName());
            /*
             * Insertamos un espacio en vez de un string vacio porque en oracle
             * no se pueden insertar strings vacios y la columna es not null
             */
            localgisLayer.setGeometrytype(" ");
            
            idLayer = localgisLayerDAO.insert(localgisLayer);
        }

        /*
         * Si estamos en una actualización de capa entonces obtenemos
         * todos los atributos para actualizar los atributos
         * correspondientes.
         */
        List listAttributesLayer = null;
        if (layerUpdated) {
            listAttributesLayer = localgisAttributeDAO.selectAttributesByLayer(idLayer,idMap);
        }
        List listColumns = geopistaColumnDAO.selectColumnsByLayer(geopistaLayer.getIdLayer());
        Iterator itColumns = listColumns.iterator();

        /*
         * Nombre de la tabla y de la columna que contiene la geometria y el identificador
         */
        String tableGeometry = null;
        String columnGeometry = null;
        while (itColumns.hasNext()) {
            GeopistaColumn geopistaColumn = (GeopistaColumn) itColumns.next();
            
            /*
             * Buscamos la columna en la lista de atributos de la capa si estabamos en una actualizacion de capa
             */
            boolean updated = false;
            LocalgisAttribute localgisAttribute = null;
            if (layerUpdated) {
                Iterator itAttributes = listAttributesLayer.iterator();
                int index = -1;
                while (itAttributes.hasNext() && !updated) {
                    localgisAttribute = (LocalgisAttribute) itAttributes.next();
                    index++;
                    if (localgisAttribute.getAttributeidgeopista() == geopistaColumn.getIdAttributeGeopista()) {
                        updated = true;
                    }
                }
            }

            /*
             * Si la columna es de tipo geometry establecemos en la capa el tipo de geometria
             */
            if (LocalgisUtils.isGeometryType(geopistaColumn.getType())) {
                String geometryType = LocalgisUtils.getGeometryType(geopistaColumn.getGeometryType());
                /*
                 * Insertamos un espacio en vez de un string vacio porque en oracle
                 * no se pueden insertar strings vacios y la columna es not null
                 */
                if (geometryType.equals("")) {
                    geometryType = " ";
                }
                localgisLayer.setGeometrytype(geometryType);
                tableGeometry = geopistaColumn.getTablename();
                columnGeometry = geopistaColumn.getName();
            }

            /*
             * Para el caso de la capa de municipios hay un problema con las
             * peticiones GetFeatureInfo porque la query asociada a esa capa
             * suele ser algo asi: "select Municipios.*,
             * Provincias.NombreOficial from ...." y en la tabla de municipios y
             * en la de provincias hay una columna que se llama igual
             * (NombreOficial). Cuando ocurre esto el mapserver falla y no
             * delvuelve correctamente el GML. Como modificaremos la query para
             * que sea algo asi como "select Municipios.*,
             * Provincias.NombreOficial as NombreOficialProvincia" también
             * tendremos que modificar la columna de Geopista. Ciertamente es
             * una chapuza pero es la manera mas simple de resolver el problema
             */
            if (localgisLayer.getLayername().equals("municipios")) {
                if (geopistaColumn.getTablename().equalsIgnoreCase("Provincias") && geopistaColumn.getName().equalsIgnoreCase("NombreOficial")) {
                    geopistaColumn.setName("NombreOficialProvincia");
                }
            }
            
            /*
             * Si la columna actual de geopista ya estaba como atributo en localgis lo actualizamos. Si no estaba la insertamos.
             */
            if (updated) {
                localgisAttribute.setAttributename(geopistaColumn.getName());
                localgisAttribute.setAttributeidalias(geopistaColumn.getIdAlias());
                localgisAttribute.setMapid(idMap);

                localgisAttributeDAO.updateByPrimaryKey(localgisAttribute);
            } else {
                localgisAttribute = new LocalgisAttribute();
                localgisAttribute.setAttributeidgeopista(geopistaColumn.getIdAttributeGeopista());
                localgisAttribute.setAttributename(geopistaColumn.getName());
                localgisAttribute.setLayerid(idLayer);
                localgisAttribute.setAttributeidalias(geopistaColumn.getIdAlias());
                localgisAttribute.setMapid(idMap);

                localgisAttributeDAO.insert(localgisAttribute);
            }
        }

		/*
		 * Modificamos la query para añadir id (sera el oid en postgres y el
		 * rowid en oracle.) con un alias determinado. En Oracle llamamos a la
		 * funcion DBMS_ROWID.ROWID_ROW_NUMBER del rowid para que nos devuelva
		 * un numero, ya que si no luego el mapserver fallara
		 */
        String connectionType = Configuration.getPropertyString(Configuration.PROPERTY_DB_CONNECTIONTYPE);
        String queryToReplace;
        String layerQuery = localgisLayer.getLayerquery();
        if (connectionType.equals("oraclespatial")) {
            queryToReplace = "SELECT DBMS_ROWID.ROWID_ROW_NUMBER("+tableGeometry+".rowid) as "+wmsConfigurator.getAliasId()+",";
        } else {
			/*
			 * En el caso de postgres, tenemos que cambiar la columna geometry
			 * para que pasemos de tener en las queries 'as "GEOMETRY"' y tengamos
			 * 'as alias_geometria' ya que si no luego el mapserver fallará con
			 * las mayúsculas.
			 */
        	layerQuery = layerQuery.replaceFirst(" [aA][sS] \"GEOMETRY\"", " as "+wmsConfigurator.getAliasGeometry());
        	
        	if (layerQuery.toString().contains("lcg_view")){
        		String newTableName=getViewName(layerQuery);
        		if (newTableName!=null)
        			queryToReplace = "SELECT \""+newTableName+"\".oid"+" as "+wmsConfigurator.getAliasId()+",";
        		else
        			queryToReplace = "SELECT \""+tableGeometry+"\".oid"+" as "+wmsConfigurator.getAliasId()+",";
        	}
        	else{
        		queryToReplace = "SELECT \""+tableGeometry+"\".oid"+" as "+wmsConfigurator.getAliasId()+",";
        	}
        }
        layerQuery = layerQuery.replaceFirst("[sS][eE][lL][eE][cC][tT]", queryToReplace);
        
        /*
         * Para el caso de la capa de municipios hay un problema con las
         * peticiones GetFeatureInfo porque la query asociada a esa capa suele
         * ser algo asi: "select Municipios.*, Provincias.NombreOficial from
         * ...." y en la tabla de municipios y en la de provincias hay una
         * columna que se llama igual (NombreOficial). Cuando ocurre esto el
         * mapserver falla y no delvuelve correctamente el GML. Ciertamente es
         * una chapuza pero es la manera mas simple de resolver el problema
         */
        if (localgisLayer.getLayername().equals("municipios")) {
            if (layerQuery.indexOf("Municipios.*, Provincias.NombreOficial") != -1) {
                layerQuery = layerQuery.replaceFirst("Municipios.*, Provincias.NombreOficial", "Municipios.*, Provincias.NombreOficial as NombreOficialProvincia");
            }
        }
        
        localgisLayer.setLayerquery(layerQuery);
            
        /*
         * Actualizmaos la capa de localgis para guardar el tipo de geometria
         */
        localgisLayerDAO.updateByPrimaryKey(localgisLayer);

        /*
         * Creamos un map de atributos donde guardamos como clave el alias del
         * atributo y como valor el nombre. El map lo creamos obteniendo las
         * columnas de una capa para todos los idiomas disponibles
         */
        Map mapAliasAttributes = new HashMap();
        List columnsAllLanguages = geopistaColumnDAO.selectColumnsByLayerTranslatedAllLanguages(geopistaLayer.getIdLayer());
        Iterator itColumnsAllLanguages = columnsAllLanguages.iterator();
        while (itColumnsAllLanguages.hasNext()) {
            GeopistaColumn geopistaColumn = (GeopistaColumn) itColumnsAllLanguages.next();
            mapAliasAttributes.put(geopistaColumn.getAlias(), geopistaColumn.getName());
        }
        
        //Insertamos el revision_expirada
        mapAliasAttributes.put("Revision Expirada", "revision_expirada");
 
        /*
         * Insertamos siempre un estilo nuevo porque para un mapa determinado la
         * misma capa se puede ver con diferente estilo. Para crear el estilo
         * pasamos la lista de atributos que hemos insertado o actualizado
         * anteriormente
         */
        
        //Aqui es necesario verificar si la imagenes esten accesibles para su publicacion
        Integer idStyle=null;
        try{
	        LocalgisStyle localgisStyle = LocalgisUtils.createLocalgisStyle(geopistaLayer.getXmlStyle(), geopistaLayer.getNameStyle(), geopistaLayer.getName(), mapAliasAttributes);
	        idStyle = localgisStyleDAO.insert(localgisStyle);
        }
        catch (Exception e){
        	logger.debug("Error al publicar el estilo de la capa: " +  geopistaLayer.getName());
        	throw e;
        }
        
        /*
         * Insertamos la relacion mapa, capa, estilo y posicion
         */
        LocalgisMapLayer localgisMapLayer = new LocalgisMapLayer();
        localgisMapLayer.setMapid(idMap);
        localgisMapLayer.setLayerid(idLayer);
        localgisMapLayer.setStyleid(idStyle);
        localgisMapLayer.setPosition(geopistaLayer.getPosition());
        localgisMapLayer.setPositionLayerFamily(geopistaLayer.getPositionLayerFamily());
        localgisMapLayer.setVisible(geopistaLayer.getVisible());
        localgisMapLayerDAO.insert(localgisMapLayer);

    }

    private String getViewName(String layerquery){
    	
    	String resultado=null;
    	try {					
			String posiciontabla="lcg_view";
			int posicionInicial=layerquery.indexOf(posiciontabla);
			int posicionFinal=layerquery.indexOf(",",posicionInicial);
			resultado=layerquery.substring(posicionInicial+posiciontabla.length()+1,posicionFinal).trim();
			//System.out.println("Resultado:<"+resultado+">");
			
			
		} catch (Exception e) {
		}
    	return resultado;
    }
    
    /** 
     * Método para insertar las capas WMS de un mapa de geopista en un mapa de localgis
     * @param geopistaMap Mapa de geopista
     * @param idMap Identificador del mapa de localgis
     */
    private void insertWMSLayers(Integer idMapGeopista, Integer idMunicipio, Integer idMap) {
        List listLayersWMS = geopistaMapServerLayerDAO.selectLayersByIdMapAndIdEntidad(idMapGeopista, idMunicipio);
        Iterator itLayersWMS = listLayersWMS.iterator();
        while (itLayersWMS.hasNext()) {
            GeopistaMapServerLayer geopistaMapServerLayer = (GeopistaMapServerLayer) itLayersWMS.next();
            
            LocalgisMapServerLayer localgisMapServerLayer = new LocalgisMapServerLayer();
            localgisMapServerLayer.setService(geopistaMapServerLayer.getService());
            localgisMapServerLayer.setUrl(geopistaMapServerLayer.getUrl());
            localgisMapServerLayer.setLayers(geopistaMapServerLayer.getParams());
            localgisMapServerLayer.setSrs(geopistaMapServerLayer.getSrs());
            localgisMapServerLayer.setFormat(geopistaMapServerLayer.getFormat());
            localgisMapServerLayer.setVersion(geopistaMapServerLayer.getVersion());
            localgisMapServerLayer.setActiva(geopistaMapServerLayer.getActiva());
            localgisMapServerLayer.setVisible(geopistaMapServerLayer.getVisible());
            localgisMapServerLayer.setMapid(idMap);
            localgisMapServerLayer.setPosition(geopistaMapServerLayer.getPosition());
            localgisMapServerLayer.setIdgeopista(geopistaMapServerLayer.getId());

            localgisMapServerLayerDAO.insert(localgisMapServerLayer);
        }
    }


    /**
     * Método para eliminar las capas y estilos que no se están utilizando
     * @throws LocalgisDBException Si ocurre algun error con la base de datos
     */
    private void cleanUnusedLayersAndStyles() throws LocalgisDBException {
        List listUnusedLayers = localgisLayerDAO.selectUnreferenceLayers();
        Iterator itUnusedLayers = listUnusedLayers.iterator();
        while (itUnusedLayers.hasNext()) {
            LocalgisLayer localgisLayer = (LocalgisLayer) itUnusedLayers.next();
            localgisLayerDAO.deleteByPrimaryKey(localgisLayer.getLayerid());
        }
        List listUnusedStyles = localgisStyleDAO.selectUnreferenceStyles();
        Iterator itUnusedStyles = listUnusedStyles.iterator();
        while (itUnusedStyles.hasNext()) {
            LocalgisStyle localgisStyle = (LocalgisStyle) itUnusedStyles.next();
            localgisStyleDAO.deleteByPrimaryKey(localgisStyle.getStyleid());
        }

    }
    
    private String getMapSRID(GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal, GeopistaMap geopistaMap) {
        // Se intenta obtener el srid del mapa. Si no, se usa el de la entidad.
        String srid = geopistaEntidadSupramunicipal.getSrid();
        try {
            String xml = geopistaMap.getXml();
            logger.debug("XML del mapa a publicar: " + xml);
            StringReader sr = new StringReader(xml);
            SAXBuilder builder = new SAXBuilder();
            Document doc = null;
            doc = builder.build(sr);
            Element root = doc.getRootElement();
            Element mapProjection = root.getChild("mapProjection");
            if (mapProjection != null) {
            	String mpString = mapProjection.getText();
            	logger.debug("Valor de mapProjection: " + mpString);
            	CoordinateSystem cs = CoordinateSystemRegistry.instance().get(mpString);
            	if (cs != null) {
            		srid = String.valueOf(cs.getEPSGCode());
            		logger.info("srid del mapa a publicar: " + srid);
            	}
            }
        } catch (Exception e) {
            logger.warn("Error al obtener srid del mapa. Se usará el de la entidad.", e);
        }
        return srid;
    }

	@Override
	public List getEntidadesPublicadas(Integer mapidgeopista, Boolean publicMap) throws LocalgisInvalidParameterException, LocalgisDBException  {
		// TODO Auto-generated method stub
		
	    boolean error = false;
        String message = null;
        if (mapidgeopista == null) {
            message = "El id del mapa no puede ser nulo";
            error = true;
        }
        if (error) {
        	logger.error(message);
            throw new LocalgisInvalidParameterException(message);
        }
        
        try {
            return localgisMapDAO.getEntidadesPublicadas(mapidgeopista,publicMap);        
        } catch (DaoException e) {
            logger.error("Error al obtener las entidades que han publicado el mapa ["+mapidgeopista+"]", e);
            throw new LocalgisDBException("Error al obtener las entidades que han publicado el mapa ["+mapidgeopista+"]",e);
        }
    }
    
	public static void main(String args[]){
		String cadena="SELECT '' as lcg_view_v_ce_ct  ,transform(\"v_ce_ct\".\"GEOMETRY\" , ?) AS \"GEOMETRY\",* from v_ce_ct WHERE \"v_ce_ct\".\"id_municipio\" in (?M) AND \"v_ce_ct\".\"clave\"<>'AN'";
		
		String posiciontabla="lcg_view";
		int posicionInicial=cadena.indexOf(posiciontabla);
		int posicionFinal=cadena.indexOf(",",posicionInicial);
		String resultado=cadena.substring(posicionInicial+posiciontabla.length()+1,posicionFinal).trim();
		System.out.println("Resultado:<"+resultado+">");
		
	}

	@Override
	public GeopistaMap selectMapByName(String nombreMapa, Integer idMunicipio) {
		return geopistaMapDAO.selectMapByName(nombreMapa, idMunicipio);
	}

	@Override
	public String getCategoryByLayerName(String layerName) {
		return geopistaLayerDAO.getCategoryByLayerName(layerName);
	}

}
