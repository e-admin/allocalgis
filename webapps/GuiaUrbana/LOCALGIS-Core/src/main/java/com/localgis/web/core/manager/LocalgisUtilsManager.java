/**
 * LocalgisUtilsManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.manager;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.model.BoundingBox;
import com.localgis.web.core.model.Scale;

public interface LocalgisUtilsManager {

	/**
	 * Devuelve la escala adecuada para que la parcela indicada por la referencia catastral
	 * aparezca centrada en una imagen del tamaño indicado.
	 * 
	 * @param referenciaCatastral
	 * 			Referencia catastral de la parcela que se desea visualizar
	 * @param width
	 * 			Ancho de la imagen en pixeles.
	 * @param height
	 * 			Alto de la imagen en pixeles.
	 * @return Una escala
	 * @throws LocalgisDBException
	 * 			Si ocurre algun error al acceder a la base de datos
	 */
	public Scale getReportScaleForParcelaByReferenciaCatastral(String referenciaCatastral, int width, int height) throws LocalgisDBException;

	/**
     * forma generica centrado en un elemento determinado
     * Devuelve la escala adecuada para que un elemento determinado 
     * aparezca centrado en una imagen del tamaño indicado.
     * 
     * @param tableName
     *            Tabla que contiene el elemento
     * @param identifierColumnName
     *            Columna que contiene el identificador del elemento
     * @param identifierValue
     *            Valor del identificador
     * @param width
     *          Ancho de la imagen en pixeles.
     * @param height
     *          Alto de la imagen en pixeles.
     * @param srid Sistema de referencia
     * @return Una escala
     * @throws LocalgisDBException
     *          Si ocurre algun error al acceder a la base de datos
     */
    public Scale getReportScale(String tableName, String identifierColumnName, Object identifierValue, int width, int height, Integer srid) throws LocalgisDBException;

	/**
	 * Devuelve si un usuario es válido para un rol y una entidad determinado
	 * @param username Usuario
	 * @param password Password
	 * @param role Rol
	 * @param idEntidad Entidad 
	 * @return Si el usuario es válido o no
	 */
	public boolean isValidUser(String username, String password, String role, Integer idEntidad);

	/**
	 * Devuelve la entidad de un usuario, si el usuario no existe devuelve -1
	 * @param username Usuario
	 * @param password Password
	 * @param role Rol
	 * @return Si el usuario es válido o no
	 */
	public Integer isValidUserEntity(String username, String password, String role);
	
	/**
	 * Devuelve la entidad de un usuario (sin confirmar el password), si el usuario no existe devuelve -1
	 * @param username Usuario
	 * @param role Rol
	 * @return Si el usuario es válido o no
	 */
	public Integer isValidUserEntityNotPass(String username, String role);
	
    /**
     * Método que devuelve el BBOX de España para un determinado SRID
     * @param srid El SRID
     * @return EL Bounding box de España
     * @throws LocalgisConfigurationException Si ocurre algun error con la configuracion
     */
    public BoundingBox getBoundingBoxSpain(String srid) throws LocalgisConfigurationException;

    
    /**
     * Método que devuelve el BBOX de España para un determinado SRID
     * @param srid El SRID
     * @return EL Bounding box de España
     * @throws LocalgisConfigurationException Si ocurre algun error con la configuracion
     */
    public BoundingBox getBoundingBoxComunidad(String srid) throws LocalgisConfigurationException;

	/**
	 * Devuelve si un usuario es válido para insertar incidencias
	 * @param username Usuario
	 * @param password Password
	 * @param role Rol
	 * @param idEntidad Entidad 
	 * @return Si el usuario es válido o no
	 */
	public boolean isValidUserIncidencias(String username, String password, String role, Integer idEntidad);
	
	public boolean isValidUserIncidenciasExterno(String idUsuario);
}
