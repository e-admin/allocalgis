package com.geopista.sql;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import com.geopista.app.AppContext;
import com.geopista.protocol.CResultadoOperacion;
import com.geopista.protocol.net.EnviarSeguro;
import com.vividsolutions.jump.task.TaskMonitor;

/**
 * Created by IntelliJ IDEA.
 * User: jramirez
 * Date: 07-may-2004
 * Time: 12:56:39
 * To change this template use File | Settings | File Templates.
 */
public class GEOPISTAConnection implements Connection{
	private static Log logger = LogFactory.getLog(GEOPISTAConnection.class);
	private String sUrl;
	private boolean autoCommit=true;
	private PoolPS commitPS= new PoolPS();
	private TaskMonitor taskMonitor;


	public GEOPISTAConnection(){
	}

	GEOPISTAConnection(String sConnString){
		this.sUrl=sConnString.substring(sConnString.indexOf(AppContext.getApplicationContext().getString(AppContext.PROTOCOL)));
	}

	/** No implementado */
	public int getHoldability() throws SQLException {
		throw new NotImplementedException("");
	}

	/** No implementado */
	public int getTransactionIsolation() throws SQLException {
		throw new NotImplementedException("");
	}

	/** No hace nada */
	public void clearWarnings() throws SQLException {
	}

	/** No hace nada */
	public void close() throws SQLException {
	}

	/** No hace nada */
	public void commit() throws SQLException {
		if (autoCommit || commitPS==null || !commitPS.iterator().hasNext())
			return;
		GEOPISTAResultSet rsRet=null;
		StringReader sr=null;
		try {

			StringWriter sw = new StringWriter();
			Marshaller.marshal(commitPS, sw);
			sr=EnviarSeguro.enviarPlanoMultiPart(sUrl,sw.toString(),taskMonitor);
			String resultado=toString(sr);
			CResultadoOperacion rs=null;
			// System.out.println(resultado);
			try
			{
				if (resultado.indexOf("GEOPISTAResultSet")<0)
				{
					rs=(CResultadoOperacion)Unmarshaller.unmarshal(CResultadoOperacion.class,new StringReader(resultado));
					if (logger.isDebugEnabled())
					{
						logger
						.debug("executeStatement(GEOPISTAPreparedStatement) - Resultado:"
								+ rs.getResultado()
								+ " Cadena:"
								+ resultado);
					}
				}

			}catch(Exception e){rs=null;}
			commitPS.clear();
			if (rs!=null) throw new SQLException(rs.getDescripcion());
			// rsRet=(GEOPISTAResultSet)Unmarshaller.unmarshal(GEOPISTAResultSet.class,new StringReader(resultado));
			return;
		}
		catch (MarshalException e)
		{   
			commitPS.clear();
			CResultadoOperacion rs=null;
			try{
				rs =(CResultadoOperacion)Unmarshaller.unmarshal(CResultadoOperacion.class,sr);
			}
			catch(Exception ex)
			{
				throw new SQLException(e.getMessage());
			}
			throw new SQLException(rs.getDescripcion());
		} catch (Exception e)
		{
			commitPS.clear();
			SQLException sqle = new SQLException(e.getMessage());
			sqle.setStackTrace(e.getStackTrace());
			sqle.initCause(e.getCause());
			
			throw sqle;
		}
	}

	/** No implementado */
	public void rollback() throws SQLException {
		throw new SQLException();
	}

	/** Devuelve true */
	public boolean getAutoCommit() throws SQLException {
		return autoCommit;
	}

	/** Devuelve false */
	public boolean isClosed() throws SQLException {
		return false;
	}

	/** Devuelve false */
	public boolean isReadOnly() throws SQLException {
		return false;
	}

	/** No implementado */
	public void setHoldability(int i) throws SQLException {
		throw new SQLException();
	}

	/** No implementado */
	public void setTransactionIsolation(int i) throws SQLException {
		throw new SQLException();
	}

	/** No hace nada */
	public void setAutoCommit(boolean b) throws SQLException {
		// autoCommit=b;
	}
	//TODO revisar donde se usa setAutoCommit y descomentar la funcion de arriba
	public void setAutoCommit1(boolean b) throws SQLException {
		autoCommit=b;
	}
	/** No hace nada */
	public void setReadOnly(boolean b) throws SQLException {
	}

	/** Devuelve null */
	public String getCatalog() throws SQLException {
		return null;
	}

	/** No hace nada */
	public void setCatalog(String s) throws SQLException {
	}

	/** No implementado */
	public DatabaseMetaData getMetaData() throws SQLException {
		throw new NotImplementedException("");
	}

	/** Devuelve null */
	public SQLWarning getWarnings() throws SQLException {
		return null;
	}

	/** No implementado */
	public Savepoint setSavepoint() throws SQLException {
		throw new SQLException();
	}

