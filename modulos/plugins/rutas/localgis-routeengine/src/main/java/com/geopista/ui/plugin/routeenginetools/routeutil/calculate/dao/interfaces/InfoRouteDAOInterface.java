/**
 * InfoRouteDAOInterface.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dao.interfaces;

import java.sql.Connection;

import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.vividsolutions.jts.geom.LineString;

public interface InfoRouteDAOInterface {
	
	/**
	 * Metodo que devuelve un linestring de la base de datos sabiendo el id de edge y el srid.
	 * El srid se usa para que la base de datos lo devuelva en el srid de la peticion.
	 * Este metodo no es utilizado nunca para rutas, ya que nunca tenemos los id de layer y feature de los nodos.
	 * @param con - Conexion a la base de datos.
	 * @param idEdge - id del edge
	 * @param srid - srid de Linestring a devolver
	 * @return - LineString correspondiente
	 */
	public LineString getLinestringFromIdEdge(Connection connection,
			ILocalGISEdge edge, int srid);
	
	public Connection getGeopistaSQLConnection();
/**
 * 
 * @param idLayer
 * @return
 */
	public String getSystemLayerIdFromIntIdLayer(int idLayer);

	public String getColumnsDescriptorFromIdLayer(int idLayer, int idFeature, String column);

	public abstract String getLayerQueryFromLayerId(int idLayer);

	public abstract Connection getConnection();

	int getIdLayerFromName(String layername);

}
