/**
 * LocalgisMapDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import java.util.List;

import com.ibatis.dao.client.Dao;
import com.localgis.web.core.model.LocalgisMap;

/**
 * DAO para manejar los mapas publicados en el servidor de mapas almacenados en
 * la base de datos de Localgis
 * 
 * @author albegarcia
 * 
 */
public interface LocalgisMapDAO extends Dao {

    /**
     * Método para insertar un mapa
     * 
     * @param record
     *            El mapa a insertar
     * @return El identificador del mapa insertado
     */
    public Integer insert(LocalgisMap record);

    /**
     * Método para obtener los mapas publicados para una entidad determinada.
     * 
     * @param idEntidad
     *            Identificaodr de la entidad
     * @param locale
     *            Locales para hacer la consulta
     * @return La lista de mapas publicados como objetos LocalgisMap
     */
    public List selectMapsByIdEntidad(Integer idEntidad, String locale);

    /**
     * Método para obtener los mapas publicados para una entidad determinado.
     * Mediante el parametro mapasPublicos se indica si se desea obtener los
     * mapas publicos o los privados
     * 
     * @param idEntidad
     *            Identificador de la entidad
     * @param mapasPublicos
     *            Si se desean obtener los mapas publicos (valor
     *            <code>true</code>) o privados (valor <code>false</code>)
     * @param locale
     *            Locales para hacer la consulta
     * @return La lista de mapas publicados como objetos LocalgisMap
     */
    public List selectMapsByIdEntidad(Integer idEntidad, Boolean mapasPublicos, String locale);

    /**
     * Devuelve un mapa publicado a partir del identificador de mapa
     * 
     * @param idMap Identificador del mapa
     * @param locale Locales para hacer la consulta
     * @return El mapa publicado
     */
    public LocalgisMap selectMapById(Integer idMap, String locale);

    /**
     * Método para eliminar un mapa por su identificador y por una configuracion publica o privada
     * 
     * @param idMap
     *            El identificador del mapa a eliminar
     * @param publicMap
     *            Si se desea borrar un mapa publico o privado
     * @return El numero de mapas eliminados
     */
    public int deleteByIdMapAndPublicMap(Integer idMap, Boolean publicMap);

    /**
     * Método establecer un mapa por defecto para una configuracion publica o
     * privada y para una entidad
     * 
     * @param idGeopista
     *            El identificador del mapa de geopista que se desea establecer
     *            por defecto
     * @param publicMap
     *            Si estamos configurando el mapa por defecto para una
     *            configuracion publica o privada
     * @param idEntidad
     *            Identificador de la entidad para la que se desea configurar el
     *            mapa por defecto
     * @return El número de mapas actualizados
     */
    public int setDefaultMap(Integer idGeopista, Boolean publicMap, Integer idEntidad);

    /**
     * Método para obtener el mapa por defecto para una configuracion publica o
     * privada y para una entidad
     * 
     * @param publicMap
     *            Si estamos configurando el mapa por defecto para una
     *            configuracion publica o privada
     * @param idEntidad
     *            Identificador de la entidad para la que se desea configurar el
     *            mapa por defecto
     * @param locale
     *            Locales para hacer la consulta
     * @return El mapa por defecto
     */
    public LocalgisMap getDefaltMap(Boolean publicMap, Integer idEntidad, String locale);

    /**
     * Devuelve un identificador de mapa de localgis a partir de un
     * identificador de mapa de Geopista y un id de entidad
     * 
     * @param idMap Identificador del mapa
     * @param Integer idEntidad
     * @return El mapa publicado
     */
    public Integer getIdMapByIdGeopista(Integer idGeopistaMap,Integer idEntidad,String locale, Boolean publicMap);

    /**
     * Método para actualizar un mapa por id
     * 
     * @param localgisMap
     *            El mapa a actualizar
     * @return El numero de mapas actualizados
     */
    public int updateMapByIdMap(LocalgisMap localgisMap);

    
    /**
     * Metodo para obtener las entidades que tienen publidado un mapa
     * @param mapidgeopista
     * @return
     */
    public List getEntidadesPublicadas(Integer mapidgeopista, Boolean publicMap);
}