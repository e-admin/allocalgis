/**
 * LocalgisMapLayerDAOImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao.sqlmap;

import java.util.HashMap;
import java.util.Map;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;
import com.localgis.web.core.dao.LocalgisMapLayerDAO;
import com.localgis.web.core.model.LocalgisMapLayer;

/**
 * Implementación utilizando iBatis de LocalgisMapLayer
 * 
 * @author albegarcia
 * 
 */
public class LocalgisMapLayerDAOImpl extends SqlMapDaoTemplate implements LocalgisMapLayerDAO {

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public LocalgisMapLayerDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.localgis.web.core.dao.LocalgisMapLayerDAO#insert(com.localgis.web.core.model.LocalgisMapLayer)
     */
    public void insert(LocalgisMapLayer record) {
        insert("localgis_maplayer.insert", record);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisMapLayerDAO#deleteLayersByIdMap(java.lang.Integer)
     */
    public int deleteLayersByIdMap(Integer idMap) {
        Map params = new HashMap();
        params.put("idMap", idMap);
        int rows = delete("localgis_maplayer.deleteLayersByIdMap", params);
        return rows;
    }

}