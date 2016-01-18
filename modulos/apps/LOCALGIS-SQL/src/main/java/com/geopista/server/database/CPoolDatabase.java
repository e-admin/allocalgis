/**
 * CPoolDatabase.java
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * @author SATEC
 * @version $Revision: 1.2 $
 *          <p/>
 *          Autor:$Author: satec $
 *          Fecha Ultima Modificacion:$Date: 2011/06/29 17:14:40 $
 *          $Name:  $
 *          $RCSfile: CPoolDatabase.java,v $
 *          $Revision: 1.2 $
 *          $Locker:  $
 *          $State: Exp $
 *          <p/>
 *          To change this template use Options | File Templates.
 */
public class CPoolDatabase {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(CPoolDatabase.class);
    public static final String CIRCULAR="CIRCULAR";
    private static  Integer numeroConexiones= new Integer(0);
    private static DataSource datasource ;
    
	public static Connection getConnection() {
		try {

            if (datasource==null)
            {
                Context context =  new InitialContext();
                datasource = (DataSource) context.lookup("jdbc/localgis");
            }
            Connection connection = datasource.getConnection();
            connection.setAutoCommit(false);
            addConexion();
			//logger.debug("connection: " + connection);
			
            return connection;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return null;

		}
	}
	
	public static Connection getSimpleConnection() {
		try {

            if (datasource==null)
            {
                Context context =  new InitialContext();
                datasource = (DataSource) context.lookup("jdbc/localgis");
            }
            Connection connection = datasource.getConnection();
            connection.setAutoCommit(false);
            addConexion();
			//logger.debug("connection: " + connection);
			
            return connection;


		} catch (Exception ex) {		
			return null;

		}
	}
	
