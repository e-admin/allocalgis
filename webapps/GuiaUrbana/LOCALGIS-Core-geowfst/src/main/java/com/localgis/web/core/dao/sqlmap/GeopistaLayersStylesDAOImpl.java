/**
 * GeopistaLayersStylesDAOImpl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.web.core.dao.sqlmap;

import com.ibatis.dao.client.DaoManager;
import com.ibatis.dao.client.template.SqlMapDaoTemplate;
import com.localgis.web.core.dao.GeopistaLayersStylesDAO;
import com.localgis.web.core.model.GeopistaLayersStyles;

public class GeopistaLayersStylesDAOImpl extends SqlMapDaoTemplate implements GeopistaLayersStylesDAO {

	public GeopistaLayersStylesDAOImpl(DaoManager daoManager) {
		super(daoManager);
	}

	public GeopistaLayersStyles selectByPrimaryKey(String layerName, Integer idEntidad) {
		GeopistaLayersStyles key = new GeopistaLayersStyles();
		key.setLayerName(layerName);
		key.setIdEntidad(idEntidad);
		GeopistaLayersStyles record = (GeopistaLayersStyles) queryForObject(
				"geopista_layers_styles.ibatorgenerated_selectByLayerName",
				key);
		return record;
	}

}