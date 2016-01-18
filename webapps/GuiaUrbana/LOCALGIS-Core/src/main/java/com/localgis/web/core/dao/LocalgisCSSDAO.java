/**
 * LocalgisCSSDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import com.localgis.web.core.model.LocalgisCSS;

/**
 * DAO para manejar los CSS de la base de datos de Localgis
 * 
 * @author albegarcia
 * 
 */
public interface LocalgisCSSDAO {

    /**
     * Inserta un CSS
     * @param record El registro a insertar
     */
    public void insert(LocalgisCSS record);

    /**
     * Actualiza un CSS
     * @param record El CSS a actualizar
     * @return El numero de registros actualizados
     */
    public int updateByPrimaryKey(LocalgisCSS record);

    /**
     * Obtiene un CSS por id de municipio
     * @param idmunicipio El id de municipio por el que se busca
     * @return Un CSS
     */
    public LocalgisCSS selectByPrimaryKey(Integer idmunicipio);

    /**
     * Elimina un CSS por id de municipio
     * @param idmunicipio El id del municipio por el que se borra
     * @return
     */
    public int deleteByPrimaryKey(Integer idmunicipio);
}