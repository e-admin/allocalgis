/**
 * PostgisDSConnection.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.datastore.postgis;

import java.sql.Connection;
import java.sql.SQLException;

import com.vividsolutions.jump.datastore.AdhocQuery;
import com.vividsolutions.jump.datastore.DataStoreConnection;
import com.vividsolutions.jump.datastore.DataStoreException;
import com.vividsolutions.jump.datastore.DataStoreMetadata;
import com.vividsolutions.jump.datastore.FilterQuery;
import com.vividsolutions.jump.datastore.Query;
import com.vividsolutions.jump.datastore.SpatialReferenceSystemID;
import com.vividsolutions.jump.io.FeatureInputStream;

/**
 */
public class PostgisDSConnection
    implements DataStoreConnection
{
  private PostgisDSMetadata dbMetadata;
  private Connection connection;

  public PostgisDSConnection(Connection conn) {
    connection = conn;
    dbMetadata = new PostgisDSMetadata(this);
  }

  public Connection getConnection()
  {
    return connection;
  }

  public DataStoreMetadata getMetadata()
  {
    return dbMetadata;
  }

    public FeatureInputStream execute(Query query) {
        if (query instanceof FilterQuery) {
            try {
                return executeFilterQuery((FilterQuery) query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if (query instanceof AdhocQuery) {
            return executeAdhocQuery((AdhocQuery) query);
        }
        throw new IllegalArgumentException("Unsupported Query type");
    }

  /**
   * Executes a filter query.
   *
   * The SRID is optional for queries - it will be determined automatically
   * from the table metadata if not supplied.
   *
   * @param query the query to execute
   * @return the results of the query
 * @throws SQLException
   */
  public FeatureInputStream executeFilterQuery(FilterQuery query) throws SQLException
  {
    SpatialReferenceSystemID srid = dbMetadata.getSRID(query.getDatasetName(), query.getGeometryAttributeName());
    String[] colNames = dbMetadata.getColumnNames(query.getDatasetName());

    PostgisSQLBuilder builder = new PostgisSQLBuilder(srid, colNames);
    String queryString = builder.getSQL(query);

    PostgisFeatureInputStream ifs = new PostgisFeatureInputStream(connection, queryString);
    return (FeatureInputStream) ifs;
  }

  public FeatureInputStream executeAdhocQuery(AdhocQuery query)
  {
    String queryString = query.getQuery();
    PostgisFeatureInputStream ifs = new PostgisFeatureInputStream(connection, queryString);
    return (FeatureInputStream) ifs;
  }


  public void close()
      throws DataStoreException
  {
    try {
      connection.close();
    }
    catch (Exception ex) { throw new DataStoreException(ex); }
  }

  public boolean isClosed() throws DataStoreException {
    try {
        return connection.isClosed();
    } catch (SQLException e) {
        throw new DataStoreException(e);
    }
  }

}