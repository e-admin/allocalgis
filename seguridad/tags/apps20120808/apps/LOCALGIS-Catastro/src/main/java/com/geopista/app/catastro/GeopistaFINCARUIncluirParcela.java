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

/** Esta clase, contiene los métodos necesarios para trabajar con la tabla parcelasrusticas.
 *  Contiene métodos que Insertan/Eliminan datos de parcelas rústicas dependiendo de que la
 *  información en el fichero de texto sobre parcelas sea correcta o incorrecta.
 */
public class GeopistaFINCARUIncluirParcela
{
  private static ApplicationContext app = AppContext.getApplicationContext();

  public Connection con = null;

  public GeopistaFINCARUIncluirParcela()
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
//    ApplicationContext app = AppContext.getApplicationContext();
    Connection conn=  app.getConnection();
    conn.setAutoCommit(false);
    return conn;
  }
  /**
   * Inserta en la tabla parcelasrusticas los datos encontrados en el fichero de texto que el usuario ha seleccionado
   * para importar.
   * @param Linea - Línea del fichero que contiene una entrada sobre parcelas
   * @return El identificador de la Parcela que ha insertado en la base de datos. Devuelve 0 cuando se han producido errores y la parcela no se ha insertado
   */
  public int IncluirParcelaRustica (String Linea)
  {

    try
    {
      String[] columnNames = {"Tipo","Inicio","Fin"};
      Object[][] data =
      {
          {"0", "2","5"},
          {"0", "5","8"},
          {"0", "8","11"},
          {"0", "11","13"},
          {"0", "13","16"},
          {"0", "16","21"},
          {"0", "21","26"},
          {"0", "26","28"},
          {"0", "28","30"},
          {"0", "30","42"},
          {"0", "42","46"},
          {"1", "53","55"},
          {"1", "55","65"},
          {"0", "65","68"},
          {"0", "68","71"},
          {"0", "71","76"},
          {"1", "76","77"}};

        JTable parcela = new JTable(data, columnNames);

        ResultSet r = null;
        PreparedStatement ps = con.prepareStatement("catfincarumaxparcelas");
        int ID_Parcela=0;

        if(!ps.execute())
        {
        }
        else
        {
          r  = ps.getResultSet();
          while( r.next())
          {
              ID_Parcela= Integer.parseInt(r.getString(1).toString());
          }
        }
        app.closeConnection(con, ps,null,r);


        ps = con.prepareStatement("catfincaruinsertarparcelas");
//        ID_Parcela=ID_Parcela+1;
        int n=0;
        String valor="";
        while(n<17)
        {
            n=n+1;
            int a=Integer.parseInt(parcela.getValueAt(n-1,1).toString());
            int b=Integer.parseInt(parcela.getValueAt(n-1,2).toString());
            valor= Linea.substring(a,b);
            switch (Integer.parseInt(parcela.getValueAt(n-1,0).toString()))
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
                excep.printStackTrace();
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

        ps.setInt(18, ID_Parcela);
        Calendar cal=Calendar.getInstance();
        ps.setDate(19,new java.sql.Date(cal.getTime().getTime()));
        try
        {
          ps.executeUpdate();
          app.closeConnection(con, ps,null,null);
        }catch(Exception es)
        {
          es.printStackTrace();
          con.rollback();
          return 0;
        }

        return ID_Parcela;

      }catch (Exception ex)
      {
      ex.printStackTrace();
        return 0;
      }//catch
     }

 /**
    * Elimina una parcela rústica de la base de datos
    * @param idparcela - Identificador de la parcela rústica a eliminar
  */
 public void eliminarParcelaRustica (int idparcela)
 {

  if(idparcela!=0)
  {
    try{
      PreparedStatement ps = con.prepareStatement("catfincarueliminarparcelas");
      ps.setInt(1,idparcela);
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