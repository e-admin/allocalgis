/**
 * LocalgisMapDAOImpl.java
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
import com.localgis.web.core.ConstantesSQL;
import com.localgis.web.core.dao.LocalgisMapDAO;
import com.localgis.web.core.model.LocalgisMap;

/**
 * Implementación utilizando iBatis de LocalgisMapDAO
 * 
 * @author albegarcia
 * 
 */
public class LocalgisMapDAOImpl extends SqlMapDaoTemplate implements LocalgisMapDAO {

    /**
     * Logger para las trazas
     */
    private static Logger logger = Logger.getLogger(LocalgisMapDAOImpl.class);

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public LocalgisMapDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.localgis.web.core.dao.LocalgisMapDAO#insert(com.localgis.web.core.model.LocalgisMap)
     */
    public Integer insert(LocalgisMap map) {
        Object newKey = insert("localgis_map.insert", map);
        return (Integer) newKey;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.localgis.web.core.dao.LocalgisMapDAO#selectMapsByIdMunicipio(java.lang.Integer,
     *      java.lang.Boolean, java.lang.String)
     */
    public List selectMapsByIdEntidad(Integer idEntidad, String locale) {
        Map parametros = new HashMap();
        parametros.put("idEntidad", idEntidad);
        parametros.put("locale", locale);
        logger.info("idEntidad:"+idEntidad);
        logger.info("locale:"+locale);
        List list = queryForList("localgis_map.selectMapsByIdEntidad", parametros);
        logger.info("Valor de lista con mapas:"+list);
        return list;

    }

    public List selectMapsByIdEntidad(Integer idEntidad, Boolean mapasPublicos, String locale) {
        Map parametros = new HashMap();
        parametros.put("idEntidad", idEntidad);
        if (mapasPublicos.booleanValue()) {
            parametros.put("mapasPublicos", ConstantesSQL.TRUE);
        } else {
            parametros.put("mapasPublicos", ConstantesSQL.FALSE);
        }
        parametros.put("locale", locale);
        List list = queryForList("localgis_map.selectMapsByIdEntidadAndPublicMaps", parametros);
        return list;

    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisMapDAO#selectMapById(java.lang.Integer, java.lang.String)
     */
    public LocalgisMap selectMapById(Integer idMap, String locale) {
        Map parametros = new HashMap();
        parametros.put("idMap", idMap);
        parametros.put("locale", locale);
        /*
         * Asumimos que vamos a encontrar como maximo un elemento. Si
         * encontramos mas sera un error grave y es correcto que falle
         */
        LocalgisMap result = (LocalgisMap)queryForObject("localgis_map.selectMapById", parametros);
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.localgis.web.core.dao.LocalgisMapDAO#deleteByIdMap(java.lang.Integer)
     */
    public int deleteByIdMapAndPublicMap(Integer idMap, Boolean publicMap) {
        Map params = new HashMap();
        params.put("idMap", idMap);
        if (publicMap != null && publicMap.booleanValue()) {
            params.put("publicMap", ConstantesSQL.TRUE);
        } else {
            params.put("publicMap", ConstantesSQL.FALSE);
        }
        int rows = delete("localgis_map.deleteByIdMapAndPublicMap", params);
        return rows;
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisMapDAO#setDefaltMap(java.lang.Integer, java.lang.Boolean, java.lang.Integer)
     */
    public int setDefaultMap(Integer idGeopista, Boolean publicMap, Integer idEntidad) {
        Map params = new HashMap();
        params.put("idGeopista", idGeopista);
        if (publicMap != null && publicMap.booleanValue()) {
            params.put("publicMap", ConstantesSQL.TRUE);
        } else {
            params.put("publicMap", ConstantesSQL.FALSE);
        }
        params.put("idEntidad", idEntidad);
        int rows = update("localgis_map.removeDefaultMap", params);
        rows = update("localgis_map.setDefaultMap", params);
        return rows;
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisMapDAO#getDefaltMap(java.lang.Boolean, java.lang.Integer)
     */
    public LocalgisMap getDefaltMap(Boolean publicMap, Integer idEntidad, String locale) {
        Map params = new HashMap();
        if (publicMap != null && publicMap.booleanValue()) {
            params.put("publicMap", ConstantesSQL.TRUE);
        } else {
            params.put("publicMap", ConstantesSQL.FALSE);
        }
        params.put("idEntidad", idEntidad);
        params.put("locale", locale);
        /*
         * Asumimos que vamos a encontrar como maximo un elemento. Si
         * encontramos mas sera un error grave y es correcto que falle
         */
        return (LocalgisMap)queryForObject("localgis_map.selectDefaultMap", params);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisMapDAO#selectMapById(java.lang.Integer, java.lang.String)
     */
    public Integer getIdMapByIdGeopista(Integer idGeopistaMap,Integer idEntidad,String locale, Boolean publicMap) {
        Map parametros = new HashMap();
        parametros.put("idMap", idGeopistaMap);
        parametros.put("idEntidad", idEntidad);
        if (publicMap != null && publicMap.booleanValue()) {
            parametros.put("publicMap", ConstantesSQL.TRUE);
        } else {
            parametros.put("publicMap", ConstantesSQL.FALSE);
        }
        parametros.put("locale", locale);
        /*
         * Para evitar que por una inconsistencia de datos se devuelvan varios
         * objetos y la llamada a queryForObject falle lo que hacemos es un
         * queryForList y nos quedamos el primer elemento
         */
        List result = queryForList("localgis_map.selectMapByIdGeopista", parametros);
        if (result.size() == 0) {
            return null;
        }
        if (result.size() > 1) {
            logger.warn("Se han obtenido varios elementos aunque se esperaba obtener uno para la query \"selectMapByIdGeopista\" con idMap = ["+idGeopistaMap+"], idEntidad = ["+idEntidad+"], publicMap = ["+publicMap+"], locale = ["+locale+"]. Devolvemos el primero.");
        }
        return (Integer)result.get(0);
    }  
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisMapDAO#updateMapByIdMap(com.localgis.web.core.model.LocalgisMap)
     */
    public int updateMapByIdMap(LocalgisMap localgisMap) {
        int rows = update("localgis_map.updateMapByIdMap", localgisMap);
        if (rows > 1) {
            logger.warn("Se han actualizado mas elementos de los esperados para la actualizacion \"updateMapByIdMap\" con idMap = ["+localgisMap.getMapid()+"].");
        }
        return rows;
    }

	@Override
	public List getEntidadesPublicadas(Integer mapidgeopista, Boolean publicMap) {
		Map parametros = new HashMap();
		parametros.put("mapidgeopista",mapidgeopista);
		if (publicMap != null && publicMap.booleanValue()) {
            parametros.put("publicMap", ConstantesSQL.TRUE);
        } else {
            parametros.put("publicMap", ConstantesSQL.FALSE);
        }
        List list = queryForList("localgis_map.selectEntidadesByIdMapGeopista", parametros);
        return list;
	}
}