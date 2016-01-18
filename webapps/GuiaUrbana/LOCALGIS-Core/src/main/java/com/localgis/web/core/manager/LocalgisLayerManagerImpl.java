/**
 * LocalgisLayerManagerImpl.java
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
import com.localgis.web.core.ConstantesSQL;
import com.localgis.web.core.LocalgisManagerBuilder;
import com.localgis.web.core.dao.GeopistaColumnDAO;
import com.localgis.web.core.dao.LocalgisAttributeDAO;
import com.localgis.web.core.dao.LocalgisLegendDAO;
import com.localgis.web.core.dao.LocalgisRestrictedAttributesDAO;
import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.model.GeopistaLayer;
import com.localgis.web.core.model.LocalgisLayer;
import com.localgis.web.core.model.LocalgisLegend;
import com.localgis.web.core.model.LocalgisLegendKey;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.core.model.LocalgisRestrictedAttribute;
import com.localgis.web.core.wms.WMSConfigurator;

public class LocalgisLayerManagerImpl implements LocalgisLayerManager {

    /**
     * Logger para las trazas
     */
    private static Logger logger = Logger.getLogger(LocalgisLayerManagerImpl.class);

    /**
     * Dao para las columnas de geopista
     */
    private GeopistaColumnDAO geopistaColumnDAO;

    /**
     * Dao para los atributos de localgis
     */
    private LocalgisAttributeDAO localgisAttributeDAO;

    /**
     * Dao para las leyendas las capas de localgis
     */    
    private LocalgisLegendDAO localgisLegendDAO;

    /**
     * Dao para las restricted attributes de localgis
     */    
    private LocalgisRestrictedAttributesDAO localgisRestrictedAttributesDAO;

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
    public LocalgisLayerManagerImpl(DaoManager daoManager, WMSConfigurator wmsConfigurator, LocalgisManagerBuilder localgisManagerBuilder) {
        this.geopistaColumnDAO = (GeopistaColumnDAO) daoManager.getDao(GeopistaColumnDAO.class);

        this.localgisAttributeDAO = (LocalgisAttributeDAO) daoManager.getDao(LocalgisAttributeDAO.class);
        this.localgisLegendDAO = (LocalgisLegendDAO) daoManager.getDao(LocalgisLegendDAO.class);
        this.localgisRestrictedAttributesDAO = (LocalgisRestrictedAttributesDAO)daoManager.getDao(LocalgisRestrictedAttributesDAO.class);
        
        this.wmsConfigurator = wmsConfigurator;
        this.localgisManagerBuilder = localgisManagerBuilder;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisLayerManager#addRestrictedAttributesLayer(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Boolean)
     */
    public void addRestrictedAttributesLayer(Integer idLayer,Integer idAttribute,Integer idMunicipio,Integer idAlias,Boolean mapPublic) throws LocalgisDBException  {
        try {
            LocalgisRestrictedAttribute record = new LocalgisRestrictedAttribute();
            record.setLayeridgeopista(idLayer);
            record.setAttributeidgeopista(idAttribute);
            record.setIdentidad(idMunicipio);
            record.setIdalias(idAlias);
            record.setMappublicBoolean(mapPublic);         
            localgisRestrictedAttributesDAO.insertRestrictedAttribute(record);
        } catch (DaoException e) {
            logger.error("Error al insertar el atributo restringido", e);
            throw new LocalgisDBException("Error al insertar el atributo restringido",e);
        }       
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisLayerManager#removeRestrictedAttributesLayer(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.Boolean)
     */
    public void removeRestrictedAttributesLayer(Integer idLayer,Integer idAttribute,Integer idMunicipio,Boolean mapPublic) throws LocalgisDBException  {
        try {
            LocalgisRestrictedAttribute record = new LocalgisRestrictedAttribute();
            record.setLayeridgeopista(idLayer);
            record.setAttributeidgeopista(idAttribute);
            record.setIdentidad(idMunicipio);
            record.setMappublicBoolean(mapPublic);
            localgisRestrictedAttributesDAO.deleteRestrictedAttribute(record);
        } catch (DaoException e) {
            logger.error("Error al eliminar el atributo restringido", e);
            throw new LocalgisDBException("Error al eliminar el atributo restringido",e);
        }       
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisLayerManager#getRestrictedAttributesLayer(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Boolean)
     */
    public List getRestrictedAttributesLayer(Integer idLayer,Integer idEntidad,String locale,Boolean mapPublic) throws LocalgisDBException  {
        locale = localgisManagerBuilder.getLocaleSelected(locale);
        try {
            return localgisRestrictedAttributesDAO.selectRestrictedAttributes(idLayer,idEntidad, locale,mapPublic);
        } catch (DaoException e) {
            logger.error("Error al obtener los atributos restringidos", e);
            throw new LocalgisDBException("Error al obtener los atributos restringidos",e);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisLayerManager#getColumnsLayer(java.lang.Integer, java.lang.String)
     */
    public List getColumnsLayer(Integer idLayer, String locale) throws LocalgisDBException  {
        locale = localgisManagerBuilder.getLocaleSelected(locale);
        try {
            return geopistaColumnDAO.selectColumnsByLayerTranslated(idLayer, locale);
        } catch (DaoException e) {
            logger.error("Error al obtener las columnas", e);
            throw new LocalgisDBException("Error al obtener las columnas",e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisLayerManager#addLegend(java.lang.Integer, java.lang.Integer, java.lang.Boolean, byte[])
     */
    public void addLegend(Integer idLayer, Integer idEntidad, Boolean mapPublic, byte[] img) throws LocalgisDBException {
        LocalgisLegend record = new LocalgisLegend();
        record.setIdentidad(idEntidad);;
        record.setLayeridgeopista(idLayer);
        if (mapPublic != null && mapPublic.booleanValue()) { 
            record.setMappublic(ConstantesSQL.TRUE);
        } else {
            record.setMappublic(ConstantesSQL.FALSE);
        }
        record.setImg(img);
        try {
            localgisLegendDAO.insert(record);
        } catch (DaoException e) {
            logger.error("Error al insertar la leyenda", e);
            throw new LocalgisDBException("Error al insertar la leyenda",e);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisLayerManager#getLegend(com.localgis.web.core.model.LocalgisLegendKey)
     */
    public LocalgisLegend getLegend(LocalgisLegendKey legendKey) throws LocalgisDBException {
        try {
            return localgisLegendDAO.selectByPrimaryKey(legendKey);
        } catch (DaoException e) {
            logger.error("Error al obtener la leyenda", e);
            throw new LocalgisDBException("Error al obtener la leyenda",e);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisLayerManager#updateLegend(com.localgis.web.core.model.LocalgisLegendKey, byte[])
     */
    public void updateLegend(LocalgisLegendKey legendKey, byte[] fileData) throws LocalgisDBException {
        LocalgisLegend record = new LocalgisLegend();
        record.setIdentidad(legendKey.getIdentidad());;
        record.setMappublic(legendKey.getMappublic());
        record.setLayeridgeopista(legendKey.getLayeridgeopista());
        record.setImg(fileData);
        try {
            localgisLegendDAO.updateByPrimaryKey(record);
        } catch (DaoException e) {
            logger.error("Error al insertar la leyenda", e);
            throw new LocalgisDBException("Error al insertar la leyenda",e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisLayerManager#removeLegend(com.localgis.web.core.model.LocalgisLegendKey)
     */
    public void removeLegend(LocalgisLegendKey legendKey) throws LocalgisDBException {
        try {
            localgisLegendDAO.deleteByPrimaryKey(legendKey);
        } catch (DaoException e) {
            logger.error("Error al eliminar la leyenda", e);
            throw new LocalgisDBException("Error al eliminar la leyenda",e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisLayerManager#getAttributesLayer(java.lang.String, java.lang.Boolean)
     */
    public List getAttributesLayer(String nameLayer, Boolean mapPublic) throws LocalgisDBException {
       return getAttributesLayer(nameLayer, mapPublic, localgisManagerBuilder.getDefaultLocale());
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisLayerManager#getAttributesLayer(java.lang.String, java.lang.Boolean, java.lang.String)
     */
    public List getAttributesLayer(String nameLayer, Boolean mapPublic, String locale) throws LocalgisDBException {
        locale = localgisManagerBuilder.getLocaleSelected(locale);
        try {
            return localgisAttributeDAO.selectAttributesTranslatedByNameLayerAndMapPublic(nameLayer, mapPublic, locale);
        } catch (DaoException e) {
            logger.error("Error al obtener los atributos", e);
            throw new LocalgisDBException("Error al obtener los atributos",e);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisLayerManager#getInternalNameGenericLayer(com.localgis.web.core.model.LocalgisLayer, com.localgis.web.core.model.LocalgisMap)
     */
    public String getInternalNameGenericLayer(LocalgisMap localgisMap, LocalgisLayer localgisLayer, int operationType) {
        return wmsConfigurator.getInternalNameGenericLayer(localgisMap, localgisLayer, operationType);
    }

    /* (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisLayerManager#getInternalNameLayerOrtofoto(com.localgis.web.core.model.LocalgisMap)
     */
    public String getInternalNameLayerOrtofoto(LocalgisMap localgisMap, int operationType) throws LocalgisConfigurationException {
        return wmsConfigurator.getInternalNameOrtofotoLayer(localgisMap, operationType);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisLayerManager#getInternalNameLayerMunicipiosAndProvincias(com.localgis.web.core.model.LocalgisMap, java.lang.Boolean)
     */
    public String getInternalNameLayerMunicipiosAndProvincias(LocalgisMap localgisMap, Boolean showMunicipios, int operationType) throws LocalgisConfigurationException {
        return wmsConfigurator.getInternalNameMunicipiosAndProvinciasLayer(localgisMap, showMunicipios, operationType);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisLayerManager#getInternalNameLayerOverview(com.localgis.web.core.model.LocalgisMap, java.util.List, java.lang.Boolean)
     */
    public String getInternalNameLayerOverview(LocalgisMap localgisMap, List layers, Boolean showMunicipios, int operationType) throws LocalgisConfigurationException {
        return wmsConfigurator.getInternalNameOverviewLayer(localgisMap, layers, showMunicipios, operationType);
    }


    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.manager.LocalgisLayerManager#getAttributesValuesLayer(java.lang.String, java.lang.String)
     */
    public List getAttributesValuesLayer(String nameLayer, String locale) throws LocalgisDBException {
        locale = localgisManagerBuilder.getLocaleSelected(locale);
        try {
            return localgisAttributeDAO.selectAttributesValuesTranslatedByNameLayer(nameLayer, locale);
        } catch (DaoException e) {
            logger.error("Error al obtener los atributos", e);
            throw new LocalgisDBException("Error al obtener los atributos",e);
        }
    }

	@Override
	public List getAttributesLayerByIdLayer(Integer idLayer, Integer idMap,
			Boolean mapPublic, String locale) throws LocalgisDBException {
		
		 locale = localgisManagerBuilder.getLocaleSelected(locale);
	        try {
	            return localgisAttributeDAO.selectAttributesTranslatedByIdLayerAndMapPublic(idLayer,idMap, mapPublic, locale);
	        } catch (DaoException e) {
	            logger.error("Error al obtener los atributos", e);
	            throw new LocalgisDBException("Error al obtener los atributos",e);
	        }
	        		
	} 
    
}
