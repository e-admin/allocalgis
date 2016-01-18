/**
 * LocalgisAttributeDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import java.util.List;

import com.localgis.web.core.model.LocalgisAttribute;

/**
 * DAO para manejar los atributos de la base de datos de Localgis
 * 
 * @author albegarcia
 * 
 */
public interface LocalgisAttributeDAO {

    /**
     * Inserta un atributo de localgis
     * 
     * @param record
     *            El atributo a insertar
     * @return El identificador (PK) del atributo insertado
     */
    public Integer insert(LocalgisAttribute record);

    /**
     * Devuelve los atributos de localgis asociados a una capa
     * 
     * @param idLayer
     *            El identificador de la capa
     * @return Una lista de objetos LocalgisAttribute
     */
    public List selectAttributesByLayer(Integer idLayer,Integer idMap);

    /**
     * Actualiza un atributo de localgis por clave primaria (por identificador)
     * 
     * @param record
     *            El atributo a actualizar, con el identificador previamente
     *            establecido
     * @return El número de registros actualizados
     */
    public int updateByPrimaryKey(LocalgisAttribute record);

    /**
     * Elimina un atributo de localgis por clave primaria
     * 
     * @param attributeid
     *            El identificador del atributo a eliminar
     * @return El numero de registros eliminados
     */
    public int deleteByPrimaryKey(Integer attributeid);
    
    /**
     * Devuelve los atributos restringidos de localgis asociados a una capa de geopista y a una configuracion publica o privada
     * 
     * @param idLayerGeopista
     *            El identificador de la capa de geopista
     * @param mapPublic
     *            Configuración publica o privada
     * @return Una lista de objetos LocalgisAttribute
     */
    public List selectRestrictedAttributesByIdLayerGeopistaAndMapPublic(Integer idLayerGeopista, Short mapPublic,Integer idEntidad);

    /**
     * Devuelve los atributos traducidos de localgis asociados a una capa y a una configuracion publica o privada
     * 
     * @param nameLayer
     *            El nombre de la capa
     * @param mapPublic
     *            Configuración publica o privada
     * @param locale
     *            Locales para realizar la traducción
     * @return Una lista de objetos LocalgisAttribute
     */
    public List selectAttributesTranslatedByNameLayerAndMapPublic(String nameLayer, Boolean mapPublic, String locale);

    public List selectAttributesTranslatedByIdLayerAndMapPublic(Integer idLayer, Integer idMap,Boolean mapPublic, String locale);

    
    /**
     * Devuelve los nombres de los posibles valores (dominios) asociados
     * a los valores de los atributos de localgis asociados a una capa y a una configuracion publica o privada
     * 
     * @param nameLayer
     *            El nombre de la capa
     * @param mapPublic
     *            Configuración publica o privada
     * @param locale
     *            Locales para realizar la traducción
     * @return Una lista de objetos LocalgisAttribute
     */
    public List selectAttributesValuesTranslatedByNameLayer(String nameLayer, String locale);


}