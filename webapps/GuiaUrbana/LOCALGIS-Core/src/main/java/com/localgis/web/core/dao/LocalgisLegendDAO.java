/**
 * LocalgisLegendDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import com.localgis.web.core.model.LocalgisLegend;
import com.localgis.web.core.model.LocalgisLegendKey;

/**
 * DAO para manejar las leyendas de la base de datos de Localgis
 * 
 * @author albegarcia
 * 
 */
public interface LocalgisLegendDAO {

    /**
     * Inserta una leyenda
     * @param record La leyenda a insertar
     */
    public void insert(LocalgisLegend record);

    /**
     * Actualiza una leyenda
     * @param record La leyenda a actualizar
     * @return El numero de leyendas actualizadas
     */
    public int updateByPrimaryKey(LocalgisLegend record);

    /**
     * Obtiene una leyenda por primary key
     * @param key La primary key por la que se busca
     * @return La layenda correspondiente
     */
    public LocalgisLegend selectByPrimaryKey(LocalgisLegendKey key);

    /**
     * Elimina una leyenda por primary key
     * @param key La primary key por la que se elimina
     * @return El numero de registros eliminados
     */
    public int deleteByPrimaryKey(LocalgisLegendKey key);
}