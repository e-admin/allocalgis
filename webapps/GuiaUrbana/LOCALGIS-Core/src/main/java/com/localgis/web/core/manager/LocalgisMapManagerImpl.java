/**
 * LocalgisMapManagerImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;



import com.ibatis.dao.client.DaoException;
import com.ibatis.dao.client.DaoManager;
import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.config.Configuration;
import com.localgis.web.core.dao.LocalgisIncidenciaDAO;
import com.localgis.web.core.dao.LocalgisLayerDAO;
import com.localgis.web.core.dao.LocalgisMapDAO;
import com.localgis.web.core.dao.LocalgisMapServerLayerDAO;
import com.localgis.web.core.dao.LocalgisMarkerDAO;
import com.localgis.web.core.dao.LocalgisMunicipioDAO;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.exceptions.LocalgisMapNotFoundException;
import com.localgis.web.core.model.GeopistaMunicipio;
import com.localgis.web.core.model.GeopistaVia;
import com.localgis.web.core.model.LocalgisIncidencia;
import com.localgis.web.core.model.LocalgisLayer;
import com.localgis.web.core.model.LocalgisLayerExt;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.core.model.LocalgisMarker;
import com.localgis.web.core.wms.WMSConfigurator;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Implementación de LocalgisMapManager
 * 
 * @author albegarcia
 *
 */
public class LocalgisMapManagerImpl implements LocalgisMapManager {

    /**
     * Logger para las trazas
     */
    private static Logger logger = Logger.getLogger(LocalgisMapManagerImpl.class);

    /**
     * Dao para las capas de localgis
     */
    private LocalgisLayerDAO localgisLayerDAO;

    /**
     * Dao para los mapas de localgis 
     */
    private LocalgisMapDAO localgisMapDAO;

    /**
     * Dao para las capas wms de localgis
     */
    private LocalgisMapServerLayerDAO localgisMapServerLayerDAO;
    
    /**
     * Dao para las marcas de posicion
     */
    private LocalgisMarkerDAO localgisMarkerDAO;

    /**
     * LocalgisManagerBuilder que construyo el objeto
     */
    private LocalgisManagerBuilder localgisManagerBuilder;

    /**
     * Acceso a datos de municipios
     */
    private LocalgisMunicipioDAO localgisMunicipioDAO;
    
    /**
     * Configurador del servidor de mapas
     */
    private WMSConfigurator wmsConfigurator;

	private LocalgisIncidenciaDAO localgisIncidenciaDAO;

    /**
     * Constructor a partir de un DAOManager, un configurador de wms y un LocalgisManagerBuilder
     */
    public LocalgisMapManagerImpl(DaoManager daoManager, WMSConfigurator wmsConfigurator, LocalgisManagerBuilder localgisManagerBuilder) {
        this.localgisLayerDAO = (LocalgisLayerDAO) daoManager.getDao(LocalgisLayerDAO.class);
        this.localgisMapDAO = (LocalgisMapDAO) daoManager.getDao(LocalgisMapDAO.class);
        this.localgisMapServerLayerDAO = (LocalgisMapServerLayerDAO) daoManager.getDao(LocalgisMapServerLayerDAO.class);
        this.localgisMarkerDAO = (LocalgisMarkerDAO) daoManager.getDao(LocalgisMarkerDAO.class);
        this.localgisIncidenciaDAO = (LocalgisIncidenciaDAO) daoManager.getDao(LocalgisIncidenciaDAO.class);
        this.localgisMunicipioDAO = (LocalgisMunicipioDAO) daoManager.getDao(LocalgisMunicipioDAO.class);

        this.wmsConfigurator = wmsConfigurator;
        this.localgisManagerBuilder = localgisManagerBuilder;
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapManager#getIdMapByIdGeopista(java.lang.Integer, java.lang.Integer, java.lang.Boolean)
     */
    public Integer getIdMapByIdGeopista(Integer idGeopistaMap,Integer idEntidad, Boolean publicMap) throws LocalgisInvalidParameterException, LocalgisMapNotFoundException,LocalgisDBException {
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
            Integer idLocalGisMap=localgisMapDAO.getIdMapByIdGeopista(idGeopistaMap, idEntidad, localgisManagerBuilder.getDefaultLocale(), publicMap);
            if (idLocalGisMap==null)
                throw new LocalgisMapNotFoundException(("El mapa con id \""+idGeopistaMap+"\" no existe."));
            else
                return idLocalGisMap;
        } catch (DaoException e) {
            logger.error("Error al obtener el id del mapa ["+idGeopistaMap+"] para la entidad ["+idEntidad+"]", e);
            throw new LocalgisDBException("Error al obtener el id del mapa ["+idGeopistaMap+"] para para la entidad ["+idEntidad+"]",e);
        }
    }
    
