/**
 * GeopistaAuthenticationDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import com.ibatis.dao.client.Dao;

/**
 * DAO para manejar la autenticacion con la base de datos de GeoPISTA
 * 
 * @author albegarcia
 * 
 */
public interface GeopistaAuthenticationDAO extends Dao {

    /**
     * Comprueba si un usuario es válido
     * @param username Usuario
     * @param encryptedPassword Password cifrada del usuario
     * @param role Role con que se desea autenticar
     * @param idEntidad Identificador de la entidad con la que se desea autenticar
     * @return Si el usuario es válido o no
     */
    public boolean isValidUser(String username, String encryptedPassword, String encryptedPassword2,String role, Integer idEntidad);
    
    /**
     * Devuelve la entidad de un usuario
     * @param username Usuario
     * @param encryptedPassword Password cifrada del usuario
     * @param role Role con que se desea autenticar
     * @return Si el usuario es válido o no
     */
    public Integer isValidUserEntity(String username, String encryptedPassword, String encryptedPassword2,String role);

    /**
     * Devuelve la entidad de un usuario
     * @param username Usuario
     * @param role Role con que se desea autenticar
     * @return Si el usuario es válido o no
     */
    public Integer isValidUserEntityNotPass(String username, String role);
    
}