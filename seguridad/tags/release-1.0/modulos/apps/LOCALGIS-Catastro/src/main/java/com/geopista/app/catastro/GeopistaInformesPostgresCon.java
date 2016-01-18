package com.geopista.app.catastro;

import com.geopista.app.AppContext;

//import java.sql.*;

import java.sql.DriverManager;
import java.sql.Driver;
import java.util.Enumeration;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.util.*;
import java.lang.*;
import org.postgresql.*;
import java.util.HashMap;

public class GeopistaInformesPostgresCon
{
private AppContext aplicacion = (AppContext) AppContext.getApplicationContext();


/**
 * Obtiene el distrito censal de una parcela
 * @param int parcela, identificador de la parcela
 * @return string distrito, devuelve el distrito censal
 */
public String distritoCensal(int parcela) //el que vale

{
  String Datos =""; 
  Connection con =null;
  PreparedStatement ps=null;
  ResultSet r =null;
  try
  {
  
    con = this.abrirConexion();
    ps = con.prepareStatement("catastroInformesDistritosCensales");
    ps.setInt(1,parcela);

    if (!ps.execute()){
      // System.out.println("no ejecuta");
    }else{
      r= ps.getResultSet();
    }
    while (r.next()){
      Datos=r.getString(1);
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
 * Busca el codigo de via de una parcela
 * @param int parcela
 * @return String codigoVia
 */

public String codigoVia(int parcela)
{
  Connection con =null;
  PreparedStatement ps=null;
  ResultSet r =null;
  String codigovia = "";

  try
  {
     con = abrirConexion();
     ps = con.prepareStatement("catastroInformesCodigoVia");       
     ps.setInt(1,parcela);

     if (!ps.execute()){
     } else{
        r= ps.getResultSet();
     }

    while (r.next()){
      codigovia=r.getString(1);
    }
    return codigovia;
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
 * Busca el nombre de via de una parcela
 * @param int parcela
 * @return String nombre de la via
 */

public String nombreVia(int parcela)
{
  Connection con =null;
  PreparedStatement ps=null;
  ResultSet r =null;
  String codigovia = "";

  try
  {
     con = abrirConexion();
     ps = con.prepareStatement("catastroNombreVia");       
     ps.setInt(1,parcela);

     if (!ps.execute()){
     } else{
        r= ps.getResultSet();
     }

    while (r.next()){
      codigovia=r.getString(1);
    }
    return codigovia;
  } catch (Exception ex)
  {
    ex.printStackTrace();
    return null;
  }//catch  
  finally{
      aplicacion.closeConnection(con,ps,null,r);
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


}