	public static Connection getConnection(String user,String password) {
		try {

            if (datasource==null)
            {
                Context context =  new InitialContext();
                datasource = (DataSource) context.lookup("jdbc/localgis");
//           		StandardXADataSource standardXAdataSource = new StandardXADataSource();
//           		standardXAdataSource.setDriverName(DBPropertiesStore.getDBContext().getString(DBConstants.DDBBCONNECTION_DRIVER));
//           		standardXAdataSource.setUrl(DBPropertiesStore.getDBContext().getString(DBConstants.DDBBCONNECTION_URL));
//           		standardXAdataSource.setUser(user);
//           		standardXAdataSource.setPassword(password);
//           		datasource = standardXAdataSource;
            }
            Connection connection = datasource.getConnection();
            
            addConexion();
			//logger.debug("connection: " + connection);
			
            return connection;


		} catch (Exception ex) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			logger.error("Exception: " + sw.toString());
			return null;

		}
	}

    public static void setDatasource(DataSource datasource) {
        CPoolDatabase.datasource = datasource;
    }

    public static boolean isPostgres(Connection conn)
    {
        
        if (conn instanceof org.enhydra.jdbc.core.CoreConnection){
            return (((org.enhydra.jdbc.core.CoreConnection)conn).con instanceof org.postgresql.PGConnection);
            
        }
        else{
            return (conn instanceof org.postgresql.PGConnection);
        }
              
       //  return (((org.enhydra.jdbc.core.CoreConnection)conn).con instanceof org.postgresql.PGConnection);
    }
    public static void addConexion()
    {
         synchronized(numeroConexiones)
         {
             numeroConexiones=new Integer(numeroConexiones.intValue()+1);
         }
         logger.debug("Numero de conexiones abiertas:"+getNumeroConexiones());
     }
    public static void releaseConexion()
    {
        synchronized(numeroConexiones)
        {
            numeroConexiones=new Integer(numeroConexiones.intValue()-1);
        }
        logger.debug("Restableciendo conexion al POOL:"+getNumeroConexiones());
    }

    public static Integer getNumeroConexiones() {
        return numeroConexiones;
    }



	public static Connection getConnnection(String lookupField) {
		try {
			Context context =  new InitialContext();
			DataSource datasource = (DataSource) context.lookup(lookupField);
            if (datasource==null) return null;
			Connection connection = datasource.getConnection();
			logger.debug("connection: " + connection);

			return connection;


		} catch (Exception ex) {
			logger.error("[CPoolDatabase.getConnection] No se puede obtener la conexión: "+lookupField);
			return null;
		}
	}
	
	public synchronized static long getNextValue(String tabla, String campo) throws Exception {
		return getNextValue(tabla, campo, 0, false);
    }
	
	public synchronized static long getNextValue(String tabla, String campo, int id_municipio, boolean inventario) throws Exception {
        Connection conn=null;
        Statement  stm=null;
        ResultSet  rs=null;
        int numIntentos=0;
        long id=0;
        try
        {
            conn = getConnection();
            do
            {
                id=getNext(tabla, campo, conn, id_municipio, inventario,false,id);
                String cadena = "select "+campo+" from "+tabla+" where "+campo+"='"+id+"'";
                stm = conn.createStatement();
                rs = stm.executeQuery (cadena) ;
                if (rs.next())
                {
                	numIntentos++;
                    try{stm.close();}catch(Exception e){};
                    try{rs.close();}catch(Exception e){};}
                else
                {
                	actualizarSecuencia(tabla, campo, conn, id_municipio, inventario,id);
                    try{stm.close();}catch(Exception e){};
                    try{rs.close();}catch(Exception e){};
                    try{conn.close();releaseConexion();}catch(Exception e){};
                    logger.info("Numero de intentos para obtener la secuencia: "+numIntentos);
                    return id;
                }
            }
            while (true);

        }
        catch (Exception e)
        {
            try{stm.close();}catch(Exception ex){};
            try{rs.close();}catch(Exception ex){};
            try{conn.close();releaseConexion();}catch(Exception ex){};
            throw e;
        }
    }
	
    public synchronized static long getNextValue(String tabla, String campo, String sequence) throws Exception {
           Connection conn=null;
           Statement  stm=null;
           ResultSet  rs=null;
           try
           {
               conn = getConnection();
               do
               {
                   long id=getNext(sequence, conn);
                   String cadena = "select "+campo+" from "+tabla+" where "+campo+"='"+id+"'";
                   stm = conn.createStatement();
                   rs = stm.executeQuery (cadena) ;
                   if (rs.next())
                   {
                       try{stm.close();}catch(Exception e){};
                       try{rs.close();}catch(Exception e){};}
                   else
                   {
                       try{stm.close();}catch(Exception e){};
                       try{rs.close();}catch(Exception e){};
                       try{conn.close();releaseConexion();}catch(Exception e){};
                       return id;
                   }
               }
               while (true);

           }
           catch (Exception e)
           {
               try{stm.close();}catch(Exception ex){};
               try{rs.close();}catch(Exception ex){};
               try{conn.close();releaseConexion();}catch(Exception ex){};
               throw e;
           }
       }
    public synchronized static long getNextValue(String tabla, String campo, String sequence,Connection connection) throws Exception {
        Connection conn=null;
        Statement  stm=null;
        ResultSet  rs=null;
        boolean bCerrarConexion=true;
        try
        {
        	if (connection!=null)
            {
                conn=connection;
                bCerrarConexion=false;
            }
            else
            {
            	conn=getConnection();
                bCerrarConexion=true;
            }
            //conn = getConnection();
            do
            {
                long id=getNext(sequence, conn);
                String cadena = "select "+campo+" from "+tabla+" where "+campo+"='"+id+"'";
                stm = conn.createStatement();
                rs = stm.executeQuery (cadena) ;
                if (rs.next())
                {
                    try{stm.close();}catch(Exception e){};
                    try{rs.close();}catch(Exception e){};}
                else
                {
                    try{stm.close();}catch(Exception e){};
                    try{rs.close();}catch(Exception e){};
                    try{if (bCerrarConexion){conn.close();releaseConexion();}}catch(Exception e1){};
                    //try{conn.close();releaseConexion();}catch(Exception e){};
                    return id;
                }
            }
            while (true);

        }
        catch (Exception e)
        {
            try{stm.close();}catch(Exception ex){};
            try{rs.close();}catch(Exception ex){};
            try{if (bCerrarConexion){conn.close();releaseConexion();}}catch(Exception e1){};
            //try{conn.close();releaseConexion();}catch(Exception ex){};
            throw e;
        }
    }

    private static long getNext(String sequence, Connection conn) throws SQLException {
        String cadena = null;
        if (isPostgres(conn)) {
            cadena = "select nextval('"+sequence+"') as valor";
        } else {
            cadena = "select "+sequence+".nextval as valor from dual";
        }
        
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery (cadena) ;
        if (!rs.next()) {
             try{rs.close();}catch(Exception e){} ;
             try{stm.close();} catch(Exception e){} ;
             throw new SQLException ("No existe secuencia para " + sequence);
       }
       long valor=rs.getInt ("valor") ;
       try{rs.close();}catch(Exception e){} ;
       try{stm.close();} catch(Exception e){} ;
       return valor;
    }

    /*
	 * Se han añadido los atributos: 
	 * id_municipio: para que las secuencias puedan ser especificas por municipio
	 * inventario:
	 *   - true: indica que es una secuencia de un epigrafe de inventario, lo consulta en la tabla sequences_inventario.
	 *   - false: no es una secuencia de inventario, lo consulta en la tabla sequences. 
	 */
    public static long getNext(String tabla, String campo, Connection conn, int id_municipio, boolean inventario) throws SQLException {
    	return getNext(tabla,campo,conn,id_municipio,inventario,true,0);
    }
    
    public static long getNext(String tabla, String campo, Connection conn, int id_municipio, boolean inventario,boolean actualizar,long idobtenido) throws SQLException {
            long nuevo = 1;
            long valor = 0;
            long minimo = 0;
            long maximo = 0;
            long incremento = 0;
            String circular = "";
            String cadena = "";
            Statement stm ;
            ResultSet rs ;
            conn.setAutoCommit(false);
            if (inventario)
            	cadena = "select * from sequences_inventario where Upper(tablename)='" + tabla.toUpperCase() + "' and upper(field)='" + campo.toUpperCase() + "' and id_municipio = "+id_municipio+" FOR UPDATE";
            else
            	cadena = "select * from sequences where Upper(tablename)='" + tabla.toUpperCase() + "' and upper(field)='" + campo.toUpperCase() + "' FOR UPDATE";
            stm = conn.createStatement();
            rs = stm.executeQuery (cadena) ;

            if (!rs.next()) {
//            	stm.close();
            	rs.close();
            	if (inventario)
            		cadena="select max(id_sequence_inventario) as id from sequences_inventario";
            	else
            		cadena="select max(id_sequence) as id from sequences";
            	rs=stm.executeQuery (cadena);
            	rs.next();
            	long id=rs.getLong("id")+1;
//            	stm.close();
            	rs.close();
            	if (inventario)
            		cadena = "insert into sequences_inventario (id_sequence_inventario,tablename, field, incrementvalue, minimumvalue, maximumvalue, circular, value, id_municipio) " +
            			"values ('"+id+"', '" + tabla.toUpperCase() + "' ,'" + campo.toUpperCase() + "','1','1', '999999999','T',1, "+id_municipio+") ";
            	else
            		cadena = "insert into sequences (id_sequence,tablename, field, incrementvalue, minimumvalue, maximumvalue, circular, value) " +
        			"values ('"+id+"', '" + tabla.toUpperCase() + "' ,'" + campo.toUpperCase() + "','1','1', '999999999','T',1) ";
            	stm = conn.createStatement();
                stm.execute (cadena) ;
                conn.commit();
                try{rs.close();}catch(Exception e){} ;
                try{stm.close();} catch(Exception e){} ;
            } else {
                incremento = rs.getInt ("incrementvalue") ;
                minimo = rs.getInt ("minimumvalue") ;
                maximo = rs.getInt ("maximumvalue") ;
                circular = rs.getString("circular");
                valor = rs.getInt ("value");

                if (rs.wasNull()) {
                    valor = rs.getInt ("minimumvalue" ) ;
                    nuevo = valor;
                } else {
                	if (idobtenido==0)
                		nuevo = valor + incremento;
                	else
                		nuevo = idobtenido + incremento;
                }

                if (nuevo > maximo) {
                    if (CIRCULAR.equals(circular)) {
                        nuevo = minimo;
                    } else {
                        try{rs.close();}catch(Exception e){} ;
                        try{stm.close();} catch(Exception e){} ;
                        throw new SQLException ("Valor máximo superado para " + tabla.toUpperCase() + " y " + campo.toUpperCase());
                    }
                }
                if (actualizar){
	                if (inventario)
	                	cadena = "update sequences_inventario set value=" + nuevo + " where tablename='" + tabla.toUpperCase() + "' and field='" + campo.toUpperCase() + "' and id_municipio = "+id_municipio;
	                else
	                	cadena = "update sequences set value=" + nuevo + " where tablename='" + tabla.toUpperCase() + "' and field='" + campo.toUpperCase()+"'";
	                if (stm.executeUpdate(cadena) != 1) {
	                    try{rs.close();}catch(Exception e){} ;
	                    try{stm.close();} catch(Exception e){} ;
	                    throw new SQLException ("No se pudo actualizar la secuencia para la " + tabla.toUpperCase() + " y " + campo.toUpperCase());
	                }
	                conn.commit();
                }
                try {rs.close() ;}catch(java.sql.SQLException e){logger.error("Error al cerrar rs");};
                try {stm.close() ;}catch(java.sql.SQLException e){logger.error("Error al cerrar stm");};
            }

            return nuevo ;
    }
    
    public static void actualizarSecuencia(String tabla, String campo, Connection conn, int id_municipio, boolean inventario,long nuevo){
    	
    	String cadena = "";
    	
    	 Statement stm=null ;
               
         try{
        	 	conn.setAutoCommit(false);
	            if (inventario)
	            	cadena = "update sequences_inventario set value=" + nuevo + " where tablename='" + tabla.toUpperCase() + "' and field='" + campo.toUpperCase() + "' and id_municipio = "+id_municipio;
	            else
	            	cadena = "update sequences set value=" + nuevo + " where tablename='" + tabla.toUpperCase() + "' and field='" + campo.toUpperCase()+"'";
	            stm = conn.createStatement();
	            if (stm.executeUpdate(cadena) != 1) {	               
	                throw new SQLException ("No se pudo actualizar la secuencia para la " + tabla.toUpperCase() + " y " + campo.toUpperCase());
	            }
	            conn.commit();
         }
         catch (Exception e){
        	 logger.error("Excepcion:",e);
         }
         finally{        	 
             try{stm.close();} catch(Exception e){} ;
         }
    }

    

}