/**
 * LocalgisAttributeDAOImpl.java
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
import com.localgis.web.core.dao.LocalgisAttributeDAO;
import com.localgis.web.core.model.LocalgisAttribute;

/**
 * Implementación utilizando iBatis de LocalgisAttributeDAO
 * 
 * @author albegarcia
 * 
 */
public class LocalgisAttributeDAOImpl extends SqlMapDaoTemplate implements LocalgisAttributeDAO {

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public LocalgisAttributeDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisAttributeDAO#insert(com.localgis.web.core.model.LocalgisAttribute)
     */
    public Integer insert(LocalgisAttribute record) {
        Object newKey = insert("localgis_attribute.insert", record);
        return (Integer) newKey;
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisAttributeDAO#selectAttributesByLayer(java.lang.Integer)
     */
    public List selectAttributesByLayer(Integer idLayer,Integer idMap) {
        Map params = new HashMap();
        params.put("idLayer", idLayer);
        params.put("idMap", idMap);
        List result = queryForList("localgis_attribute.selectAttributesByLayer", params);
        return result;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisAttributeDAO#deleteByPrimaryKey(java.lang.Integer)
     */
    public int deleteByPrimaryKey(Integer attributeid) {
        LocalgisAttribute key = new LocalgisAttribute();
        key.setAttributeid(attributeid);
        int rows = delete("localgis_attribute.deleteByPrimaryKey", key);
        return rows;
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisAttributeDAO#updateByPrimaryKey(com.localgis.web.core.model.LocalgisAttribute)
     */
    public int updateByPrimaryKey(LocalgisAttribute record) {
        int rows = update("localgis_attribute.updateByPrimaryKey", record);
        return rows;
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisAttributeDAO#selectRestrictedAttributesByIdLayerGeopistaAndMapPublic(java.lang.Integer, java.lang.Short)
     */
    public List selectRestrictedAttributesByIdLayerGeopistaAndMapPublic(Integer idLayerGeopista, Short mapPublic,Integer idEntidad) {
        Map params = new HashMap();
        params.put("idLayerGeopista", idLayerGeopista);
        params.put("mapPublic", mapPublic);
        params.put("idEntidad", idEntidad);
        List result = queryForList("localgis_attribute.selectRestrictedAttributesByIdLayerGeopistaAndMapPublic", params);
        return result;

    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisAttributeDAO#selectAttributesTranslatedByNameLayerAndMapPublic(java.lang.String, java.lang.Short, java.lang.String)
     */
    public List selectAttributesTranslatedByNameLayerAndMapPublic(String nameLayer, Boolean mapPublic, String locale) {
        Map params = new HashMap();
        params.put("nameLayer", nameLayer);
        if (mapPublic != null && mapPublic.booleanValue()) {
            params.put("mapPublic", ConstantesSQL.TRUE);
        } else {
            params.put("mapPublic", ConstantesSQL.FALSE);
        }
        params.put("locale", locale);
        List result = queryForList("localgis_attribute.selectAttributesTranslatedByNameLayerAndMapPublic", params);
        return result;
    }
    
    
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisAttributeDAO#selectAttributesValuesTranslatedByNameLayer(java.lang.String,  java.lang.String)
     */
    public List selectAttributesValuesTranslatedByNameLayer(String nameLayer, String locale) {
        Map params = new HashMap();
        params.put("nameLayer", nameLayer);
        params.put("locale", locale);
        List result = queryForList("localgis_attribute.selectAttributesValuesTranslatedByNameLayer", params);
        return result;
    }

	@Override
	public List selectAttributesTranslatedByIdLayerAndMapPublic(
			Integer idLayer, Integer idMap, Boolean mapPublic, String locale) {
		 Map params = new HashMap();
	     params.put("idLayer", idLayer);
	     params.put("idMap", idMap);
        if (mapPublic != null && mapPublic.booleanValue()) {
            params.put("mapPublic", ConstantesSQL.TRUE);
        } else {
            params.put("mapPublic", ConstantesSQL.FALSE);
        }
        params.put("locale", locale);
        List result = queryForList("localgis_attribute.selectAttributesTranslatedByIdLayerAndMapPublic", params);
        return result;
	}



}