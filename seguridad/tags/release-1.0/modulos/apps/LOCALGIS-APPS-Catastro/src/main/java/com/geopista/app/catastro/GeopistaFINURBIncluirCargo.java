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

/** Esta clase, contiene los métodos necesarios para trabajar con la tabla cargos y titulares.
 *  Contiene métodos que Insertan/Eliminan datos de cargos y titulares dependiendo de que la
 *  información en el fichero de texto sobre cargos y titulares sea correcta o incorrecta.
 */


public class GeopistaFINURBIncluirCargo
{
   private static ApplicationContext app = AppContext.getApplicationContext();

   public Connection con = null;

  public GeopistaFINURBIncluirCargo()
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
  		* Inserta en la tabla cargos y en la tabla titulares los datos encontrados en el fichero de texto que el usuario ha seleccionado
  		* para importar.
  		* @param Linea Es la línea encontrada en el fichero de texto que contiene los datos de cargos y de titulares
  		* @return Devuelve el identificador del cargo insertado en la base de datos. Devuelve 0 cuando se han producido errores y el cargo y/o titular no han sido insertados
	*/
  public int IncluirCargo (String Linea)
    {
      try
      {
      String[] columnNames = {"Tipo","Inicio","Fin"};
      Object[][] data = {
          {"1", "16","20"},
          {"1", "20","21"},
          {"1", "21","22"},
          {"0", "22","30"},
          {"0", "31","36"},//3 enteros y 2 decimales
          {"0", "56","58"},
          {"1", "58","60"},
          {"0", "60","65"},
          {"0", "95","99"},
          {"1", "99","100"},
          {"0", "100","104"},
          {"1", "104","105"},
          {"0", "105","110"},
          {"1", "110","114"},
          {"1", "114","139"},
          {"0", "139","144"},
          {"1", "144","146"},
          {"1", "146","149"},
          {"1", "149","152"},
          {"0", "428","432"},
          {"0", "432","444"},
          {"0", "444","456"},
          {"0", "456","468"},
          {"0", "468","480"},
          {"1", "480","481"},
          {"0", "481","485"},
          {"0", "485","489"},
          {"0", "489","495"},
          {"0", "495","502"},
          {"0", "502","509"},
          {"0", "509","516"},
          {"2", "516","517"},
          {"0", "517","521"},
          {"1", "521","522"}};

        JTable Cargo = new JTable(data, columnNames);


      Object[][] data1 = {
          {"1", "152","160"},
          {"1", "160","161"},
          {"1", "161","162"},
          {"1", "162","222"},
          {"0", "222","226"},
          {"1", "226","246"},
          {"0", "246","249"},
          {"0", "249","251"},
          {"0", "251","253"},
          {"0", "253","256"},
          {"0", "256","261"},
          {"0", "291","295"},
          {"1", "295","296"},
          {"0", "296","300"},
          {"1", "300","301"},
          {"0", "301","306"},
          {"1", "306","310"},
          {"1", "310","335"},
          {"1", "335","337"},
          {"1", "337","340"},
          {"1", "340","343"},
          {"0", "344","348"},
          {"0", "348","353"},
          {"1", "353","378"},
          {"1", "378","403"},
          {"1", "403","428"}};
        JTable Titular = new JTable(data1, columnNames);
        int ID_Cargo=0;
        int ID_Titular=0;
        boolean NuevoTitular=false;
        String ID_Construccion= Linea.substring(2,20);
        ResultSet r = null;
        PreparedStatement ps = con.prepareStatement("catfinurbmaxcargo");

          if(!ps.execute())
          {
          }
          else
          {
            r  = ps.getResultSet();
            while (r.next())
            {
              ID_Cargo= Integer.parseInt(r.getString(1).toString());
            }
                   app.closeConnection(con, ps,null,r);
          }

//        ID_Cargo=ID_Cargo+1;
        String nif= Linea.substring(152,160);

        r = null;
        ps = con.prepareStatement("catfinurbidtitular");
        ps.setString(1,nif);
          if(!ps.execute())
          {
          }
          else
          {
            r  = ps.getResultSet();
            while (r.next())
            {
              ID_Titular= Integer.parseInt(r.getString(1).toString());
            }

               app.closeConnection(con, ps,null,r);
          }

        if (ID_Titular==0)
        {
            NuevoTitular=true;
            r = null;
            ps = con.prepareStatement("catfinurbmaxtitular");
            if(!ps.execute())
            {
            }
            else
            {
              r  = ps.getResultSet();
              while (r.next())
              {
                ID_Titular= Integer.parseInt(r.getString(1).toString());
              }

//              ID_Titular=ID_Titular+1;
            }
        }
        else { NuevoTitular=false;}

      //1º se insertan los titulares
      PreparedStatement s = null;
        if (NuevoTitular==true)
        {
          s = con.prepareStatement("catfinurbinsertartitular");

        }
        else
        {
          s = con.prepareStatement("catfinurbactualizartitular");
        }
        int n=0;
        String valor="";
     
       while(n<26)
        {
            n=n+1;
            int a=Integer.parseInt(Titular.getValueAt(n-1,1).toString());
            int b=Integer.parseInt(Titular.getValueAt(n-1,2).toString());
            valor= Linea.substring(a,b);
            switch (Integer.parseInt(Titular.getValueAt(n-1,0).toString())){

            case 0:
              try
              {
                 s.setInt(n,Integer.parseInt(valor));
              }
              catch(Exception excep)
              {
                 return 0;
              }
              break;
            case 1:
              s.setString(n,valor);
              break;
           case 2:
              if (valor.equals("S"))
              {
                s.setBoolean(n,true);
              }
              else
              {
                s.setBoolean(n,false);

              }

                break;
            default:
              break;

        }}
        s.setInt(27, ID_Titular);
        s.executeUpdate();
    		app.closeConnection(con, ps,null,null);

        s = con.prepareStatement("catfinurbinsertarcargos");
        n=0;
        valor="";
        String consulta="";
        while(n<34)
        {
            n=n+1;
            int a=Integer.parseInt(Cargo.getValueAt(n-1,1).toString());
            int b=Integer.parseInt(Cargo.getValueAt(n-1,2).toString());
            valor= Linea.substring(a,b);
            switch (Integer.parseInt(Cargo.getValueAt(n-1,0).toString())){
            case 0:
              try
              {

                if((a==31)||(a==105))
                {
                   if (valor.length()==5)
                  {
                    String parte_1 = valor.substring(0,3);
                    String parte_2 = valor.substring(2);
                    String cadena_num = parte_1 + "." + parte_2;
                    Float c = Float.valueOf(cadena_num);
                    s.setFloat(n, c.floatValue());
                    consulta = consulta + c.floatValue() + ",";
                  }
                  else
                  {
                    return 0;
                  }
                }

                else if(a==509)
                {
                   if (valor.length()==7)
                  {
                    String parte_1_1 = valor.substring(0,3);
                    String parte_2_1 = valor.substring(4);
                    String cadena_num_1 = parte_1_1 + "." + parte_2_1;
                    Float c_1 = Float.valueOf(cadena_num_1);
                    s.setFloat(n, c_1.floatValue());
                    consulta = consulta + c_1.floatValue() + ",";
                  }
                  else
                  {
                    return 0;
                  }
                }
                else
                {
                    ps.setInt(n,Integer.parseInt(valor));
                    consulta = consulta + valor + ",";
                }
              }
              catch(Exception excep)
              {
                
                return 0;
              }
              break;
            case 1:
              s.setString(n,valor);
              consulta = consulta + "'"+ valor + "',";
              break;
           case 2:
              if (valor.equals("S"))
              {
                s.setBoolean(n,true);
                consulta = consulta + true + ",";
              }
              else
              {
                s.setBoolean(n,false);
                consulta = consulta + false + ",";
              }
                break;
            default:
              break;

        }}
        s.setInt(35, ID_Cargo);
        consulta = consulta + ID_Cargo + ",";
        s.setString(36, ID_Construccion);
        consulta = consulta + "'"+ID_Construccion + "',";
        s.setInt(37, ID_Titular);
        consulta = consulta + ID_Titular +",";
        Calendar cal=Calendar.getInstance();
        s.setDate(38,new java.sql.Date(cal.getTime().getTime()));
        consulta = consulta + "'"+ new java.sql.Date(cal.getTime().getTime()) +"',";
        s.executeUpdate();
        app.closeConnection(con, ps,null,null);
        return ID_Cargo;
      }catch (Exception ex)
      {
          return 0;
      }//catch

     }

/**
    * Elimina un cargo de la base de datos
    * @param idcargo - Identificador del cargo a eliminar
  */
 public void eliminarCargo (int idcargo)
 {
  if(idcargo!=0)
  {
    try{
      PreparedStatement ps = con.prepareStatement("catfinurbeliminarcargo");
      ps.setInt(1,idcargo);
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
