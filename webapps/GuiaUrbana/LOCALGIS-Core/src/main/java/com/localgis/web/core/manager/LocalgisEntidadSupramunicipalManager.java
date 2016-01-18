/**
 * LocalgisEntidadSupramunicipalManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.manager;

import java.util.List;

import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.model.GeopistaCoverageLayer;
import com.localgis.web.core.model.GeopistaEntidadSupramunicipal;

/**
 * Manager para la gestión de una entidad supramunicipal de Localgis
 * 
 * @author davidcaaveiro
 * 
 */
public interface LocalgisEntidadSupramunicipalManager {

    /**
     * Método que el contenido del css configurado para la entidad o null si
     * no existe
     * 
     * @param idEntidad
     *            Identificador de entidad
     * @return String contenido del css
     */
    public String getCSS(Integer idEntidad) throws LocalgisInvalidParameterException, LocalgisDBException;

    /**
     * Método para elminar el css configurado para la entidad
     * 
     * @param idEntidad
     *            Identificador de entidad
     * @return String contenido del css
     */
    public void removeCSS(Integer idEntidad) throws LocalgisInvalidParameterException, LocalgisDBException;

    /**
     * Método que actualiza el contenido css configurado para la entidad o
     * null si no existe
     * 
     * @param idEntidad
     *            Identificador de entidad
     * @param content
     *            contenido del css
     * 
     * @return int numero de filas acfectadas
     */
    public int updateCSS(Integer idEntidad, String content) throws LocalgisInvalidParameterException, LocalgisDBException;

    /**
     * Método que inserta el contenido css configurado para la entidad o null
     * si no existe
     * 
     * @param idEntidad
     *            Identificador de entidad
     * @param content
     *            contenido del css
     * 
     * @return int numero de filas acfectadas
     */
    public void insertCSS(Integer idEntidad, String content) throws LocalgisInvalidParameterException, LocalgisDBException;

    /**
     * Método que devuelve la coverage layer para una entidad determinada
     * 
     * @param idEntidad
     *            Identificador de entidad
     * @return La coverage layer asociada al municipio
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public GeopistaCoverageLayer getCoverageLayer(Integer idEntidad) throws LocalgisInvalidParameterException, LocalgisDBException;

    /**
     * Método que devuelve las capas de los mapas asociadas a una entidad
     * 
     * @param idMuncipio
     *            El identificador de entidad del que se desean obtener las
     *            capas
     * @return La lista de capas como objetos LocalgisLayer
     * @throws LocalgisDBException Si ocurre algún error con la base de datos.
     */
    public List getLayers(Integer idEntidad) throws LocalgisDBException;

    /**
     * Método para obtener una entidad supramunicipal a partir del id de entidad
     * @param idEntidad Identificador de entidad
     * @return La entidad supramunicipal
     * @throws LocalgisDBException Si ocurre algún error con la base de datos.
     */
    public GeopistaEntidadSupramunicipal getEntidadSupramunicipal(Integer idEntidad) throws LocalgisDBException;

    /**
     * Método para obtener todas las entidades supramunicipales
     * @return Una lista con las entidades supramunicipales
     * @throws LocalgisDBException Si ocurre algún error con la base de datos.
     */
    public List<GeopistaEntidadSupramunicipal> getAllEntidadesSupramunicipales() throws LocalgisDBException;
    
    /**
     * Método para obtener los municipios de una entidad determinada
     * @param idEntidad Identificador de entidad
     * @return La lista de municipios como una lista de objetos GeopistaMunicipio
     * @throws LocalgisDBException Si ocurre algún error con la base de datos.
     */
    public List getMunicipiosByIdEntidad(Integer idEntidad) throws LocalgisDBException;

    
    public List getNucleosMunicipio(Integer municipios) throws LocalgisDBException;
}
