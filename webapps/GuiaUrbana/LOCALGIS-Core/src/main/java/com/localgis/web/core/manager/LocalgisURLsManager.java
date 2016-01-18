/**
 * LocalgisURLsManager.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.manager;

import com.localgis.web.core.exceptions.LocalgisConfigurationException;
import com.localgis.web.core.exceptions.LocalgisDBException;
import com.localgis.web.core.exceptions.LocalgisInvalidParameterException;
import com.localgis.web.core.exceptions.LocalgisMapNotFoundException;
import com.localgis.web.core.model.LocalgisMap;
import com.localgis.web.core.model.Scale;

/**
 * Manager para obtener URLs de Localgis
 * 
 * @author albegarcia
 *
 */
public interface LocalgisURLsManager {

    /**
     * Método que devuelve la URL que permite visualizar un mapa a partir de un
     * x e y de centrado, un ancho y un alto a una escala determinada
     * 
     * @param idMap
     *            El id del mapa
     * @param x
     *            La x de centrado
     * @param y
     *            La y de centrado
     * @param scale
     *            La escala
     * @param width
     *            El ancho
     * @param height
     *            El alto
     * @param imageName
     *            Nombre de la imagen a mostrar en pantalla para señalar el punto
     * @return Un array de String donde la primera posición contendrá la URL que
     *         permite visualizar el mapa y la segunda la URL de la guia urbana
     *         para visualizar el mapa
     * @throws LocalgisInvalidParameterException
     *             Si algun parametros es invalido
     * @throws LocalgisConfigurationException
     *             Si ocurre algun error con la configuracion
     * @throws LocalgisMapNotFoundException
     *             Si el mapa no existe en la base de datos
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public String[] getURLMapByXAndY (int idMap, double x, double y, Scale scale, int width, int height, String imageName) throws LocalgisInvalidParameterException, LocalgisConfigurationException, LocalgisMapNotFoundException, LocalgisDBException;
    
    /**
     * Método que devuelve la URL que permite visualizar un mapa a partir de un
     * x e y de centrado, un ancho y un alto a una escala determinada
     * 
     * @param idMap
     *            El id del mapa
     * @param x
     *            La x de centrado
     * @param y
     *            La y de centrado
     * @param scale
     *            La escala
     * @param width
     *            El ancho
     * @param height
     *            El alto
     * @return Un array de String donde la primera posición contendrá la URL que
     *         permite visualizar el mapa y la segunda la URL de la guia urbana
     *         para visualizar el mapa
     * @throws LocalgisInvalidParameterException
     *             Si algun parametros es invalido
     * @throws LocalgisConfigurationException
     *             Si ocurre algun error con la configuracion
     * @throws LocalgisMapNotFoundException
     *             Si el mapa no existe en la base de datos
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public String[] getURLMapByXAndY (int idMap, double x, double y, Scale scale, int width, int height) throws LocalgisInvalidParameterException, LocalgisConfigurationException, LocalgisMapNotFoundException, LocalgisDBException;

    /**
     * Método que devuelve la URL que permite visualizar un mapa a partir de un
     * numero de policía de centrado, un ancho y un alto a una escala
     * determinada
     * 
     * @param idMap
     *            El id del mapa
     * @param idNumeroPolicia
     *            Identificador del número de policía para centrar
     * @param y
     *            La y de centrado
     * @param scale
     *            La escala
     * @param width
     *            El ancho
     * @param height
     *            El alto
     * @return Un array de String donde la primera posición contendrá la URL que
     *         permite visualizar el mapa y la segunda la URL de la guia urbana
     *         para visualizar el mapa
     * @throws LocalgisInvalidParameterException
     *             Si algun parametros es invalido
     * @throws LocalgisConfigurationException
     *             Si ocurre algun error con la configuracion
     * @throws LocalgisMapNotFoundException
     *             Si el mapa no existe en la base de datos
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public String[] getURLMapByIdNumeroPolicia (int idMap, int idNumeroPolicia, Scale scale, int width, int height) throws LocalgisInvalidParameterException, LocalgisConfigurationException, LocalgisMapNotFoundException, LocalgisDBException;
    
    /**
     * Método que devuelve la URL que permite visualizar un mapa a partir de un
     * identificador de via de centrado, un ancho y un alto a una escala
     * determinada
     * 
     * @param idMap
     *            El id del mapa
     * @param idVia
     *            Identificador de la via para centrar
     * @param y
     *            La y de centrado
     * @param scale
     *            La escala
     * @param width
     *            El ancho
     * @param height
     *            El alto
     * @return Un array de String donde la primera posición contendrá la URL que
     *         permite visualizar el mapa y la segunda la URL de la guia urbana
     *         para visualizar el mapa
     * @throws LocalgisInvalidParameterException
     *             Si algun parametros es invalido
     * @throws LocalgisConfigurationException
     *             Si ocurre algun error con la configuracion
     * @throws LocalgisMapNotFoundException
     *             Si el mapa no existe en la base de datos
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public String[] getURLMapByIdVia (int idMap, int idVia, Scale scale, int width, int height) throws LocalgisInvalidParameterException, LocalgisConfigurationException, LocalgisMapNotFoundException, LocalgisDBException;
    
    /**
     * Método que devuelve la URL que permite visualizar un mapa a partir de una
     * referencia catastral de centrado, un ancho y un alto a una escala
     * determinada
     * 
     * @param idMap
     *            El id del mapa
     * @param referenciaCatastral
     *            Referencia catastral para centrar
     * @param y
     *            La y de centrado
     * @param scale
     *            La escala
     * @param width
     *            El ancho
     * @param height
     *            El alto
     * @return Un array de String donde la primera posición contendrá la URL que
     *         permite visualizar el mapa y la segunda la URL de la guia urbana
     *         para visualizar el mapa
     * @throws LocalgisInvalidParameterException
     *             Si algun parametros es invalido
     * @throws LocalgisConfigurationException
     *             Si ocurre algun error con la configuracion
     * @throws LocalgisMapNotFoundException
     *             Si el mapa no existe en la base de datos
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public String[] getURLMapByReferenciaCatastral (int idMap, String referenciaCatastral, Scale scale, int width, int height) throws LocalgisInvalidParameterException, LocalgisConfigurationException, LocalgisMapNotFoundException, LocalgisDBException;

    /**
     * Método que devuelve la URL que permite visualizar el mapa de informes de
     * forma generica centrado en un elemento determinado
     * 
     * @param idEntidad
     *            Identificador de la entidad
     * @param tableName
     *            Tabla que contiene los elementos por los que se quiere centrar
     * @param identifierColumnName
     *            Columna que contiene el identificador del elemento por el que
     *            se quiere centrar
     * @param identifierValue
     *            Valor del identificador por el que se desea centrar
     * @param mapId
     *            Identificador del mapa a mostrar
     * @param publicMap
     *            Indica si es mapa publico o privado
     * @param layers
     *            Capas del mapa a mostrar
     * @param style
     *            Estilo que se desea aplicar al mapa
     * @param scale
     *            Escala a la que se desea mostrar la imagen
     * @param width
     *            El ancho
     * @param height
     *            El alto
     * @return La URL que permite visualizar el mapa
     * @throws LocalgisInvalidParameterException
     *             Si algun parametros es invalido
     * @throws LocalgisConfigurationException
     *             Si ocurre algun error con la configuracion
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     */
    public String getURLReportMap (Integer idEntidad, String tableName, String identifierColumnName, Object identifierValue, LocalgisMap localgisMap, boolean publicMap, String layers, Scale scale, int width, int height) throws LocalgisInvalidParameterException, LocalgisConfigurationException, LocalgisDBException;

