/**
 * WMSConfigurator.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.wms;

import java.util.List;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.exceptions.LocalgisWMSException;
import com.localgis.web.core.model.BoundingBox;
import com.localgis.web.core.model.GeopistaEntidadSupramunicipal;
import com.localgis.web.core.model.LocalgisLayer;
import com.localgis.web.core.model.LocalgisMap;

/**
 * Interfaz que deben de cumplir los configuradores de mapas
 * 
 * @author albegarcia
 * 
 */
public interface WMSConfigurator {

    /**
     * Constantes que indican el tipo de conexion (postgis u oraclespatial
     */
    public static final int ORACLESPATIAL_CONNECTION = 0;

    public static final int POSTGIS_CONNECTION = 1;

    /**
     * Constantes que indican el tipo de operacion
     */
    public static final int SHOW_MAP_OPERATION = 0;

    public static final int PRINT_MAP_OPERATION = 1;

    public static final int SAVE_MAP_OPERATION = 2;

    /**
     * Constantes que indican el tipo de peticion
     */
    public static final int GET_MAP_REQUESTS = 0;

    public static final int GET_FEATURE_INFO_REQUESTS = 1;

    public static final int GET_LEGEND_GRAPHICS_REQUESTS = 2;

    public static final int ALL_REQUESTS = 3;

    /**
     * Método para configurar el servidor de mapas para una entidad supramunicipal y una
     * configuracion (publica o privada) determinada
     * 
     * @param geopistaEntidadSupramunicipal
     *            Entidad para la que se desea configurar el servidor de mapas
     * @param publicConfiguration
     *            Si se desea configurar para los mapas publicos (true) o
     *            privados (false)
     * @throws LocalgisWMSException
     *             Si sucede algun error con el servidor WMS
     * @throws LocalgisConfigurationException
     *             Si sucede algun error de configuracion
     */
    public void configureWMSServer(GeopistaEntidadSupramunicipal geopistaEntidadSupramunicipal, Boolean publicConfiguration) throws LocalgisWMSException, LocalgisConfigurationException;

    /**
     * Devuelve el alias de la geometría
     * 
     * @return El alias de la geometría
     */
    public String getAliasGeometry();

    /**
     * Devuelve el alias del identificador
     * 
     * @return El alias del identificador
     */
    public String getAliasId();

    /**
     * Devuelve el nombre interno manejado por el servidor de mapas para una
     * capa de un mapa determinado
     * 
     * @param localgisMap
     *            Mapa al que pertenece la capa
     * @param localgisLayer
     *            Capa cuyo nombre interno se desea saber
     * @param operationType
     *            Tipo de operacion
     * @return El nombre interno asociado a la capa del mapa
     */
    public String getInternalNameGenericLayer(LocalgisMap localgisMap, LocalgisLayer localgisLayer, int operationType);

    /**
     * Devuelve el nombre interno manejado por el servidor de mapas para la capa
     * de ortofotos de un mapa determinado
     * 
     * @param localgisMap
     *            Mapa al que pertenece la capa
     * @param operationType
     *            Tipo de operacion
     * @return El nombre interno asociado a la capa de ortofotos del mapa
     * @throws LocalgisConfigurationException
     *             Si ocurre algún error de configuracion
     */
    public String getInternalNameOrtofotoLayer(LocalgisMap localgisMap, int operationType) throws LocalgisConfigurationException;

    /**
     * Devuelve el nombre interno manejado por el servidor de mapas para la capa
     * de municipios y provincias de un mapa determinado
     * 
     * @param localgisMap
     *            Mapa al que pertenece la capa
     * @param showMunicipios
     *            Si se desean mostrar los municipios
     * @param operationType
     *            Tipo de operacion
     * @return El nombre interno asociado a la capa de municipios y provincias
     *         del mapa
     * @throws LocalgisConfigurationException
     *             Si ocurre algún error de configuracion
     */
    public String getInternalNameMunicipiosAndProvinciasLayer(LocalgisMap localgisMap, Boolean showMunicipios, int operationType) throws LocalgisConfigurationException;

    /**
     * Devuelve el nombre interno manejado por el servidor de mapas para la capa
     * de vista general (la que muestra todas las capas del mapa)
     * 
     * @param localgisMap
     *            Mapa al pertenece la capa
     * @param layers
     *            Capas del mapa como una lista de objetos LocalgisMap
     * @param showMunicipios
     *            Si se desea mostrar la capa de municipios o no
     * @param operationType
     *            Tipo de operacion
     * @return El nombre interno asociado a la capa de vista general
     * @throws LocalgisConfigurationException
     *             Si ocurre algún error de configuracion
     */
    public String getInternalNameOverviewLayer(LocalgisMap localgisMap, List layers, Boolean showMunicipios, int operationType) throws LocalgisConfigurationException;

    /**
     * Devuelve la URL del servidor de mapas para una entidad supramunicipal, un mapa y una
     * configuracion publica o privada determinada y que sirva para un
     * determinado tipo de peticiones
     * 
     * @param idEntidad
     *            Identificador de la entidad
     * @param idMap
     *            Identificador del mapa
     * @param publicMap
     *            Configuración pública o privada
     * @param requestType
     *            Tipo de peticion
     * @return La URL del servidor
     * @throws LocalgisInvalidParameterException
     *             Si algún parametro es inválido
     * @throws LocalgisConfigurationException
     *             Si ocurre algún error de configuración
     */
    public String getMapServerURL(Integer idEntidad, Integer idMap, Boolean publicMap, int requestType) throws LocalgisInvalidParameterException, LocalgisConfigurationException;

    
    public String getMapServerURLInternal(Integer idEntidad, Integer idMap, Boolean publicMap, int requestType) throws LocalgisInvalidParameterException, LocalgisConfigurationException;

    /**
     * Método que devuelve el BBOX de España para un determinado SRID
     * @param srid El SRID
     * @return EL Bounding box de España
     * @throws LocalgisConfigurationException Si ocurre algun error con la configuracion
     */
    public BoundingBox getBoundingBoxSpain(String srid) throws LocalgisConfigurationException;

    
    /**
     * Metood que devuelve el BBOX de la comunidad identificada
     * @param srid
     * @return
     * @throws LocalgisConfigurationException
     */
    public BoundingBox getBoundingBoxComunidad(String srid) throws LocalgisConfigurationException;

}
