package com.geopista.sql;

import org.apache.log4j.Logger;

import java.sql.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Calendar;
import java.util.ArrayList;

public class GEOPISTAResultSet implements java.sql.ResultSet{

	Logger logger = Logger.getLogger(GEOPISTAResultSet.class);
	private ResultSetMetaData rsmd=null;
	private Object[] aaoData=null; // Filas/Columnas
	private int iFetchDirection=ResultSet.FETCH_FORWARD;
	private int iRow=-1;
	private boolean bWasNull=false;
	private Statement statement;
	public static final int RESULT_SET=-1; // Indica Resultset, no updateCount
	private int iUpdateCount=RESULT_SET;
	private int iColumns=0;

	public GEOPISTAResultSet(){
	}
	//TODO Solucionar lo de las filas en dos dimensiones!!!
	public GEOPISTAResultSet(int iUpdateCount){
		this.iUpdateCount=iUpdateCount;
	}

	/** Lee los campos de un ResultSet cerrándolo al final */
	/** REFACTORIZACION ORACLE public GEOPISTAResultSet(Statement st, ResultSet rs) throws SQLException{*/
	public GEOPISTAResultSet(Statement st, ResultSet rs, boolean isOracle) throws SQLException{
		try
		{
			this.statement=st;
			this.rsmd=new GEOPISTAResultSetMetaData(rs.getMetaData());
			ArrayList alData=new ArrayList();
			iColumns=rsmd.getColumnCount();
			Object [] aoRow=null;
			while(rs.next()){
				aoRow=(Object[])java.lang.reflect.Array.newInstance(java.lang.Object.class,iColumns);
				for (int i=1;i<=iColumns;i++)
				{
					/*
                    if (rs.getObject(i)!=null&&(rs.getObject(i) instanceof oracle.sql.TIMESTAMP))
                        aoRow[i-1]=new java.sql.Timestamp (((oracle.sql.TIMESTAMP)rs.getObject(i)).timestampValue().getTime());
                    else
                        aoRow[i-1]=rs.getObject(i);
					 */

					/** REFACTORIZACION ORACLE */
					if ((rs.getObject(i)!=null) && (isOracle)){
						if (rs.getObject(i) instanceof oracle.sql.TIMESTAMP)
							aoRow[i-1]=new java.sql.Timestamp (((oracle.sql.TIMESTAMP)rs.getObject(i)).timestampValue().getTime());
						else aoRow[i-1]=rs.getObject(i);
					}else{
						aoRow[i-1]=rs.getObject(i);
					}
					/**/
				}
				alData.add(aoRow);
			}
			rs.close();
			this.aaoData=(Object[])alData.toArray();
		}catch (SQLException sqlEx)
		{
			throw sqlEx;
		}
		catch (Exception ex)
		{
			logger.error("Error al recojer los datos: ",ex);
		}
	}

	/** Devuelve ResultSet.CONCUR_READ_ONLY */
	public int getConcurrency() throws SQLException {
		return ResultSet.CONCUR_READ_ONLY;
	}

	/** Devuelve le direccion de lectura de resultados */
	public int getFetchDirection() throws SQLException {
		return this.iFetchDirection;
	}

	/** Devuelve el tamaño total del resultset */
	public int getFetchSize() throws SQLException {
		return (this.aaoData!=null?this.aaoData.length:-1);
	}

	/** Fila actual del resultset */
	public int getRow() throws SQLException {
		return this.iRow+1; // Las filas del resultset empiezan en 1
	}

	/** Devuelve ResultSet.TYPE_SCROLL_INSENSITIVE */
	public int getType() throws SQLException {
		return ResultSet.TYPE_SCROLL_INSENSITIVE;
	}

	public void afterLast() throws SQLException {
		this.iRow=aaoData.length;
	}

	public void beforeFirst() throws SQLException {
		this.iRow=-1;
	}

	/** No implementado */
	public void cancelRowUpdates() throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void clearWarnings() throws SQLException {
		throw new NotImplementedException("");
	}

	/** No hace nada, no es necesario utilizarlo */
	public void close() throws SQLException {
	}

	/** No implementado */
	public void deleteRow() throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void insertRow() throws SQLException {
		throw new NotImplementedException("");
	}

	/** No hace nada */
	public void moveToCurrentRow() throws SQLException {
	}

	/** No implementado */
	public void moveToInsertRow() throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void refreshRow() throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateRow() throws SQLException {
		throw new NotImplementedException("");
	}

	public boolean first() throws SQLException {
		this.iRow=0;
		return (aaoData!=null && aaoData.length>0)?true : false;
	}