    public List<String> getGeometryFromLayer(String layer, String municipio) throws LocalgisDBException {
    	List<String> lista = null;
    	
    	try {
    		lista = localgisLayerDAO.getGeometryFromLayer(layer, municipio);
    	} catch(DaoException e) {
    		logger.error("Error al obtener las geometrías de la capa [" + layer + "]", e);
    		throw new LocalgisDBException("Error al obtener las geometrías de la capa [" + layer + "]", e);
    	}
    	
    	return lista;
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapManager#getPublishedMap(java.lang.Integer)
     */
    public LocalgisMap getPublishedMap(Integer idMap) throws LocalgisInvalidParameterException, LocalgisDBException {
        /*
         * Comprobacion de los parametros de entrada
         */
        boolean error = false;
        String message = null;
        if (idMap == null) {
            message = "El id del mapa no puede ser nulo";
            error = true;
        }
        if (error) {
        	logger.error(message);
            throw new LocalgisInvalidParameterException(message);
        }
        
        try {
            return localgisMapDAO.selectMapById(idMap, localgisManagerBuilder.getDefaultLocale());        
        } catch (DaoException e) {
            logger.error("Error al obtener el mapa con id ["+idMap+"]", e);
            throw new LocalgisDBException("Error al obtener el mapa con id ["+idMap+"]",e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapManager#getMapLayers(java.lang.Integer)
     */
    public List getMapLayers(Integer idMap) throws LocalgisDBException {
        return getMapLayers(idMap, localgisManagerBuilder.getDefaultLocale());
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapManager#getMapLayers(java.lang.Integer, java.lang.String)
     */
    public List getMapLayers(Integer idMap, String locale) throws LocalgisDBException {
        locale = localgisManagerBuilder.getLocaleSelected(locale);
        try {
            return localgisLayerDAO.selectLayersByIdMap(idMap, locale);
        } catch (DaoException e) {
            logger.error("Error al obtener las capas", e);
            throw new LocalgisDBException("Error al obtener las capas",e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapManager#getMapLayersByIdGeopista(java.lang.Integer)
     */
    public List getMapLayersByIdGeopista(Integer idMapGeopista) throws LocalgisDBException {
        return getMapLayersByIdGeopista(idMapGeopista, localgisManagerBuilder.getDefaultLocale());
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapManager#getMapLayersByIdGeopista(java.lang.Integer)
     */

    public List getMapLayersByIdGeopistaAndEntidad(Integer idMapGeopista,Integer idEntidad) throws LocalgisDBException {
    	return getMapLayersByIdGeopistaAndEntidad(idMapGeopista,idEntidad,1);
    }
    public List getMapLayersByIdGeopistaAndEntidad(Integer idMapGeopista,Integer idEntidad,int tipoPublicacion) throws LocalgisDBException {
    	String locale = localgisManagerBuilder.getDefaultLocale();
        try {
            return localgisLayerDAO.selectLayersByIdMapGeopistaAndEntidad(idMapGeopista, locale,idEntidad,tipoPublicacion);
        } catch (DaoException e) {
            logger.error("Error al obtener las capas", e);
            throw new LocalgisDBException("Error al obtener las capas",e);
        }
    }
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapManager#getMapLayersByIdGeopista(java.lang.Integer, java.lang.String)
     */
    public List getMapLayersByIdGeopista(Integer idMapGeopista, String locale) throws LocalgisDBException {
        locale = localgisManagerBuilder.getLocaleSelected(locale);
        try {
            return localgisLayerDAO.selectLayersByIdMapGeopista(idMapGeopista, locale);
        } catch (DaoException e) {
            logger.error("Error al obtener las capas", e);
            throw new LocalgisDBException("Error al obtener las capas",e);
        }
    }
    


    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapManager#addMarker(java.lang.Integer, java.lang.String, java.lang.Double, java.lang.Double, java.lang.Double, java.lang.String, java.lang.String)
     */
    public Integer addMarker(Integer idMap, String username, Double x, Double y, Double scale, String markname, String marktext) throws LocalgisInvalidParameterException, LocalgisDBException {
        /*
         * Comprobacion de los parametros de entrada
         */
        boolean error = false;
        String message = null;
        if (idMap == null) {
            message = "El id del mapa no puede ser nulo";
            error = true;
        } else if (username == null || username.trim().equals("")) {
            message = "El nombre del usuario no puede ser nulo o vacio";
            error = true;
        } else if (x == null) {
            message = "La x no puede ser nula";
            error = true;
        } else if (y == null) {
            message = "La y no puede ser nula";
            error = true;
        } else if (scale == null) {
            message = "La escala no puede ser nula";
            error = true;
        } else if (markname == null || markname.trim().equals("")) {
            message = "El nombre de la marca no puede ser nulo o vacio";
            error = true;
        }
        if (error) {
            throw new LocalgisInvalidParameterException(message);
        }
        LocalgisMarker localgisMarker = new LocalgisMarker();
        try {
            localgisMarker.setMapid(idMap);
            localgisMarker.setUsername(username);
            localgisMarker.setX(x);
            localgisMarker.setY(y);
            localgisMarker.setScale(scale);
            localgisMarker.setMarkname(markname);
            localgisMarker.setMarktext(marktext);
            return localgisMarkerDAO.insert(localgisMarker);
        } catch (DaoException e) {
            logger.error("Error al insertar la marca de posición", e);
            throw new LocalgisDBException("Error al insertar la marca de posición",e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapManager#updateMarker(java.lang.Integer, java.lang.String, java.lang.String, java.lang.String)
     */
    public void updateMarker(Integer markerId, String username, String markname, String marktext) throws LocalgisInvalidParameterException, LocalgisDBException {
        /*
         * Comprobacion de los parametros de entrada
         */
        boolean error = false;
        String message = null;
        if (markerId == null) {
            message = "El id de la marca no puede ser nulo";
            error = true;
        } else if (username == null || username.trim().equals("")) {
            message = "El nombre del usuario no puede ser nulo o vacio";
            error = true;
        } else if (markname == null || markname.trim().equals("")) {
            message = "El nombre de la marca no puede ser nulo o vacio";
            error = true;
        }
        if (error) {
            throw new LocalgisInvalidParameterException(message);
        }
        try {
            // Obtenemos el marcador para comprobar que el usuario sea el propietario
            LocalgisMarker localgisMarker = localgisMarkerDAO.selectByPrimaryKey(markerId);
            if (localgisMarker != null && localgisMarker.getUsername().equals(username)) {
                localgisMarker.setMarkerid(markerId);
                localgisMarker.setMarkname(markname);
                localgisMarker.setMarktext(marktext);
                localgisMarkerDAO.updateByPrimaryKey(localgisMarker);
            } else if (localgisMarker == null) {
                logger.error("Error al modificar la marca de posición. La marca de posicion no existe.");
                throw new LocalgisInvalidParameterException("La marca de posición no existe.");
            } else {
                logger.error("Error al modificar la marca de posición. El usuario no es el propietario de la marca.");
                throw new LocalgisInvalidParameterException("El usuario no es el propietario de la marca.");
            }
        } catch (DaoException e) {
            logger.error("Error al actualizar la marca de posición", e);
            throw new LocalgisDBException("Error al actualizar la marca de posición",e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapManager#deleteMarker(java.lang.Integer, java.lang.String)
     */
    public void deleteMarker(Integer markerId, String username) throws LocalgisInvalidParameterException, LocalgisDBException {
        /*
         * Comprobacion de los parametros de entrada
         */
        boolean error = false;
        String message = null;
        if (markerId == null) {
            message = "El id de la marca no puede ser nulo";
            error = true;
        } else if (username == null || username.trim().equals("")) {
            message = "El nombre del usuario no puede ser nulo o vacio";
            error = true;
        }
        if (error) {
        	logger.error(message);
            throw new LocalgisInvalidParameterException(message);
        }
        try {
            // Obtenemos el marcador para comprobar que el usuario sea el propietario
            LocalgisMarker localgisMarker = localgisMarkerDAO.selectByPrimaryKey(markerId);
            if (localgisMarker != null && localgisMarker.getUsername().equals(username)) {
                localgisMarkerDAO.deleteByPrimaryKey(markerId);
            } else if (localgisMarker == null) {
                logger.error("Error al eliminar la marca de posición. La marca de posicion no existe.");
                throw new LocalgisInvalidParameterException("La marca de posición no existe.");
            } else {
                logger.error("Error al eliminar la marca de posición. El usuario no es el propietario de la marca.");
                throw new LocalgisInvalidParameterException("El usuario no es el propietario de la marca.");
            }
        } catch (DaoException e) {
            logger.error("Error al eliminar la marca de posición", e);
            throw new LocalgisDBException("Error al eliminar la marca de posición",e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapManager#getMarkers(java.lang.Integer, java.lang.String)
     */
    public List getMarkers(Integer idMap, String username) throws LocalgisInvalidParameterException, LocalgisDBException {
        /*
         * Comprobacion de los parametros de entrada
         */
        boolean error = false;
        String message = null;
        if (idMap == null) {
            message = "El id del mapa no puede ser nulo";
            error = true;
        } else if (username == null || username.trim().equals("")) {
            message = "El nombre del usuario no puede ser nulo o vacio";
            error = true;
        }
        if (error) {
        	logger.error(message);
            throw new LocalgisInvalidParameterException(message);
        }
        try {
            return localgisMarkerDAO.selectMarkersByMapIdAndUsername(idMap, username);
        } catch (DaoException e) {
            logger.error("Error al insertar la marca de posición");
            throw new LocalgisDBException("Error al insertar la marca de posición",e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapManager#getMapWMSLayers(java.lang.Integer)
     */
    public List getMapWMSLayers(Integer idMap) throws LocalgisDBException {
        try {
            return localgisMapServerLayerDAO.selectMapServerLayersByIdMap(idMap);
        } catch (DaoException e) {
            logger.error("Error al obtener las capas", e);
            throw new LocalgisDBException("Error al obtener las capas",e);
        }
    }

    /**
     * Comprueba los parametros para una peticion de getMapServerURL generica
     * @param idEntidad Identificador de la entidad
     * @param idMap Identificador del mapa
     * @param publicMap Si el mapa es publico o privado
     * @throws LocalgisInvalidParameterException Si algun parametro es invalido
     */
    private void checkParamsForGetMapServerURLGeneric(Integer idEntidad, Integer idMap, Boolean publicMap) throws LocalgisInvalidParameterException {
        /*
         * Comprobacion de los parametros de entrada
         */
        boolean error = false;
        String message = null;
        if (idEntidad == null) {
            message = "El id de la entidad no puede ser nulo";
            error = true;
        } else if (idMap == null) {
            message = "El id del mapa no puede ser nulo";
            error = true;
        }
        if (error) {
        	logger.error(message);
            throw new LocalgisInvalidParameterException(message);
        }
    }
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapManager#getMapServerURL(java.lang.Integer, java.lang.Integer, java.lang.Boolean, int)
     */
    public String getMapServerURL(Integer idEntidad, Integer idMap, Boolean publicMap, int requestType) throws LocalgisInvalidParameterException, LocalgisConfigurationException {
        checkParamsForGetMapServerURLGeneric(idEntidad, idMap, publicMap);
        return wmsConfigurator.getMapServerURL(idEntidad, idMap, publicMap, requestType);
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisMapManager#getMapServerURL(java.lang.Integer, java.lang.Integer, java.lang.Boolean, int)
     */
    public String getMapServerURLInternal(Integer idEntidad, Integer idMap, Boolean publicMap, int requestType) throws LocalgisInvalidParameterException, LocalgisConfigurationException {
        checkParamsForGetMapServerURLGeneric(idEntidad, idMap, publicMap);
        return wmsConfigurator.getMapServerURLInternal(idEntidad, idMap, publicMap, requestType);
    }

	@SuppressWarnings("deprecation")
	@Override
	public Integer addIncidencia(String identificador, Integer idMap, String layerName,
			Integer idFeature, String tipoIncidencia,
			String gravedadIncidencia, String emailContacto,
			String descripcion, Double x, Double y, Integer idMunicipio)
			throws LocalgisInvalidParameterException, LocalgisDBException {
        /*
         * Comprobacion de los parametros de entrada
         */
        boolean error = false;
        String message = null;
        if (idMap == null) {
            message = "El id del mapa no puede ser nulo";
            error = true;
        } else if (identificador == null || identificador.trim().equals("")) {
            message = "El identificador no puede ser nulo o vacio";
            error = true;
        } else if (x == null) {
            message = "La x no puede ser nula";
            error = true;
        } else if (y == null) {
            message = "La y no puede ser nula";
            error = true;
        } else if (tipoIncidencia == null || tipoIncidencia.trim().equals("")) {
            message = "El tipo de incidencia no puede ser nulo o vacio";
            error = true;
        } else if (gravedadIncidencia == null || gravedadIncidencia.trim().equals("")) {
	            message = "La gravedad de la incidencia no puede ser nula o vacia";
	            error = true;
	    } else if (emailContacto == null || emailContacto.trim().equals("")) {
            message = "El email no puede ser nulo o vacio";
            error = true;
	    } else if (descripcion == null || descripcion.trim().equals("")) {
            message = "La descripción no puede ser nula o vacia";
            error = true;
	    }
        if (error) {
            throw new LocalgisInvalidParameterException(message);
        }
        LocalgisIncidencia localgisIncidencia = new LocalgisIncidencia();
        try {
        	LocalgisMap localgisMap = localgisMapDAO.selectMapById(new Integer(idMap), localgisManagerBuilder.getDefaultLocale()); 
        	GeometryFactory factory = new GeometryFactory();
        	
        	String sridVisualizacion=null;
        	try {
				sridVisualizacion=Configuration.getPropertyString(Configuration.PROPERTY_DISPLAYPROJECTION);
			} catch (Exception e) {
				sridVisualizacion=localgisMap.getSrid();
			}
        	
        	
       //    	PrecisionModel precisionModel = new PrecisionModel(100000000);
        	//GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(),new Integer(localgisMap.getSrid()));
        	GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(),new Integer(sridVisualizacion));

        	
        	Coordinate coord = new Coordinate( x, y );
        	Point point = factory.createPoint( coord );
        	//Geometry geom = point;
        	
        	String geometry = "SRID="+sridVisualizacion+";POINT("+x+" "+y+")";
        	//OSB 20120110 Eliminar el parámetro SRID en la construcción de texto de la geometría del punto de la incidencia
        	String geometryInsert = "POINT("+x+" "+y+")";
        	logger.info("Geometry: "+ geometry);
        	Integer srid = new Integer(sridVisualizacion);
        	// Habría que obtener el idMunicipio de la BBDD y no ponerlo directamente:
            localgisIncidencia.setMapid(idMap);
            localgisIncidencia.setSrid(srid);
            localgisIncidencia.setIdentificador(identificador);
            

            if (idFeature != null) {
               localgisIncidencia.setId_feature(idFeature);
            }
            if (layerName != null) {
                localgisIncidencia.setLayer_name(layerName);
            }
            localgisIncidencia.setTipo_incidencia(tipoIncidencia);
            localgisIncidencia.setGravedad_incidencia(gravedadIncidencia);
            localgisIncidencia.setEmail(emailContacto);
            localgisIncidencia.setDescripcion(descripcion);
            
            //Recuperar idMunicpio
            logger.info("SRID Visualizacion:"+sridVisualizacion);
            GeopistaMunicipio municipio = (GeopistaMunicipio) localgisMunicipioDAO.selectMunicipioByGeometry(sridVisualizacion, geometryInsert);
            if (municipio!=null) {
            localgisIncidencia.setId_municipio(new Integer(municipio.getId()));
            }else{
            	logger.info("Guardando incidencia sin idMunicipio");
            }
            //localgisIncidencia.setGeometry(point);
            localgisIncidencia.setValor(geometryInsert);            
            
            return localgisIncidenciaDAO.insert(localgisIncidencia);
        } catch (DaoException e) {
            logger.error("Error al insertar la incidencia", e);
            throw new LocalgisDBException("Error al insertar la incidencia",e);
        }
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String getDomain(String domainName,Integer idEntidad) {
        try{
            return localgisIncidenciaDAO.getDomain(domainName, idEntidad);
        } catch (Exception e) {
            logger.error("Error al recuperar los domininios:", e);
            return "";
        }
	}

	@SuppressWarnings("deprecation")
	@Override
    public String getLayerIdFromIdGuiaUrbana(String layerId){
        String idGeopista="";
        try {
        	idGeopista= (String) localgisLayerDAO.getIdLayerFromGuiaUrbana(layerId);
        } catch (DaoException e) {
            logger.error("Error al obtener las capas", e);
            System.out.println("Error al intentar recuperar idLayer getLayerIdFromIdGuiaUrbana");
            //throw new LocalgisDBException("Error al obtener las capas",e);
        }
        return idGeopista;
    }

	@Override
	public LocalgisLayerExt getLayerById(Integer idLayer,Integer idMap,String locale) {
		LocalgisLayerExt localgisLayer=null;
        try {
        	localgisLayer= (LocalgisLayerExt) localgisLayerDAO.selectLayerById(idLayer,idMap,locale);
        } catch (DaoException e) {
            logger.error("Error al obtener las capas", e);
            System.out.println("Error al intentar recuperar idLayer (geopista)");
            //throw new LocalgisDBException("Error al obtener las capas",e);
        }
        return localgisLayer;
		
	}

	@Override
	public LocalgisLayer getLayerByName(String layername) {
		LocalgisLayer localgisLayer=null;
        try {
        	localgisLayer= (LocalgisLayer) localgisLayerDAO.selectLayerByName(layername);
        } catch (DaoException e) {
            logger.error("Error al obtener las capas", e);
            System.out.println("Error al intentar recuperar el nombre de la capa");
            //throw new LocalgisDBException("Error al obtener las capas",e);
        }
        return localgisLayer;
	}

	@Override
	public List<HashMap<String, Object>> selectPublicAnexosByIdLayerAndIdFeature(
			int idLayer, int idFeature) {
		return localgisLayerDAO.selectPublicAnexosByIdLayerAndIdFeature(idLayer, idFeature);
	}

	@Override
	public List<HashMap<String, Object>> selectAllAnexosByIdLayerAndIdFeature(
			int idLayer, int idFeature) {
		return localgisLayerDAO.selectAllAnexosByIdLayerAndIdFeature(idLayer, idFeature);
	}

	@Override
	public GeopistaMunicipio selectMunicipioByGeometry(String srid,
			String geometrySelect) {
		return localgisMunicipioDAO.selectMunicipioByGeometry(srid, geometrySelect);
	}

	@Override
	public List<HashMap<String,Object>> getLayersInArea(ArrayList listaCompletaCapas,String srid,String bbox) {
		// TODO Auto-generated method stub
		return localgisLayerDAO.getLayersInArea(listaCompletaCapas,srid,bbox);
	}

	 

}
