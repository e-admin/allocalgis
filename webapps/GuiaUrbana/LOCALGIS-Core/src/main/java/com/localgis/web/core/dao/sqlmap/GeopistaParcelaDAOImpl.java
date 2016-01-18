/**
 * GeopistaParcelaDAOImpl.java
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
import com.localgis.web.core.dao.GeopistaParcelaDAO;
import com.localgis.web.core.model.BoundingBox;
import com.localgis.web.core.model.GeopistaParcela;

/**
 * Implementación utilizando iBatis de GeopistaParcelaDAO
 * 
 * @author albegarcia
 * 
 */
public class GeopistaParcelaDAOImpl extends SqlMapDaoTemplate implements GeopistaParcelaDAO {

    /**
     * Logger para las trazas
     */
    private static Logger logger = Logger.getLogger(GeopistaParcelaDAOImpl.class);

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public GeopistaParcelaDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.GeopistaParcelaDAO#selectParcelaByReferenciaCatastral(java.lang.String)
     */
    public GeopistaParcela selectParcelaByReferenciaCatastral(String referenciaCatastral, Integer srid) {
        Map params = new HashMap();
        params.put("referenciaCatastral", referenciaCatastral);
        params.put("srid", srid);
        List geopistaParcelas = queryForList("geopista_parcelas.selectParcelaByReferenciaCatastral", params);
        if (geopistaParcelas.size() > 0) {
            return (GeopistaParcela)geopistaParcelas.get(0);
        } else {
            return null;
        }
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.GeopistaParcelaDAO#selectBox3DByReferenciaCatastral(java.lang.String)
     */
	public BoundingBox selectBoundingBoxByReferenciaCatastral(String referenciaCatastral) {
		Map params = new HashMap();
		params.put("referenciaCatastral", referenciaCatastral);
        /*
         * Para evitar que por una inconsistencia de datos se devuelvan varios
         * objetos y la llamada a queryForObject falle lo que hacemos es un
         * queryForList y nos quedamos el primer elemento
         */
        List result = queryForList("geopista_parcelas.selectBox3DByReferenciaCatastral", params);
        if (result.size() == 0) {
            return null;
        }
        if (result.size() > 1) {
            logger.warn("Se han obtenido varios elementos aunque se esperaba obtener uno para la query \"selectBox3DByReferenciaCatastral\" con referenciaCatastral = ["+referenciaCatastral+"]. Devolvemos el primero.");
        }
        return (BoundingBox)result.get(0);
	}
}