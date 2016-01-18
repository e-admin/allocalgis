/**
 * LocalgisGeoFeatureManagerImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.manager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibatis.dao.client.DaoManager;
import com.localgis.web.core.dao.EntidadlocalgisEntidadextDAO;
import com.localgis.web.core.dao.GeopistaEntidadesMunicipiosDAO;
import com.localgis.web.core.dao.GeopistaLayersStylesDAO;
import com.localgis.web.core.dao.ProcedureDefaultsDAO;
import com.localgis.web.core.dao.ProcedurePropertiesDAO;
import com.localgis.web.core.dao.ProceduresDAO;
import com.localgis.web.core.model.EntidadlocalgisEntidadext;
import com.localgis.web.core.model.GeopistaEntidadesMunicipiosKey;
import com.localgis.web.core.model.GeopistaLayersStyles;
import com.localgis.web.core.model.ProcedureDefaults;
import com.localgis.web.core.model.ProcedureProperty;
import com.localgis.web.core.model.Procedure;
import com.localgis.web.core.model.ProcedurePropertyKey;

/**
 * Implementación de LocalgisGeoFeatureManager
 * 
 * @author davidcaaveiro
 * 
 */
public class LocalgisGeoFeatureManagerImpl implements LocalgisGeoFeatureManager {

    /**
     * Logger para las trazas
     */
    private static Logger logger = Logger.getLogger(LocalgisGeoFeatureManagerImpl.class);

    /**
     * Daos
     */
    private EntidadlocalgisEntidadextDAO entidadlocalgisEntidadextDAO;
    
    private GeopistaEntidadesMunicipiosDAO geopistaEntidadesMunicipiosDAO;
   
    private GeopistaLayersStylesDAO geopistaLayersStylesDAO;
    
    private ProceduresDAO proceduresDAO;
    
    private ProcedurePropertiesDAO procedurePropertiesDAO;
    
    private ProcedureDefaultsDAO procedureDefaultsDAO;
    
    /**
     * Constructor a partir de un DAOManager
     */
    public LocalgisGeoFeatureManagerImpl(DaoManager daoManager) {
        this.entidadlocalgisEntidadextDAO = (EntidadlocalgisEntidadextDAO) daoManager.getDao(EntidadlocalgisEntidadextDAO.class);
        this.geopistaEntidadesMunicipiosDAO = (GeopistaEntidadesMunicipiosDAO) daoManager.getDao(GeopistaEntidadesMunicipiosDAO.class);
        this.geopistaLayersStylesDAO = (GeopistaLayersStylesDAO) daoManager.getDao(GeopistaLayersStylesDAO.class);
        this.proceduresDAO = (ProceduresDAO) daoManager.getDao(ProceduresDAO.class);
        this.procedurePropertiesDAO = (ProcedurePropertiesDAO) daoManager.getDao(ProcedurePropertiesDAO.class);
        this.procedureDefaultsDAO = (ProcedureDefaultsDAO) daoManager.getDao(ProcedureDefaultsDAO.class);
    }   
        
    public Integer getIdEntidadLocalgis(String idEntidadExt){
    	Integer idEntidad = null;
    	EntidadlocalgisEntidadext entidadlocalgisEntidadext = this.entidadlocalgisEntidadextDAO.selectByPrimaryKey(idEntidadExt);	
    	if(entidadlocalgisEntidadext!=null){
    		idEntidad = entidadlocalgisEntidadext.getIdEntidad();
    	}
    	return idEntidad;
    }
    
    public String getIdEntidadExt(Integer idEntidadLocalgis){
    	String idEntidad = null;
    	EntidadlocalgisEntidadext entidadlocalgisEntidadext = this.entidadlocalgisEntidadextDAO.selectBySecondPrimaryKey(idEntidadLocalgis);	
    	if(entidadlocalgisEntidadext!=null){
    		idEntidad = entidadlocalgisEntidadext.getIdEntidadext();
    	}
    	return idEntidad;
    }  
    
    public Integer getIdEntidadByIdMunicipio(Integer idMunicipio){
    	Integer idEntidad = null;
    	GeopistaEntidadesMunicipiosKey publicEntidadesMunicipiosKey = this.geopistaEntidadesMunicipiosDAO.selectByPrimaryKey(idMunicipio);	
    	if(publicEntidadesMunicipiosKey!=null){
    		idEntidad = publicEntidadesMunicipiosKey.getIdEntidad();
    	}
    	return idEntidad;
    }    
    
    public String getSldStyle(String layerName, Integer idEntidad){
    	String xmlStyle = "";
    	GeopistaLayersStyles geopistaLayersStyles = this.geopistaLayersStylesDAO.selectByPrimaryKey(layerName,idEntidad);	
    	if(geopistaLayersStyles!=null){
    		xmlStyle = geopistaLayersStyles.getXmlStyle();
    		xmlStyle=xmlStyle.replaceAll("\n", "").replace("\r", "");
    	}
    	return xmlStyle;
    }  
        
    public boolean setIdEntidadLocalgisIdEntidadExt(Integer idEntidadLocalgis, String idEntidadExt){
    	EntidadlocalgisEntidadext entidadlocalgisEntidadext = new EntidadlocalgisEntidadext();
    	entidadlocalgisEntidadext.setIdEntidad(idEntidadLocalgis);
    	entidadlocalgisEntidadext.setIdEntidadext(idEntidadExt);
    	this.entidadlocalgisEntidadextDAO.updateByPrimaryKey(entidadlocalgisEntidadext);	
    	return true;
    }    
        
