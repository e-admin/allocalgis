package com.geopista.sql;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: jramirez
 * Date: 07-may-2004
 * Time: 13:24:48
 * To change this template use File | Settings | File Templates.
 */

public class GEOPISTADriver implements Driver{
static{
    GEOPISTADriver driver=new GEOPISTADriver();
    try{
        DriverManager.registerDriver(driver);
    }catch (SQLException se){
        se.printStackTrace();
    }
}
    public int getMajorVersion() {
        return 0;
    }

    public int getMinorVersion() {
        return 1;
    }

    public boolean jdbcCompliant() {
        return false;
    }

    public boolean acceptsURL(String s) throws SQLException {
        return s.startsWith("jdbc:pista:");
    }

    public Connection connect(String s, Properties properties) throws SQLException {
        if (!acceptsURL(s)) throw new SQLException("no acepta url");
        return new GEOPISTAConnection(s);
    }

    public DriverPropertyInfo[] getPropertyInfo(String s, Properties properties) throws SQLException {
        return new DriverPropertyInfo[]{};
    }

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}
}
