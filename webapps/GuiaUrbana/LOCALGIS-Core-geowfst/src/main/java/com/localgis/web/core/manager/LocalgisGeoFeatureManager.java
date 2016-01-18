/**
 * LocalgisGeoFeatureManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.localgis.web.core.model.ProcedureDefaults;
import com.localgis.web.core.model.ProcedureProperty;
import com.localgis.web.core.model.Procedure;
import com.localgis.web.core.model.ProcedurePropertyKey;


/**
 * Manager para la recuperacion de la aplicación Geowfst
 * 
 * @author davidcaaveiro
 * 
 */
public interface LocalgisGeoFeatureManager {

	public Integer getIdEntidadLocalgis(String idEntidadExt);
		
	public String getIdEntidadExt(Integer idEntidadLocalgis);
	
	public Integer getIdEntidadByIdMunicipio(Integer idMunicipio);	

	public String getSldStyle(String layerName, Integer idEntidad);
	
	public boolean setIdEntidadLocalgisIdEntidadExt(Integer idEntidadLocalgis, String idEntidadExt);
	 
	public Procedure getProceduresByLayerName(String layerName);    
	    
	public Procedure getProceduresByProcedureType(String layerName);
	    
	public List<Procedure> getAllProcedures();
	
    public void insertProcedure(Procedure procedure);
    
    public void updateProcedure(Procedure procedure);
    
    public void deleteProcedure(String procedureId);
       
    public List<ProcedureProperty> getProcedureProperties(String procedureId);
    
    public HashMap<String, ProcedureProperty> getProcedurePropertiesMap(String procedureId);
   
    public ProcedureDefaults getProcedureDefaults(String procedureId);    
    
    public void insertProcedureDefaults(ProcedureDefaults procedureDefaults);
    
    public void updateProcedureDefaults(ProcedureDefaults procedureDefaults);
    
    public void deleteProcedureDefaults(String procedureId);
    
    public void insertProcedureProperty(ProcedureProperty procedureProperty);
    
    public void updateProcedureProperty(ProcedureProperty procedureProperty);
    
    public void deleteProcedureProperty(ProcedurePropertyKey procedurePropertyKey);
    
    public void insertProcedureProperties(HashMap<String,ProcedureProperty> procedureProperties);
    
    public void updateProcedureProperties(String procedureId, HashMap<String,ProcedureProperty> procedureProperties);
    
    public void deleteProcedureProperties(String procedureId);   
    
}