	public boolean isAfterLast() throws SQLException {
		boolean bRet=false;
		if (aaoData!=null && aaoData.length>0)
			bRet=(this.iRow>aaoData.length);
		return bRet;
	}

	public boolean isBeforeFirst() throws SQLException {
		boolean bRet=false;
		if (aaoData!=null && aaoData.length>0)
			bRet=(this.iRow==-1);
		return bRet;
	}

	public boolean isFirst() throws SQLException {
		return this.iRow==0;
	}

	public boolean isLast() throws SQLException {
		boolean bRet=false;
		if (aaoData!=null && aaoData.length>0)
			bRet=(this.iRow==aaoData.length-1);
		return bRet;
	}

	public boolean last() throws SQLException {
		boolean bRet=false;
		if (aaoData!=null && aaoData.length>0){
			this.iRow=aaoData.length-1;
		}
		return bRet;
	}

	public boolean next() throws SQLException {
		boolean bRet=false;
		switch (this.iFetchDirection){
		case ResultSet.FETCH_FORWARD:
			if (aaoData!=null && aaoData.length>0 && this.iRow<aaoData.length){
				this.iRow++;
				bRet=(this.iRow<aaoData.length);
			};
			break;
		case ResultSet.FETCH_REVERSE:
			bRet=previous();
			break;
		default:
			throw new SQLException("FetchDirection=UNKNOWN");
		}
		return bRet;
	}

	public boolean previous() throws SQLException {
		boolean bRet=false;
		switch (this.iFetchDirection){
		case ResultSet.FETCH_FORWARD:
			if (aaoData!=null && aaoData.length>0 && this.iRow>=0){
				this.iRow--;
				bRet=(this.iRow>=0);
			}
			break;
		case ResultSet.FETCH_REVERSE:
			bRet=next();
			break;
		default:
			throw new SQLException("FetchDirection=UNKNOWN");
		}
		return bRet;
	}

	/** false */
	public boolean rowDeleted() throws SQLException {
		return false;
	}

	/** false */
	public boolean rowInserted() throws SQLException {
		return false;
	}

	/** false */
	public boolean rowUpdated() throws SQLException {
		return false;
	}

	public boolean wasNull() throws SQLException {
		return this.bWasNull;
	}

	public byte getByte(int i) throws SQLException {
		byte bRet=0;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				Byte b=(Byte)((Object[])aaoData[this.iRow])[i-1];
				bRet=b.byteValue();
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return bRet;
	}

	public double getDouble(int i) throws SQLException {
		double dRet=0;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				dRet=((Number)((Object[])aaoData[this.iRow])[i-1]).doubleValue();
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return dRet;
	}

	public float getFloat(int i) throws SQLException {
		float fRet=0;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				fRet=((Number)((Object[])aaoData[this.iRow])[i-1]).floatValue();
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return fRet;
	}

	public int getInt(int i) throws SQLException {
		int iRet=0;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				iRet=((Number)((Object[])aaoData[this.iRow])[i-1]).intValue();
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return iRet;
	}

	public long getLong(int i) throws SQLException {
		long lRet=0;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				lRet=((Number)((Object[])aaoData[this.iRow])[i-1]).longValue();
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return lRet;
	}

	public short getShort(int i) throws SQLException {
		short sRet=0;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				sRet=((Number)((Object[])aaoData[this.iRow])[i-1]).shortValue();
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return sRet;
	}

	/** cambia la direccion de lectura */
	public void setFetchDirection(int i) throws SQLException {
		this.iFetchDirection=i;
	}

	/** no hace nada */
	public void setFetchSize(int i) throws SQLException {
	}

	/** No implementado */
	public void updateNull(int i) throws SQLException {
		throw new NotImplementedException("");
	}

	/** va a una posicion absoluta en las filas */
	public boolean absolute(int i) throws SQLException {
		boolean bRet=true;
		int iOldRow=this.iRow;
		if (i<0)
			this.iRow=aaoData.length+i;
		else if (i>0)
			this.iRow=i+1;
		if (this.iRow<0||this.iRow>=aaoData.length){
			this.iRow=iOldRow;
			bRet=false;
		}
		return bRet;
	}

	public boolean getBoolean(int i) throws SQLException {
		boolean bRet=false;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				Boolean s=(Boolean)((Object[])aaoData[this.iRow])[i-1];
				bRet=s.booleanValue();
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return bRet;
	}

	/** Movimiento relativo en las filas */
	public boolean relative(int i) throws SQLException {
		this.iRow+=i;
		return (this.aaoData!=null && this.iRow>=0 && this.iRow<this.aaoData.length);
	}

