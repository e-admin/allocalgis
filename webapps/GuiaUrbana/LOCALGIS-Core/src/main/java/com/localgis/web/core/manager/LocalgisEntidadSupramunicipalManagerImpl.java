/**
 * LocalgisEntidadSupramunicipalManagerImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.manager;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.ibatis.dao.client.DaoException;
import com.ibatis.dao.client.DaoManager;
import com.localgis.web.core.dao.GeopistaCoverageLayerDAO;
import com.localgis.web.core.dao.GeopistaEntidadSupramunicipalDAO;
import com.localgis.web.core.dao.GeopistaLayerDAO;
import com.localgis.web.core.dao.GeopistaMunicipioDAO;
import com.localgis.web.core.dao.GeopistaNucleoDAO;
import com.localgis.web.core.dao.LocalgisCSSDAO;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.model.GeopistaCoverageLayer;
import com.localgis.web.core.model.GeopistaEntidadSupramunicipal;
import com.localgis.web.core.model.GeopistaLayer;
import com.localgis.web.core.model.GeopistaMunicipio;
import com.localgis.web.core.model.GeopistaNucleo;
import com.localgis.web.core.model.LocalgisCSS;

/**
 * Implementación de LocalgisEntidadSupramunicipalManager
 * 
 * @author davidcaaveiro
 *
 */
public class LocalgisEntidadSupramunicipalManagerImpl implements LocalgisEntidadSupramunicipalManager {

    /**
     * Logger para las trazas
     */
    private static Logger logger = Logger.getLogger(LocalgisEntidadSupramunicipalManagerImpl.class);

    /**
     * Dao para las coverage layers de geopista
     */
    private GeopistaCoverageLayerDAO geopistaCoverageLayerDAO;

    /**
     * Dao para las entidades supramunicipales de geopista
     */
    private GeopistaEntidadSupramunicipalDAO geopistaEntidadSupramunicipalDAO;

    /**
     * Dao para los municipios de geopista
     */
    private GeopistaMunicipioDAO geopistaMunicipioDAO;

    /**
     * Dao para las capas de geopista
     */
    private GeopistaLayerDAO geopistaLayerDAO;

    /**
     * Dao para las los css de los estilos 
     */    
    private LocalgisCSSDAO localgisCSSDAO;

    
    private GeopistaNucleoDAO geopistaNucleoDAO;
    
