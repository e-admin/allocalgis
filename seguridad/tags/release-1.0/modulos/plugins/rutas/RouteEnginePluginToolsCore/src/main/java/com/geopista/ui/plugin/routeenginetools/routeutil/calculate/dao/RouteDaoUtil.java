package com.geopista.ui.plugin.routeenginetools.routeutil.calculate.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.localgis.util.ConnectionUtilities;

public class RouteDaoUtil {

	public static void closeConnection(Connection connection, Statement preparedStatement, ResultSet rs){
		if(rs!=null)
			try
		{
				rs.close();
		}catch(Exception e){}

		if(preparedStatement!=null)
			try
		{
				preparedStatement.close();
		}catch(Exception e){}

		if(connection!=null)
			try
		{
				connection.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static void closeConnection(Connection connection)
	{
		if (connection != null) {        
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public static String getGeometryColumn(int idLayer) {
        //TODO: Obtener el nombre de la columna geometría a partir de el Network y sus properties.
        return "\"GEOMETRY\"";
	}
	
	public static String getQueryFromIdLayer(Connection con, int idLayer,int srid) {
		String unformattedQuery = "";
		String sqlQuery = "select selectquery FROM queries WHERE id_layer = ?";
		PreparedStatement preparedStatement = null;
	    ResultSet rs = null;
	    try {
//	    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Consultando en base de datos");
	    	preparedStatement = con.prepareStatement(sqlQuery);
	    	preparedStatement.setInt(1,idLayer);
			rs = preparedStatement.executeQuery ();
		    if(rs.next()){ 
//		    	if (LOGGER.isDebugEnabled()) LOGGER.debug("Tabla encontrada.");
		    	unformattedQuery = rs.getString("selectquery");
		    }
		    preparedStatement.close();
	    	rs.close();
	    	
		} catch (SQLException e) {
//			LOGGER.error("Error obtaining layer query",e);
			e.printStackTrace();
		} finally {
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
            
        }
		return formatQuery(unformattedQuery,srid);
	}
	
	private static String formatQuery(String unformattedQuery,int srid) {
		// Sustituyo el ?M por un is not null. Siempre debe haber un valor de id de municipio.
		unformattedQuery = unformattedQuery.replaceAll("\\?M","\\?m").replaceAll("= {0,}\\?m", "IS NOT NULL");
		unformattedQuery = unformattedQuery.replaceAll(" IN "," in ").replaceAll("\\?M","\\?m").replaceAll("in {0,}\\( {0,}\\?m {0,}\\)", "IS NOT NULL");
		unformattedQuery = unformattedQuery.replaceAll("\\?T","\\?t").replaceAll("\\?t",Integer.toString(srid));
		return unformattedQuery;
	}


}
