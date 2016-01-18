/**
 * GeopistaMapDAOImpl.java
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
import com.localgis.web.core.dao.GeopistaMapDAO;
import com.localgis.web.core.model.GeopistaMap;

/**
 * Implementación utilizando iBatis de GeopistaMapDAO
 * 
 * @author albegarcia
 * 
 */
public class GeopistaMapDAOImpl extends SqlMapDaoTemplate implements GeopistaMapDAO {

    /**
     * Logger para las trazas
     */
    private static Logger logger = Logger.getLogger(GeopistaMapDAOImpl.class);

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public GeopistaMapDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.localgis.web.core.dao.GeopistaMapDAO#selectMapsByIdEntidad(java.lang.Integer,
     *      java.lang.String)
     */
    public List selectMapsByIdEntidad(Integer idEntidad, String locale) {
        Map parametros = new HashMap();
        parametros.put("idEntidad", idEntidad);
        parametros.put("locale", locale);
        List list = queryForList("geopista_maps.selectMapsByIdEntidad", parametros);
        return list;
    }
    
    public GeopistaMap selectMapById(Integer idEntidad, Integer idMap,String locale) {
        Map parametros = new HashMap();
        parametros.put("idEntidad", idEntidad);
        parametros.put("idMap", idMap);
        parametros.put("locale", locale);
        /*
         * Para evitar que por una inconsistencia de datos se devuelvan varios
         * objetos y la llamada a queryForObject falle lo que hacemos es un
         * queryForList y nos quedamos el primer elemento
         */
        List result = queryForList("geopista_maps.selectMapById", parametros);
        if (result.size() == 0) {
            return null;
        }
        if (result.size() > 1) {
            logger.error("Se han obtenido varios elementos aunque se esperaba obtener uno para la query \"selectMapById\" con idEntidad = ["+idEntidad+"], idMap = ["+idMap+"], locale = ["+locale+"]. Devolvemos el primero.");
        }
        return (GeopistaMap)result.get(0);
    }    
    
    public GeopistaMap selectMapByName(String nombreMapa,Integer idMunicipio) {
        Map parametros = new HashMap();
        parametros.put("nombreMapa", nombreMapa);
        parametros.put("idMunicipio", idMunicipio);
        /*
         * Para evitar que por una inconsistencia de datos se devuelvan varios
         * objetos y la llamada a queryForObject falle lo que hacemos es un
         * queryForList y nos quedamos el primer elemento
         */
        List result = queryForList("geopista_maps.selectMapByName", parametros);
        if (result.size() == 0) {
            return null;
        }
        if (result.size() > 1) {
            logger.error("Se han obtenido varios elementos aunque se esperaba obtener uno para la query \"selectMapById\" con idMunicipio = ["+idMunicipio+"]. Devolvemos el primero.");
        }
        return (GeopistaMap)result.get(0);
    }    
}