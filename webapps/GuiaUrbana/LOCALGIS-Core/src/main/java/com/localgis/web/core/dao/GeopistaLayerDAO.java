/**
 * GeopistaLayerDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import java.util.List;

import com.ibatis.dao.client.Dao;

/**
 * DAO para manejar las capas de la base de datos de Geopista
 * 
 * @author albegarcia
 * 
 */
public interface GeopistaLayerDAO extends Dao {

    /**
     * Devuelve las capas de un mapa determinado para una entidad determinada
     * 
     * @param idMap
     *            El identificador del mapa
     * @param idEntidad
     *            El identificador de la entidad
     * @return Una lista de objetos GeopistaLayer
     */
    public List selectLayersByIdMapAndIdEntidad(Integer idMap, Integer idEntidad);

    /**
     * Devuelve todas las capas de los mapas asociados a un municipio determinado
     * 
     * @param idEntidad
     *            El identificador de la entidad
     * @return Una lista de objetos GeopistaLayer
    */
    
    public List selectLayersByIdEntidad(Integer idEntidad);

    
    /**
     * 
     * @param layerName
     * @return
     */
    public String getCategoryByLayerName(String  layerName);
}
