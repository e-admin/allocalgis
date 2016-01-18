package com.geopista.app.catastro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import com.geopista.app.AppContext;


public class CatastroRusticaActualizarPostgre
{
	
   public Connection conn = null;


 public CatastroRusticaActualizarPostgre()
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

public String ActualizarTitular (int ID, ArrayList Titular, ArrayList TitularTipo)
{
  try
  {
  	
    /*String query = "Update titularesrusticos set id_titular=?, numeroimputacion=?, coeficienteparticipacion=?," +
                  " personalidad=?, nif=?, caracter_control_nif=?, nombre=?, claveexencion=?, "+
                  " codigo_delegacion_meh=?, codigo_municipio_dgc=?," +
                  " primer_numero=?, primera_letra=?, segundo_numero=?, segunda_letra=?, kilometro=?," + 
                  " bloque=?, direccion_no_estructurada=?, escalera=?, planta=?, puerta=?,codigo_postal=?" + 
                  " where id_titular=" + ID;*/
    PreparedStatement s = null;
    s = conn.prepareStatement("catrusedactualizartitular");
    Iterator Datos = Titular.iterator();
    Iterator TipoDatos = TitularTipo.iterator();
    int n =0;
    while (TipoDatos.hasNext())
    {
        n=n+1;
        switch (Integer.parseInt(TipoDatos.next().toString())){
        case 0:
           s.setInt(n,Integer.parseInt(Datos.next().toString()));
           break;
        case 1:
            s.setString(n,Datos.next().toString());
            break;
       case 2:
            break;
        default:
          break;
      }    
    }
    s.setInt(n+1, ID);
   s.executeUpdate();  
   conn.commit();
   s.close();
   conn.close();
  return "Correcto";
  }catch (Exception ex)
  {
    System.out.println("Error");
    System.out.println(ex.getMessage());
    ex.printStackTrace();
      return "Error";
  }//catch  
 }

public ArrayList DatosTitular (int ID_Titular)
{
  ArrayList Datos = new ArrayList(); 
  int ID_Via=0;
  try
  {
  
  //  String query = "select * from  titularesrusticos where id_titular=?";
    PreparedStatement s = null;
    ResultSet r = null;
    
    s = conn.prepareStatement("catrusedtitulares");
    s.setInt(1, ID_Titular);
    r = s.executeQuery();  

    while (r.next())
    {

        Datos.add(r.getString("numeroimputacion")); 
        Datos.add(r.getString("coeficienteparticipacion")); 
        Datos.add(r.getString("personalidad")); 
        Datos.add(r.getString("nif")); 
        Datos.add(r.getString("caracter_control_nif"));   
        Datos.add(r.getString("nombre")); 
        Datos.add(r.getString("claveexencion")); 
        Datos.add(r.getString("codigo_delegacion_meh")); 
        Datos.add(r.getString("codigo_municipio_dgc")); 
        Datos.add(r.getString("primer_numero"));      
        Datos.add(r.getString("primera_letra")); 
        Datos.add(r.getString("segundo_numero")); 
        Datos.add(r.getString("segunda_letra")); 
        Datos.add(r.getString("kilometro")); 
        Datos.add(r.getString("bloque")); 
        Datos.add(r.getString("direccion_no_estructurada")); 
        Datos.add(r.getString("escalera"));   
        Datos.add(r.getString("planta"));      
        Datos.add(r.getString("puerta")); 
        Datos.add(r.getString("codigo_postal"));     
        ID_Via=Integer.parseInt(r.getString("id_via").toString());
    }     

//    query = "select tipovianormalizadocatastro,nombrecatastro from vias where id_via=" + ID_Via;
    s = null;
    r = null;
  
    s = conn.prepareStatement("catconsvia");
    s.setInt(1, ID_Via);
    r = s.executeQuery();
    while (r.next())
    {
        Datos.add(r.getString(1) + r.getString(2));   
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
finally
{
	return Datos;
}

  
}

 public String AltaTitular (ArrayList Titular, ArrayList TitularTipo)
{
  try
  {

//    String query ="select max(id_titular) from titularesrusticos";
    PreparedStatement s = null;
    ResultSet r = null;
    s = conn.prepareStatement("catrusedidtitular");
    r = s.executeQuery();  
    int ID_Nuevo=0;
    while (r.next())
    {
        ID_Nuevo= Integer.parseInt(r.getString(1).toString());             
    }

    s = null;
    r = null;
    
/*    query = "Insert into titularesrusticos "+
                  "(id_titular, numeroimputacion, coeficienteparticipacion, personalidad," +
                  "nif,caracter_control_nif, nombre, claveexencion,codigo_delegacion_meh, codigo_municipio_dgc," + //4
                  "primer_numero, primera_letra, segundo_numero, segunda_letra, kilometro," +  //5
                  " bloque, direccion_no_estructurada, escalera, planta, puerta,codigo_postal) " + //6
                  "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";*/


    s = null;
    r = null;
    s = conn.prepareStatement("catrusednuevotitular");
    s.setInt(1, ID_Nuevo+1);
    Iterator Datos = Titular.iterator();
    Iterator TipoDatos = TitularTipo.iterator();
    int n =1;
    TipoDatos.next();
    Datos.next();
    while (TipoDatos.hasNext())
    {
        n=n+1;
        switch (Integer.parseInt(TipoDatos.next().toString())){
        case 0:
           s.setInt(n,Integer.parseInt(Datos.next().toString()));
           break;
        case 1:
            s.setString(n,Datos.next().toString());
            break;
       case 2:
            break;
        default:
          break;
      }    
              System.out.println(n);
    }
   s.executeUpdate();  
   conn.commit();
   s.close();
   conn.close();
  return "Correcto";
  }catch (Exception ex)
  {
    System.out.println("Error");
    System.out.println(ex.getMessage());
    ex.printStackTrace();
      return "Error";
  }//catch  
 }

public String BajaTitular (int ID)
{
  try
  {
  	
//    String query = "Delete from titularesrusticos  where ID_Titular =" + ID;
    PreparedStatement s = null;
  
    s = conn.prepareStatement("catrusedbajatitular");
    s.setInt(1,ID);
    s.executeUpdate();  
    conn.commit();
    s.close();
    conn.close();
   return "Correcto";
  }catch (Exception ex)
  {
    System.out.println("Error");
    System.out.println(ex.getMessage());
    ex.printStackTrace();
      return "Error";
  }//catch  
 }

 
public int ObtenerIDTitular (int ID_Parcela)
{
  try
  {
  	
//    String query = "select numeroimputacion from parcelasrusticas  where ID_Parcela =" + ID_Parcela;
    PreparedStatement s = null;
    ResultSet r = null;
  
    s = conn.prepareStatement("catrusedgetnumeroimputacion");
    s.setInt(1,ID_Parcela);
    r = s.executeQuery();  
    String NumeroImputacion="";
    while (r.next())
    {

        NumeroImputacion= r.getString("numeroimputacion");
    }     

//    query = "select ID_Titular from titularesrusticos where numeroimputacion='" + NumeroImputacion +"'";
    s = null;
    r = null;
  
    s = conn.prepareStatement("catrusedgetidtitular");
    s.setString(1,NumeroImputacion);    
    r = s.executeQuery();
    int ID_Titular=0;
    while (r.next())
    {
       ID_Titular= Integer.parseInt(r.getString(1).toString());   
      }

   s.close();
   r.close(); 
   conn.close();
   return ID_Titular;

 }catch (Exception ex)
  {
    System.out.println("Error");
    System.out.println(ex.getMessage());
    ex.printStackTrace();
    return 0;
  }
}

//SUBPARCELAS

 public String ActualizarSubparcela (int ID, ArrayList Subparcela, ArrayList SubparcelaTipo)
{
  try
  {
  	
/*    String query = "Update subparcelasrusticas set id_subparcela=?, id_parcela=?," + 
                  " letrasubparcela=?, tiposubparcela=?, coeficientefiscalf3=?, ejerciciofinalizacion=?, "+
                  " hectareas=?, areas=?, centiareas=?, " + 
                  " calificacionanterior=?, intensidadanterior=?," +
                  " calificacionactual=?, intensidadactual=?, referencia1=?, referencia2=?" +
                  " where ID_Subparcela=" + ID;*/
    PreparedStatement s = null;

    s = conn.prepareStatement("catrusedactualizarsubparcela");
    Iterator Datos = Subparcela.iterator();
    Iterator TipoDatos = SubparcelaTipo.iterator();
    int n =0;
    while (TipoDatos.hasNext())
    {
        n=n+1;
        switch (Integer.parseInt(TipoDatos.next().toString())){
        case 0:
           s.setInt(n,Integer.parseInt(Datos.next().toString()));
           break;
        case 1:
            s.setString(n,Datos.next().toString());
            break;
        default:
          break;
      }    
    }
    s.setInt(n+1, ID);
   s.executeUpdate();  
   conn.commit();
   s.close();
   conn.close();
  return "Correcto";
  }catch (Exception ex)
  {
    System.out.println("Error");
    System.out.println(ex.getMessage());
    ex.printStackTrace();
      return "Error";
  }//catch  
 }

 public String AltaSubparcela (ArrayList Subparcela, ArrayList SubparcelaTipo)
{
  try
  {

   // String query ="select max(id_subparcela) from subparcelasrusticas";
    PreparedStatement s = null;
    ResultSet r = null;
    Connection conn = getDBConnection ();
    s = conn.prepareStatement("catrusedidsubparcela");
    r = s.executeQuery();  
    int ID_Nuevo=0;
    while (r.next())
    {
        ID_Nuevo= Integer.parseInt(r.getString(1).toString());             
    }

    s = null;
    r = null;
    
/*    query = "Insert into subparcelasrusticas (id_subparcela, id_parcela," + 
                  " letrasubparcela, tiposubparcela, coeficientefiscalf3, ejerciciofinalizacion, "+
                  " hectareas, areas, centiareas, " + 
                  " calificacionanterior, intensidadanterior," +
                  " calificacionactual, intensidadactual, referencia1, referencia2, fechaalta)" +
                  "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";*/



    s = null;
    r = null;

    s = conn.prepareStatement("catrusednuevasubparcela");
    s.setInt(1, ID_Nuevo+1);
    Iterator Datos = Subparcela.iterator();
    Iterator TipoDatos = SubparcelaTipo.iterator();
    int n =1;
    TipoDatos.next();
    Datos.next();
    while (TipoDatos.hasNext())
    {
        n=n+1;
        switch (Integer.parseInt(TipoDatos.next().toString())){
        case 0:
           s.setInt(n,Integer.parseInt( Datos.next().toString()));
           break;
        case 1:
            s.setString(n, Datos.next().toString());
            break;
       case 2:
            break;
        default:
          break;
      }    
              System.out.println(n);
    }
    n=n+1;
    Calendar cal=Calendar.getInstance();
    s.setDate(n,new java.sql.Date(cal.getTime().getTime()));
    s.executeUpdate();  
    conn.commit();
    s.close();
    conn.close();
    return "Correcto";
  }catch (Exception ex)
  {
    System.out.println("Error");
    System.out.println(ex.getMessage());
    ex.printStackTrace();
      return "Error";
  }//catch  
 }

 
public ArrayList Subparcelas(int Referencia)
{
  ArrayList Datos = new ArrayList(); 
  try
  {
  
//    String    query = "select ID_subparcela from subparcelasrusticas where id_parcela=?";
    PreparedStatement s = null;
    ResultSet r = null;
  
    s = conn.prepareStatement("catrusedgetidsubparcela");
    s.setInt(1, Referencia);
    r = s.executeQuery();
    while (r.next())
    {
        Datos.add(r.getString("ID_Subparcela"));   

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


public ArrayList DatosSubparcela (int ID_Subparcela)
{
  ArrayList Datos = new ArrayList(); 
  try
  {
   
//    String query = "select * from  Subparcelasrusticas where ID_Subparcela=?";
    PreparedStatement s = null;
    ResultSet r = null;
  
    s = conn.prepareStatement("catrusedsubparcelas");
    s.setInt(1, ID_Subparcela);
    r = s.executeQuery();  
    while (r.next())
    {
        Datos.add(r.getString("letrasubparcela"));   
        Datos.add(r.getString("tiposubparcela"));      
        Datos.add(r.getString("coeficientefiscalf3")); 
        Datos.add(r.getString("ejerciciofinalizacion")); 
        Datos.add(r.getString("hectareas")); 
        Datos.add(r.getString("areas")); 
        Datos.add(r.getString("centiareas")); 
        Datos.add(r.getString("calificacionanterior")); 
        Datos.add(r.getString("intensidadanterior")); 
        Datos.add(r.getString("calificacionactual")); 
        Datos.add(r.getString("intensidadactual")); 
        Datos.add(r.getString("referencia1")); 
        Datos.add(r.getString("referencia2")); 
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
 public String BajaSubparcela (int ID)
{
  try
  {
  	
//    String query = "Update subparcelasrusticas set fecha_baja=? where ID_Subparcela=" + ID;
    PreparedStatement s = null;
  
    s = conn.prepareStatement("catrusedbajasubparcela");
    Calendar cal=Calendar.getInstance();
    s.setDate(1,new java.sql.Date(cal.getTime().getTime()));
    s.setInt(2,ID);
   s.executeUpdate();  
   conn.commit();
   s.close();
   conn.close();
  return "Correcto";
  }catch (Exception ex)
  {
    System.out.println("Error");
    System.out.println(ex.getMessage());
    ex.printStackTrace();
      return "Error";
  }//catch  
 }


//CONSTRUCCIONES

public ArrayList DatosConstrucciones (int Orden, int ID_Subparcela)
{
  ArrayList Datos = new ArrayList(); 
  try
  {
   
    
//    String query = "select * from  construccionesrusticas where id_subparcela=? AND numeroorden=?";
    PreparedStatement s = null;
    ResultSet r = null;

    s = conn.prepareStatement("catrusedconstrucciones"); 
    s.setInt(1,ID_Subparcela);   
    s.setInt(2,Orden);  
    r = s.executeQuery();  
    while (r.next())
    {
        Datos.add(r.getString("id_construccion")); 
        Datos.add(r.getString("numeroorden"));         
        Datos.add(r.getString("largo")); 
        Datos.add(r.getString("ancho"));   
        Datos.add(r.getString("alto"));      
        Datos.add(r.getString("superficievolumen")); 
        Datos.add(r.getString("anio_construccion")); 
        Datos.add(r.getString("exactitudanio")); 
        Datos.add(r.getString("tipologia")); 
        Datos.add(r.getString("anio_reforma")); 
        Datos.add(r.getString("tipo_reforma")); 
        Datos.add(r.getString("usopredominante")); 
        Datos.add(r.getString("cargassingulares")); 
        Datos.add(r.getString("estadoconservacion")); 
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

 public String AltaConstruccion (int ID_Subparcela, ArrayList Subparcela, ArrayList SubparcelaTipo)
{
  try
  {

 //   String query ="select max(id_construccion) from construccionesrusticas";
    PreparedStatement s = null;
    ResultSet r = null;
   
    s = conn.prepareStatement("catrusedidconstruccion");
    r = s.executeQuery();  
    int ID_Nuevo=0;
    while (r.next())
    {
        ID_Nuevo= Integer.parseInt(r.getString(1).toString());             
    }

    s = null;
    r = null;
   /* query = "Insert into construccionesrusticas " +
                  "(id_construccion, id_subparcela, numero_orden," + //3
                  " largo, ancho, alto, superficievolumen,"+
                  " anio_construccion, exactitudanio, tipologia, anio_reforma," +
                  "tipo_reforma, usopredominante, cargassingulares," +
                  "estadoconservacion, fechaalta)" +
                  "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";*/


    s = conn.prepareStatement("catrusednuevaconstruccion");
    s.setInt(1, ID_Nuevo);

    Iterator Datos = Subparcela.iterator();
    Iterator TipoDatos = SubparcelaTipo.iterator();
    int n =1;
    TipoDatos.next();
    Datos.next();
    while (TipoDatos.hasNext())
    {
        n=n+1;
        switch (Integer.parseInt(TipoDatos.next().toString())){
        case 0:
           s.setInt(n,Integer.parseInt(Datos.next().toString()));
           break;
        case 1:
            s.setString(n,Datos.next().toString());
            break;
       case 2:
            break;
        default:
          break;
      }    
              System.out.println(n);
    }
    n=n+1;
    Calendar cal=Calendar.getInstance();
    s.setDate(n,new java.sql.Date(cal.getTime().getTime()));
   s.executeUpdate();  
   conn.commit();
   s.close();
   conn.close();
  return "Correcto";
  }catch (Exception ex)
  {
    System.out.println("Error");
    System.out.println(ex.getMessage());
    ex.printStackTrace();
      return "Error";
  }//catch  
 }
 public String ActualizarConstruccion (int ID, ArrayList UC, ArrayList UCTipo)
{
  try
  {
  	
/*    String query = "Update construccionesrusticas set id_construccion=?, "+
                  " id_subparcela=?, numero_orden=?," + 
                  " largo=?, ancho=?, alto=?, superficievolumen=?,"+
                  " anio_construccion=?, exactitudanio=?, tipologia=?, anio_reforma=?," +
                  "tipo_reforma=?, usopredominante=?, cargassingulares=?," +
                  "estadoconservacion=? where id_construccion=" + ID;*/
    PreparedStatement s = null;
    s = conn.prepareStatement("catrusedactualizarconstruccion");
    Iterator Datos = UC.iterator();
    Iterator TipoDatos = UCTipo.iterator();
    int n =0;
    while (TipoDatos.hasNext())
    {
        n=n+1;
        switch (Integer.parseInt(TipoDatos.next().toString())){
        case 0:
           s.setInt(n,Integer.parseInt(Datos.next().toString()));
           break;
        case 1:
            s.setString(n,Datos.next().toString());
            break;
       case 2:
            break;
        default:
          break;
      }    
    }
    s.setInt(n+1,ID);
   s.executeUpdate();  
   conn.commit();
   s.close();
   conn.close();
  return "Correcto";
  }catch (Exception ex)
  {
    System.out.println("Error");
    System.out.println(ex.getMessage());
    ex.printStackTrace();
      return "Error";
  }//catch  
 }

public String BajaConstruccion (int ID)
{
  try
  {
  	
//    String query = "Update construccionesrusticas set fecha_baja=? where ID_Construccion=" + ID;
    PreparedStatement s = null;
  
    s = conn.prepareStatement("catrusedbajaconstruccion");
//    s.setString(1,"B");
    Calendar cal=Calendar.getInstance();
    s.setDate(1,new java.sql.Date(cal.getTime().getTime()));
    s.setInt(2,ID);
   s.executeUpdate();  
   conn.commit();
   s.close();
   conn.close();
  return "Correcto";
  }catch (Exception ex)
  {
    System.out.println("Error");
    System.out.println(ex.getMessage());
    ex.printStackTrace();
      return "Error";
  }//catch  
 }

public ArrayList Construcciones(int ID_Subparcela)
{
  ArrayList Datos = new ArrayList(); 
  try
  {
    
//    String query = "select numeroorden from construccionesrusticas where id_subparcela=" + ID_Subparcela;
    PreparedStatement s = null;
    ResultSet r = null;
  
    s = conn.prepareStatement("catrusedgetnumeroorden");
    s.setInt(1,ID_Subparcela);
    r = s.executeQuery();
    while (r.next())
    {
        Datos.add(r.getString("numeroorden"));   
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



}