/**
 * LocalgisRestrictedAttributesDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import java.util.List;

import com.localgis.web.core.model.LocalgisRestrictedAttribute;

/**
 * DAO para manejar las capas de servidores WMS externos de la base de datos de
 * Localgis
 * 
 * @author albegarcia
 * 
 */
public interface LocalgisRestrictedAttributesDAO {

    /**
     * Borra un atributo restricted de un servidor WMS externo
     * 
     * @param record
     *            El registro a borrar
     * @return El numero de registros borrados
     */
	
    public int deleteRestrictedAttribute(LocalgisRestrictedAttribute record);

    /**
     * Inserta un atributo restricted de un servidor WMS externo
     * 
     * @param record
     *            El registro a insertar
     * 
     */
	
	public void insertRestrictedAttribute(LocalgisRestrictedAttribute record) ;


    /**
     * Método para obtener los atributos restringidos de una capa para una entidad y una configuracion de mapas publicos o privados
     * 
     * @param idLayer
     *            Identificador de la capa de geopista
     * @param idEntidad
     *            Identificador del municipio
     * @param locale
     *            Locale
     * @param mapPublic
     *            Mapa publico o privado
     * @return Una lista de capas como objetos LocalgisRestrictedAttribute
     * 
     */
    public List selectRestrictedAttributes(Integer idLayer,Integer idEntidad ,String locale,Boolean mapPublic);
}