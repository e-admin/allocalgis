/**
 * GeopistaTableDAOImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao.sqlmap;

import org.apache.log4j.Logger;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;
import com.localgis.web.core.dao.GeopistaTableDAO;
import com.localgis.web.core.utils.LocalgisUtils;

/**
 * Implementacion usando iBatis de GeopistaTableDAO
 * @author albegarcia
 *
 */
public class GeopistaTableDAOImpl extends SqlMapDaoTemplate implements GeopistaTableDAO {

    /**
     * Logger para las trazas
     */
    private static Logger logger = Logger.getLogger(GeopistaParcelaDAOImpl.class);

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public GeopistaTableDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.GeopistaTableDAO#selectGMLGeometryTypeByTableName()
     */
    public String selectGMLGeometryTypeByTableName(String tableName) {
        Short geometryType = (Short)queryForObject("geopista_tables.selectGeometryTypeByTableName", tableName);
        if (geometryType != null) {
            return LocalgisUtils.getGeometryType(geometryType);
        } else {
            return null;
        }
    }

}
