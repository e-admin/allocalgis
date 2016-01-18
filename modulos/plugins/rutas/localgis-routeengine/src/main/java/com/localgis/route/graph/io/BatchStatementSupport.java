/**
 * BatchStatementSupport.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.route.graph.io;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

import com.geopista.sql.NotImplementedException;

/**
 * Experimental. Intento de agrupar queries en Geopistaconnections
 * 
 * @author juacas
 * 
 */
public class BatchStatementSupport implements PreparedStatement
{
    StringBuilder queries = new StringBuilder();
    int num = 0;
    int size = 5; // Número de statements
    private int lenght; // longitud mÃ¡xima de la query
    private Connection connection;
    private String lastStatement;
    private ArrayList<Object> params=new ArrayList<Object>();

    public BatchStatementSupport(Connection connection, int size, int lenght)
    {
	this.connection = connection;
	this.size = size;
	this.lenght=lenght;
    }

    protected void addStatement(String sql)
    {
	this.lastStatement= sql;
    }

    public int executeUpdate() throws SQLException
    {
	// aÃ±ade la última sentencia si existe
	if (this.lastStatement != null)
	    {
		if (num != 0)
		    queries.append("; \r\n");
		queries.append(statementWithParams(lastStatement));
		this.lastStatement = null;
		this.params.clear();
		num++;
	    }
	if (num % size == 0 || queries.length()>this.lenght)
	    {
		executeUpdateNow();
	    }
	return 0;
    }

    private String statementWithParams(String lastStatement2)
    {
	String stat=lastStatement2;
	for (Object param:this.params)
	    {
		StringBuilder value = new StringBuilder();
		if (param instanceof Number)
		    {
			value.append(param.toString());
		    }
		else
		    {
			if ("NULL".equals(param))
			    {
				value.append("NULL");
			    }
			else
			value.append("'").append(param.toString()).append("'");
		    }
		stat=stat.replaceFirst("[?]", value.toString());
	    }
	return stat;
    }

    private int realExecute() throws SQLException
    {
	String query = queries.toString();
	if (query.contains("FROMnetwork"))
	    {
		throw new IllegalStateException();
	    }
	PreparedStatement st = connection.prepareStatement(query);
	return st.executeUpdate();
    }

    protected void executeUpdateNow() throws SQLException
    {
	realExecute();
	clearBatchSupport();
    }

    private void clearBatchSupport()
    {
	queries.delete(0, queries.length());
	num = 0;
    }

