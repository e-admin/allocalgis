/**
 * GeopistaMapGenericElementDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import com.ibatis.dao.client.Dao;
import com.localgis.web.core.model.BoundingBox;
import com.localgis.web.core.model.Point;

/**
 * DAO para manejar elementos genericos de mapas de Geopista
 * 
 * @author albegarcia
 * 
 */
public interface GeopistaMapGenericElementDAO extends Dao {

    /**
     * Devuelve el punto de centrado de un elemento a partir del nombre de la
     * tabla donde esta el elemento, la columna que identifica a un elemento y
     * el valor de dicho elemento
     * 
     * @param tableName
     *            Nombre de la tabla
     * @param identifierColumnName
     *            Nombre de la columna identificador
     * @param identifierValue
     *            Valor del identificador
     * @param srid Sistema de referencia del punto
     * @return El punto de centrado del elemento
     */
    public Point selectCenteredPointMapGenericElement(String tableName, String identifierColumnName, Object identifierValue, Integer srid);

    /**
     * Devuelve el bounding box de un elemento a partir del nombre de la
     * tabla donde esta el elemento, la columna que identifica a un elemento y
     * el valor de dicho elemento
     * 
     * @param tableName
     *            Nombre de la tabla
     * @param identifierColumnName
     *            Nombre de la columna identificador
     * @param identifierValue
     *            Valor del identificador
     * @param srid Sistema de referencia de los puntos
     * @return El bounding box del elemento
     */
    public BoundingBox selectBoundingBoxMapGenericElement(String tableName, String identifierColumnName, Object identifierValue, Integer srid);
}