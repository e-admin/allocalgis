/**
 * LocalgisIncidenciaDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import java.util.List;

import com.localgis.web.core.model.LocalgisIncidencia;
import com.localgis.web.core.model.LocalgisMarker;

/**
 * DAO para manejar los marcadores de la base de datos de Localgis
 * 
 * @author albegarcia
 * 
 */
public interface LocalgisIncidenciaDAO {

    /**
     * Método para insertar un marcador en la base de datos
     * 
     * @param record
     *            El marcador a insertar
     * @return El identificador del marcador insertado
     */
    public Integer insert(LocalgisIncidencia record);
    
    public String getDomain(String domainName,Integer idEntidad);


}