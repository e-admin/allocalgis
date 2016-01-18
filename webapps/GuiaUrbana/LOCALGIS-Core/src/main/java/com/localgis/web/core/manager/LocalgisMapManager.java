/**
 * LocalgisMapManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.exceptions.LocalgisMapNotFoundException;
import com.localgis.web.core.model.GeopistaMunicipio;
import com.localgis.web.core.model.LocalgisLayer;
import com.localgis.web.core.model.LocalgisLayerExt;
import com.localgis.web.core.model.LocalgisMap;

/**
 * Manager para la gestion de un mapa de Localgis
 * 
 * @author albegarcia
 * 
 */
public interface LocalgisMapManager {

    /**
     * Constantes que indican el tipo de peticion
     */
    public static final int GET_MAP_REQUESTS = 0;
    public static final int GET_FEATURE_INFO_REQUESTS = 1;
    public static final int GET_LEGEND_GRAPHICS_REQUESTS = 2;
    public static final int ALL_REQUESTS = 3;

    /**
     * Devuelve un identificador de mapa dado un identificador de geopista y un
     * identificador de entidad supramunicipal.
     * 
     * @param idEntidad
     *            Identificador de la entidad
     * @param idGeopistaMap
     *            Identificador del mapa en Geopista
     * @param publicMap
     *            Si el mapa es publico o privado
     * 
     * @return IdMap de Localgis
     * @throws LocalgisInvalidParameterException
     *             Si los parametros son invalidos
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public Integer getIdMapByIdGeopista(Integer idGeopistaMap, Integer idEntidad, Boolean publicMap) throws LocalgisInvalidParameterException, LocalgisMapNotFoundException, LocalgisDBException;

    /**
     * Devuelve un mapa publicado a partir del identificador de mapa
     * 
     * @param idMap
     *            Identificador de mapa a obtener
     * @return Una colección de los mapas publicadas en el servidor de mapas
     * @throws LocalgisInvalidParameterException
     *             Si los parametros son invalidos
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public LocalgisMap getPublishedMap(Integer idMap) throws LocalgisInvalidParameterException, LocalgisDBException;

    /**
     * Método que devuelve las capas de un mapa determinado
     * 
     * @param idMap
     *            El identificador del mapa del que se desean obtener las capas
     * @return La lista de capas como objetos LocalgisLayerExt
     */
    public List getMapLayers(Integer idMap) throws LocalgisDBException;

    /**
     * Método que devuelve las capas de un mapa determinado
     * 
     * @param idMap
     *            El identificador del mapa del que se desean obtener las capas
     * @param locale
     *            Locales a utilizar
     * @return La lista de capas como objetos LocalgisLayerExt
     */
    public List getMapLayers(Integer idMap, String locale) throws LocalgisDBException;

    /**
     * Método que devuelve las capas de un mapa determinado
     * 
     * @param idMapGeopista
     *            El identificador del mapa del que se desean obtener las capas
     * @return La lista de capas como objetos LocalgisLayerExt
     */
    public List getMapLayersByIdGeopista(Integer idMapGeopista) throws LocalgisDBException;

    /**
     * Método que devuelve las capas de un mapa determinado
     * 
     * @param idMapGeopista
     *            El identificador del mapa del que se desean obtener las capas
     * @param locale
     *            Locales a utilizar
     * @return La lista de capas como objetos LocalgisLayerExt
     */
    public List getMapLayersByIdGeopista(Integer idMapGeopista, String locale) throws LocalgisDBException;

    /**
     * Método que devuelve las capas de un mapa determinado
     * 
     * @param idMapGeopista
     *            El identificador del mapa del que se desean obtener las capas
     * @return La lista de capas como objetos LocalgisLayerExt
     */
    public List getMapLayersByIdGeopistaAndEntidad(Integer idMapGeopista,Integer idEntidad) throws LocalgisDBException;

    /**
     * Igual que el anterior pero devuelve solo las capas de un tipo determinado (Privadas o publicas)
     * @param idMapGeopista
     * @param idEntidad
     * @return
     * @throws LocalgisDBException
     */
    public List getMapLayersByIdGeopistaAndEntidad(Integer idMapGeopista,Integer idEntidad,int tipoPublicacion) throws LocalgisDBException;

    
    /**
     * Método para añadir una marca de posición
     * 
     * @param idMap
     *            El id del mapa asociado a la marca
     * @param username
     *            El usuario asociado a la marca
     * @param x
     *            La posición x asociada a la marca
     * @param y
     *            La posición y asociada a la marca
     * @param scale
     *            La escala asociada a la marca
     * @param markname
     *            El nombre de la marca
     * @param marktext
     *            El texto (opcional) de la marca
     * @return El identificador de la marca de posición creada
     * @throws LocalgisInvalidParameterException
     *             Si algún parametro es inválido
     * @throws LocalgisDBException
     *             Si ocurre algún error con la base de datos
     */
    public Integer addMarker(Integer idMap, String username, Double x, Double y, Double scale, String markname, String marktext) throws LocalgisInvalidParameterException, LocalgisDBException;

