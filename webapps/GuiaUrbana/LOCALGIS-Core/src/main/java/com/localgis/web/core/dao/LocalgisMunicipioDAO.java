/**
 * LocalgisMunicipioDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import com.ibatis.dao.client.Dao;
import com.localgis.web.core.model.BoundingBox;
import com.localgis.web.core.model.GeopistaMunicipio;

/**
 * DAO para manejar los municipios de la base de datos de localgis
 * 
 * @author albegarcia
 * 
 */
public interface LocalgisMunicipioDAO extends Dao {

    /**
     * Método que devuelve el bounding box de la geometria de un municipio en
     * una proyeccion determinada
     * 
     * @param idMunicipio
     *            Identificador del que se desea obtener el box3d de la
     *            geometría
     * @param srid
     *            Sistema de proyeccion en el que se desea obtener el bbox
     * @return Un El bbox de la geometría
     */
    public BoundingBox selectBoundingBoxByIdMunicipioAndSRID(Integer idMunicipio, Integer srid);

    /**
     * Método que devuelve el bounding box de la geometria de una entidad 
     * supramunicipal en una proyeccion determinada
     * 
     * @param idEntidad
     *            Identificador del que se desea obtener el box3d de la
     *            geometría
     * @param srid
     *            Sistema de proyeccion en el que se desea obtener el bbox
     * @return Un El bbox de la geometría
     */
    public BoundingBox selectBoundingBoxByIdEntidadAndSRID(Integer idEntidad, Integer srid);
    
    
    /**
     * Retorna un objeto municipio que contiene al punto pasado como parámetro
     * @param geometry Geometría formato String 
     * @return
     */
    public GeopistaMunicipio selectMunicipioByGeometry(String srid, String geometry);
    	
  
    

}