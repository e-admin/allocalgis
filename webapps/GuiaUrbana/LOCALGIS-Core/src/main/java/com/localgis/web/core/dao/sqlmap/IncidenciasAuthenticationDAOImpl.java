/**
 * IncidenciasAuthenticationDAOImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao.sqlmap;

import java.util.HashMap;
import java.util.Map;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;
import com.localgis.web.core.dao.IncidenciasAuthenticationDAO;

/**
 * Implementación utilizando iBatis de GeopistaAuthenticationDAO
 * 
 * @author albegarcia
 * 
 */
public class IncidenciasAuthenticationDAOImpl extends SqlMapDaoTemplate implements IncidenciasAuthenticationDAO {

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public IncidenciasAuthenticationDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.GeopistaAuthenticationDAO#isValidUser(java.lang.String, java.lang.String, java.lang.String, java.lang.Integer)
     */
    public boolean isValidUserIncidencias(String username, String encryptedPassword, String encryptedPassword2,String role, Integer idEntidad) {
        Map params = new HashMap();
        params.put("username", username.toUpperCase());
        params.put("encryptedPassword", encryptedPassword);
        params.put("encryptedPassword2", encryptedPassword2);
        params.put("role", role);
        params.put("idEntidad", idEntidad);
        Integer result = (Integer)queryForObject("incidencias_authentication.isValidUserIncidencias", params);
        return result.intValue() > 0;
    }
    
    public boolean isValidUserIncidenciasExterno(String idUsuario){
    	Map params = new HashMap();
        params.put("idUsuario", idUsuario.toUpperCase());
        Integer result = (Integer)queryForObject("incidencias_authentication.isValidUserIncidenciasExterno", params);
        return result.intValue() > 0;
    }
    
}