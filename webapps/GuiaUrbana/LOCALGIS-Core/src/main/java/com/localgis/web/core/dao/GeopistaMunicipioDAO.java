/**
 * GeopistaMunicipioDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import java.util.List;

import com.ibatis.dao.client.Dao;
import com.localgis.web.core.model.GeopistaMunicipio;

/**
 * DAO para manejar los municipios de la base de datos de geopista
 * 
 * @author albegarcia
 * 
 */
public interface GeopistaMunicipioDAO extends Dao {

    /**
     * Método que devuelve todos los municipios
     * 
     * @return Una lista de objetos GeopistaMunicipio
     */
    public List selectAll();

    /**
     * Método que devuelve el municipio a partir de su id
     * 
     * @param idMunicipio
     *            Identificador del municipio
     * @return El municipio almacenado en BD
     */
    public GeopistaMunicipio selectMunicipioById(Integer idMunicipio);

    /**
     * Método que devuelve la lista de municipios asociados a una entidad
     * 
     * @param idEntidad
     *            Identificador de la entidad
     * @return La lista de municipios almacenados en BD como una lista de objetos GeopistaMunicipio
     */
    public List selectMunicipiosByIdEntidad(Integer idEntidad);

}