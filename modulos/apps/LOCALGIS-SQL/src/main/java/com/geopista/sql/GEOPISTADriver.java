/**
 * GEOPISTADriver.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
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
