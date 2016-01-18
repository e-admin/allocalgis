/**
 * LocalgisMunicipioDAOImpl.java
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
import com.localgis.web.core.dao.LocalgisMunicipioDAO;
import com.localgis.web.core.model.BoundingBox;
import com.localgis.web.core.model.GeopistaMunicipio;

/**
 * Implementación utilizando iBatis de LocalgisMunicipioDAO
 * 
 * @author albegarcia
 * 
 */
public class LocalgisMunicipioDAOImpl extends SqlMapDaoTemplate implements LocalgisMunicipioDAO {

    /**
     * Constructor a partir de un DaoManager
     * 
     * @param daoManager
     *            El DaoManager para construirlo
     */
    public LocalgisMunicipioDAOImpl(DaoManager daoManager) {
        super(daoManager);
    }

    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisMunicipioDAO#selectBoundingBoxByIdMunicipioAndSRID(java.lang.Integer, java.lang.String)
     */
    public BoundingBox selectBoundingBoxByIdMunicipioAndSRID(Integer idMunicipio, Integer srid) {
        Map params = new HashMap();
        params.put("idMunicipio", idMunicipio);
        params.put("srid", srid);
        /*
         * Asumimos que vamos a encontrar como maximo un elemento. Si
         * encontramos mas sera un error grave y es correcto que falle
         */
        BoundingBox bbox = (BoundingBox) queryForObject("localgis_municipios.selectBoundingBoxByIdMunicipio", params);
        return bbox;
    }
    
    /*
     * (non-Javadoc)
     * @see com.localgis.web.core.dao.LocalgisMunicipioDAO#selectBoundingBoxByIdEntidadAndSRID(java.lang.Integer, java.lang.String)
     */
    public BoundingBox selectBoundingBoxByIdEntidadAndSRID(Integer idEntidad, Integer srid) {
        Map params = new HashMap();
        params.put("idEntidad", idEntidad);
        params.put("srid", srid);
        /*
         * Asumimos que vamos a encontrar como maximo un elemento. Si
         * encontramos mas sera un error grave y es correcto que falle
         */
        BoundingBox bbox = (BoundingBox) queryForObject("localgis_municipios.selectBoundingBoxByIdEntidad", params);
        return bbox;
    }

	@Override
	public GeopistaMunicipio selectMunicipioByGeometry(String srid, String geometry) {
        Map params = new HashMap();
     
        params.put("srid", srid);
        params.put("geometrypoint", geometry);
        GeopistaMunicipio municipio = (GeopistaMunicipio) queryForObject("localgis_municipios.selectMunicipioByGeometryPoint", params);
        
        return municipio;
	}
    
 

}