/**
 * LocalgisLayerManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.manager;

import java.util.List;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.model.LocalgisLayer;
import com.localgis.web.core.model.LocalgisLegend;
import com.localgis.web.core.model.LocalgisLegendKey;
import com.localgis.web.core.model.LocalgisMap;

/**
 * Manager para la gestion de una capa de Localgis
 * 
 * @author albegarcia
 * 
 */
public interface LocalgisLayerManager {

    /**
     * Constantes que indican el tipo de operacion
     */
    public static final int SHOW_MAP_OPERATION = 0;
    public static final int PRINT_MAP_OPERATION = 1;
    public static final int SAVE_MAP_OPERATION = 2;

    /**
     * Añade un atributo restringido a una capa
     * 
     * @param idLayer
     *            Identificador de la capa
     * @param idAttribute
     *            Identificador del atributo
     * @param idMunicipio
     *            Identificador del municipio
     * @param idAlias
     *            Identificador del alias
     * @param mapPublic
     *            Configuración pública o privada
     * @throws LocalgisDBException
     *             Si ocurre algún error con la base de datos
     */
    public void addRestrictedAttributesLayer(Integer idLayer, Integer idAttribute, Integer idMunicipio, Integer idAlias, Boolean mapPublic) throws LocalgisDBException;

    /**
     * Elimina un atributo restringido de una capa
     * 
     * @param idLayer
     *            Identificador de la capa
     * @param idAttribute
     *            Identificador del atributo
     * @param idMunicipio
     *            Identificador del municipio
     * @param mapPublic
     *            Configuración pública o privada
     * @throws LocalgisDBException
     *             Si ocurre algún error con la base de datos
     */
    public void removeRestrictedAttributesLayer(Integer idLayer, Integer idAttribute, Integer idMunicipio, Boolean mapPublic) throws LocalgisDBException;

    /**
     * Devuelve los atributos restringidos de una capa
     * 
     * @param idLayer
     *            Identificador de la capa
     * @param idAttribute
     *            Identificador del atributo
     * @param idEntidad
     *            Identificador de la entidad supramunicipal
     * @param mapPublic
     *            Configuración pública o privada
     * @return Una lista de objetos LocalgisRestrictedAttribute
     * @throws LocalgisDBException
     *             Si ocurre algún error con la base de datos
     */
    public List getRestrictedAttributesLayer(Integer idLayer, Integer idEntidad, String locale, Boolean mapPublic) throws LocalgisDBException;

    /**
     * Devuelve los atributos de una capa determinada para unas locales
     * determinadas
     * 
     * @param idLayer
     *            Id de la capa de la que se desean obtener los atributos
     * @param locale
     *            Locales con las que se quiere traducir
     * @return Los atributos de la capa como objetos GeopistaColumn
     */
    public List getColumnsLayer(Integer idLayer, String locale) throws LocalgisDBException;

    /**
     * Método para añadir una leyenda a una capa determinada para una entidad y
     * una configuración pública o privada
     * 
     * @param idLayer
     *            Id de la capa
     * @param idEntidad
     *            Id de la entidad
     * @param mapPublic
     *            Configuracion pública o privada
     * @param img
     *            Imagen
     * @throws LocalgisDBException
     *             Si ocurre algún error con la base de datos
     */
    public void addLegend(Integer idLayer, Integer idEntidad, Boolean mapPublic, byte[] img) throws LocalgisDBException;

    /**
     * Método para obtener una leyendea
     * 
     * @param legendKey
     *            Clave de la leyenda
     * @return La leyenda
     * @throws LocalgisDBException
     *             Si ocurre algún error con la base de datos
     */
    public LocalgisLegend getLegend(LocalgisLegendKey legendKey) throws LocalgisDBException;

    /**
     * Método para actualizar una leyenda
     * 
     * @param legendKey
     *            Clave de la leyenda
     * @param fileData
     *            Nueva imagen de la leyenda
     * @throws LocalgisDBException
     *             Si ocurre algún error con la base de datos
     */
    public void updateLegend(LocalgisLegendKey legendKey, byte[] fileData) throws LocalgisDBException;