	public byte[] getBytes(int i) throws SQLException {
		byte abRet[]=null;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				abRet=(byte[])((Object[])aaoData[this.iRow])[i-1];
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return abRet;
	}

	/** No implementado */
	public void updateByte(int i, byte b) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateDouble(int i, double v) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateFloat(int i, float v) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateInt(int i, int i1) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateLong(int i, long l) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateShort(int i, short i1) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateBoolean(int i, boolean b) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateBytes(int i, byte[] bytes) throws SQLException {
		throw new NotImplementedException("");
	}

	public InputStream getAsciiStream(int i) throws SQLException {
		InputStream isRet=null;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				isRet=new ByteArrayInputStream(((Object[])aaoData[this.iRow])[i-1].toString().getBytes("US-ASCII"));
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return isRet;
	}

	public InputStream getBinaryStream(int i) throws SQLException {
		InputStream isRet=null;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				isRet=new ByteArrayInputStream((byte[])((Object[])aaoData[this.iRow])[i-1]);
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return isRet;
	}

	/** No implementado */
	public InputStream getUnicodeStream(int i) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateAsciiStream(int i, InputStream inputStream, int i1) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateBinaryStream(int i, InputStream inputStream, int i1) throws SQLException {
		throw new NotImplementedException("");
	}

	public Reader getCharacterStream(int i) throws SQLException {
		Reader rRet=null;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				rRet=new StringReader(((Object[])aaoData[this.iRow])[i-1].toString());
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return rRet;
	}

	/** No implementado */
	public void updateCharacterStream(int i, Reader reader, int i1) throws SQLException {
		throw new NotImplementedException("");
	}

	public Object getObject(int i) throws SQLException {
		Object oRet=null;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				oRet=((Object[])aaoData[this.iRow])[i-1];
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return oRet;
	}

	/** No implementado */
	public void updateObject(int i, Object o) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateObject(int i, Object o, int i1) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public String getCursorName() throws SQLException {
		throw new SQLException("not implemented");
	}

	public String getString(int i) throws SQLException {
		String sRet=null;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				sRet=((Object[])aaoData[this.iRow])[i-1].toString();
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return sRet;
	}

	/** No implementado */
	public void updateString(int i, String s) throws SQLException {
		throw new NotImplementedException("");
	}

	public byte getByte(String s) throws SQLException {
		return getByte(findColumn(s));
	}

	public double getDouble(String s) throws SQLException {
		return getDouble(findColumn(s));
	}

	public float getFloat(String s) throws SQLException {
		return getFloat(findColumn(s));
	}

	public int findColumn(String s) throws SQLException {
		int iRet=-1;
		for (int i=1;i<=rsmd.getColumnCount();i++){
			if (rsmd.getColumnName(i).equalsIgnoreCase(s)){
				iRet=i;
				break;
			}
		}
		if (iRet==-1)
			throw new SQLException(s);
		return iRet;
	}

	public int getInt(String s) throws SQLException {
		return getInt(findColumn(s));
	}

	public long getLong(String s) throws SQLException {
		return getLong(findColumn(s));
	}

	public short getShort(String s) throws SQLException {
		return getShort(findColumn(s));
	}

	/** No implementado */
	public void updateNull(String s) throws SQLException {
		throw new NotImplementedException("");
	}

	public boolean getBoolean(String s) throws SQLException {
		return getBoolean(findColumn(s));
	}

	public byte[] getBytes(String s) throws SQLException {
		return getBytes(findColumn(s));
	}

	/** No implementado */
	public void updateByte(String s, byte b) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateDouble(String s, double v) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateFloat(String s, float v) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateInt(String s, int i) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateLong(String s, long l) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateShort(String s, short i) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateBoolean(String s, boolean b) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateBytes(String s, byte[] bytes) throws SQLException {
		throw new NotImplementedException("");
	}

	public BigDecimal getBigDecimal(int i) throws SQLException {
		BigDecimal bRet=null;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				bRet=(BigDecimal)((Object[])aaoData[this.iRow])[i-1];
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return bRet;
	}

	public BigDecimal getBigDecimal(int i, int i1) throws SQLException {
		return getBigDecimal(i).setScale(i1);
	}

	/** No implementado */
	public void updateBigDecimal(int i, BigDecimal bigDecimal) throws SQLException {
		throw new NotImplementedException("");
	}

