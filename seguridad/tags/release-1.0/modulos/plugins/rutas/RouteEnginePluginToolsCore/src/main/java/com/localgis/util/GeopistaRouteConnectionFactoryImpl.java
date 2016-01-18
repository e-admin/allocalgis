package com.localgis.util;
import java.sql.Connection;
import java.sql.SQLException;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;
import com.localgis.util.RouteConnectionFactory;
 
public class GeopistaRouteConnectionFactoryImpl implements RouteConnectionFactory{

	@Override
	public Connection getConnection() {
		ApplicationContext context = AppContext.getApplicationContext();
		Connection conn = null;
		try {
			conn = context.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	} 

}
