/**
 * PostgisValueConverterFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.datastore.postgis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jump.datastore.jdbc.ValueConverter;
import com.vividsolutions.jump.datastore.jdbc.ValueConverterFactory;
import com.vividsolutions.jump.feature.AttributeType;

/**
 *
 */
public class PostgisValueConverterFactory
{
  // should lazily init these
  private final ValueConverter WKT_GEOMETRY_MAPPER = new WKTGeometryValueConverter();
  private final ValueConverter WKB_GEOMETRY_MAPPER = new WKBGeometryValueConverter();

  private final Connection conn;
  private final WKBReader wkbReader = new WKBReader();
  private final WKTReader wktReader = new WKTReader();

  public PostgisValueConverterFactory(Connection conn) {
    this.conn = conn;
  }

  public ValueConverter getConverter(ResultSetMetaData rsm, int columnIndex)
      throws SQLException
  {
    String classname = rsm.getColumnClassName(columnIndex);
    String dbTypeName = rsm.getColumnTypeName(columnIndex);

    // MD - this is slow - is there a better way?
    if (dbTypeName.equalsIgnoreCase("geometry"))
        // WKB is now the normal way to store geometry in PostGIS [mmichaud 2007-05-13]
        return WKB_GEOMETRY_MAPPER;

    if (dbTypeName.equalsIgnoreCase("bytea"))
        return WKB_GEOMETRY_MAPPER;

    // handle the standard types
    ValueConverter stdConverter = ValueConverterFactory.getConverter(rsm, columnIndex);
    if (stdConverter != null)
      return stdConverter;

    // default - can always show it as a string!
    return ValueConverterFactory.STRING_MAPPER;
  }

  class WKTGeometryValueConverter implements ValueConverter
  {
    public AttributeType getType() { return AttributeType.GEOMETRY; }
    public Object getValue(ResultSet rs, int columnIndex)
        throws IOException, SQLException, ParseException
    {
      Object valObj = rs.getObject(columnIndex);
      String s = valObj.toString();
      Geometry geom = wktReader.read(s);
      return geom;
    }
  }

  class WKBGeometryValueConverter implements ValueConverter
  {
    public AttributeType getType() { return AttributeType.GEOMETRY; }
    public Object getValue(ResultSet rs, int columnIndex)
        throws IOException, SQLException, ParseException
    {
      //Object obj = rs.getObject(columnIndex);
      //byte[] bytes = (byte[]) obj;
      byte[] bytes = rs.getBytes(columnIndex);
      Geometry geom = wkbReader.read(bytes);
      return geom;
    }
  }
}