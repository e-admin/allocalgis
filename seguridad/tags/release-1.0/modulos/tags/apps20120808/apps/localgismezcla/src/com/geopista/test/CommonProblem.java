package com.geopista.test;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class CommonProblem {

	
	public static int TIPO_INMUEBLE_URBANA=2;
	public static int TIPO_INMUEBLE_RUSTICA=3;
	public static int TIPO_VIA_URBANA=4;
	public static int TIPO_VIA_RUSTICA=5;
	
	
	public static Connection getConnection(String entorno) throws SQLException{
		Connection connection=null;
		try {
			
			Class.forName("org.postgresql.Driver");
			DriverManager.setLogWriter(new PrintWriter((System.err)));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (entorno.equals("DEV"))
			connection = DriverManager.getConnection("jdbc:postgresql://pamod-dev.c.ovd.interhost.com:5432/geopista","geopista","01g7PT3a");
		else if (entorno.equals("PRO"))
			connection = DriverManager.getConnection("jdbc:postgresql://pamod-app1.c.ovd.interhost.com:5432/geopista","geopista","a3TP7g10");
		connection.setAutoCommit(false);
		return connection;

	}
	/**
	 * Ejecutamos la query. Si hay mas de un elemento no seguimos porque puede ser
	 * un problema.
	 * @param conn
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public static int testExecuteQuery(Connection conn,String query,String numInventario,int idMunicipio,long valor1,int valor2) throws Exception{
			
		PreparedStatement ps=null;
		ResultSet rs=null;
		int total=0;
		try{
			ps= null;
			ps= conn.prepareStatement(query);
	        ps.setLong(1, valor1);
	        if (valor2!=-1)
		        ps.setInt(2, valor2);
	        rs=ps.executeQuery();
	        if (rs.next()){
	        	total=rs.getInt("total");
	        	if (total>1)
	        		throw new Exception("Numero de elementos superior al total:"+total+" para elemento:"+numInventario+" Municipo:"+idMunicipio);
	        }
	        else
        		throw new Exception("Numero de elementos superior al total:"+total+" para elemento:"+numInventario+" Municipo:"+idMunicipio);
	        
	        return total;
		}
		finally{					
	        rs.close();
	        ps.close();
		}
        
        
	}
	
	/**
	 * Ejecutamos la query. Si hay mas de un elemento no seguimos porque puede ser
	 * un problema.
	 * @param conn
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public static int testExecuteQuery2(Connection conn,String query,String numInventario,int idMunicipio,long valor1,int valor2) throws Exception{
			
		PreparedStatement ps=null;
		ResultSet rs=null;
		int total=0;
		try{
			ps= null;
			ps= conn.prepareStatement(query);
	        ps.setLong(1, valor1);
	        if (valor2!=-1){
		        ps.setInt(2, valor2);
		        ps.setInt(3, valor2);
	        }
	        rs=ps.executeQuery();
	        if (rs.next()){
	        	total=rs.getInt("total");
	        	if (total>1)
	        		throw new Exception("Numero de elementos superior al total:"+total+" para elemento:"+numInventario+" Municipo:"+idMunicipio);
	        }
	        else
        		throw new Exception("Numero de elementos superior al total:"+total+" para elemento:"+numInventario+" Municipo:"+idMunicipio);
	        
	        return total;
		}
		finally{					
	        rs.close();
	        ps.close();
		}
        
        
	}
	
	public static void logErrors(String operacion,String datos){
		Logger loggerError = Logger.getLogger("ErrorLog");
		org.apache.log4j.FileAppender fileAppenderNew = null;
		try {
			org.apache.log4j.FileAppender fileAppender = (org.apache.log4j.FileAppender)loggerError
					.getAppender("ErrorLog");
			if (fileAppender==null)
				return;
			org.apache.log4j.Layout layout = fileAppender.getLayout();
			fileAppenderNew = new org.apache.log4j.FileAppender(layout,
					"logs/ErroresConversion_" + operacion+".log");
			fileAppenderNew.setAppend(true);
			loggerError.addAppender(fileAppenderNew);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		loggerError.info(datos);
	}
	
}
