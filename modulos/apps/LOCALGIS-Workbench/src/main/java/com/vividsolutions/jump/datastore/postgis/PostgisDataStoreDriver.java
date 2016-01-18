/**
 * PostgisDataStoreDriver.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.datastore.postgis;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;

import com.vividsolutions.jump.datastore.DataStoreConnection;
import com.vividsolutions.jump.datastore.DataStoreDriver;
import com.vividsolutions.jump.parameter.ParameterList;
import com.vividsolutions.jump.parameter.ParameterListSchema;

/**
 * A driver for supplying {@link PostgisDSConnection}s
 */
public class PostgisDataStoreDriver
    implements DataStoreDriver
{
  public static final String DRIVER_NAME = "PostGIS";
  public static final String JDBC_CLASS = "org.postgresql.Driver";
  public static final String URL_PREFIX = "jdbc:postgresql://";

  public static final String PARAM_Server = "Server";
  public static final String PARAM_Port = "Port";
  public static final String PARAM_Instance = "Instance";
  public static final String PARAM_User = "User";
  public static final String PARAM_Password = "Password";

  private static final String[] paramNames = new String[] {
    PARAM_Server,
    PARAM_Port,
    PARAM_Instance,
    PARAM_User,
    PARAM_Password
    };
  private static final Class[] paramClasses = new Class[]
  {
    String.class,
    Integer.class,
    String.class,
    String.class,
    String.class
    };
  private final ParameterListSchema schema = new ParameterListSchema(paramNames, paramClasses);;

  public PostgisDataStoreDriver() {
  }

  public String getName()
  {
    return DRIVER_NAME;
  }
  public ParameterListSchema getParameterListSchema()
  {
    return schema;
  }
  public DataStoreConnection createConnection(ParameterList params)
      throws Exception
  {
    String host = params.getParameterString(PARAM_Server);
    int port = params.getParameterInt(PARAM_Port);
    String database = params.getParameterString(PARAM_Instance);
    String user = params.getParameterString(PARAM_User);
    String password = params.getParameterString(PARAM_Password);

    String url
        = String.valueOf(new StringBuffer(URL_PREFIX).append
        (host).append
        (":").append
        (port).append
        ("/").append(database));

    Driver driver = (Driver) Class.forName(JDBC_CLASS).newInstance();
    DriverManager.registerDriver(driver);

    Connection conn = DriverManager.getConnection(url, user, password);
    return new PostgisDSConnection(conn);
  }
  public boolean isAdHocQuerySupported() {
      return true;
  }

}