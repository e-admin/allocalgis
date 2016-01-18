/**
 * GeopistaColumnDAOImpl.java
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
import com.localgis.web.core.dao.GeopistaColumnDAO;

/**
 * Implementación utilizando iBatis de GeopistaColumnDAO
 * 
 * @author albegarcia
 * 
 */
public class GeopistaColumnDAOImpl extends SqlMapDaoTemplate implements GeopistaColumnDAO {

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public GeopistaColumnDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.GeopistaColumnDAO#selectColumnsByLayer(java.lang.Integer)
     */
    public List selectColumnsByLayer(Integer idLayer) {
        Map params = new HashMap();
        params.put("idLayer", idLayer);
        List list = queryForList("geopista_columns.selectColumnsByLayer", params);
        return list;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.localgis.web.core.dao.GeopistaColumnDAO#selectColumnsByLayerTranslated(java.lang.Integer,
     *      java.lang.String)
     */
    public List selectColumnsByLayerTranslated(Integer idLayer, String locale) {
        Map params = new HashMap();
        params.put("idLayer", idLayer);
        params.put("locale", locale);
        List list = queryForList("geopista_columns.selectColumnsByLayerTranslated", params);
        return list;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.GeopistaColumnDAO#selectColumnsByLayerAllLanguages(java.lang.Integer)
     */
    public List selectColumnsByLayerTranslatedAllLanguages(Integer idLayer) {
        Map params = new HashMap();
        params.put("idLayer", idLayer);
        List list = queryForList("geopista_columns.selectColumnsByLayerTranslatedAllLanguages", params);
        return list;
    }
}