	public URL getURL(int i) throws SQLException {
		URL uRet=null;
		String sUrl=getString(i);
		if (sUrl!=null)
			try{
				uRet=new URL(sUrl);
			}catch (MalformedURLException mue){
				throw new SQLException("Malformed URL: "+sUrl);
			}
			return uRet;
	}

	public Array getArray(int i) throws SQLException {
		Array aRet=null;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				aRet=(Array)((Object[])aaoData[this.iRow])[i-1];
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return aRet;
	}

	/** No implementado */
	public void updateArray(int i, Array array) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public Blob getBlob(int i) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateBlob(int i, Blob blob) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public Clob getClob(int i) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateClob(int i, Clob clob) throws SQLException {
		throw new NotImplementedException("");
	}

	public Date getDate(int i) throws SQLException {
		Date dRet=null;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				dRet=(Date)((Object[])aaoData[this.iRow])[i-1];
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return dRet;
	}

	/** No implementado */
	public void updateDate(int i, Date date) throws SQLException {
		throw new NotImplementedException("");
	}

	public Ref getRef(int i) throws SQLException {
		Ref rRet=null;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				rRet=(Ref)((Object[])aaoData[this.iRow])[i-1];
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return rRet;
	}

	/** No implementado */
	public void updateRef(int i, Ref ref) throws SQLException {
		throw new NotImplementedException("");
	}

	public ResultSetMetaData getMetaData() throws SQLException {
		return this.rsmd;
	}


	/** No implementado */
	public SQLWarning getWarnings() throws SQLException {
		throw new NotImplementedException("");
	}

	public Statement getStatement() throws SQLException {
		return this.statement;
	}

	public Time getTime(int i) throws SQLException {
		Time tRet=null;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				tRet=(Time)((Object[])aaoData[this.iRow])[i-1];
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return tRet;
	}

	/** No implementado */
	public void updateTime(int i, Time time) throws SQLException {
		throw new NotImplementedException("");
	}

	public Timestamp getTimestamp(int i) throws SQLException {
		Timestamp tRet=null;
		try{
			if (((Object[])aaoData[this.iRow])[i-1]==null)
				this.bWasNull=true;
			else{
				this.bWasNull=false;
				tRet=(Timestamp)((Object[])aaoData[this.iRow])[i-1];
			}
		}catch (Exception e){
			throw new SQLException(e.getMessage());
		}
		return tRet;
	}

	/** No implementado */
	public void updateTimestamp(int i, Timestamp timestamp) throws SQLException {
		throw new NotImplementedException("");
	}

	public InputStream getAsciiStream(String s) throws SQLException {
		return getAsciiStream(findColumn(s));
	}

	public InputStream getBinaryStream(String s) throws SQLException {
		return getBinaryStream(findColumn(s));
	}

	/** No implementado */
	public InputStream getUnicodeStream(String s) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateAsciiStream(String s, InputStream inputStream, int i) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateBinaryStream(String s, InputStream inputStream, int i) throws SQLException {
		throw new NotImplementedException("");
	}

	public Reader getCharacterStream(String s) throws SQLException {
		return getCharacterStream(findColumn(s));
	}

	/** No implementado */
	public void updateCharacterStream(String s, Reader reader, int i) throws SQLException {
		throw new NotImplementedException("");
	}

	public Object getObject(String s) throws SQLException {
		return getObject(findColumn(s));
	}

	/** No implementado */
	public void updateObject(String s, Object o) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateObject(String s, Object o, int i) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public Object getObject(int i, Map map) throws SQLException {
		throw new NotImplementedException("");
	}

	public String getString(String s) throws SQLException {
		return getString(findColumn(s));
	}

	/** No implementado */
	public void updateString(String s, String s1) throws SQLException {
		throw new NotImplementedException("");
	}

	public BigDecimal getBigDecimal(String s) throws SQLException {
		return getBigDecimal(findColumn(s));
	}

	public BigDecimal getBigDecimal(String s, int i) throws SQLException {
		return getBigDecimal(findColumn(s)).setScale(i);
	}

	/** No implementado */
	public void updateBigDecimal(String s, BigDecimal bigDecimal) throws SQLException {
		throw new NotImplementedException("");
	}

	public URL getURL(String s) throws SQLException {
		return getURL(findColumn(s));
	}

	public Array getArray(String s) throws SQLException {
		return getArray(findColumn(s));
	}

	public void updateArray(String s, Array array) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public Blob getBlob(String s) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateBlob(String s, Blob blob) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public Clob getClob(String s) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public void updateClob(String s, Clob clob) throws SQLException {
		throw new NotImplementedException("");
	}

	public Date getDate(String s) throws SQLException {
		return getDate(findColumn(s));
	}