    public Integer addIncidencia(String username, Integer idMap, String layerName, Integer idFeature, String tipoIncidencia, String gravedadIncidencia, String emailContacto, String descripcion, Double x, Double y, Integer idMunicipio) throws LocalgisInvalidParameterException, LocalgisDBException;

    public String getDomain(String domainName,Integer idMunicipio); 

    /**
     * Método para modificar una marca de posición
     * 
     * @param markerId
     *            El id de la marca a actualizar
     * @param username
     *            El usuario asociado a la marca
     * @param markname
     *            El nombre de la marca
     * @param marktext
     *            El texto (opcional) de la marca
     * @throws LocalgisInvalidParameterException
     *             Si algún parametro es inválido
     * @throws LocalgisDBException
     *             Si ocurre algún error con la base de datos
     */
    public void updateMarker(Integer markerId, String username, String markname, String marktext) throws LocalgisInvalidParameterException, LocalgisDBException;

    /**
     * Método para eliminar una marca de posición
     * 
     * @param markerId
     *            El id de la marca a actualizar
     * @param username
     *            El usuario asociado a la marca
     * @throws LocalgisInvalidParameterException
     *             Si algún parametro es inválido
     * @throws LocalgisDBException
     *             Si ocurre algún error con la base de datos
     */
    public void deleteMarker(Integer markerId, String username) throws LocalgisInvalidParameterException, LocalgisDBException;

    /**
     * Método para consultar las marcas de posición para un usuario y un mapa
     * determinado
     * 
     * @param idMap
     *            El id del mapa asociado a la marca
     * @param username
     *            El usuario asociado a la marca
     * @return Una lista de objetos LocalgisMarker
     * @throws LocalgisInvalidParameterException
     *             Si algún parametro es inválido
     * @throws LocalgisDBException
     *             Si ocurre algún error con la base de datos
     */
    public List getMarkers(Integer idMap, String username) throws LocalgisInvalidParameterException, LocalgisDBException;

    /**
     * Método que devuelve las capas de servidores WMS externas de un mapa
     * determinado
     * 
     * @param idMap
     *            El identificador del mapa del que se desean obtener las capas
     * @return La lista de capas como objetos LocalgisMapServerLayer
     */
    public List getMapWMSLayers(Integer idMap) throws LocalgisDBException;

    /**
     * Método para obtener la URL del servidor de mapas para una entidad
     * supramunicipal, un mapa y una configuracion publica o privada determinada
     * y que sirva para un determinado tipo de peticiones
     * 
     * @param idEntidad
     *            Identificador de la entidad
     * @param idMap
     *            Identificador del mapa
     * @param publicMap
     *            Configuración pública o privada
     * @return La URL del servidor
     * @throws LocalgisInvalidParameterException
     *             Si algún parametro es inválido
     * @throws LocalgisConfigurationException
     *             Si ocurre algún error de configuración
     */
    public String getMapServerURL(Integer idEntidad, Integer idMap, Boolean publicMap, int requestType) throws LocalgisInvalidParameterException, LocalgisConfigurationException;
    
    /* Devuelve la URL Interna del mapserver que se utiliza para el GetfeatureOnfo*/
    public String getMapServerURLInternal(Integer idEntidad, Integer idMap, Boolean publicMap, int requestType) throws LocalgisInvalidParameterException, LocalgisConfigurationException;

    
    public List<String> getGeometryFromLayer(String layer, String municipio) throws LocalgisDBException;

    
    public String getLayerIdFromIdGuiaUrbana(String layerId);

	public LocalgisLayerExt getLayerById(Integer IdLayer,Integer idMap,String locale);

	public LocalgisLayer getLayerByName(String layername);

	public List<HashMap<String, Object>> selectPublicAnexosByIdLayerAndIdFeature(
			int idLayer, int idFeature);

	public List<HashMap<String, Object>> selectAllAnexosByIdLayerAndIdFeature(
			int idLayer, int idFeature);

	public GeopistaMunicipio selectMunicipioByGeometry(String srid,
			String geometrySelect);

	public List<HashMap<String,Object>> getLayersInArea(ArrayList listaCompletaCapas,String srid,String bbox);
    
    

}
