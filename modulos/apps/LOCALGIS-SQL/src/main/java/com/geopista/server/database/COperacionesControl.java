/**
 * COperacionesControl.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.geopista.protocol.CResultadoOperacion;

/**
 * @author SATEC
 * @version $Revision: 1.13 $
 *          <p/>
 *          Autor:$Author: satec $
 *          Fecha Ultima Modificacion:$Date: 2012/07/30 10:10:05 $
 *          $Name:  $
 *          $RCSfile: COperacionesControl.java,v $
 *          $Revision: 1.13 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class COperacionesControl {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(COperacionesControl.class);
    public static CResultadoOperacion ejecutarQuery(String sSentencia, Vector vParametros) {
        Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
        Vector auxVector=new Vector();
        boolean bCerrarConexion=true;

		try {
			//logger.debug("Inicio.");

    		//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			
        	
			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
			    return new CResultadoOperacion(false, "No se puede obtener la conexión");
			}

            preparedStatement = connection.prepareStatement (sSentencia);
            if (vParametros!=null)
            {
                for (int i=0;i<vParametros.size();i++)
                {
                	Object obj = vParametros.get(i);
                	if(obj instanceof Integer){
                		preparedStatement.setInt(i+1, (Integer)obj);
                	}
                	else if(obj instanceof Long){
                		preparedStatement.setLong(i+1, (Long)obj);
                	}
                	else{       
                		preparedStatement.setString (i+1, (String)obj);
                	}
                   
                }
            }
            rs = preparedStatement.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int iNumCampos = meta.getColumnCount();
            
            while (rs.next()) {
                if (iNumCampos<=1)
                {
                    String auxString = rs.getString(1);
                    auxVector.add(auxString);
                }
                else
                {
                    Vector auxCampos= new Vector();
                    for (int i=1;i<=iNumCampos;i++)
                    {
                        try
                            {
                                if ((meta.getColumnClassName(i).toUpperCase().indexOf("TIME")<0)&&
                                        (meta.getColumnClassName(i).toUpperCase().indexOf("DATE")<0))
                                   throw new Exception();

                                java.util.Date auxDate = new java.util.Date(rs.getTimestamp(i).getTime());
                                if (auxDate!=null)
                                    auxCampos.add(auxDate);
                                else
                                    auxCampos.add("");
                            }catch(Exception ex){
                              try
                            {
                                String auxString = rs.getString(i);
                                if (auxString!=null)
                                    auxCampos.add(auxString);
                                else
                                    auxCampos.add("");
                            }catch(Exception e)
                            {                  }
                        }
                    }
                    auxVector.add(auxCampos);
                }
			}

            rs.close();
			preparedStatement.close();
			connection.close();
			CPoolDatabase.releaseConexion();
        } catch (Exception ex) {
            try {rs.close();} catch (Exception ex2) {}
			try {preparedStatement.close();} catch (Exception ex2) {}
			try {connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception al ejecutar la sentencia: "+sSentencia + sw.toString());
            if (vParametros!=null)
               for (Enumeration e=vParametros.elements();e.hasMoreElements();) logger.error("Parametros: "+e.nextElement());
			return new CResultadoOperacion(false, "Exception: " + sw.toString());
    	}
        CResultadoOperacion resultado = new CResultadoOperacion(true,"Operación ejecutar query realizada con exito");
        //System.out.println("Añadimos un vector de :"+auxVector.size());
        resultado.setVector(auxVector);
        return resultado;
    }
    
    public static CResultadoOperacion ejecutarNewQuery(String sSentencia, Vector vParametros,Connection conn) {
        Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
        Vector auxVector=new Vector();
        boolean bCerrarConexion=true;

		try {
			//logger.debug("Inicio.");

    		//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			//connection = CPoolDatabase.getConnection();
        	if (conn!=null)
            {
                connection=conn;
                bCerrarConexion=false;
            }
            else
            {
            	connection=CPoolDatabase.getConnection();
                bCerrarConexion=true;
            }			
        	
			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
			    return new CResultadoOperacion(false, "No se puede obtener la conexión");
			}

			logger.debug("Sentencia:"+sSentencia);
            preparedStatement = connection.prepareStatement (sSentencia);
            if (vParametros!=null)
            {
                for (int i=0;i<vParametros.size();i++)
                {
                	
                	Object obj = vParametros.get(i);
                	if(obj instanceof Integer){
                		preparedStatement.setInt(i+1, (Integer)obj);
                	}
                	else if(obj instanceof Long){
                		preparedStatement.setLong(i+1, (Long)obj);
                	}
                	else{       
                		preparedStatement.setString (i+1, (String)obj);
                	}
                	                	
                    //String aux=(String)vParametros.get(i);
                    //preparedStatement.setString (i+1, aux);
                }
            }
            try {
				//logger.info("Sentencia Filled: "+((org.enhydra.jdbc.standard.StandardXAPreparedStatement)preparedStatement).ps.toString());
				logger.info("Sentencia Filled: "+preparedStatement.toString());
			} catch (Exception e) {}

            rs = preparedStatement.executeQuery();
            ResultSetMetaData meta = rs.getMetaData();
            int iNumCampos = meta.getColumnCount();
            
            while (rs.next()) {
                if (iNumCampos<=1)
                {
                    String auxString = rs.getString(1);
                    auxVector.add(auxString);
                }
                else
                {
                    Vector auxCampos= new Vector();
                    for (int i=1;i<=iNumCampos;i++)
                    {
                        try
                            {
                                if ((meta.getColumnClassName(i).toUpperCase().indexOf("TIME")<0)&&
                                        (meta.getColumnClassName(i).toUpperCase().indexOf("DATE")<0))
                                   throw new Exception();

                                java.util.Date auxDate = new java.util.Date(rs.getTimestamp(i).getTime());
                                if (auxDate!=null)
                                    auxCampos.add(auxDate);
                                else
                                    auxCampos.add("");
                            }catch(Exception ex){
                              try
                            {
                                String auxString = rs.getString(i);
                                if (auxString!=null)
                                    auxCampos.add(auxString);
                                else
                                    auxCampos.add("");
                            }catch(Exception e)
                            {                  }
                        }
                    }
                    auxVector.add(auxCampos);
                }
			}

            rs.close();
			preparedStatement.close();
            try{if (bCerrarConexion){connection.close();CPoolDatabase.releaseConexion();}}catch(Exception e){};
			//connection.close();
            //CPoolDatabase.releaseConexion();
        } catch (Exception ex) {
            try {rs.close();} catch (Exception ex2) {}
			try {preparedStatement.close();} catch (Exception ex2) {}
			//try {connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
            try{if (bCerrarConexion){connection.close();CPoolDatabase.releaseConexion();}}catch(Exception e){};

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception al ejecutar la sentencia: "+sSentencia + sw.toString());
            if (vParametros!=null)
               for (Enumeration e=vParametros.elements();e.hasMoreElements();) logger.error("Parametros: "+e.nextElement());
			return new CResultadoOperacion(false, "Exception: " + sw.toString());
    	}
        CResultadoOperacion resultado = new CResultadoOperacion(true,"Operación ejecutar query realizada con exito");
        //System.out.println("Añadimos un vector de :"+auxVector.size());
        resultado.setVector(auxVector);
        return resultado;
    }

    
    public static CResultadoOperacion ejecutarSentencia(String sSentencia, Vector vParametros) {
        Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
        try {
			logger.debug("Inicio.");
     		//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
			    return new CResultadoOperacion(false, "No se puede obtener la conexión");
			}

            preparedStatement = connection.prepareStatement (sSentencia);
            if (vParametros!=null)
            {
                for (int i=0;i<vParametros.size();i++)
                {
                    try
                    {
                        String aux=(String)vParametros.get(i);
                        preparedStatement.setString (i+1, aux);
                    }catch(Exception e)
                    {
                        try
                        {
                             java.util.Date aux = (java.util.Date)vParametros.get(i);
                            preparedStatement.setTimestamp(i+1,new java.sql.Timestamp(aux.getTime()));

                        }catch(Exception ex)
                        {
                            ex.printStackTrace();
                        }
                    }
                }
            }
            preparedStatement.execute();
            preparedStatement.close();
			connection.close();
            CPoolDatabase.releaseConexion();
        } catch (Exception ex) {
            try {rs.close();} catch (Exception ex2) {}
			try {preparedStatement.close();} catch (Exception ex2) {}
			try {connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {
			}
            StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception al ejecutar la sentencia: "+sSentencia + sw.toString());
            if (vParametros!=null)
                for (Enumeration e=vParametros.elements();e.hasMoreElements();) logger.error("Parametros: "+e.nextElement());
			return new CResultadoOperacion(false, "Exception: " + sw.toString());
    	}
        CResultadoOperacion resultado = new CResultadoOperacion(true,"Operación ejecutar sentencia realizada con exito");
        return resultado;
    }

    public static CResultadoOperacion grabarLogin(String sIdSesion, String sIdUser, String sAppDef, HttpServletRequest request)
    {
        Connection connection = null;
		PreparedStatement preparedStatement = null;
        ResultSet rs = null;
    	try {
			//logger.debug("Almacenando Login:"+sIdSesion+" para el usuario:"+sIdUser);
    		//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
			    return new CResultadoOperacion(false, "No se puede obtener la conexión");
			}
            preparedStatement = connection.prepareStatement("Select appid from appgeopista where Upper(def)=Upper(?)");
            preparedStatement.setString(1,sAppDef);
            rs=  preparedStatement.executeQuery();
            if (rs.next()){
            	String sIdApp=rs.getString(1);
            	rs.close();
            	try{
            		preparedStatement = connection.prepareStatement("insert into iusercnt (id,userid,appid,timestamp) values (?,?,?,?)");
					//preparedStatement = connection.prepareStatement("insert into iusercnt (id,userid,appid,timestamp,ip) values (?,?,?,?,?)");
            		preparedStatement.setString(1, sIdSesion);
            		preparedStatement.setInt(2, Integer.parseInt(sIdUser));
            		preparedStatement.setInt(3, Integer.parseInt(sIdApp));
            		Calendar cal=Calendar.getInstance();
            		preparedStatement.setTimestamp(4,new java.sql.Timestamp(cal.getTime().getTime()));
            		//preparedStatement.setString(5, request.getRemoteAddr());
            		preparedStatement.execute();
            		preparedStatement.close();
            	}	catch(Exception ex){
            		logger.error("Ha habido un error al grabar la conexión, ya estaría conectado",ex);
            		logger.error("Tamaño de la sesion:"+sIdSesion+ " Long:"+sIdSesion.length());
					logger.error("SQL Ejecutando query Login: "+((org.postgresql.PGStatement)preparedStatement).toString());

            		//ex.printStackTrace();
            	
            	}
            }else {
            	rs.close();
            }
            connection.commit();
            connection.close();
            CPoolDatabase.releaseConexion();
        } catch (Exception ex) {
              try {rs.close();} catch (Exception ex2) {}
              try {preparedStatement.close();} catch (Exception ex2) {}
              try {connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
            StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new CResultadoOperacion(false, "Exception al grabar el login: " + sw.toString()+ " Sesion:"+sIdSesion);
    	}
        CResultadoOperacion resultado = new CResultadoOperacion(true,"Operación ejecutar grabarlogin realizada con exito");
        return resultado;
    }
public static String getPassword(String sIdUser)
    {
        Connection connection = null;
		PreparedStatement preparedStatement = null;
		String sPassword=null;
		
        ResultSet rs = null;
    	try {
			//logger.debug("Almacenando Login:"+sIdSesion+" para el usuario:"+sIdUser);
    		//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
			    return null;
			}
            preparedStatement = connection.prepareStatement("select password from iuseruserhdr where id=?");
            preparedStatement.setInt(1,Integer.parseInt(sIdUser));
            rs=  preparedStatement.executeQuery();
            if (rs.next()){
            	sPassword=rs.getString(1);
            }            
            rs.close();            
            connection.close();
            CPoolDatabase.releaseConexion();
        } catch (Exception ex) {
              try {rs.close();} catch (Exception ex2) {}
              try {preparedStatement.close();} catch (Exception ex2) {}
              try {connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
            StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return null;
    	}
        return sPassword;
    }
    public static CResultadoOperacion grabarLogout(String sIdSesion)
    {
        Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			//logger.debug("Inicio.");
    		//****************************************
			//** Obtener una conexion de la base de datos
			//****************************************************
			connection = CPoolDatabase.getConnection();
			if (connection == null) {
				logger.warn("No se puede obtener la conexión");
			    return new CResultadoOperacion(false, "No se puede obtener la conexión");
			}
            preparedStatement = connection.prepareStatement("update iusercnt set timeclose=? where id=?");
            Calendar cal=Calendar.getInstance();
            preparedStatement.setTimestamp(1,new java.sql.Timestamp(cal.getTime().getTime()));
            preparedStatement.setString(2,sIdSesion);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.commit();
            connection.close();
            CPoolDatabase.releaseConexion();
            logger.info("Se ha realizado la grabacion del cierre de conexion de la sesion:"+sIdSesion);
        } catch (Exception ex) {
        	try {preparedStatement.close();} catch (Exception ex2) {}
			try {connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
            StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return new CResultadoOperacion(false, "Exception al grabar el login: " + sw.toString());
    	}
        CResultadoOperacion resultado = new CResultadoOperacion(true,"Operación ejecutar grabarlogin realizada con exito");
        return resultado;
    }

    public static CResultadoOperacion cerrarSesiones()
    {
           Connection connection = null;
           PreparedStatement preparedStatement = null;
           try {
               logger.debug("Inicio cerrar sesiones.");
               //****************************************
               //** Obtener una conexion de la base de datos
               //****************************************************
               connection = CPoolDatabase.getConnection();
               if (connection == null) {
                   logger.warn("No se puede obtener la conexión");
                   return new CResultadoOperacion(false, "No se puede obtener la conexión");
               }
               preparedStatement = connection.prepareStatement("update iusercnt set timeclose=? where timeclose is null");
               preparedStatement.setTimestamp(1,new java.sql.Timestamp(new java.util.Date().getTime()));
               preparedStatement.execute();
               preparedStatement.close();
               connection.commit();
               connection.close();
               CPoolDatabase.releaseConexion();
           } catch (Exception ex) {
                 try {preparedStatement.close();} catch (Exception ex2) {}
                 try {connection.close();CPoolDatabase.releaseConexion();} catch (Exception ex2) {}
               StringWriter sw = new StringWriter();
               PrintWriter pw = new PrintWriter(sw);
               ex.printStackTrace(pw);
               logger.error("Excepcion al cerrar la sesiones: " + sw.toString());
               return new CResultadoOperacion(false, "Exception al cerrar las sesiones: " + sw.toString());
           }
           CResultadoOperacion resultado = new CResultadoOperacion(true,"Operación cerrar sesiones ejecutada con exito");
           return resultado;
       }

}