	/** No implementado */
	public void updateDate(String s, Date date) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public Date getDate(int i, Calendar calendar) throws SQLException {
		throw new NotImplementedException("");
	}

	public Ref getRef(String s) throws SQLException {
		return getRef(findColumn(s));
	}

	/** No implementado */
	public void updateRef(String s, Ref ref) throws SQLException {
		throw new NotImplementedException("");
	}

	public Time getTime(String s) throws SQLException {
		return getTime(findColumn(s));
	}

	/** No implementado */
	public void updateTime(String s, Time time) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public Time getTime(int i, Calendar calendar) throws SQLException {
		throw new NotImplementedException("");
	}

	public Timestamp getTimestamp(String s) throws SQLException {
		return getTimestamp(findColumn(s));
	}

	/** No implementado */
	public void updateTimestamp(String s, Timestamp timestamp) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public Timestamp getTimestamp(int i, Calendar calendar) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public Object getObject(String s, Map map) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public Date getDate(String s, Calendar calendar) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public Time getTime(String s, Calendar calendar) throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public Timestamp getTimestamp(String s, Calendar calendar) throws SQLException {
		throw new NotImplementedException("");
	}


	// Metodos para el marshall/unmarshall
	/** Obtiene los datos como un Object[] serializado que contiene Object[]'s */
	public byte[] getData() throws IOException{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ObjectOutputStream oos=new ObjectOutputStream(baos);
		oos.writeObject(aaoData);
		return baos.toByteArray();
	}

	/** Almacena los datos como un Object[] serializado que contiene Object[]'s */
	public void setData(byte[] abData) throws IOException, ClassNotFoundException{
		ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(abData));
		this.aaoData=(Object[]) ois.readObject();
	}

	/** Metodo javabean complementario para el unmarshal */
	public void setMetaData(ResultSetMetaData rsmd){
		this.rsmd=rsmd;
	}

	/** Almacena las filas actualizadas (el tipo de objeto retornado
	 * es el mismo en queries y updates) */
	public void setUpdateCount(int i){
		this.iUpdateCount=i;
	}
	/** Obtiene las filas actualizadas (el tipo de objeto retornado por
	 * el servidor es el mismo en queries y updates) */
	public int getUpdateCount(){
		return this.iUpdateCount;
	}
	public int getHoldability() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}
	public Reader getNCharacterStream(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public Reader getNCharacterStream(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public NClob getNClob(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public NClob getNClob(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	 
	public String getNString(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public String getNString(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public RowId getRowId(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public RowId getRowId(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public SQLXML getSQLXML(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	public SQLXML getSQLXML(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	 
	public boolean isClosed() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	public void updateAsciiStream(int arg0, InputStream arg1)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateAsciiStream(String arg0, InputStream arg1)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateAsciiStream(int arg0, InputStream arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateAsciiStream(String arg0, InputStream arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateBinaryStream(int arg0, InputStream arg1)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateBinaryStream(String arg0, InputStream arg1)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateBinaryStream(int arg0, InputStream arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateBinaryStream(String arg0, InputStream arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateBlob(int arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateBlob(String arg0, InputStream arg1) throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateBlob(int arg0, InputStream arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateBlob(String arg0, InputStream arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateCharacterStream(int arg0, Reader arg1)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateCharacterStream(String arg0, Reader arg1)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateCharacterStream(int arg0, Reader arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateCharacterStream(String arg0, Reader arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateClob(String arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateClob(int arg0, Reader arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateClob(String arg0, Reader arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateNCharacterStream(int arg0, Reader arg1)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateNCharacterStream(String arg0, Reader arg1)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateNCharacterStream(int arg0, Reader arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateNCharacterStream(String arg0, Reader arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	
	public void updateNClob(int arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateNClob(String arg0, NClob arg1) throws SQLException {
		// TODO Auto-generated method stub

	}
	 
	public void updateNClob(int arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateNClob(String arg0, Reader arg1) throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateNClob(int arg0, Reader arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateNClob(String arg0, Reader arg1, long arg2)
	throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateNString(int arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateNString(String arg0, String arg1) throws SQLException {
		// TODO Auto-generated method stub

	}
	
	public void updateRowId(int arg0, RowId arg1) throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateRowId(String arg0, RowId arg1) throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateSQLXML(int arg0, SQLXML arg1) throws SQLException {
		// TODO Auto-generated method stub

	}
	public void updateSQLXML(String arg0, SQLXML arg1) throws SQLException {
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
}
