/**
 * LocalgisMapServerLayerDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import java.util.List;

import com.localgis.web.core.model.LocalgisMapServerLayer;

/**
 * DAO para manejar las capas de servidores WMS externos de la base de datos de
 * Localgis
 * 
 * @author albegarcia
 * 
 */
public interface LocalgisMapServerLayerDAO {

    /**
     * Inserta una capa de un servidor WMS externo
     * 
     * @param record
     *            El registro a insertar
     * @return La clave del registro insertado (el identificador)
     */
    public Integer insert(LocalgisMapServerLayer record);

    /**
     * Método para obtener las capas externas de un mapa determinado
     * 
     * @param idMap
     *            Identificador del mapa
     * @return Una lista de capas como objetos LocalgisLayer
     * 
     */
    public List selectMapServerLayersByIdMap(Integer idMap);
    
    /**
     * Método para obtener las capas de los mapas publicos o privados para un
     * municipio determinado
     * 
     * @param idMunicipio
     *            Identificador del municipio
     * @param publicMaps
     *            Si se desean obtener las capas de los mapas publicos (true) o
     *            privados (false)
     * @return Una lista de objetos LocalgisLayer
     */
    public List selectMapServerLayersByIdMunicipio(Integer idMunicipio, Boolean publicMaps);

    /**
     * Método para eliminar las capas wms de un mapa determinado
     * 
     * @param idMap
     *            Identificador del mapa
     * 
     * @return El número de registros eliminados
     */
    public int deleteMapServerLayersByIdMap(Integer idMap);

}