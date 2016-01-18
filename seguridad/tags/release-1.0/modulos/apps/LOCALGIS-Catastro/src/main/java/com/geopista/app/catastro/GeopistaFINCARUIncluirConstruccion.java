package com.geopista.app.catastro;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;

import com.vividsolutions.jump.util.Blackboard;

import java.lang.Integer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import javax.swing.JTable;

/** Esta clase, contiene los métodos necesarios para trabajar con la tabla construccionesrusticas.
 *  Contiene métodos que Insertan/Eliminan datos de construcciones dependiendo de que la
 *  información en el fichero de texto sobre construcciones rústicas sea correcta o incorrecta.
 */

public class GeopistaFINCARUIncluirConstruccion
{
  private static ApplicationContext app = AppContext.getApplicationContext();

  public Connection con = null;

  public GeopistaFINCARUIncluirConstruccion()
  {
    try
    {
      con = getDBConnection();
    }
    catch(Exception e)
    {
      e.printStackTrace();
    }
  }
  /**
      * Realiza la conexión con la base de datos
      * @return Connection - Devuelve la conexión establecida con la base de datos
  */
  public static Connection getDBConnection () throws SQLException
  {

    Connection conn=  app.getConnection();
    conn.setAutoCommit(false);
    return conn;
  }
	/**
		* Inserta en la tabla construccionesrusticas los datos encontrados en el fichero de texto que el usuario ha seleccionado
		* para importar.
		* @param Linea Es la línea encontrada en el fichero de texto que contiene los datos de construcciones
		* @param ID_Subparcela Identificador de Subparcela que corresponde a esta construccion
		* @return Devuelve el identificador de construcción insertada en la base de datos. Devuelve 0 cuando se han producido errores y la construccion no se ha insertado
	*/
    public int IncluirConstruccionRustica (String Linea, int ID_Subparcela)
    {

    try
    {
      String consulta = "insert into construccionesrusticas (numeroorden,largo,ancho,alto,superficievolumen,anio_construccion,exactitudanio,tipologia,anio_reforma,tipo_reforma,usopredominante,estadoconservacion,cargassingulares,id_construccion,id_subparcela,fechaalta)";
      String[] columnNames = {"Tipo","Inicio","Fin"};
      Object[][] data =
      {
          {"0", "25","28"},
          {"0", "42","45"},
          {"0", "45","48"},
          {"0", "48","50"},
          {"0", "50","56"},
          {"0", "56","60"},
          {"1", "60","61"},
          {"1", "61","66"},
          {"0", "66","70"},
          {"1", "70","71"},
          {"1", "71","72"},
          {"1", "72","73"},
          {"0", "73","76"}};

        JTable subparcela = new JTable(data, columnNames);

        ResultSet r = null;
        PreparedStatement ps = con.prepareStatement("catfincarumaxconstrucciones");
        int ID_Construccion=0;

        if(ps.execute())
        {
          r  = ps.getResultSet();
          while( r.next())
          {
              ID_Construccion = Integer.parseInt(r.getString(1).toString());
          }
        }
              app.closeConnection(con, ps,null,r);

        ps = con.prepareStatement("catfincaruinsertarconstrucciones");

//        ID_Construccion=ID_Construccion+1;
        int n=0;
        String valor="";
        while(n<13)
        {
            n=n+1;
            int a=Integer.parseInt(subparcela.getValueAt(n-1,1).toString());
            int b=Integer.parseInt(subparcela.getValueAt(n-1,2).toString());
            valor = Linea.substring(a,b);
            switch (Integer.parseInt(subparcela.getValueAt(n-1,0).toString()))
            {
            case 0:
               try
               {
                  if(valor.length() < 5)
                  {
                    ps.setInt(n,Integer.parseInt(valor));
                  }
                  else
                  {

                    Float valorD = Float.valueOf(valor.toString());
                    ps.setFloat(n,valorD.floatValue());
                  }
               }
               catch(Exception excep)
               {
                 return 0;
               }
               break;
            case 1:
                 ps.setString(n,valor);
                break;
            case 2:
              if (valor.equals("S"))
                    {ps.setBoolean(n,true);
                    }
              else
                   {ps.setBoolean(n,false);
                   }
              break;
            default:
              break;

          }//try
        }//while

        ps.setInt(14, ID_Construccion);
        ps.setInt(15, ID_Subparcela);
        Calendar cal=Calendar.getInstance();
        ps.setDate(16,new java.sql.Date(cal.getTime().getTime()));
        try
        {
          ps.executeUpdate();
          app.closeConnection(con, ps,null,null);

        }catch(Exception es)
        {
          es.printStackTrace();
          return 0;
        }
        return ID_Construccion;

      }catch (Exception ex)
      {
      ex.printStackTrace();
        return 0;
      }//catch
     }

 /**
    * Elimina una construccion rústica de la base de datos
    * @param idconstruccion - Identificador de la construccion rústica a eliminar
  */

 public void eliminarConstruccionRustica (int idconstruccion)
 {
  if(idconstruccion!=0)
  {
    try
    {
      PreparedStatement ps = con.prepareStatement("catfincarueliminarconstrucciones");
      ps.setInt(1,idconstruccion);
      ps.executeUpdate();
        app.closeConnection(con, ps,null,null);
    }
    catch(Exception es)
    {
      es.printStackTrace();
    }
  }
 }

}