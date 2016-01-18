/**
 * GEOPISTAPreparedStatement.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.sql;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.StringWriter;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class GEOPISTAPreparedStatement implements PreparedStatement{

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {

		return sQueryCatalogId+" con parámetros:"+lParams.toString();
	}

	String sQueryCatalogId=null;
	private GEOPISTAConnection conn;
	List lParams=new ArrayList();
	private GEOPISTAResultSet resultSet=null;
	int iFetchDirection=ResultSet.FETCH_FORWARD;

	public GEOPISTAPreparedStatement(){
	}

	GEOPISTAPreparedStatement(GEOPISTAConnection conn, String sQueryCatalogId){
		this.sQueryCatalogId=sQueryCatalogId;
		this.conn=conn;
	}

	/** Llena con objetos nulos la lista para que alcance la longitud necesaria
	 * para insertar
	 * @param iRSPos Posicion del resultset en la que vamos s insertar
	 */
	private void allocNulls(int iRSPos){
		while (lParams.size()<iRSPos){
			lParams.add(null);
		}
	}

	/** Ejecuta la sentencia y devuelve las filas afectadas. Si lo que llega
	 * es un resultset, lanza la excepcion.
	 */
	public int executeUpdate() throws SQLException {
		int iRet=0;
		try{
			GEOPISTAResultSet rs=this.conn.executeStatement(this,false);
			this.resultSet=rs;
			iRet=rs.getUpdateCount();
			if (iRet==GEOPISTAResultSet.RESULT_SET)
				throw new SQLException();
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return iRet;
	}

	/** No implementado */
	public void addBatch() throws SQLException {
		throw new SQLException("");
	}

	/** Vacia la lista de parametros */
	public void clearParameters() throws SQLException {
		lParams.clear();
	}

	/** Ejecuta la sentencia devolviendo true si llega un resultset */
	public boolean execute() throws SQLException {
		boolean bRet=true;
		try {
			GEOPISTAResultSet rs=this.conn.executeStatement(this,false);
			this.resultSet=rs;
			bRet=rs.getUpdateCount()==GEOPISTAResultSet.RESULT_SET;
		} catch (Exception e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			throw new SQLException(e.getMessage());
		}
		return bRet;
	}

	public void setByte(int i, byte b) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,new Byte(b));
	}

	public void setDouble(int i, double v) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,new Double(v));
	}

	public void setFloat(int i, float v) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,new Float(v));
	}

	public void setInt(int i, int i1) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,new Integer(i1));
	}

	public void setNull(int i, int i1) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,null);
	}

	public void setLong(int i, long l) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,new Long(l));
	}

	public void setShort(int i, short i1) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,new Short(i1));
	}

	public void setBoolean(int i, boolean b) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,new Boolean(b));
	}

	public void setBytes(int i, byte[] bytes) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,bytes);
	}

	public void setAsciiStream(int i, InputStream inputStream, int i1) throws SQLException {
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try {
			int j=0;
			int iByte=inputStream.read();
			while(iByte!=-1 && ++j<=i1){
				baos.write(iByte);
				iByte=inputStream.read();
			}
			allocNulls(i);
			lParams.set(i-1, new String(baos.toByteArray(),"US-ASCII"));
		} catch (IOException e) {
			throw new SQLException (e.getMessage());
		}
	}

	public void setBinaryStream(int i, InputStream inputStream, int i1) throws SQLException {
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		try {
			int j=0;
			int iByte=inputStream.read();
			while(iByte!=-1 && ++j<=i1){
				baos.write(iByte);
				iByte=inputStream.read();
			}
			allocNulls(i);
			lParams.set(i-1, baos.toByteArray());
		} catch (IOException e) {
			throw new SQLException (e.getMessage());
		}
	}

	/** No implementado */
	public void setUnicodeStream(int i, InputStream inputStream, int i1) throws SQLException {
		throw new NotImplementedException("");
	}

	public void setCharacterStream(int i, Reader reader, int i1) throws SQLException {
		StringWriter sw=new StringWriter();
		try {
			int j=0;
			while(reader.ready() && ++j<=i1){
				sw.write(reader.read());
			}
			allocNulls(i);
			lParams.set(i-1, sw.toString());
		} catch (IOException e) {
			throw new SQLException (e.getMessage());
		}
	}

	public void setObject(int i, Object o) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,o);
	}

	public void setObject(int i, Object o, int i1) throws SQLException {
		setObject(i,o);
	}

	public void setObject(int i, Object o, int i1, int i2) throws SQLException {
		setObject(i,o);
	}

	public void setNull(int i, int i1, String s) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,null);
	}

	public void setString(int i, String s) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,s);
	}

	public void setBigDecimal(int i, BigDecimal bigDecimal) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,bigDecimal);
	}

	public void setURL(int i, URL url) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,url.toString());
	}

	public void setArray(int i, Array array) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,array);
	}

	/** No implementado */
	public void setBlob(int i, Blob blob) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void setClob(int i, Clob clob) throws SQLException {
		throw new NotImplementedException("");
	}

	public void setDate(int i, Date date) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,date);
	}

	/** No implementado */
	public ParameterMetaData getParameterMetaData() throws SQLException {
		throw new NotImplementedException("");
	}

	public void setRef(int i, Ref ref) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,ref);
	}

	public ResultSet executeQuery() throws SQLException {
		GEOPISTAResultSet rs=null;
		try{
			rs=this.conn.executeStatement(this,false);
			this.resultSet=rs;
			if (rs.getUpdateCount()!=GEOPISTAResultSet.RESULT_SET)
				throw new SQLException();
		}catch (Exception e){
			
			if (e.getCause() != null)
			{
				SQLException se = new SQLException(e.getCause().getMessage());
				se.setStackTrace(e.getCause().getStackTrace());		
				se.initCause(e);
				throw se;
			}else{
				throw new SQLException(e.getMessage());
			}
		}
		return rs;
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		if (this.resultSet!=null)
			return resultSet.getMetaData();
		else
			return null;
	}

	public void setTime(int i, Time time) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,time);
	}

	public void setTimestamp(int i, Timestamp timestamp) throws SQLException {
		allocNulls(i);
		lParams.set(i-1,timestamp);
	}

	/** No implementado */
	public void setDate(int i, Date date, Calendar calendar) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void setTime(int i, Time time, Calendar calendar) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void setTimestamp(int i, Timestamp timestamp, Calendar calendar) throws SQLException {
		throw new NotImplementedException("");
	}

	public int getFetchDirection() throws SQLException {
		if (this.resultSet!=null)
			return resultSet.getFetchDirection();
		else
			return ResultSet.FETCH_FORWARD;
	}

	public int getFetchSize() throws SQLException {
		if (this.resultSet!=null)
			return resultSet.getFetchSize();
		else
			return -1;
	}

	/** Devuelve 0 */
	public int getMaxFieldSize() throws SQLException {
		return 0;
	}

	/** Devuelve 0 */
	public int getMaxRows() throws SQLException {
		return 0;
	}

	/** Devuelve 0 */
	public int getQueryTimeout() throws SQLException {
		return 0;
	}

	/** Devuelve ResultSet.CONCUR_READ_ONLY */
	public int getResultSetConcurrency() throws SQLException {
		return ResultSet.CONCUR_READ_ONLY;
	}

	/** No implementado */
	public int getResultSetHoldability() throws SQLException {
		throw new NotImplementedException("");
	}

	/** Devuelve ResultSet.TYPE_SCROLL_INSENSITIVE */
	public int getResultSetType() throws SQLException {
		return  ResultSet.TYPE_SCROLL_INSENSITIVE;
	}

	public int getUpdateCount() throws SQLException {
		int iRet=-1;
		if (this.resultSet!=null)
			iRet=this.resultSet.getUpdateCount();
		return iRet;
	}

	/** No implementado */
	public void cancel() throws SQLException {
		throw new SQLException();
	}

	/** No implementado */
	public void clearBatch() throws SQLException {
		throw new SQLException("");
	}

	/** No implementado */
	public void clearWarnings() throws SQLException {
		throw new NotImplementedException("");
	}

	/** No hace nada, no es necesario utilizarlo */
	public void close() throws SQLException {
		//lParams=null;
		resultSet=null;
	}

	/** No implementado */
	public boolean getMoreResults() throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public int[] executeBatch() throws SQLException {
		throw new NotImplementedException("");
	}

	public void setFetchDirection(int i) throws SQLException {
		this.iFetchDirection=i;
	}

	/** No hace nada */
	public void setFetchSize(int i) throws SQLException {
	}

	/** No hace nada */
	public void setMaxFieldSize(int i) throws SQLException {
	}

	/** No hace nada */
	public void setMaxRows(int i) throws SQLException {
	}

	/** No hace nada */
	public void setQueryTimeout(int i) throws SQLException {
	}

	/** No implementado */
	public boolean getMoreResults(int i) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void setEscapeProcessing(boolean b) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public int executeUpdate(String s) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void addBatch(String s) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void setCursorName(String s) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public boolean execute(String s) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public int executeUpdate(String s, int i) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public boolean execute(String s, int i) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public int executeUpdate(String s, int[] ints) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public boolean execute(String s, int[] ints) throws SQLException {
		throw new NotImplementedException("");
	}

	public Connection getConnection() throws SQLException {
		return this.conn;
	}

	public ResultSet getGeneratedKeys() throws SQLException {
		throw new NotImplementedException("");
	}

	public ResultSet getResultSet() throws SQLException {
		return this.resultSet;
	}

	/** No implementado */
	public SQLWarning getWarnings() throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public int executeUpdate(String s, String[] strings) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public boolean execute(String s, String[] strings) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public ResultSet executeQuery(String s) throws SQLException {
		throw new NotImplementedException("");
	}

	// Metodos para el marshal/unmarshal

	public void setQueryId(String s){
		this.sQueryCatalogId=s;
	}

	public String getQueryId(){
		return this.sQueryCatalogId;
	}

	public byte[] getParams() throws IOException{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ObjectOutputStream oos=new ObjectOutputStream(baos);
		oos.writeObject(lParams.toArray());
		return baos.toByteArray();
	}

	public void setParams(byte[] abData) throws IOException, ClassNotFoundException{
		ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(abData));
		this.lParams=Arrays.asList((Object[]) ois.readObject());
	}

	public void setConnection(GEOPISTAConnection conn){
		this.conn=conn;
	}

	public void setAsciiStream(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setAsciiStream(int arg0, InputStream arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setBinaryStream(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setBinaryStream(int arg0, InputStream arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setBlob(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setBlob(int arg0, InputStream arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setCharacterStream(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setCharacterStream(int arg0, Reader arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setClob(int arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNCharacterStream(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNCharacterStream(int arg0, Reader arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	
	public void setNClob(int arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}
	 
	public void setNClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNClob(int arg0, Reader arg1, long arg2) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setNString(int arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub

	}
	
	public void setRowId(int arg0, RowId arg1) throws SQLException {
		// TODO Auto-generated method stub

	}

	public void setSQLXML(int arg0, SQLXML arg1) throws SQLException {
		// TODO Auto-generated method stub

	}
	 
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isPoolable() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public void setPoolable(boolean arg0) throws SQLException {
		// TODO Auto-generated method stub

	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
}
