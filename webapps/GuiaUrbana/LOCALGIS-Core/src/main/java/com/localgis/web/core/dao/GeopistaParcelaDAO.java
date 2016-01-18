/**
 * GeopistaParcelaDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao;

import com.ibatis.dao.client.Dao;
import com.localgis.web.core.model.BoundingBox;
import com.localgis.web.core.model.GeopistaParcela;

/**
 * DAO para manejar las parcelas de la base de datos de Geopista
 * 
 * @author albegarcia
 * 
 */
public interface GeopistaParcelaDAO extends Dao {

    /**
     * Método que devuelve una parcela a partir de su referencia catastral
     * 
     * @param referenciaCatastral Referencia catastral de la parcela
     * 
     * @return Parcela deGeopista
     *         <code>com.geopista.app.guiaurbana.api.model.GeopistaParcela</code>
     */
    public GeopistaParcela selectParcelaByReferenciaCatastral(String referenciaCatastral, Integer srid);
    
    /**
     * Método que devuelve el bounding box de la geometria de una parcela
     * 
     * 
     * @param referenciaCatastral
     *            Referencia catastral de la parcela de la que se desea obtener
     *            el bunding box de la geometría
     * @return Un El bbox de la geometría de la parcela
     */
    public BoundingBox selectBoundingBoxByReferenciaCatastral(String referenciaCatastral);
}