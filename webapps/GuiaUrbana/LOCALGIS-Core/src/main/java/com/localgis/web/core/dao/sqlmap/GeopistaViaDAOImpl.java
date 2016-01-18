/**
 * GeopistaViaDAOImpl.java
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
import com.localgis.web.core.dao.GeopistaViaDAO;
import com.localgis.web.core.model.GeopistaVia;

/**
 * Implementación utilizando iBatis de GeopistaViaDAO
 * 
 * @author albegarcia
 * 
 */
public class GeopistaViaDAOImpl extends SqlMapDaoTemplate implements GeopistaViaDAO {

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public GeopistaViaDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.GeopistaViaDAO#selectViaById(java.lang.Integer)
     */
    public GeopistaVia selectViaById(Integer id, Integer srid) {
        Map params = new HashMap();
        params.put("id", id);
        params.put("srid", srid);
        List geopistaVias = queryForList("geopista_vias.selectViaById", params);
        if (geopistaVias.size() > 0) {
            return (GeopistaVia)geopistaVias.get(0);
        } else {
            return null;
        }
    }
}