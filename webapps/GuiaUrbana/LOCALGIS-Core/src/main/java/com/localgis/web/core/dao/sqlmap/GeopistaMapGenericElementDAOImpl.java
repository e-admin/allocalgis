/**
 * GeopistaMapGenericElementDAOImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao.sqlmap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;
import com.localgis.web.core.dao.GeopistaMapGenericElementDAO;
import com.localgis.web.core.model.BoundingBox;
import com.localgis.web.core.model.Point;

/**
 * Implementación utilizando iBatis de GeopistaMapGenericElementDAO
 * 
 * @author albegarcia
 * 
 */
public class GeopistaMapGenericElementDAOImpl extends SqlMapDaoTemplate implements GeopistaMapGenericElementDAO {

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public GeopistaMapGenericElementDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }
    
    
    public boolean isValidTableName (String tableName) {
    	boolean isValid = false;
    	if (tableName != null) {
    		Map params = new HashMap();
    		params.put("tableName", tableName);
    		Integer result = (Integer)queryForObject("geopista_map_generic_element.isValidTableName", params);
    		isValid = (result.intValue() > 0);
    	}
    	return isValid;
    }
    
    public boolean isValidColumnName (String tableName, String columnName) {
    	boolean isValid = false;
    	if (columnName != null) {
    		Map params = new HashMap();
    		params.put("tableName", tableName);
    		params.put("columnName", columnName);
    		Integer result = (Integer)queryForObject("geopista_map_generic_element.isValidColumnName", params);
    		isValid = (result.intValue() > 0);
    	}
    	return isValid;
    }
   
    public Point selectCenteredPointMapGenericElement(String tableName, String identifierColumnName, Object identifierValue, Integer srid) {
        Point result = null;
        
    	if (isValidTableName(tableName) && isValidColumnName(tableName, identifierColumnName)) 
    	{
	    	Map params = new HashMap();
	        params.put("tableName", tableName);
	        params.put("identifierColumnName", identifierColumnName);
	        params.put("identifierValue", identifierValue);
	        params.put("srid", srid);
	        List elements = queryForList("geopista_map_generic_element.selectCenteredPointMapGenericElement", params);
	        if (elements.size() > 0) {
	        	result = (Point)elements.get(0);
	        } else {
	        	result = null;
	        }
        }
        
        return result;
    }
    
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.GeopistaMapGenericElementDAO#selectBoundingBoxMapGenericElement(java.lang.String, java.lang.String, java.lang.Object)
     */
    public BoundingBox selectBoundingBoxMapGenericElement(String tableName, String identifierColumnName, Object identifierValue, Integer srid) {
        Map params = new HashMap();
        params.put("tableName", tableName);
        params.put("identifierColumnName", identifierColumnName);
        params.put("identifierValue", identifierValue);
        params.put("srid", srid);
        List elements = queryForList("geopista_map_generic_element.selectBoundingBoxMapGenericElement", params);
        if (elements.size() > 0) {
            return (BoundingBox)elements.get(0);
        } else {
            return null;
        }
    }
    
    
    
    
    
    
    
    
}