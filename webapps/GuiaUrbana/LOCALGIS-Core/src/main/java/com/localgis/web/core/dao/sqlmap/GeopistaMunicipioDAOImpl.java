/**
 * GeopistaMunicipioDAOImpl.java
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
import com.localgis.web.core.dao.GeopistaMunicipioDAO;
import com.localgis.web.core.model.GeopistaMunicipio;

/**
 * Implementación utilizando iBatis de GeopistaMunicipioDAO
 * 
 * @author albegarcia
 * 
 */
public class GeopistaMunicipioDAOImpl extends SqlMapDaoTemplate implements GeopistaMunicipioDAO {

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public GeopistaMunicipioDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.localgis.web.core.dao.GeopistaMunicipioDAO#selectAll()
     */
    public List selectAll() {
		Map params = new HashMap();
        List list = queryForList("geopista_municipios.selectAll",params);
        return list;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.GeopistaMunicipioDAO#selectMunicipioById(java.lang.Integer)
     */
    public GeopistaMunicipio selectMunicipioById(Integer idMunicipio) {
        Map params = new HashMap();
        params.put("idMunicipio", idMunicipio);
        /*
         * Asumimos que vamos a encontrar como maximo un elemento. Si
         * encontramos mas sera un error grave y es correcto que falle
         */
        GeopistaMunicipio result = (GeopistaMunicipio) queryForObject("geopista_municipios.selectMunicipioById", params);
        return result;
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.GeopistaMunicipioDAO#selectMunicipiosByIdEntidad(java.lang.Integer)
     */
    public List selectMunicipiosByIdEntidad(Integer idEntidad) {
        Map params = new HashMap();
        params.put("idEntidad", idEntidad);
        /*
         * Asumimos que vamos a encontrar como maximo un elemento. Si
         * encontramos mas sera un error grave y es correcto que falle
         */
        List result = queryForList("geopista_municipios.selectMunicipiosByIdEntidad", params);
        return result;
    }

}