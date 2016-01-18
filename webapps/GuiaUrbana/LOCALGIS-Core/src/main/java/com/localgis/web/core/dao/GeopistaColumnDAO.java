/**
 * GeopistaColumnDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import java.util.List;

/**
 * DAO para manejar las columnas de la base de datos de Geopista
 * 
 * @author albegarcia
 * 
 */
public interface GeopistaColumnDAO {

    /**
     * Devuelve las columnas asociadas a una capa
     * 
     * @param idLayer
     *            Identificador de la capa
     * @return Una lista de objetos GeopistaColumn en las que el alias ira vacío.
     */
    public List selectColumnsByLayer(Integer idLayer);

    /**
     * Devuelve las columnas asociadas a una capa para unas locales determinadas
     * 
     * @param idLayer
     *            Identificador de la capa
     * @param locale
     *            Locales asociadas
     * @return Una lista de objetos GeopistaColumn en las que el alias ira
     *         traducido siempre que se pueda traducir. En otro caso ira a NULL.
     */
    public List selectColumnsByLayerTranslated(Integer idLayer, String locale);

    /**
     * Devuelve las columnas asociadas a una capa para todos los idiomas disponibles
     * 
     * @param idLayer
     *            Identificador de la capa
     * @return Una lista de objetos GeopistaColumn
     */
    public List selectColumnsByLayerTranslatedAllLanguages(Integer idLayer);

}