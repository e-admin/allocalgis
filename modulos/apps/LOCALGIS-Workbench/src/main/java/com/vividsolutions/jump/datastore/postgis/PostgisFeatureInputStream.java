/**
 * PostgisFeatureInputStream.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.datastore.postgis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.vividsolutions.jump.feature.Feature;
import com.vividsolutions.jump.feature.FeatureSchema;
import com.vividsolutions.jump.io.BaseFeatureInputStream;

/**
 * Reads features from an Oracle database.
 */
public class PostgisFeatureInputStream
    extends BaseFeatureInputStream
{
  private FeatureSchema featureSchema;
  private Connection conn;
  private String queryString;
  private boolean initialized = false;
  private Exception savedException;

  private Statement stmt = null;
  private ResultSet rs = null;
  private PostgisResultSetConverter mapper;

  int geometryColIndex = -1;

  public PostgisFeatureInputStream(Connection conn, String queryString) {
    this.conn = conn;
    this.queryString = queryString;
  }

  /**
   * @return The underlaying {@link Connection}.
   */
  public Connection getConnection(){return conn;}

  private void init()
      throws SQLException
  {
    if (initialized)
      return;
    initialized = true;

    //conn.setDefaultRowPrefetch(100);
    stmt = conn.createStatement();
    String parsedQuery = queryString;
    //String parsedQuery = QueryUtil.parseQuery(queryString);
    rs = stmt.executeQuery(parsedQuery);
    mapper = new PostgisResultSetConverter(conn, rs);
    featureSchema = mapper.getFeatureSchema();
  }

  protected Feature readNext()
      throws Exception
  {
    if (savedException != null)
      throw savedException;
    if (! initialized)
      init();
    if (rs == null)
      return null;
    if (! rs.next())
      return null;
    return getFeature();
  }

  private Feature getFeature()
      throws Exception
  {
    return mapper.getFeature();
  }

  public void close() throws SQLException {
    if (rs != null) {
      rs.close();
    }
    if (stmt != null) {
      stmt.close();
    }
  }

  public FeatureSchema getFeatureSchema()
  {
    if (featureSchema != null)
      return featureSchema;

    try {
      init();
    }
    catch (SQLException ex)
    {
      savedException = ex;
    }
    if (featureSchema == null)
      featureSchema = new FeatureSchema();
    return featureSchema;
  }
}