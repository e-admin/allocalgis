/**
 * GeopistaCoverageLayerDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import com.localgis.web.core.model.GeopistaCoverageLayer;

/**
 * DAO para manejar las coverage layers de la base de datos de Geopista
 * 
 * @author albegarcia
 * 
 */
public interface GeopistaCoverageLayerDAO {

    /**
     * Devuelve la coverage layer de una entidad
     * 
     * @param idEntidad Identificador de la entidad
     * @return La coverage layer asociada a la entidad
     */
    public GeopistaCoverageLayer selectCoverageLayerByIdEntidad(Integer idEntidad);

}