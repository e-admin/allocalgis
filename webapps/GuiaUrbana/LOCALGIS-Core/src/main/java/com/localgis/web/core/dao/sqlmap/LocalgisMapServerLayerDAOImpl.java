/**
 * LocalgisMapServerLayerDAOImpl.java
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
import com.localgis.web.core.ConstantesSQL;
import com.localgis.web.core.dao.LocalgisMapServerLayerDAO;
import com.localgis.web.core.model.LocalgisMapServerLayer;

/**
 * Implementación utilizando iBatis de LocalgisMapServerLayerDAO
 * 
 * @author albegarcia
 * 
 */
public class LocalgisMapServerLayerDAOImpl extends SqlMapDaoTemplate implements LocalgisMapServerLayerDAO {

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public LocalgisMapServerLayerDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisMapServerLayerDAO#insert(com.localgis.web.core.model.LocalgisMapServerLayer)
     */
    public Integer insert(LocalgisMapServerLayer record) {
        Object newKey = insert("localgis_map_server_layer.insert", record);
        return (Integer) newKey;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisMapServerLayerDAO#selectMapServerLayersByIdMap(java.lang.Integer)
     */
    public List selectMapServerLayersByIdMap(Integer idMap) {
        Map params = new HashMap();
        params.put("idMap", idMap);
        List result = queryForList("localgis_map_server_layer.selectMapServerLayersByIdMap", params);
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisMapServerLayerDAO#selectMapServerLayersByIdMunicipio(java.lang.Integer, java.lang.Boolean)
     */
    public List selectMapServerLayersByIdMunicipio(Integer idMunicipio, Boolean publicMaps) {
        Map params = new HashMap();
        params.put("idMunicipio", idMunicipio);
        if (publicMaps.booleanValue()) {
            params.put("publicMaps", ConstantesSQL.TRUE);
        } else {
            params.put("publicMaps", ConstantesSQL.FALSE);
        }
        List result = queryForList("localgis_map_server_layer.selectMapServerLayersByIdMunicipio", params);
        return result;
    }

    public int deleteMapServerLayersByIdMap(Integer idMap) {
        Map params = new HashMap();
        params.put("idMap", idMap);
        int rows = delete("localgis_map_server_layer.deleteMapServerLayersByIdMap", params);
        return rows;
    }
}