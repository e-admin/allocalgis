package com.geopista.app.editor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.geopista.app.AppContext;


public class GeopistaConsultasPostgreCon 
{
  private Connection con = null;

  public GeopistaConsultasPostgreCon(){
    try{
        con = getDBConnection();
    }
    catch (Exception e){
    e.printStackTrace();
    }
  }


public static Connection getDBConnection() throws SQLException {

  AppContext app = (AppContext) AppContext.getApplicationContext();
  Connection conn= app.getConnection();
  conn.setAutoCommit(false);
  return conn;
}  

public ArrayList nombreCamposDeLayer(String nombreLayer)

{
  ArrayList Datos = new ArrayList(); 
  try
  {
    ResultSet r =null;
    PreparedStatement ps = con.prepareStatement("editorconsultanombrecampolayer");
    ps.setString(1,nombreLayer);
    if (!ps.execute()){
      // System.out.println("no ejecuta");
    }else{
      r= ps.getResultSet();
    }
    while (r.next()){
      Datos.add(r.getString(1));
    }
    return Datos;
  } catch (Exception ex)
  {

    ex.printStackTrace();
    return null;
  }//catch  
}


}
