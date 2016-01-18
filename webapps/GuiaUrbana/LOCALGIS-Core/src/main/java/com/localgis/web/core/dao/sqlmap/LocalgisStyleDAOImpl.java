/**
 * LocalgisStyleDAOImpl.java
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

import org.apache.log4j.Logger;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;
import com.localgis.web.core.dao.LocalgisStyleDAO;
import com.localgis.web.core.model.LocalgisStyle;

/**
 * Implementación utilizando iBatis de LocalgisStyleDAO
 * 
 * @author albegarcia
 * 
 */
public class LocalgisStyleDAOImpl extends SqlMapDaoTemplate implements LocalgisStyleDAO {

    /**
     * Logger para las trazas
     */
    private static Logger logger = Logger.getLogger(LocalgisStyleDAOImpl.class);

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public LocalgisStyleDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.localgis.web.core.dao.LocalgisStyleDAO#insert(com.localgis.web.core.model.LocalgisStyle)
     */
    public Integer insert(LocalgisStyle record) {
        Object newKey = insert("localgis_style.insert", record);
        return (Integer) newKey;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.localgis.web.core.dao.LocalgisStyleDAO#selectUnreferenceStyles()
     */
    public List selectUnreferenceStyles() {
		Map params = new HashMap();
        List result = queryForList("localgis_style.selectUnreferenceStyles",params);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.localgis.web.core.dao.LocalgisStyleDAO#selectStylesByIdLayer(java.lang.Integer)
     */
    public List selectStylesByIdLayer(Integer idLayer) {
        Map params = new HashMap();
        params.put("idLayer", idLayer);
        List result = queryForList("localgis_style.selectStylesByIdLayer", params);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.localgis.web.core.dao.LocalgisStyleDAO#deleteByPrimaryKey(java.lang.Integer)
     */
    public int deleteByPrimaryKey(Integer styleid) {
        LocalgisStyle key = new LocalgisStyle();
        key.setStyleid(styleid);
        int rows = delete("localgis_style.deleteByPrimaryKey", key);
        return rows;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisStyleDAO#selectStyleByIdLayerAndIdMap(java.lang.Integer, java.lang.Integer)
     */
    public LocalgisStyle selectStyleByIdLayerAndIdMap(Integer idLayer, Integer idMap) {
        Map params = new HashMap();
        params.put("idLayer", idLayer);
        params.put("idMap", idMap);
        /*
         * Para evitar que por una inconsistencia de datos se devuelvan varios
         * objetos y la llamada a queryForObject falle lo que hacemos es un
         * queryForList y nos quedamos el primer elemento
         */
        List result = queryForList("localgis_style.selectStyleByIdLayerAndIdMap", params);
        if (result.size() == 0) {
            return null;
        }
        if (result.size() > 1) {
            logger.warn("Se han obtenido varios elementos aunque se esperaba obtener uno para la query \"selectStyleByIdLayerAndIdMap\" con idLayer = ["+idLayer+"], idMap = ["+idMap+"]. Devolvemos el primero.");
        }
        return (LocalgisStyle)result.get(0);
    }


}