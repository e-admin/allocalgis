/**
 * LocalgisStyleDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import java.util.List;

import com.ibatis.dao.client.Dao;
import com.localgis.web.core.model.LocalgisStyle;

/**
 * DAO para manejar los estilos de la base de datos de Geopista
 * 
 * @author albegarcia
 * 
 */
public interface LocalgisStyleDAO extends Dao {

    /**
     * Inserta un estilo
     * 
     * @param record
     *            El registro a insertar
     * @return La clave del registro insertado
     */
    public Integer insert(LocalgisStyle record);

    /**
     * Método para obtener los estilos que no estén referenciadas por ningun
     * mapa
     * 
     * @return La lista de estilos que no están referenciadas como objetos
     *         LocalgisStyle
     */
    public List selectUnreferenceStyles();

    /**
     * Método para obtener los estilos asociados a una capa
     * 
     * @param idLayer
     *            El id de la capa de la que se desean consultar los estilos
     * @return La lista de estilos asociados a la capa como objetos
     *         LocalgisStyle
     */
    public List selectStylesByIdLayer(Integer idLayer);

    /**
     * Método para obtener el estilo asociado a una capa para un mapa determinado
     * 
     * @param idLayer
     *            El id de la capa de la que se desean consultar los estilos
     * @param idMap
     *            El id del mapa asociado a la capa de la que se desean consultar los estilos
     * @return El estilo asociado a la capa
     */
    public LocalgisStyle selectStyleByIdLayerAndIdMap(Integer idLayer, Integer idMap);
    
    /**
     * Método para eliminar un estilo por primary key
     * 
     * @param styleid
     *            Identificador del estilo a eliminar
     * @return El número de registros eliminados
     */
    public int deleteByPrimaryKey(Integer styleid);

}