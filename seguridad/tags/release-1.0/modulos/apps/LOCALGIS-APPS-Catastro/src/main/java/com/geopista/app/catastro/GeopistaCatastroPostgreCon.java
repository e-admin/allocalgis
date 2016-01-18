package com.geopista.app.catastro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.geopista.app.AppContext;

public class GeopistaCatastroPostgreCon 
{
 public Connection conn = null;


 public GeopistaCatastroPostgreCon()
  {
      try
      {
        conn = getDBConnection();
      }
      catch(Exception e)
      {
        e.printStackTrace();
      }
  }

 public static Connection getDBConnection () throws SQLException
  {
    AppContext app =(AppContext) AppContext.getApplicationContext();
    Connection con=  app.getConnection();
    con.setAutoCommit(false);
    return con;
  }  
  
public ArrayList DatosIdentificacion(String Construcc, String cargo)
{
  ArrayList Datos = new ArrayList(); 
  try
  {
//    String query = "select ID_Distrito, Numero_Cargo, Primer_Caracter_Control, Segundo_Caracter_Control, Coeficiente_Participacion, Numero_Fijo_Inmueble from     Cargos where ID_Construccion=?";
    PreparedStatement s = null;
    ResultSet r = null;
    s = conn.prepareStatement("catconsdatosidentificacion");
    Construcc= Construcc + cargo;
    System.out.println(Construcc);
    s.setString(1, Construcc);
    if(!s.execute())
    {
      System.out.println("No ejecuta");
    }
    else{
      r = s.executeQuery();
      while (r.next())
      {
        Datos.add(r.getString(1));   
        Datos.add(r.getString(2));   
        Datos.add(r.getString(3)); 
        Datos.add(r.getString(4));   
        Datos.add(r.getString(5));   
        Datos.add(r.getString(6)); 
      }
    }
    s.close();
    r.close();
    conn.close();
    return Datos;
  } catch (Exception ex)
  {
    System.out.println("Error");
    System.out.println(ex.getMessage());
    ex.printStackTrace();
    return null;
  }//catch  
}

public ArrayList DatosLocalizacion(String Construcc, String cargo)
{
  ArrayList Datos = new ArrayList(); 
  int ID_Via=0;
  try
  {
  
  //  String query = "select * from  Cargos where ID_Construccion=?";
    PreparedStatement s = null;
    ResultSet r = null;
    s = conn.prepareStatement("catconscargo");
    Construcc = Construcc+cargo;
    s.setString(1, Construcc);
    if(!s.execute())
    {
      System.out.println("No ejecuta");
    }
    else{
      r = s.executeQuery();

      while (r.next())
      {
          Datos.add(r.getString("Codigo_Entidad_Menor"));   
          Datos.add(r.getString("Codigo_Postal")); 
          Datos.add(r.getString("Primer_Numero")); 
          Datos.add(r.getString("Primera_Letra")); 
          Datos.add(r.getString("Segundo_Numero")); 
          Datos.add(r.getString("Segunda_Letra")); 
          Datos.add(r.getString("Bloque")); 
          Datos.add(r.getString("Kilometro")); 
          Datos.add(r.getString("Direccion_No_Estructurada")); 
          Datos.add(r.getString("Escalera")); 
          Datos.add(r.getString("Planta")); 
          Datos.add(r.getString("Puerta")); 
          ID_Via =Integer.parseInt(r.getString("ID_Via").toString());
      }
    }
//    query = "select codigocatastro,tipovianormalizadocatastro,nombrecatastro from vias where id_via=" + ID_Via;
    s = null;
    r = null;
    s = conn.prepareStatement("catconsvia");
    s.setInt(1,ID_Via);
    if(!s.execute())
    {
      System.out.println("No ejecuta");
    }
    else{
        r = s.executeQuery();
        while (r.next())
        {
            Datos.add(r.getString(1));   
            Datos.add(r.getString(2));
            Datos.add(r.getString(3));
            r.next();                
        }
   }    
   s.close();
   r.close(); 
   conn.close();
   return Datos;
    
  }catch (Exception ex)
  {
    System.out.println("Error");
    System.out.println(ex.getMessage());
    ex.printStackTrace();
    return null;
  }//catch  
}


public ArrayList DatosTitular(String Construcc, String cargo)
{
  ArrayList Datos = new ArrayList(); 
  int ID_Titular =0;
  try
  {
//    String query = "select ID_Titular from  Cargos where ID_Construccion=?";
    PreparedStatement s = null;
    ResultSet r = null;

    s = conn.prepareStatement("catconsgetidtitular");
    Construcc = Construcc+cargo;
    s.setString(1, Construcc);
    if(!s.execute())
    {
      System.out.println("No ejecuta");
    }
    else{
      r = s.executeQuery();
       while (r.next())
      {
        ID_Titular=Integer.parseInt(r.getString(1).toString());
        r.next();
      }
    }
//    query = "select * from Titulares where id_titular=" + ID_Titular;
    s = null;
    r = null;
    s = conn.prepareStatement("catconstitular");
    s.setInt(1, ID_Titular);
    if(!s.execute())
    {
      System.out.println("No ejecuta");
    }
    else{
        r = s.executeQuery();
        while (r.next())
        {
            Datos.add(r.getString("NIF"));   
            Datos.add(r.getString("Caracter_Control_NIF")); 
            Datos.add(r.getString("Personalidad")); 
            Datos.add(r.getString("Identidad")); 
        }
    }
   s.close();
   r.close(); 
   conn.close();
   return Datos;
    
  }catch (Exception ex)
  {
    System.out.println("Error");
    System.out.println(ex.getMessage());
    ex.printStackTrace();
    return null;
  }//catch 
}

public ArrayList DatosLocalizacionTitular(String Construcc, String cargo)
{
  ArrayList Datos = new ArrayList(); 
  int ID_Titular =0;
  int ID_Via =0;
  try
  {
//   String query = "select ID_Titular from  Cargos where ID_Construccion=?";
    PreparedStatement s = null;
    ResultSet r = null;
    s = conn.prepareStatement("catconsgetidtitular");
    Construcc = Construcc+cargo;
    s.setString(1, Construcc);
    if(!s.execute())
    {
      System.out.println("No ejecuta");
    }
    else{
        r = s.executeQuery();
         while (r.next())
        {
          ID_Titular=Integer.parseInt(r.getString(1).toString());
          r.next();
        }
    }
//    query = "select * from Titulares where id_titular=" + ID_Titular;
    s = null;
    r = null;
    s = conn.prepareStatement("catconstitular");
    s.setInt(1, ID_Titular);
    if(!s.execute())
    {
      System.out.println("No ejecuta");
    }
    else{
      r = s.executeQuery();
      while (r.next())
      {
          Datos.add(r.getString("Codigo_Delegacion_MEH"));   
          Datos.add(r.getString("Codigo_Municipio_DGC")); 
          Datos.add(r.getString("Codigo_Provincia_INE"));   
          Datos.add(r.getString("Codigo_Municipio_INE"));      
          Datos.add(r.getString("Codigo_Postal")); 
          Datos.add(r.getString("Primer_Numero")); 
          Datos.add(r.getString("Primera_Letra")); 
          Datos.add(r.getString("Segundo_Numero")); 
          Datos.add(r.getString("Segunda_Letra")); 
          Datos.add(r.getString("Bloque")); 
          Datos.add(r.getString("Kilometro")); 
          Datos.add(r.getString("Direccion_No_Estructurada")); 
          Datos.add(r.getString("Escalera")); 
          Datos.add(r.getString("Planta")); 
          Datos.add(r.getString("Puerta")); 
          Datos.add(r.getString("Apartado_Correos")); 
          Datos.add(r.getString("Pais")); 
          Datos.add(r.getString("Provincia")); 
          Datos.add(r.getString("Nombre_Municipio")); 
          ID_Via =Integer.parseInt(r.getString("ID_Via").toString());
      }
    }
//    query = "select codigocatastro,tipovianormalizadocatastro,nombrecatastro from vias where id_via=" + ID_Via;
    s = null;
    r = null;
    s = conn.prepareStatement("catconsvia");
    s.setInt(1,ID_Via);
    if(!s.execute())
    {
      System.out.println("No ejecuta");
    }
    else{
        r = s.executeQuery();
        while (r.next())
        {
            Datos.add(r.getString("codigocatastro"));
            Datos.add(r.getString("tipovianormalizadocatastro"));
            Datos.add(r.getString("nombrecatastro"));
            r.next();                
        }
  }    
   s.close();
   r.close(); 
   conn.close();
   return Datos;
    
  }catch (Exception ex)
  {
    System.out.println("Error");
    System.out.println(ex.getMessage());
    ex.printStackTrace();
    return null;
  }//catch 
}


public ArrayList DatosEconomicos (String Construcc, String cargo)
{
  ArrayList Datos = new ArrayList(); 
  try
  {
    //    String query = "select * from  Cargos where ID_Construccion=?";
    PreparedStatement s = null;
    ResultSet r = null;

    s = conn.prepareStatement("catconscargo");
    Construcc = Construcc+cargo;
    s.setString(1, Construcc);
    if(!s.execute())
    {
      System.out.println("No ejecuta");
    }
    else{
        r = s.executeQuery();  
        while (r.next())
        {
            Datos.add(r.getString("anio_valor_catastral")); 
            Datos.add(r.getString("valor_catastral"));   
            Datos.add(r.getString("valor_catastral_construccion"));      
            Datos.add(r.getString("valor_catastral_suelo")); 
            Datos.add(r.getString("Clave_Uso_DGC")); 
            Datos.add(r.getString("base_liquidable")); 
            Datos.add(r.getString("anio_ultima_revision")); 
            Datos.add(r.getString("anio_ultima_notificacion")); 
            Datos.add(r.getString("numero_ultima_notificacion")); 
            Datos.add(r.getString("superficie_elementos_constructivos")); 
            Datos.add(r.getString("superficie_suelo")); 
            Datos.add(r.getString("coeficiente_propiedad")); 
        }    
    }
   s.close();
   r.close(); 
   conn.close();
   return Datos;
    
  }catch (Exception ex)
  {
    System.out.println("Error");
    System.out.println(ex.getMessage());
    ex.printStackTrace();
    return null;
  }//catch 
}


 public ArrayList Cargos (String referencia)

{
  ArrayList resultado1 = new ArrayList(); 
  try
  {
    referencia= referencia + "%";
//    String query = "select numero_cargo from Cargos where ID_Construccion like '" + referencia + "'";
    PreparedStatement s = null;
    ResultSet r = null;

    s = conn.prepareStatement("catconsgetnumerocargo");
    s.setString(1,referencia);
    if(!s.execute())
    {
      System.out.println("No ejecuta");
    }
    else{
          r = s.executeQuery();
          while (r.next())
          {
              resultado1.add(r.getString(1));   
              r.next();                
          }
    }
    s.close();
    r.close();
    conn.close();
    return resultado1;
    
   }catch (Exception ex)
   {
    System.out.println("Error");
    System.out.println(ex.getMessage());
    ex.printStackTrace();
    return null;
   }//catch 
 }
}