    public Procedure getProceduresByLayerName(String layerName){
    	Procedure procedure = this.proceduresDAO.selectByLayerName(layerName);	
    	return procedure;
    }
    
    public Procedure getProceduresByProcedureType(String layerName){
    	Procedure procedure = this.proceduresDAO.selectByProcedureType(layerName);	
    	return procedure;
    }
    
    public List<Procedure> getAllProcedures(){	
    	return this.proceduresDAO.selectAll();
    }
    
    public void insertProcedure(Procedure procedure){	
    	this.proceduresDAO.insertSelective(procedure);
    }
    
    public void updateProcedure(Procedure procedure){	
    	this.proceduresDAO.updateByPrimaryKeySelective(procedure);
    }
    
    public void deleteProcedure(String procedureId){	
    	this.proceduresDAO.deleteByPrimaryKey(procedureId);
    }
    
    public ProcedureDefaults getProcedureDefaults(String procedureId){
    	ProcedureDefaults procedureDefaults = this.procedureDefaultsDAO.selectByPrimaryKey(procedureId);	
    	return procedureDefaults;
    }
    
    public void insertProcedureDefaults(ProcedureDefaults procedureDefaults){
    	this.procedureDefaultsDAO.insertSelective(procedureDefaults);
    }
    
    public void updateProcedureDefaults(ProcedureDefaults procedureDefaults){
    	this.procedureDefaultsDAO.updateByPrimaryKeySelective(procedureDefaults);
    }
    
    public void deleteProcedureDefaults(String procedureId){
    	this.procedureDefaultsDAO.deleteByPrimaryKey(procedureId);
    }
    
    public List<ProcedureProperty> getProcedureProperties(String procedureId){
    	return this.procedurePropertiesDAO.selectByProcedureId(procedureId);	
    }
    
    public HashMap<String,ProcedureProperty> getProcedurePropertiesMap(String procedureId){
    	return this.procedurePropertiesDAO.selectByProcedureIdMap(procedureId);	
    }
    
    public void insertProcedureProperty(ProcedureProperty procedureProperty){
    	this.procedurePropertiesDAO.insertSelective(procedureProperty);
    }
    
    public void updateProcedureProperty(ProcedureProperty procedureProperty){
    	this.procedurePropertiesDAO.updateByPrimaryKeySelective(procedureProperty);	    	
    }
    
    public void deleteProcedureProperty(ProcedurePropertyKey procedurePropertyKey){
	    this.procedurePropertiesDAO.deleteByPrimaryKey(procedurePropertyKey);
    }
    
    public void insertProcedureProperties(HashMap<String,ProcedureProperty> procedureProperties){
    	Iterator<String> it = procedureProperties.keySet().iterator();
    	while(it.hasNext()){   
	    	this.procedurePropertiesDAO.insertSelective(procedureProperties.get(it.next()));        	
    	}
    }
    
    public void updateProcedureProperties(String procedureId, HashMap<String,ProcedureProperty> procedureProperties){
    	HashMap<String,ProcedureProperty> procedurePropertiesBD = getProcedurePropertiesMap(procedureId);    	
    	Iterator<String> itNew = procedureProperties.keySet().iterator();
    	while(itNew.hasNext()){    		
    		String propertyNew = itNew.next();
        	Iterator<String> itBD = procedurePropertiesBD.keySet().iterator();
        	while(itBD.hasNext()){  
        		String propertyBD = itBD.next();
        		if(procedureProperties.get(propertyBD)!=null){
        			//updateBD () procedureProperties.get(propertyNew)
        			this.procedurePropertiesDAO.updateByPrimaryKeySelective(procedureProperties.get(propertyNew));
        			//borrar de ambos hash
        			procedureProperties.remove(propertyNew);
        			procedurePropertiesBD.remove(propertyNew);
        		}
        	}
    	}
    	Iterator<String> itBD = procedurePropertiesBD.keySet().iterator();
    	while(itBD.hasNext()){  
    		String propertyBD = itBD.next();
    		//deleteBD () procedurePropertiesBD.get(propertyNew)
    		ProcedurePropertyKey procedurePropertyKey = new ProcedurePropertyKey();
    		procedurePropertyKey.setId(procedurePropertiesBD.get(propertyBD).getId());
    		procedurePropertyKey.setProperty(propertyBD);
    		this.procedurePropertiesDAO.deleteByPrimaryKey(procedurePropertyKey);    		
    	}
    	itNew = procedureProperties.keySet().iterator();
    	while(itNew.hasNext()){    		
    		String propertyNew = itNew.next();
    		//insertBD () procedureProperties.get(propertyNew)
    		this.procedurePropertiesDAO.insertSelective(procedureProperties.get(propertyNew));
    	}
    }
    
    public void deleteProcedureProperties(String procedureId){
    	ProcedurePropertyKey procedurePropertyKey = new ProcedurePropertyKey();
    	procedurePropertyKey.setId(procedureId);
	    this.procedurePropertiesDAO.deleteByPrimaryKeyId(procedurePropertyKey);
    }
    
}
