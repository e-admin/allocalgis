/**
 * ISigmClient.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.client.sigm;

import java.util.HashMap;
import java.util.List;

import com.localgis.web.core.model.Procedure;
import com.localgis.web.core.model.ProcedureDefaults;
import com.localgis.web.core.model.ProcedureProperty;
import com.localgis.web.core.model.ProcedurePropertyKey;
import com.localgis.web.gwfst.ws.sigm.beans.PropertyAndValue;

public interface ISigmClient {
	
	/**
	 * Constantes
	 */
    public static final String	MENSAJE_XML	= "mensajeXML";
     
    /**
     * Metodos abstractos
     */
    public PropertyAndValue [] getInfoAll(Integer idEntidad, String nombreEntidad, String idFeature) throws Exception;
	public String [] getSearchAll(Integer idEntidad, String featureType, PropertyAndValue [] searchPropertyAndValues) throws Exception;
	public String getInfoByPrimaryKey(Integer idEntidad, String nombreEntidad, String idFeature, String property) throws Exception;
	public List<Procedure> getAllProcedures() throws Exception;
	public Boolean insertProcedure(Procedure procedure) throws Exception;
	public Boolean updateProcedure(Procedure procedure) throws Exception;
	public Boolean deleteProcedure(String procedureId) throws Exception;
	public ProcedureDefaults getProcedureDefaults(String procedureId) throws Exception;
	public Boolean insertProcedureDefaults(ProcedureDefaults procedureDefaults) throws Exception;
	public Boolean updateProcedureDefaults(ProcedureDefaults procedureDefaults) throws Exception;
	public Boolean deleteProcedureDefaults(String procedureId) throws Exception;
	public List<ProcedureProperty> getProcedureProperties(String procedureId) throws Exception;
	public HashMap<String, ProcedureProperty> getProcedurePropertiesMap(String procedureId) throws Exception;
	public Boolean insertProcedureProperty(ProcedureProperty procedureProperty) throws Exception;
	public Boolean updateProcedureProperty(ProcedureProperty procedureProperty) throws Exception;
	public Boolean deleteProcedureProperty(ProcedurePropertyKey procedurePropertyId) throws Exception;
	public Boolean insertProcedureProperties(HashMap<String, ProcedureProperty> procedureProperties) throws Exception;
	public Boolean updateProcedureProperties(String procedureId, HashMap<String, ProcedureProperty> procedureProperties) throws Exception;
	public Boolean deleteProcedureProperties(String procedureId) throws Exception;
	
}