    /**
     * Constructor a partir de un DAOManager
     */
    public LocalgisEntidadSupramunicipalManagerImpl(DaoManager daoManager) {
        this.geopistaCoverageLayerDAO = (GeopistaCoverageLayerDAO) daoManager.getDao(GeopistaCoverageLayerDAO.class);
        this.geopistaEntidadSupramunicipalDAO = (GeopistaEntidadSupramunicipalDAO) daoManager.getDao(GeopistaEntidadSupramunicipalDAO.class);
        this.geopistaLayerDAO = (GeopistaLayerDAO) daoManager.getDao(GeopistaLayerDAO.class);
        this.geopistaMunicipioDAO = (GeopistaMunicipioDAO) daoManager.getDao(GeopistaMunicipioDAO.class);
        this.localgisCSSDAO = (LocalgisCSSDAO) daoManager.getDao(LocalgisCSSDAO.class);
        this.geopistaNucleoDAO = (GeopistaNucleoDAO) daoManager.getDao(GeopistaNucleoDAO.class);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager#getCSS(java.lang.Integer)
     */
    public String getCSS(Integer idEntidad) throws LocalgisInvalidParameterException, LocalgisDBException {
        String message = new String();
        boolean error = false;
        if (idEntidad == null) {
            message = "El id de la entidad del mapa no puede ser nulo";
            error = true;
        }
        if (error) {
        	logger.error(message);
            throw new LocalgisInvalidParameterException(message);
        }
        
        try {
            LocalgisCSS localgisCSS = localgisCSSDAO.selectByPrimaryKey(idEntidad);
            if (localgisCSS != null) {
                return localgisCSS.getContent();
            }
            else {
                return null;
            }
        } catch (DaoException e) {
            logger.error("Error al obtener los CSS para la entidad ["+ idEntidad +"]");
            throw new LocalgisDBException("Error al obtener los CSS para la entidad ["+ idEntidad +"]",e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager#removeCSS(java.lang.Integer)
     */
    public void removeCSS(Integer idEntidad) throws LocalgisInvalidParameterException, LocalgisDBException {
        String message = new String();
        boolean error = false;
        if (idEntidad == null) {
            message = "El id de la entidad del mapa no puede ser nulo";
            error = true;
        }
        if (error) {
        	logger.error(message);
            throw new LocalgisInvalidParameterException(message);
        }
        
        try {
            localgisCSSDAO.deleteByPrimaryKey(idEntidad);
        } catch (DaoException e) {
            logger.error("Error al eliminar el CSS de la entidad ["+ idEntidad +"]");
            throw new LocalgisDBException("Error al eliminar el CSS de la entidad ["+ idEntidad +"]",e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager#updateCSS(java.lang.Integer, java.lang.String)
     */
    public int updateCSS(Integer idEntidad,String content) throws LocalgisInvalidParameterException, LocalgisDBException {
        String message = new String();
        boolean error = false;
        if (idEntidad == null) {
            message = "El id de la entidad del mapa no puede ser nulo";
            error = true;
        }
        if (error) {
        	logger.error(message);
            throw new LocalgisInvalidParameterException(message);
        }
        
        try {
            LocalgisCSS localgisCSS = new LocalgisCSS();
            localgisCSS.setIdentidad(idEntidad);
            localgisCSS.setContent(content);
            return localgisCSSDAO.updateByPrimaryKey(localgisCSS);
            
        } catch (DaoException e) {
            logger.error("Error al actualizar los CSS para la entidad ["+ idEntidad +"]");
            throw new LocalgisDBException("Error al actualizar los CSS para la entidad ["+ idEntidad +"]",e);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager#insertCSS(java.lang.Integer, java.lang.String)
     */
    public void insertCSS(Integer idEntidad,String content) throws LocalgisInvalidParameterException, LocalgisDBException {
        String message = new String();
        boolean error = false;
        if (idEntidad == null) {
            message = "El id de la entidad del mapa no puede ser nulo";
            error = true;
        }
        if (error) {
        	logger.error(message);
            throw new LocalgisInvalidParameterException(message);
        }
        
        try {
            LocalgisCSS localgisCSS = new LocalgisCSS();
            localgisCSS.setIdentidad(idEntidad);
            localgisCSS.setContent(content);
            localgisCSSDAO.insert(localgisCSS);
            
        } catch (DaoException e) {
            logger.error("Error al insertar los CSS para la entidad ["+ idEntidad +"]");
            throw new LocalgisDBException("Error al insertar los CSS para la entidad ["+ idEntidad +"]",e);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager#getCoverageLayer(java.lang.Integer)
     */
    public GeopistaCoverageLayer getCoverageLayer(Integer idEntidad) throws LocalgisInvalidParameterException, LocalgisDBException {
        String message = new String();
        boolean error = false;
        if (idEntidad == null) {
            message = "El id de la entidad del mapa no puede ser nulo";
            error = true;
        }
        if (error) {
        	logger.error(message);
            throw new LocalgisInvalidParameterException(message);
        }
        
        try {
            return geopistaCoverageLayerDAO.selectCoverageLayerByIdEntidad(idEntidad);
        } catch (DaoException e) {
            logger.error("Error al obtener la coverage layer para la entidad ["+idEntidad+"]");
            throw new LocalgisDBException("Error al obtener la coverage layer para el municipio ["+idEntidad+"]",e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager#getLayers(java.lang.Integer)
     */
    public List getLayers(Integer idEntidad) throws LocalgisDBException {
        try {
            return geopistaLayerDAO.selectLayersByIdEntidad(idEntidad);
        } catch (DaoException e) {
            logger.error("Error al obtener las capas", e);
            throw new LocalgisDBException("Error al obtener las capas",e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager#getMunicipio(java.lang.Integer)
     */
    public GeopistaEntidadSupramunicipal getEntidadSupramunicipal(Integer idEntidad) throws LocalgisDBException {
        try {
            GeopistaEntidadSupramunicipal entidadSupramunicipal = geopistaEntidadSupramunicipalDAO.selectByPrimaryKey(idEntidad);
            return entidadSupramunicipal;
        } catch (DaoException e) {
            logger.error("Error al obtener la entidad", e);
            throw new LocalgisDBException("Error al obtener la entidad",e);
        }
    }
    

    /* (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager#getAllEntidadesSupramunicipales()
     */
    public List<GeopistaEntidadSupramunicipal> getAllEntidadesSupramunicipales() throws LocalgisDBException {
        try {
            List<GeopistaEntidadSupramunicipal> entidadSupramunicipalList = geopistaEntidadSupramunicipalDAO.selectAll();
            return entidadSupramunicipalList;
        } catch (DaoException e) {
            logger.error("Error al obtener las entidades", e);
            throw new LocalgisDBException("Error al obtener las entidades",e);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager#getMunicipio(java.lang.Integer)
     */
    //public List getNucleosMunicipio(List municipios) throws LocalgisDBException {
    public List getNucleosMunicipio(Integer idMunicipios) throws LocalgisDBException {
        try {
           // List nucleosMunicipio = geopistaNucleoDAO.selectAll();
        	List nucleosMunicipio = geopistaNucleoDAO.selectNucleoByIdMunicipio(idMunicipios) ;
            return nucleosMunicipio;
        } catch (DaoException e) {
            logger.error("Error al obtener la entidad", e);
            throw new LocalgisDBException("Error al obtener la entidad",e);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisEntidadSupramunicipalManager#getMunicipiosByIdEntidad(java.lang.Integer)
     */
    public List getMunicipiosByIdEntidad(Integer idEntidad) throws LocalgisDBException {
        try {
            List municipios = geopistaMunicipioDAO.selectMunicipiosByIdEntidad(idEntidad);
            return municipios;
        } catch (DaoException e) {
            logger.error("Error al obtener la entidad", e);
            throw new LocalgisDBException("Error al obtener la entidad",e);
        }
    }
    

}