    /**
     * Método que devuelve la URL que permite visualizar el mapa de informes de
     * forma generica centrado en un elemento determinado
     * 
     * @param idEntidad
     *            Identificador de la entidad
     * @param tableName
     *            Tabla que contiene los elementos por los que se quiere centrar
     * @param identifierColumnName
     *            Columna que contiene el identificador del elemento por el que
     *            se quiere centrar
     * @param identifierValue
     *            Valor del identificador por el que se desea centrar
     * @param mapId
     *            Identificador del mapa a mostrar
     * @param publicMap
     *            Indica si es mapa publico o privado
     * @param layers
     *            Capas del mapa a mostrar
     * @param style
     *            Estilo que se desea aplicar al mapa. No se tiene en cuenta
     * @param scale
     *            Escala a la que se desea mostrar la imagen
     * @param width
     *            El ancho
     * @param height
     *            El alto
     * @return La URL que permite visualizar el mapa
     * @throws LocalgisInvalidParameterException
     *             Si algun parametros es invalido
     * @throws LocalgisConfigurationException
     *             Si ocurre algun error con la configuracion
     * @throws LocalgisDBException
     *             Si ocurre algun error con la base de datos
     * @deprecated Usar la otra alternativa de <code>getURLReportMap</code> en la que no se recibe el estilo
     */
    public String getURLReportMap (Integer idEntidad, String tableName, String identifierColumnName, Object identifierValue, LocalgisMap localgisMap, boolean publicMap, String layers, String style, Scale scale, int width, int height) throws LocalgisInvalidParameterException, LocalgisConfigurationException, LocalgisDBException;
}
