/**
 * JDBCUtil.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.datastore.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Utilities for JDBC.
 *
 * @author Martin Davis
 * @version 1.0
 */
public class JDBCUtil
{
  public static void execute(Connection conn, String sql, ResultSetBlock block) {
      try {
          Statement statement = conn.createStatement();
          try {
              ResultSet resultSet = statement.executeQuery(sql);
              try {
                  block.yield(resultSet);
              } finally {
                  resultSet.close();
              }
          } finally {
              statement.close();
          }
      } catch (Exception e) {
          throw new RuntimeException(e);
      }
  }
}