    /**
     * Método para actualizar una leyenda
     * 
     * @param legendKey
     *            Clave de la leyenda
     * @param fileData
     *            Nueva imagen de la leyenda
     * @throws LocalgisDBException
     *             Si ocurre algún error con la base de datos
     */
    public void removeLegend(LocalgisLegendKey legendKey) throws LocalgisDBException;

    /**
     * Devuelve los atributos de una capa a partir del nombre de la capa, de la
     * configuracion publica o privada del mapa a la que pertenece la capa
     * 
     * @param nameLayer
     *            Nombre de la capa
     * @param mapPublic
     *            Configuracion (publica o privada) del mapa a la que pertence
     *            la capa
     * @return Una lista de atributos como objetos LocalgisAttributeTranslated
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public List getAttributesLayer(String nameLayer, Boolean mapPublic) throws LocalgisDBException;

    /**
     * Devuelve los atributos de una capa a partir del nombre de la capa, de la
     * configuracion publica o privada del mapa a la que pertenece la capa y de
     * las locales
     * 
     * @param nameLayer
     *            Nombre de la capa
     * @param mapPublic
     *            Configuracion (publica o privada) del mapa a la que pertence
     *            la capa
     * @param locale
     *            Locales para traducir
     * @return Una lista de atributos como objetos LocalgisAttributeTranslated
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public List getAttributesLayer(String nameLayer, Boolean mapPublic, String locale) throws LocalgisDBException;

    public List getAttributesLayerByIdLayer(Integer idLayer, Integer idMap,Boolean mapPublic, String locale) throws LocalgisDBException;

    
    /**
     * Devuelve el nombre interno de una capa de un mapa determinado
     * @param localgisMap Mapa al que pertenece la capa
     * @param localgisLayer Capa de la que se desea saber el nombre interno
     * @param operationType Tipo de operacion
     * @return El nombre interno de la capa
     */
    public String getInternalNameGenericLayer(LocalgisMap localgisMap, LocalgisLayer localgisLayer, int operationType);

    /**
     * Devuelve el nombre interno de la capa de ortofotos de un mapa determinado
     * @param localgisMap Mapa al que pertenece la capa
     * @return El nombre interno de la capa
     * @param operationType Tipo de operacion
     * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
     */
    public String getInternalNameLayerOrtofoto(LocalgisMap localgisMap, int operationType) throws LocalgisConfigurationException;
    
    /**
     * Devuelve el nombre interno de la capa de municipios y provincias de un mapa determinado
     * @param localgisMap Mapa al que pertenece la capa
     * @param showMunicipios Si se desean mostrar los municipios
     * @return El nombre interno de la capa
     * @param operationType Tipo de operacion
     * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
     */
    public String getInternalNameLayerMunicipiosAndProvincias(LocalgisMap localgisMap, Boolean showMunicipios, int operationType) throws LocalgisConfigurationException;

    /**
     * Devuelve el nombre interno de la capa que permite mostrar la vision general de un mapa
     * @param localgisMap El mapa al que pertence la capa
     * @param layers La lista de capas que se quieren mostrar
     * @param showMunicipios Si se desean mostrar los municipios
     * @param operationType Tipo de operacion
     * @return El nombre interno de la capa
     * @throws LocalgisConfigurationException Si ocurre algun error de configuracion
     */
    public String getInternalNameLayerOverview(LocalgisMap localgisMap, List layers, Boolean showMunicipios, int operationType) throws LocalgisConfigurationException;

    
    /**
     * Devuelve las traducciones de los valores de los atributos
     * 
     * @param nameLayer
     *            Nombre de la capa
     * @param locale
     *            Locales para traducir
     * @return Una lista de atributos como objetos LocalgisAttributeValueTranslated
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public List getAttributesValuesLayer(String nameLayer,  String locale) throws LocalgisDBException;
    
}
