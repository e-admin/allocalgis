/**
 * GeopistaMapDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import java.util.List;

import com.ibatis.dao.client.Dao;
import com.localgis.web.core.model.GeopistaMap;

/**
 * DAO para manejar los mapas de la base de datos de Geopista
 * 
 * @author albegarcia
 * 
 */
public interface GeopistaMapDAO extends Dao {

    /**
     * Método que devuelve la lista de mapas asociados a un municipio
     * 
     * @param idEntidad
     *            Identificador de la entidad de la que se desea obtener los mapas
     * @param locale
     *            Locales para hacer la consulta
     * @return Una lista de mapas como objetos
     *         <code>com.geopista.app.guiaurbana.api.model.MapaGeopista</code>
     */
    public List selectMapsByIdEntidad(Integer idEntidad, String locale);

    /**
     * Método que devuelve un mapa especifico para un municipio
     * 
     * @param idEntidad
     *            Identificador de la entidad de la que se desea obtener los mapas
     * @param idMap
     *            Identificador de mapa
     * @param locale
     *            Locales para hacer la consulta
     * @return Mapa de Geopista
     *         <code>com.geopista.app.guiaurbana.api.model.MapaGeopista</code>
     */
    public GeopistaMap selectMapById(Integer idEntidad, Integer idMap,String locale);


    /**
     * 
     * @param nombreMapa
     * @param nombreMunicipio
     * @return
     */
    public GeopistaMap selectMapByName(String nombreMapa,Integer idMunicipio);
    	 
}