    /**
     * No realiza nada. Las consultas se ejecutan AL CERRAR LA CONEXION EN {@link BatchConnectionFacade}
     * 
     * @throws SQLException
     */
    public void close() throws SQLException
    {
	// executeUpdateNow(); // espera a cerrar la conexión
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public int executeUpdate(String sql) throws SQLException
    {
	addStatement(sql);
	executeUpdate();
	return 0;
    }

    @Override
    public int getMaxFieldSize() throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setMaxFieldSize(int max) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public int getMaxRows() throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setMaxRows(int max) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setEscapeProcessing(boolean enable) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public int getQueryTimeout() throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setQueryTimeout(int seconds) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void cancel() throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void clearWarnings() throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setCursorName(String name) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public boolean execute(String sql) throws SQLException
    {
	addStatement(sql);
	executeUpdate();
	return false;
    }

    @Override
    public ResultSet getResultSet() throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public int getUpdateCount() throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public boolean getMoreResults() throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public int getFetchDirection() throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public int getFetchSize() throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public int getResultSetConcurrency() throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public int getResultSetType() throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void addBatch(String sql) throws SQLException
    {
	addStatement(sql);
    }

    @Override
    public void clearBatch() throws SQLException
    {
	this.clearBatchSupport();
    }

    @Override
    public int[] executeBatch() throws SQLException
    {
	executeUpdate();
	return new int[] {};
    }

    @Override
    public Connection getConnection() throws SQLException
    {
	return connection;
    }

    @Override
    public boolean getMoreResults(int current) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public ResultSet getGeneratedKeys() throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public boolean execute(String sql, String[] columnNames) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public int getResultSetHoldability() throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public boolean isClosed() throws SQLException
    {
	return false;
    }

    @Override
    public void setPoolable(boolean poolable) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public boolean isPoolable() throws SQLException
    {
	return false;
    }

    @Override
    public ResultSet executeQuery() throws SQLException
    {
	throw new NotImplementedException();

    }

    @Override
    public void setNull(int parameterIndex, int sqlType) throws SQLException
    {
	this.params.ensureCapacity(parameterIndex);
	this.params.add(parameterIndex-1, "NULL");
    }

    @Override
    public void setBoolean(int parameterIndex, boolean x) throws SQLException
    {
	this.params.ensureCapacity(parameterIndex);
	this.params.add(parameterIndex-1, x);
    }

    @Override
    public void setByte(int parameterIndex, byte x) throws SQLException
    {
	this.params.ensureCapacity(parameterIndex);
	this.params.add(parameterIndex-1, x);
    }

    @Override
    public void setShort(int parameterIndex, short x) throws SQLException
    {
	this.params.ensureCapacity(parameterIndex);
	this.params.add(parameterIndex-1, x);
    }

    @Override
    public void setInt(int parameterIndex, int x) throws SQLException
    {
	this.params.ensureCapacity(parameterIndex);
	this.params.add(parameterIndex-1, x);
    }

    @Override
    public void setLong(int parameterIndex, long x) throws SQLException
    {
	this.params.ensureCapacity(parameterIndex);
	this.params.add(parameterIndex-1, x);
    }

    @Override
    public void setFloat(int parameterIndex, float x) throws SQLException
    {
	this.params.ensureCapacity(parameterIndex);
	this.params.add(parameterIndex-1, x);
    }

    @Override
    public void setDouble(int parameterIndex, double x) throws SQLException
    {
	this.params.ensureCapacity(parameterIndex);
	this.params.add(parameterIndex-1, x);
    }

    @Override
    public void setBigDecimal(int parameterIndex, BigDecimal x) throws SQLException
    {
	this.params.ensureCapacity(parameterIndex);
	this.params.add(parameterIndex-1, x);
    }

    @Override
    public void setString(int parameterIndex, String x) throws SQLException
    {
	this.params.ensureCapacity(parameterIndex);
	this.params.add(parameterIndex-1, x);
    }

    @Override
    public void setBytes(int parameterIndex, byte[] x) throws SQLException
    {
	throw new NotImplementedException();

    }

    @Override
    public void setDate(int parameterIndex, Date x) throws SQLException
    {
	this.params.ensureCapacity(parameterIndex);
	this.params.add(parameterIndex-1, x);
    }

    @Override
    public void setTime(int parameterIndex, Time x) throws SQLException
    {
	throw new NotImplementedException();

    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x) throws SQLException
    {
	this.params.ensureCapacity(parameterIndex);
	this.params.add(parameterIndex-1, x);
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, int length) throws SQLException
    {
	throw new NotImplementedException();

    }

    @Override
    public void setUnicodeStream(int parameterIndex, InputStream x, int length) throws SQLException
    {
	throw new NotImplementedException();

    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, int length) throws SQLException
    {
	throw new NotImplementedException();

    }

    @Override
    public void clearParameters() throws SQLException
    {
	this.params.clear();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType) throws SQLException
    {
	throw new NotImplementedException();

    }

    @Override
    public void setObject(int parameterIndex, Object x) throws SQLException
    {
	throw new NotImplementedException();

    }

    @Override
    public boolean execute() throws SQLException
    {
	executeUpdate();
	return true;
    }

    @Override
    public void addBatch() throws SQLException
    {
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, int length) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setRef(int parameterIndex, Ref x) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setBlob(int parameterIndex, Blob x) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setClob(int parameterIndex, Clob x) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setArray(int parameterIndex, Array x) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setDate(int parameterIndex, Date x, Calendar cal) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setTime(int parameterIndex, Time x, Calendar cal) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setNull(int parameterIndex, int sqlType, String typeName) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setURL(int parameterIndex, URL x) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public ParameterMetaData getParameterMetaData() throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setRowId(int parameterIndex, RowId x) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setNString(int parameterIndex, String value) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value, long length) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setNClob(int parameterIndex, NClob value) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setClob(int parameterIndex, Reader reader, long length) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream, long length) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader, long length) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setSQLXML(int parameterIndex, SQLXML xmlObject) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setObject(int parameterIndex, Object x, int targetSqlType, int scaleOrLength) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x, long length) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x, long length) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader, long length) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setAsciiStream(int parameterIndex, InputStream x) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setBinaryStream(int parameterIndex, InputStream x) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setCharacterStream(int parameterIndex, Reader reader) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setNCharacterStream(int parameterIndex, Reader value) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setClob(int parameterIndex, Reader reader) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setBlob(int parameterIndex, InputStream inputStream) throws SQLException
    {
	throw new NotImplementedException();
    }

    @Override
    public void setNClob(int parameterIndex, Reader reader) throws SQLException
    {
	throw new NotImplementedException();
    }

	@Override
	public void closeOnCompletion() throws SQLException {
		throw new NotImplementedException();
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		throw new NotImplementedException();
	}

}
