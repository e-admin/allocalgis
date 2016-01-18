/**
 * LocalgisIncidenciaDAOImpl.java
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
import com.localgis.web.core.dao.LocalgisIncidenciaDAO;
import com.localgis.web.core.dao.LocalgisMarkerDAO;
import com.localgis.web.core.model.LocalgisDomain;
import com.localgis.web.core.model.LocalgisIncidencia;
import com.localgis.web.core.model.LocalgisMarker;

/**
 * Implementación utilizando iBatis de LocalgisMarkerDAO
 * 
 * @author albegarcia
 * 
 */
public class LocalgisIncidenciaDAOImpl extends SqlMapDaoTemplate implements LocalgisIncidenciaDAO {

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public LocalgisIncidenciaDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisMarkerDAO#insert(com.localgis.web.core.model.LocalgisMarker)
     */
    public Integer insert(LocalgisIncidencia record) {
        Object newKey = insert("localgis_incidencia.insert", record);
        return (Integer) newKey;
    }
    
    public String getDomain(String domainName,Integer idEntidad) {
        
	    Map parametros = new HashMap();
	    parametros.put("idEntidad", idEntidad);
	    parametros.put("domainName", domainName);
	    /*
	     * Para evitar que por una inconsistencia de datos se devuelvan varios
	     * objetos y la llamada a queryForObject falle lo que hacemos es un
	     * queryForList y nos quedamos el primer elemento
	     */
	    String cadenaResultado="";
	    List result = queryForList("localgis_incidencia.getDomain", parametros);
	    for (int i=0;i<result.size();i++){
	    	LocalgisDomain domain=(LocalgisDomain)result.get(i);
	    	if (i==result.size()-1)
	    		cadenaResultado+=domain.getPattern()+":"+domain.getTraduccion();
	    	else
	    		cadenaResultado+=domain.getPattern()+":"+domain.getTraduccion()+";";
	    	
	    }
	    return cadenaResultado;
    }
 
}