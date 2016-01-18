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

/** Esta clase, contiene los métodos necesarios para trabajar con la tabla subparcelasrusticas.
 *  Contiene métodos que Insertan/Eliminan datos de subparcelas dependiendo de que la
 *  información en el fichero de texto sobre subparcelas rústicas sea correcta o incorrecta.
 */

public class GeopistaFINCARUIncluirSubparcela
{
  private static ApplicationContext app = AppContext.getApplicationContext();

  public Connection con = null;

  public GeopistaFINCARUIncluirSubparcela()
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
        * @return Devuelve la conexión establecida con la base de datos
  */
  public static Connection getDBConnection () throws SQLException
  {

    Connection conn=  app.getConnection();
    conn.setAutoCommit(false);
    return conn;
  }
	/**
		* Inserta en la tabla subparcelasrusticas los datos encontrados en el fichero de texto que el usuario ha seleccionado
		* para importar.
		* @param Linea Es la línea encontrada en el fichero de texto que contiene los datos de subparcelas
		* @param ID_Parcela Identificador de Parcela que corresponde a esta Subparcela
		* @return Devuelve el identificador de subparcela insertada en la base de datos. Devuelve 0 cuando se han producido errores y la subparcela no se ha insertado
	*/
    public int IncluirSubparcelaRustica (String Linea, int ID_Parcela)
    {

    try
    {
      String[] columnNames = {"Tipo","Inicio","Fin"};
      Object[][] data =
      {
          {"1", "21","25"},
          {"1", "25","26"},
          {"1", "26","28"},
          {"0", "28","32"},
          {"0", "32","37"},
          {"0", "37","39"},
          {"0", "39","41"},
          {"1", "41","43"},
          {"0", "43","45"},
          {"1", "45","47"},
          {"0", "47","49"},
          {"1", "49","56"},
          {"1", "56","63"}};

        JTable subparcela = new JTable(data, columnNames);

        ResultSet r = null;
        PreparedStatement ps = con.prepareStatement("catfincarumaxsubparcelas");
        int ID_SubParcela=0;

        if(ps.execute())
        {
          r  = ps.getResultSet();
          while( r.next())
          {
              ID_SubParcela= Integer.parseInt(r.getString(1).toString());
          }
        }
              app.closeConnection(con, ps,null,r);


        //Falta el ID_Municipio
        ps = con.prepareStatement("catfincaruinsertarsubparcelas");

      //  ID_SubParcela=ID_SubParcela+1;
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
                excep.printStackTrace();
                 return 0;
               }
               break;
            case 1:
                 ps.setString(n,valor);
                break;
            case 2:
              if (valor.equals("S"))
              {
                    ps.setBoolean(n,true);
              }
              else
              {
                    ps.setBoolean(n,false);
              }
              break;
            default:
              break;

          }//try
        }//while

        ps.setInt(14, ID_SubParcela);
        ps.setInt(15, ID_Parcela);
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
        return ID_SubParcela;

      }catch (Exception ex)
      {
      ex.printStackTrace();
        return 0;
      }//catch
     }

/**
    * Elimina una subparcela rústica de la base de datos
    * @param idsubparcela - Identificador de la subparcela rústica a eliminar
  */
 public void eliminarSubparcelaRustica (int idsubparcela)
 {
  if(idsubparcela!=0)
  {
    try{
      PreparedStatement ps = con.prepareStatement("catfincarueliminarsubparcelas");
       ps.setInt(1,idsubparcela);
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