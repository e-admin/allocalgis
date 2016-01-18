/**
 * LocalgisMapLayerDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import com.localgis.web.core.model.LocalgisMapLayer;

/**
 * DAO para manejar las asociaciones de mapas-capas-estilos de la base de datos
 * de Localgis
 * 
 * @author albegarcia
 * 
 */
public interface LocalgisMapLayerDAO {

    /**
     * Inserta una asociación mapa-capa-estilo
     * 
     * @param record
     *            El registro a insertar
     */
    public void insert(LocalgisMapLayer record);

    /**
     * Método para eliminar las capas de un mapa determinado
     * 
     * @param idMap
     *            Identificador del mapa
     * 
     * @return El número de registros eliminados
     */
    public int deleteLayersByIdMap(Integer idMap);

}