	/** No implementado */
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		throw new SQLException();
	}

	/** No implementado */
	public void rollback(Savepoint savepoint) throws SQLException {
		throw new SQLException();
	}

	/** crea un statement nulo (no se puede ejecutar sin asignarle un queryID) */
	public Statement createStatement() throws SQLException {
		return new GEOPISTAPreparedStatement(this,null);
	}

	/** crea un statement nulo (no se puede ejecutar sin asignarle un queryID) */
	public Statement createStatement(int i, int i1) throws SQLException {
		return new GEOPISTAPreparedStatement(this,null);
	}

	/** crea un statement nulo (no se puede ejecutar sin asignarle un queryID) */
	public Statement createStatement(int i, int i1, int i2) throws SQLException {
		return new GEOPISTAPreparedStatement(this,null);
	}

	/** devuelve un mapa vacío */
	public Map getTypeMap() throws SQLException {
		return new java.util.Hashtable();
	}

	/** no implementado */
	public void setTypeMap(Map map) throws SQLException {
		throw new NotImplementedException("");
	}

	/** devuelve s */
	public String nativeSQL(String s) throws SQLException {
		return s;
	}

	/** no implementado */
	public CallableStatement prepareCall(String s) throws SQLException {
		throw new NotImplementedException("");
	}

	/** no implementado */
	public CallableStatement prepareCall(String s, int i, int i1) throws SQLException {
		throw new NotImplementedException("");
	}

	/** no implementado */
	public CallableStatement prepareCall(String s, int i, int i1, int i2) throws SQLException {
		throw new NotImplementedException("");
	}

	/** Crea un nuevo statement con la query "s" del catalogo */
	public PreparedStatement prepareStatement(String s) throws SQLException {
		return new GEOPISTAPreparedStatement(this,s);
	}

	/** Crea un nuevo statement con la query "s" del catalogo */
	public PreparedStatement prepareStatement(String s, int i) throws SQLException {
		return new GEOPISTAPreparedStatement(this,s);
	}

	/** Crea un nuevo statement con la query "s" del catalogo */
	public PreparedStatement prepareStatement(String s, int i, int i1) throws SQLException {
		return new GEOPISTAPreparedStatement(this,s);
	}

	/** Crea un nuevo statement con la query "s" del catalogo */
	public PreparedStatement prepareStatement(String s, int i, int i1, int i2) throws SQLException {
		return new GEOPISTAPreparedStatement(this,s);
	}

	/** Crea un nuevo statement con la query "s" del catalogo */
	public PreparedStatement prepareStatement(String s, int[] ints) throws SQLException {
		return new GEOPISTAPreparedStatement(this,s);
	}

	/** No implementado */
	public Savepoint setSavepoint(String s) throws SQLException {
		throw new SQLException();
	}

	/** Crea un nuevo statement con la query "s" del catalogo */
	public PreparedStatement prepareStatement(String s, String[] strings) throws SQLException {
		return new GEOPISTAPreparedStatement(this,s);
	}

	/** Realiza una peticion http con un GEOPISTAPreparedStatement serializado */
	public GEOPISTAResultSet executeStatement(GEOPISTAPreparedStatement ps, boolean query)throws Exception{
		GEOPISTAResultSet rsRet=null;
		StringReader sr=null; 
		if (autoCommit==false && !query )
		{
			commitPS.add(ps);
			return new GEOPISTAResultSet(1);
		}
		try {
			StringWriter sw = new StringWriter();
			Marshaller.marshal(ps, sw);
			sr=EnviarSeguro.enviarPlanoMultiPart(sUrl,sw.toString(),taskMonitor);
			String resultado=toString(sr);
			CResultadoOperacion rs=null;
			// System.out.println(resultado);
			try
			{
				if (resultado.indexOf("GEOPISTAResultSet")<0)
				{	
					rs=(CResultadoOperacion)Unmarshaller.unmarshal(CResultadoOperacion.class,new StringReader(resultado));
					if (logger.isDebugEnabled())
					{
						logger
						.debug("executeStatement(GEOPISTAPreparedStatement) - Resultado:"
								+ rs.getResultado()
								+ " Cadena:"
								+ resultado);
					}
				}

			}catch(Exception e){rs=null;}
			if (rs!=null) throw new Exception(rs.getDescripcion());
			
			rsRet=(GEOPISTAResultSet)Unmarshaller.unmarshal(GEOPISTAResultSet.class,new StringReader(resultado));
			return rsRet;
		}
		catch (MarshalException e)
		{
			CResultadoOperacion rs=null;
			System.out.println("TERMINO");
			try{
				rs =(CResultadoOperacion)Unmarshaller.unmarshal(CResultadoOperacion.class,sr);
			}
			catch(Exception ex)
			{
				throw e;
			}
			throw new Exception(rs.getDescripcion());
		} catch (Exception e)
		{
			throw e;
		}


	}

	/** No hace nada */
	public void setClosed(boolean b){

	}
	private String toString(StringReader sr) throws Exception
	{
		if (sr==null)return null;
		try
		{

			BufferedReader br = new BufferedReader(sr);
			String line=null;
			StringBuffer resultado=new StringBuffer();
			while ((line = br.readLine()) != null) {
				resultado.append(line);
			}
			return resultado.toString();
		}catch(Exception e)
		{
			logger.error("Error al convertir a String",e);
			throw e;
		}
	}

	public Array createArrayOf(String arg0, Object[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Blob createBlob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Clob createClob() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	 
	public Struct createStruct(String arg0, Object[] arg1) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public Properties getClientInfo() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getClientInfo(String arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isValid(int arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
	 


	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

    //No hace nada
    public java.sql.SQLXML createSQLXML() throws java.sql.SQLException{ throw new NotImplementedException("");}

    //No hace nada
    public java.sql.NClob createNClob() throws java.sql.SQLException{ throw new NotImplementedException("");}

    //No hace nada
    public void setClientInfo(java.lang.String arg0, java.lang.String arg1) throws java.sql.SQLClientInfoException{ }
    
    //No hace nada
    public void setClientInfo(java.util.Properties arg0) throws java.sql.SQLClientInfoException{}

	public void setTaskMonitor(TaskMonitor taskMonitor) {
		this.taskMonitor=taskMonitor;
		
	}
}
