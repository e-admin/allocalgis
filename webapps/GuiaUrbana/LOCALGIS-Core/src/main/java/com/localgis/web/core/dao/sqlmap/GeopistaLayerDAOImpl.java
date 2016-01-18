/**
 * GeopistaLayerDAOImpl.java
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
import com.localgis.web.core.dao.GeopistaLayerDAO;

/**
 * Implementación utilizando iBatis de GeopistaLayerDAO
 * 
 * @author albegarcia
 * 
 */
public class GeopistaLayerDAOImpl extends SqlMapDaoTemplate implements GeopistaLayerDAO {

    /**
     * Constructor a partir de un DaoManager
     * @param daoManager El DaoManager para construirlo
     */
    public GeopistaLayerDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.GeopistaLayerDAO#selectLayersByIdMapAndIdEntidad(java.lang.Integer, java.lang.Integer)
     */
    public List selectLayersByIdMapAndIdEntidad(Integer idMap, Integer idEntidad) {
        Map params = new HashMap();
        params.put("idMap", idMap);
        params.put("idEntidad", idEntidad);
        List result = queryForList("geopista_layers.selectLayersByIdMapAndIdEntidad", params);
        return result;
    }
	
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.GeopistaLayerDAO#selectLayersByIdEntidad(java.lang.Integer)
     */
	public List selectLayersByIdEntidad(Integer idEntidad) {
	    Map params = new HashMap();
	    params.put("idEntidad", idEntidad);
	    List result = queryForList("geopista_layers.selectLayersByIdEntidad", params);
	    return result;
	}

	@Override
	public String getCategoryByLayerName(String layerName) {
	    Map params = new HashMap();
	    params.put("layername", layerName);
	    Object result = queryForObject("geopista_layers.getCategoryByLayerName", params);
	    return (String)result;
	}
}
