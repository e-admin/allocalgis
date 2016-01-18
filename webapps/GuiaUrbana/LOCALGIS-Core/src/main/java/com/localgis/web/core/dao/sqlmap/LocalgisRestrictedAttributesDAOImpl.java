/**
 * LocalgisRestrictedAttributesDAOImpl.java
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
import com.localgis.web.core.dao.LocalgisRestrictedAttributesDAO;
import com.localgis.web.core.model.LocalgisRestrictedAttribute;

/**
 * Implementación utilizando iBatis de LocalgisRestrictedAttributesDAO
 * 
 * @author albegarcia
 * 
 */
public class LocalgisRestrictedAttributesDAOImpl extends SqlMapDaoTemplate implements
		LocalgisRestrictedAttributesDAO {

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
	public LocalgisRestrictedAttributesDAOImpl(DaoManager daoManager) {
		super(daoManager);		
	}

	/*
	 * (non-Javadoc)
	 * @see com.localgis.web.core.dao.LocalgisRestrictedAttributesDAO#deleteRestrictedAttribute(com.localgis.web.core.model.LocalgisRestrictedAttribute)
	 */
    public int deleteRestrictedAttribute(LocalgisRestrictedAttribute record) {
        int rows = delete("localgis_restricted_attribute.deleteByPrimaryKey", record);
        return rows;
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisRestrictedAttributesDAO#insertRestrictedAttribute(com.localgis.web.core.model.LocalgisRestrictedAttribute)
     */
	public void insertRestrictedAttribute(LocalgisRestrictedAttribute record) {
        insert("localgis_restricted_attribute.insertRestrictedAttribute", record);
  	}

	/*
	 * (non-Javadoc)
	 * @see com.localgis.web.core.dao.LocalgisRestrictedAttributesDAO#selectRestrictedAttributes(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Boolean)
	 */
	public List selectRestrictedAttributes(Integer idLayer,Integer idEntidad,String locale, Boolean mapPublic) {
        Map params = new HashMap();
        params.put("idLayer", idLayer);
        params.put("idEntidad", idEntidad);
        params.put("locale",locale);
        if (mapPublic.booleanValue()) {
            params.put("mapasPublicos", ConstantesSQL.TRUE);
        } else {
            params.put("mapasPublicos", ConstantesSQL.FALSE);
        }
        List result = queryForList("localgis_restricted_attribute.selectRestrictedAttributesByLayerAndByPublic", params);
        return result;
	}

}
