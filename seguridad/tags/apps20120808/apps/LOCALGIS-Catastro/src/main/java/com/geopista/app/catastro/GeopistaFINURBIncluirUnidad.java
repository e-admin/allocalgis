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

/** Esta clase, contiene los métodos necesarios para trabajar con la tabla unidadesconstructivas.
 *  Contiene métodos que Insertan/Eliminan datos de unidades constructivas dependiendo de que la
 *  información en el fichero de texto sobre unidades constructivas sea correcta o incorrecta.
 */

public class GeopistaFINURBIncluirUnidad
{
   private static ApplicationContext app = AppContext.getApplicationContext();

   public Connection con = null;

  public GeopistaFINURBIncluirUnidad()
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
		* Inserta en la tabla unidadesconstructivas los datos encontrados en el fichero de texto que el usuario ha seleccionado
		* para importar.
		* @param Linea Es la línea encontrada en el fichero de texto que contiene los datos de unidades constructivas
		* @param ID_Parcela Identificador de Parcela que corresponde a esta unidad constructiva
		* @return Devuelve el identificador de unidad constructiva insertada en la base de datos. Devuelve 0 cuando se han producido errores y la unidad constructiva no se ha insertado
	*/
   public int IncluirUC (String Linea, int ID_Parcela)
    {

      try
      {
      String[] columnNames = {"Tipo","Inicio","Fin"};
      Object[][] data = {
          {"1", "16","20"},
          {"1", "58","60"},
          {"0", "95","99"},
          {"1", "99","100"},
          {"0", "100","104"},
          {"1", "104","105"},
          {"0", "105","110"},
          {"1", "110","114"},
          {"1", "114","138"},
          {"0", "139","144"},
          {"0", "144","148"},
          {"0", "148","153"},
          {"2", "153","154"},
          {"1", "159","162"},
          {"1", "162","163"},
          {"2", "163","164"},
          {"1", "164","165"},
          {"2", "165","166"},
          {"0", "166","169"},
          {"2", "169","170"},
          {"2", "170","171"},
          {"1", "171","172"},
          {"1", "172","173"},
          {"1", "173","174"},
          {"1", "174","175"},
          {"1", "175","176"},
          {"1", "176","177"},
          {"1", "177","178"}};


        JTable UC = new JTable(data, columnNames);
        int ID_UC=0;
        ResultSet r = null;
        PreparedStatement ps = con.prepareStatement("catfinurbmaxunidadconstructiva");
        if(!ps.execute())
        {
        }
        else
        {
          r  = ps.getResultSet();

          while( r.next())
          {
            ID_UC= Integer.parseInt(r.getString(1).toString());
          }//del while

                app.closeConnection(con, ps,null,r);
        }

        ps = con.prepareStatement("catfinurbinsertarunidadconstructiva");

        int n=0;
        String valor="";
        while(n<28)
        {
            n=n+1;
            int a=Integer.parseInt(UC.getValueAt(n-1,1).toString());
            int b=Integer.parseInt(UC.getValueAt(n-1,2).toString());
            valor= Linea.substring(a,b);
            switch (Integer.parseInt(UC.getValueAt(n-1,0).toString())){
               case 0:
                try
                {
                  if(a==105)
                  {
                    if (valor.length()==5)
                    {
                      String parte_1 = valor.substring(0,3);
                      String parte_2= valor.substring(3,5);
                      String cadena_num = parte_1 + "." + parte_2;
                      Float c = Float.valueOf(cadena_num);
                      ps.setFloat(n, c.floatValue());
                    }
                    else
                    {
                       return 0;
                    }
                  }
                  else
                  {
                    ps.setInt(n,Integer.parseInt(valor));
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
                  {ps.setBoolean(n,true);}
                else
                   {ps.setBoolean(n,false);}

            default:
              break;
        }}
        ID_UC = ID_UC + 1;
        ps.setInt(29, ID_UC);
        ps.setInt(30, ID_Parcela);
        Calendar cal=Calendar.getInstance();
        ps.setDate(31,new java.sql.Date(cal.getTime().getTime()));
        ps.executeUpdate();
        app.closeConnection(con, ps,null,null);
        return ID_UC;
      }catch (Exception ex)
      {
          return 0;
      }//catch
     }

/**
    * Elimina una unidad constructiva de la base de datos
    * @param idunidad - Identificador de la unidad constructiva a eliminar
  */

  public void eliminarUnidad (int idunidad)
  {
    if(idunidad!=0)
    {
      try
      {
        PreparedStatement ps = con.prepareStatement("catfinurbeliminarunidad");
        ps.setInt(1,idunidad);
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