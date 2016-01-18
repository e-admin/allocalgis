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

	public String getQueryFromIdLayer(int idLayer);

	public String getColumnsDescriptorFromIdLayer(int idLayer, int idFeature, String column);

}
