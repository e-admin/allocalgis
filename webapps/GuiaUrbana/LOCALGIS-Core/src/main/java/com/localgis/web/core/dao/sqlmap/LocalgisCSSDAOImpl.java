/**
 * LocalgisCSSDAOImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao.sqlmap;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;
import com.localgis.web.core.dao.LocalgisCSSDAO;
import com.localgis.web.core.model.LocalgisCSS;

/**
 * Implementación utilizando iBatis de LocalgisCSSDAO
 * 
 * @author albegarcia
 * 
 */
public class LocalgisCSSDAOImpl extends SqlMapDaoTemplate implements LocalgisCSSDAO {

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public LocalgisCSSDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisCSSDAO#insert(com.localgis.web.core.model.LocalgisCSS)
     */
    public void insert(LocalgisCSS record) {
        insert("localgis_css.insert", record);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisCSSDAO#updateByPrimaryKey(com.localgis.web.core.model.LocalgisCSS)
     */
    public int updateByPrimaryKey(LocalgisCSS record) {
        int rows = update("localgis_css.updateByPrimaryKey", record);
        return rows;
    }



    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisCSSDAO#selectByPrimaryKey(java.lang.Integer)
     */
    public LocalgisCSS selectByPrimaryKey(Integer idmunicipio) {
        LocalgisCSS key = new LocalgisCSS();
        key.setIdentidad(idmunicipio);
        /*
         * Asumimos que vamos a encontrar como maximo un elemento. Si
         * encontramos mas sera un error grave y es correcto que falle
         */
        LocalgisCSS record = (LocalgisCSS) queryForObject("localgis_css.selectByPrimaryKey", key);
        return record;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisCSSDAO#deleteByPrimaryKey(java.lang.Integer)
     */
    public int deleteByPrimaryKey(Integer idmunicipio) {
        LocalgisCSS key = new LocalgisCSS();
        key.setIdentidad(idmunicipio);
        int rows = delete("localgis_css.deleteByPrimaryKey", key);
        return rows;
    }
}