/**
 * ValueConverterFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.vividsolutions.jump.datastore.jdbc;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

import com.vividsolutions.jump.feature.AttributeType;


/**
 * Standard data converters for JDBC.
 * Clients can extend this class, or simply call it.
 *
 * @author Martin Davis
 * @version 1.0
 */
public class ValueConverterFactory
{
  public static final ValueConverter DOUBLE_MAPPER = new DoubleConverter();
  public static final ValueConverter INTEGER_MAPPER = new IntegerConverter();
  public static final ValueConverter DATE_MAPPER = new DateConverter();
  public static final ValueConverter STRING_MAPPER = new StringConverter();

  public ValueConverterFactory()
  {

  }

  /**
   * Handles finding a converter for standard JDBC types.
   * Clients should handle custom types themselves.
   *
   * @param rsm
   * @param columnIndex
   * @return null if no converter could be found
   * @throws SQLException
   */
  public static ValueConverter getConverter(ResultSetMetaData rsm, int columnIndex)
      throws SQLException
  {
    String classname = rsm.getColumnClassName(columnIndex);
    String dbTypeName = rsm.getColumnTypeName(columnIndex);
    int precision = rsm.getPrecision(columnIndex);
    int scale = rsm.getScale(columnIndex);
    int sqlType = rsm.getColumnType(columnIndex);

    if (sqlType == Types.INTEGER
        || classname.equalsIgnoreCase("java.lang.Integer")
        || (classname.equalsIgnoreCase("java.math.BigDecimal")
        && precision == 10 && scale == 0))
        return INTEGER_MAPPER;

    if (classname.equalsIgnoreCase("java.math.BigDecimal"))
        return DOUBLE_MAPPER;

    if (classname.equalsIgnoreCase("java.sql.Timestamp"))
        return DATE_MAPPER;

    if (classname.equalsIgnoreCase("java.String"))
      return STRING_MAPPER;

    // default is null
    return null;
  }

  public static class IntegerConverter implements ValueConverter
  {
    public AttributeType getType() { return AttributeType.INTEGER; }
    public Object getValue(ResultSet rs, int columnIndex)
        throws SQLException
    {
      return new Integer(rs.getInt(columnIndex));
    }
  }

  public static class  DoubleConverter implements ValueConverter
  {
    public AttributeType getType() { return AttributeType.DOUBLE; }
    public Object getValue(ResultSet rs, int columnIndex)
        throws SQLException
    {
      return new Double(rs.getDouble(columnIndex));
    }
  }

  public static class  StringConverter implements ValueConverter
  {
    public AttributeType getType() { return AttributeType.STRING; }
    public Object getValue(ResultSet rs, int columnIndex)
        throws SQLException
    {
      return rs.getString(columnIndex);
    }
  }

  public static class  DateConverter implements ValueConverter
  {
    public AttributeType getType() { return AttributeType.DATE; }
    public Object getValue(ResultSet rs, int columnIndex)
        throws SQLException
    {
      return rs.getDate(columnIndex);
    }
  }
}