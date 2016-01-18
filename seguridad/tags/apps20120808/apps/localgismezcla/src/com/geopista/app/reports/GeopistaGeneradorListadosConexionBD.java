package com.geopista.app.reports;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;

import com.geopista.app.AppContext;



/**
 * Clase de Conexion para recoger los datos necesarios para mostrarlos en 
 * los combos y Listbox, para generar las plantillas de los Listados.
 */
public class GeopistaGeneradorListadosConexionBD 
{
  private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
  
/*  public static Connection getDBConnection (String username, String password, 
  String thinConn, String driverClass) throws SQLException{
    Connection con =null;
    try 
    {
      Class.forName(driverClass);    
      con= DriverManager.getConnection(thinConn, username, password);
      con.setAutoCommit(false);
    }catch (Exception e){
        e.printStackTrace();
    }finally
      {
        return con;        
      }
 }

*/

/**
 * Método que devuelve un ArrayList con las capas de la tabla "tables"
 * para seleccionar las capas de las que se podrá generar información  en los listados.
 * @retun ArrayList capasGeopista Listado de las capas
 */

public ArrayList capasGeopista()
{
  Connection con =null;
  PreparedStatement ps=null;
  ResultSet r =null;
  String codigovia = "";
  ArrayList Datos = new ArrayList(); 
  
  try
  {
     con = abrirConexion();
     ps = con.prepareStatement("capasTablas");       
     if (!ps.execute()){
     } else{
        r= ps.getResultSet();
     }

    while (r.next()){
        Datos.add(r.getString("name"));
    }
    return Datos;
  } catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }//catch  
  finally{
      aplicacion.closeConnection(con,ps,null,r);
  }
}








/**
 * Método que devuelve un ArrayList con las capas de la tabla "tables"
 * para seleccionar las capas de las que se podrá generar información  * en los listados.
 * @param String nombre de la tabla
 * @return ArrayList campoDeUnaTabla, con la lista de campos
 */
 //Cambio
public ArrayList camposDeUnaTabla(String nombre)

{
  Connection con =null;
  PreparedStatement ps=null;
  ResultSet r =null;
  String codigovia = "";
  ArrayList Datos = new ArrayList(); 

  try
  {
     con = abrirConexion();
     ps = con.prepareStatement("camposSinGeometria");       
     ps.setString(1,nombre);

     if (!ps.execute()){
     } else{
        r= ps.getResultSet();
     }
    while (r.next())
    {
        Datos.add(r.getString("nombre"));   
    }
    
    return Datos;
  } catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }//catch  

  finally{
      aplicacion.closeConnection(con,ps,null,r);
  }
}

/**
 * Método que devuelve un ArrayList con las capas de la tabla "tables"
 * que tienen geometria para poder realizar las consultas espaciales.
 */
public ArrayList tablasConGeometria()

{
  Connection con =null;
  PreparedStatement ps=null;
  ResultSet r =null;
  String codigovia = "";
  ArrayList Datos = new ArrayList(); 

  try
  {
     con = abrirConexion();
     ps = con.prepareStatement("tablasconGeometria");       

     if (!ps.execute()){
     } else{
        r= ps.getResultSet();
     }

    while (r.next())
    {
        Datos.add(r.getString("name"));   
    }
    
    return Datos;
  } catch (Exception ex)
  {
   
 
    ex.printStackTrace();
    return null;
  }//catch  

  finally{
      aplicacion.closeConnection(con,ps,null,r);
  }
}

/**
 * Método que devuelve un String con el tipo de datos del campo
 * @param String tabla, nombre de la tabla
 * @param String campo, nombre del campo
 * @int tipoDatosCampo, el tipo de datos
 */
public int tipoDatosCampo(String tabla, String campo)

{
  int resultado = -1;
    Statement s = null;
    ResultSet r = null;
    ResultSetMetaData rM = null;
    Connection conn=null;

  try
  {
    String query = "select " + campo + " From " + tabla;

     conn = this.abrirConexionPostgres();// getDBConnection (user,pass,url, driver);

    s = conn.createStatement();
    r = s.executeQuery(query);
    rM = r.getMetaData();
    resultado =  rM.getColumnType(1);
    return resultado;
  } catch (Exception ex)
  {
    ex.printStackTrace();
    return -1;
  }//catch  
  finally{
          try {
            Enumeration e = DriverManager.getDrivers();
              while (e.hasMoreElements())
              {
               DriverManager.deregisterDriver((Driver)e.nextElement());
              }
               DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());           
               aplicacion.closeConnection(conn,null,s,r);
          }catch (Exception e){}
    }
  
}


/**
 * Método que comprueba las condiciones sql
 * @param String sql
 * @return String compruebaCondiciones
 */
public String compruebaCondiciones(String sql)

