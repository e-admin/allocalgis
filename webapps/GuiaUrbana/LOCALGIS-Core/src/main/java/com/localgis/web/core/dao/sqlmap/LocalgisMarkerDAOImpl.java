/**
 * LocalgisMarkerDAOImpl.java
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
import com.localgis.web.core.dao.LocalgisMarkerDAO;
import com.localgis.web.core.model.LocalgisMarker;

/**
 * Implementación utilizando iBatis de LocalgisMarkerDAO
 * 
 * @author albegarcia
 * 
 */
public class LocalgisMarkerDAOImpl extends SqlMapDaoTemplate implements LocalgisMarkerDAO {

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public LocalgisMarkerDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisMarkerDAO#insert(com.localgis.web.core.model.LocalgisMarker)
     */
    public Integer insert(LocalgisMarker record) {
        Object newKey = insert("localgis_marker.insert", record);
        return (Integer) newKey;
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisMarkerDAO#deleteByPrimaryKey(java.lang.Integer)
     */
    public int deleteByPrimaryKey(Integer markerid) {
        LocalgisMarker key = new LocalgisMarker();
        key.setMarkerid(markerid);
        int rows = delete("localgis_marker.deleteByPrimaryKey", key);
        return rows;
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisMarkerDAO#selectMarkersByMapIdAndUsername(java.lang.Integer, java.lang.String)
     */
    public List selectMarkersByMapIdAndUsername(Integer mapid, String username) {
        Map params = new HashMap();
        params.put("mapid", mapid);
        params.put("username", username);
        List result = queryForList("localgis_marker.selectMarkersByMapIdAndUsername", params);
        return result;
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisMarkerDAO#updateByPrimaryKey(com.localgis.web.core.model.LocalgisMarker)
     */
    public int updateByPrimaryKey(LocalgisMarker record) {
        int rows = update("localgis_marker.updateByPrimaryKey", record);
        return rows;

    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisMarkerDAO#selectByPrimaryKey(java.lang.Integer)
     */
    public LocalgisMarker selectByPrimaryKey(Integer markerid) {
        Map params = new HashMap();
        params.put("markerid", markerid);
        /*
         * Asumimos que vamos a encontrar como maximo un elemento. Si
         * encontramos mas sera un error grave y es correcto que falle
         */
        return (LocalgisMarker)queryForObject("localgis_marker.selectByPrimaryKey", params);
    }

}