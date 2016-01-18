/**
 * GeopistaNucleoDAOImpl.java
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
import com.localgis.web.core.dao.GeopistaNucleoDAO;
import com.localgis.web.core.model.GeopistaNucleo;

/**
 * Implementación utilizando iBatis de GeopistaNucleoDAO
 * 
 * @author  
 * 
 */
public class GeopistaNucleoDAOImpl extends SqlMapDaoTemplate implements GeopistaNucleoDAO {

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public GeopistaNucleoDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.localgis.web.core.dao.GeopistaNucleoDAO#selectAll()
     */
    public List selectAll() {
		Map params = new HashMap();
        List list = queryForList("geopista_nucleo.selectAll",params);
        return list;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.GeopistaNucleo#selectNucleoById(java.lang.Integer)
     */
    public GeopistaNucleo selectNucleoById(Integer idMunicipio) {
        Map params = new HashMap();
        params.put("idMunicipio", idMunicipio);
        /*
         * Asumimos que vamos a encontrar como maximo un elemento. Si
         * encontramos mas sera un error grave y es correcto que falle
         */
        GeopistaNucleo result = (GeopistaNucleo) queryForObject("geopista_nucleo.selectNucleoById", params);
        return result;
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.GeopistaNucleoDAO#selectNucleoByIdMunicipio(java.lang.Integer)
     */
    public List selectNucleoByIdMunicipio(Integer idMunicipio) {
        Map params = new HashMap();
        params.put("idMunicipio", idMunicipio);
        
        List result = queryForList("geopista_nucleo.selectNucleoByIdMunicipio", params);
        return result;
    }

}