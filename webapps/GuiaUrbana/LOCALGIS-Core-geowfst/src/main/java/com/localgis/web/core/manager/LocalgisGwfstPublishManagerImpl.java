/**
 * LocalgisGwfstPublishManagerImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.manager;

import org.apache.log4j.Logger;

import com.ibatis.dao.client.DaoManager;
import com.localgis.web.core.dao.EntidadTipoMapDAO;
import com.localgis.web.core.model.EntidadTipoMap;

/**
 * Implementación de LocalgisAutoPublishManager
 * 
 * @author davidcaaveiro
 * 
 */
public class LocalgisGwfstPublishManagerImpl implements LocalgisGwftsPublishManager {

    /**
     * Logger para las trazas
     */
    private static Logger logger = Logger.getLogger(LocalgisGwfstPublishManagerImpl.class);

  
    /**
     * Dao para la tabla entidad_tipo_map
     */
    private EntidadTipoMapDAO entidadTipoMapDAO;

    /**
     * Constructor a partir de un DAOManager
     */
    public LocalgisGwfstPublishManagerImpl(DaoManager daoManager) {
    	this.entidadTipoMapDAO = (EntidadTipoMapDAO) daoManager.getDao(EntidadTipoMapDAO.class);    	
    }    

    public Integer getIdMap(Integer idEntidad, String featureType){
    	Integer idMap = null;
    	EntidadTipoMap key = new EntidadTipoMap();
		key.setIdEntidad(idEntidad);
		key.setTipo(featureType);
		EntidadTipoMap entidadTipoMap = this.entidadTipoMapDAO.selectByPrimaryKey(key);
		if(entidadTipoMap!=null){
			idMap = entidadTipoMap.getMapid();		
		}
    	return idMap;
    }   
    
    public void insertPublish(Integer idEntidad, String featureType,Integer idMap){
    	EntidadTipoMap record = new EntidadTipoMap();
    	record.setIdEntidad(idEntidad);
    	record.setTipo(featureType);
    	record.setMapid(idMap);
		this.entidadTipoMapDAO.insert(record);	
    }

    public void updatePublish(Integer idEntidad, String featureType,Integer idMap){
    	EntidadTipoMap record = new EntidadTipoMap();
    	record.setIdEntidad(idEntidad);
    	record.setTipo(featureType);
    	record.setMapid(idMap);
		this.entidadTipoMapDAO.updateByPrimaryKey(record);	
    }
    

}
