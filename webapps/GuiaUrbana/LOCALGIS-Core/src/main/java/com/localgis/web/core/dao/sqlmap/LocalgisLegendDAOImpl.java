/**
 * LocalgisLegendDAOImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao.sqlmap;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;
import com.localgis.web.core.dao.LocalgisLegendDAO;
import com.localgis.web.core.model.LocalgisLegend;
import com.localgis.web.core.model.LocalgisLegendKey;

/**
 * Implementación utilizando iBatis de LocalgisLegendDAO
 * 
 * @author albegarcia
 * 
 */
public class LocalgisLegendDAOImpl extends SqlMapDaoTemplate implements LocalgisLegendDAO {

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public LocalgisLegendDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisLegendDAO#insert(com.localgis.web.core.model.LocalgisLegend)
     */
    public void insert(LocalgisLegend record) {
        insert("localgisguiaurbana_legend.abatorgenerated_insert", record);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisLegendDAO#updateByPrimaryKey(com.localgis.web.core.model.LocalgisLegend)
     */
    public int updateByPrimaryKey(LocalgisLegend record) {
        int rows = update("localgisguiaurbana_legend.abatorgenerated_updateByPrimaryKey", record);
        return rows;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisLegendDAO#selectByPrimaryKey(com.localgis.web.core.model.LocalgisLegendKey)
     */
    public LocalgisLegend selectByPrimaryKey(LocalgisLegendKey key) {
        /*
         * Asumimos que vamos a encontrar como maximo un elemento. Si
         * encontramos mas sera un error grave y es correcto que falle
         */
        LocalgisLegend record = (LocalgisLegend) queryForObject("localgisguiaurbana_legend.abatorgenerated_selectByPrimaryKey", key);
        return record;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisLegendDAO#deleteByPrimaryKey(com.localgis.web.core.model.LocalgisLegendKey)
     */
    public int deleteByPrimaryKey(LocalgisLegendKey key) {
        int rows = delete("localgisguiaurbana_legend.abatorgenerated_deleteByPrimaryKey", key);
        return rows;
    }
}