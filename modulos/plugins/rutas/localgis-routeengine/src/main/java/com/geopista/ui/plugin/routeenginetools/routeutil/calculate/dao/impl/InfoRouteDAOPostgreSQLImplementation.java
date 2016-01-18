/**
 * InfoRouteDAOPostgreSQLImplementation.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgis.PGgeometry;

import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dao.RouteDaoUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dao.interfaces.InfoRouteDAOInterface;
import com.localgis.route.graph.io.LocalGISNetworkDAO;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.LineString;

/**
 * @author javieraragon
 *
 */
public class InfoRouteDAOPostgreSQLImplementation implements InfoRouteDAOInterface{


	private RouteConnectionFactory connectionFactory;

	public InfoRouteDAOPostgreSQLImplementation(RouteConnectionFactory routeConnectionFactory) 
	{
	this.connectionFactory=routeConnectionFactory;
	}
	@Override
	public LineString getLinestringFromIdEdge(Connection connection, ILocalGISEdge edge, int srid) {
		
		LineString lineStringGeometry = null;
		
		int idFeature = -1; 
		int idLayer = -1;
		try{
			idLayer = edge.getIdLayer();
			idFeature = edge.getIdFeature();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		if (idLayer > 0 && idFeature > 0){
			
			PreparedStatement preparedStatement = null;
		    ResultSet rs = null;
			try{
			// Preparamos la query para obtener la geometria original del 
			String sqlQuery = "SELECT AsText(" + RouteDaoUtil.getGeometryColumn(idLayer) + 
			") as geom FROM (" + RouteDaoUtil.getQueryFromIdLayer(connection, idLayer, srid) + ") resultTable WHERE resultTable.id = ?";
			
			preparedStatement = connection.prepareStatement(sqlQuery);
	    	preparedStatement.setInt(1,idFeature);
	    	
	    	rs = preparedStatement.executeQuery ();
		    if(rs.next()){ 
//		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Tabla encontrada.");
		    	PGgeometry geo = new PGgeometry(rs.getString("geom"));
		    	lineStringGeometry = (LineString) com.localgis.util.JtsPostgisOperations.convert2JTS(geo);
		    }
		    preparedStatement.close();
	    	rs.close();
			
			}catch (Exception e) {		
				e.printStackTrace();
			}finally{
				RouteDaoUtil.closeConnection(connection,preparedStatement,rs);
			}
		}
		return lineStringGeometry;
	}

	
	
	
	@Override
	public Connection getGeopistaSQLConnection()
	{
	return this.getConnection();
	}

	
	@ Override
	public String getSystemLayerIdFromIntIdLayer(int idLayer)
	{
		String unformattedQuery = "";
		String sqlQuery = "select name from layers where id_layer = " + idLayer;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = getGeopistaSQLConnection();

		try {
			if (con != null){
				preparedStatement = con.prepareStatement(sqlQuery);
				preparedStatement.setInt(1,idLayer);
				rs = preparedStatement.executeQuery ();

				if(rs.next()){ 
					unformattedQuery = rs.getString("name");
				}
				preparedStatement.close();
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}

		return unformattedQuery;

	}
	@Override
	public int getIdLayerFromName(String layername)
	{
	   
	    String sqlQuery= "select id_layer from layers where name='"+layername+"'";
	    PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    Connection con = getGeopistaSQLConnection();
		try {
			preparedStatement = con.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
			if(rs.next()){ 
				return rs.getInt("id_layer");
			}
			preparedStatement.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return -1;
	}
	
	@Override
	public String getColumnsDescriptorFromIdLayer(int idLayer, int idFeature, String column)
	{
		if(column == null || column.equals("SIN TIPO")) return "";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = getGeopistaSQLConnection();
		LocalGISNetworkDAO lgNetDao=new LocalGISNetworkDAO();
		
		String sqlQuery=null;
		try {
			String layerQuery = lgNetDao.getQueryFromIdLayer(con, idLayer, "25830");
			
			sqlQuery = "SELECT " + column  +" FROM (" + layerQuery + ") resultTable where resultTable.id = " + idFeature ;
			preparedStatement = con.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
			if(rs.next()){ 
				return rs.getString(column);
			}
			preparedStatement.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("QUERY:"+sqlQuery);
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}

		return "";
	}
	
	@Override
	@Deprecated
	public String getLayerQueryFromLayerId(int idLayer)
	{
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = getGeopistaSQLConnection();
		
		try {
		    	int DATABASETYPE=1;// postgis TODO obtener el tipo de base de datos
			String sqlQuery = "select q.id,q.selectquery from queries q where q.databasetype="+DATABASETYPE+
				" and q.id_layer="+idLayer;
			preparedStatement = con.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
			if(rs.next()){ 
				return rs.getString("selectquery");
			}
			preparedStatement.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}

		return null;
	}




	@Override
	public Connection getConnection()
	{
	    return connectionFactory.getConnection();
	}
	
	
}