{
  String Resultado  = "N"; 
  Statement s = null;
  ResultSet r = null;
  Connection conn=null;

  try
  {
    
     conn = this.abrirConexionPostgres();

    s = conn.createStatement();
    r = s.executeQuery(sql);

    if (r.next())
    {

        Resultado="S";   
    }
    
    return Resultado;
  } catch (Exception ex)
  {
   
    Resultado="N";
    ex.printStackTrace();
    return Resultado;
  }//catch  
  finally{
        try {
            Enumeration e = DriverManager.getDrivers();
              while (e.hasMoreElements())
              {
               DriverManager.deregisterDriver((Driver)e.nextElement());
              }
               DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());           
               aplicacion.closeConnection(conn,null,s,r);
          }catch (Exception e){}
  }
}


public Connection abrirConexion() throws SQLException
{
    Connection conn = null;
try {
 //Quitamos los drivers
      Class.forName("com.geopista.sql.GEOPISTADriver");
      String sConn = aplicacion.getString("geopista.conexion.url");
      conn = DriverManager.getConnection(sConn);     
   
  AppContext app = (AppContext) AppContext.getApplicationContext();
  conn= app.getConnection();
  conn.setAutoCommit(false);
  } catch (Exception e ) {return null;}
  
  return conn;
 
}



/**
 * Método que devuelve si un campo tiene dominio o no lo tiene
 * @param String tabla, nombre de la tabla
 * @param String campo, nombre del campo
 * @return int campoDominio, -1 si el campo no tiene dominio
 */

public int campoConDominio(String tabla, String campo)

{
  int  dato = -1; //Implica que no hay dominios 
    Connection conn = null;
    String sql = " select  c.id_domain from columns c, tables t, domains d, domainnodes nd where t.id_table = c.id_table and c.id_domain = d.id and d.id = nd.id_domain and nd.type = 4 and c.name='" + campo +"' and t.name='"+ tabla + "'";
    Statement s = null;
    ResultSet r = null;

  try
  {
    conn = this.abrirConexionPostgres();// (user,pass,url, driver);
    s = conn.createStatement();
    r = s.executeQuery(sql);
    if (r.next())
    {
        dato=r.getInt(1);
    }
    
    return dato;
  } catch (Exception ex)
  {
    ex.printStackTrace();
     return -1;
  }//catch  
  finally{
           //Liberar todos los drivers y poner el de Geopista
          try {
            Enumeration e = DriverManager.getDrivers();
              while (e.hasMoreElements())
              {
               DriverManager.deregisterDriver((Driver)e.nextElement());
              }
               DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());           
               aplicacion.closeConnection(conn,null,s,r);
          }catch (Exception e){}
    }
}
 

/**
 * Método que devuelve el tipo de datos, para poner en la lista las operaciones disponibles
 * @param String tabla
 * @param String campo
 * @return int devolverTipoDatos, 
 */

  public int devolverTipoDatos(String tabla, String campo)
  {
    int resultado=-1;
    Connection conn = null;
    Statement stmt=null;
    ResultSet rs=null;

    try {
    //Quitamos los drives y registramos el de postgres
    //Borr
    String sql = "Select " + campo + " from " + tabla;
        try {
            conn =     this.abrirConexionPostgres();
            stmt = conn.createStatement();       /**Se crea una sentencia **/
            rs = stmt.executeQuery(sql);

            //Liberar todos los drivers y poner el de Geopista
            Enumeration e = DriverManager.getDrivers();
              while (e.hasMoreElements())
              {
               DriverManager.deregisterDriver((Driver)e.nextElement());
              }
               DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());           

            
        } catch (Exception e){
          e.printStackTrace();
          return resultado;
        }

          if (rs.next()){
            ResultSetMetaData rM = null;
            rM = rs.getMetaData();
            resultado =  rM.getColumnType(1);
            }
    
    } catch(Exception e){
      return resultado;
    }
    finally{
      //quitamos los drivers y registramos geopista.
           //Liberar todos los drivers y poner el de Geopista
          try {
            Enumeration e = DriverManager.getDrivers();
              while (e.hasMoreElements())
              {
               DriverManager.deregisterDriver((Driver)e.nextElement());
              }
               DriverManager.registerDriver(new com.geopista.sql.GEOPISTADriver());           
               aplicacion.closeConnection(conn,null,stmt,rs);
          }catch (Exception e){}
      
    }
    return resultado;
  }


public Connection abrirConexionPostgres() throws Exception {

    String url=aplicacion.getString("conexion.url");
    String user = aplicacion.getString("conexion.user");
    String pass =aplicacion.getUserPreference("conexion.pass","",false);
    String driver =aplicacion.getString("conexion.driver");
    Connection conn = null;
  try {
      Enumeration e = DriverManager.getDrivers();
      while (e.hasMoreElements())
      {
       DriverManager.deregisterDriver((Driver)e.nextElement());
      }
      DriverManager.registerDriver(new org.postgresql.Driver());
     conn = getDBConnection (user,pass,url, driver);
  }catch (Exception e){e.printStackTrace();}
    return conn;
};

public static Connection getDBConnection (String username, String password,
  String thinConn, String driverClass) throws SQLException{
    Connection con =null;
    try
    {
      Class.forName(driverClass);
      con= DriverManager.getConnection(thinConn, username, password);
      con.setAutoCommit(false);
    }catch (Exception e){
        e.printStackTrace();
    }finally
      {
        return con;
      }
} 


} // De la clase 

