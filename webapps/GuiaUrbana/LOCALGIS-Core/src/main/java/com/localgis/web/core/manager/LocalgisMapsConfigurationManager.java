/**
 * LocalgisMapsConfigurationManager.java
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
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.exceptions.LocalgisWMSException;
import com.localgis.web.core.model.GeopistaMap;
import com.localgis.web.core.model.LocalgisMap;

/**
 * Manager para la configuración de mapas de Localgis
 * 
 * @author albegarcia
 * 
 */
public interface LocalgisMapsConfigurationManager {

    /**
     * Devuelve una coleccion de las entidades supramunicipales disponibles 
     * 
     * @return Una lista de las entidades disponibles (Objetos
     *         GeopistaEntidadSupramunicipal)
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public List getEntidadesSupramunicipales() throws LocalgisDBException;

    /**
     * Devuelve una coleccion de los mapas disponibles en Geopista para una
     * entidad determinada
     * 
     * @param idEntidad
     *            Identificador de la entidad
     * @return Una lista de los mapas disponibles en Geopista (Objetos
     *         GeopistaMap)
     * @throws LocalgisInvalidParameterException
     *             Si los parametros son invalidos
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public List getAvailableMaps(Integer idEntidad) throws LocalgisInvalidParameterException, LocalgisDBException;

    /**
     * Devuelve un mapa especifico
     * 
     * @param idEntidad
     *            Identificador de la entidad
     * @param idMap
     *            Identificador del mapa
     * @return El mapa asociado
     * @throws LocalgisInvalidParameterException
     *             Si los parametros son invalidos
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public GeopistaMap getAvailableMap(Integer idEntidad, Integer idMap) throws LocalgisInvalidParameterException, LocalgisDBException;

    /**
     * Devuelve una coleccion de los mapas publicados en el servidor de mapas
     * 
     * @param idEntidad
     *            Identificador de la entidad
     * @return Una colección de los mapas publicadas en el servidor de mapas
     * @throws LocalgisInvalidParameterException
     *             Si los parametros son invalidos
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public List getPublishedMaps(Integer idEntidad) throws LocalgisInvalidParameterException, LocalgisDBException;

    /**
     * Devuelve una coleccion de los mapas publicados en el servidor de mapas
     * 
     * @param idEntidad
     *            Identificador de la entidad
     * @param mapasPublicos
     *            Si se desean obtener los mapas que publicos (valor true) o
     *            privados (valor false)
     * @return Una colección de los mapas publicadas en el servidor de mapas
     * @throws LocalgisInvalidParameterException
     *             Si los parametros son invalidos
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public List getPublishedMaps(Integer idEntidad, Boolean mapasPublicos) throws LocalgisInvalidParameterException, LocalgisDBException;

    /**
     * Método para publicar y despublicar simultaneamente un conjunto de mapas
     * del servidor de mapas
     * 
     * @param mapsToPublish
     *            Mapas a publicar
     * @param mapsToRemove
     *            Mapas a despublicar
     * @param mapsToRepublish
     *            Mapas a republicar
     * @param idEntidad
     *            Entidad en el que se desean publicar/despublicar
     * @param publicMaps
     *            Si se desea que los mapas sean publicos o privados
     * @param idMapGeopistaDefault
     *            Identificador del mapa de geopista que se desea establecer por
     *            defecto
     * @throws LocalgisConfigurationException
     *             Si ocurre algun error con la configuracion
     * @throws LocalgisInvalidParameterException
     *             Si los parametros son invalidos
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     * @throws LocalgisWMSException
     *             Si ocurre algun error con el servidor de mapas
     */
    public void publishAndRemoveMapsFromWMSServer(GeopistaMap[] mapsToPublish, LocalgisMap[] mapsToRemove, LocalgisMap[] mapsToRepublish, Integer idEntidad, Boolean publicMaps,
            Integer idMapGeopistaDefault,boolean publicarTodasEntidades) throws LocalgisWMSException, LocalgisConfigurationException, LocalgisInvalidParameterException, LocalgisDBException;

    /**
     * Método para publicar un mapa en el servidor de mapas
     * 
     * @param geopistaMap
     *            El mapa a publicar
     * @param idEntidad
     *            El identificador de la entidad en la que se publica el mapa
     * @param publicMap
     *            Si el mapa se publica de forma publica o no
     * @throws LocalgisConfigurationException
     *             Si ocurre algun error con la configuracion
     * @throws LocalgisInvalidParameterException
     *             Si los parametros son invalidos
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     * @throws LocalgisWMSException
     *             Si ocurre algun error con el servidor de mapas
     */
    public void addMapToWMSServer(GeopistaMap geopistaMap, Integer idEntidad, Boolean publicMap) throws LocalgisConfigurationException, LocalgisInvalidParameterException, LocalgisDBException,
            LocalgisWMSException;

    /**
     * Método para obtener el mapa por defecto para una entidad y una
     * configuración (publica o privada) determinada
     * 
     * @param idEntidad
     *            El identificador de la entidad
     * @param mapPublic
     *            La configuracion publica o privada
     * @return El mapa por defecto
     * @throws LocalgisInvalidParameterException
     *             Si los parametros son invalidos
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public LocalgisMap getDefaultMap(Integer idEntidad, Boolean mapPublic) throws LocalgisInvalidParameterException, LocalgisDBException;

    /**
     * Método para reconfigurar el servidor de mapas a partir del contenido de
     * la base de datos
     * 
     * @throws LocalgisConfigurationException
     *             Si ocurre algun error con la configuracion
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     * @throws LocalgisWMSException
     *             Si ocurre algun error con el servidor de mapas
     */
    public void reconfigureWMSServer() throws LocalgisConfigurationException, LocalgisDBException, LocalgisWMSException;

    /**
     * Método para reconfigurar el servidor de mapas a partir del contenido de
     * la base de datos para una entidad y una configuracion, publica o
     * privada, determinada
     * 
     * @param idEntidad
     *            Identificador de la entidad a configurar
     * @param publicMaps
     *            Si se desea reconfigurar para la configuracion publica o
     *            privada
     * @throws LocalgisInvalidParameterException
     *             Si los parametros son invalidos
     * @throws LocalgisConfigurationException
     *             Si ocurre algun error con la configuracion
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     * @throws LocalgisWMSException
     *             Si ocurre algun error con el servidor de mapas
     */
    public void reconfigureWMSServer(Integer idEntidad, Boolean publicMaps) throws LocalgisInvalidParameterException, LocalgisConfigurationException, LocalgisDBException, LocalgisWMSException;

    
    /**
     * Obtiene la lista de entidades que tienen publicado este mapa
     * @param mapidgeopista
     * @return 
     */
	public List getEntidadesPublicadas(Integer mapidgeopista,Boolean publicMap) throws LocalgisInvalidParameterException, LocalgisDBException;

	public GeopistaMap selectMapByName(String nombreMapa, Integer idMunicipio);

	public String getCategoryByLayerName(String layerName);

}
