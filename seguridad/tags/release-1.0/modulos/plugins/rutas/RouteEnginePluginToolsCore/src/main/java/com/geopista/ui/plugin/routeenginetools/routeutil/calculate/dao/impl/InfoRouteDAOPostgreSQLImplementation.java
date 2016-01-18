/**
 * 
 */
package com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgis.PGgeometry;

import com.geopista.app.AppContext;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dao.RouteDaoUtil;
import com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dao.interfaces.InfoRouteDAOInterface;
import com.localgis.route.graph.structure.basic.ILocalGISEdge;
import com.localgis.util.ConnectionUtilities;
import com.localgis.util.GeopistaRouteConnectionFactoryImpl;
import com.localgis.util.RouteConnectionFactory;
import com.vividsolutions.jts.geom.LineString;

/**
 * @author javieraragon
 *
 */
public class InfoRouteDAOPostgreSQLImplementation implements InfoRouteDAOInterface{


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
			// Preparamos la query para obtener la geometría original del 
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
	public Connection getGeopistaSQLConnection() {
		Connection resultConnection = null;
		
		if (!AppContext.getApplicationContext().isLogged()){
			AppContext.getApplicationContext().login(); 
		} 
		
		try {
			resultConnection = AppContext.getApplicationContext().getConnection();
		} catch (SQLException e) { e.printStackTrace(); }
		
		return resultConnection;
	}

	
	@ Override
	public String getQueryFromIdLayer(int idLayer) {

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
	public String getColumnsDescriptorFromIdLayer(int idLayer, int idFeature, String column){
		if(column == null || column.equals("SIN TIPO")) return "";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Connection con = getGeopistaSQLConnection();
		try {
			String sqlQuery = "SELECT " + column  +" FROM " + getQueryFromIdLayer( idLayer) + " resultTable where resultTable.id = " + idFeature ;
			preparedStatement = con.prepareStatement(sqlQuery);
			rs = preparedStatement.executeQuery ();
			if(rs.next()){ 
				return rs.getString(column);
			}
			preparedStatement.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}

		return "";
	}
	
	
}
