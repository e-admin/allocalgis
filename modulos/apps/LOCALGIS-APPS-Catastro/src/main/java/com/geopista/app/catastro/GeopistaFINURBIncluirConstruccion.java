/**
 * GeopistaFINURBIncluirConstruccion.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.app.catastro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

import javax.swing.JTable;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;

/** Esta clase, contiene los métodos necesarios para trabajar con la tabla construcciones.
 *  Contiene métodos que Insertan/Eliminan datos de construcciones dependiendo de que la
 *  información en el fichero de texto sobre construcciones sea correcta o incorrecta.
 */

public class GeopistaFINURBIncluirConstruccion
{
  private static ApplicationContext app = AppContext.getApplicationContext();

   public Connection con = null;
   int cuenta=0;
   public GeopistaFINURBIncluirConstruccion()
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
		* Inserta en la tabla construcciones los datos encontrados en el fichero de texto que el usuario ha seleccionado
		* para importar.
		* @param Linea Es la línea encontrada en el fichero de texto que contiene los datos de construcciones
		* @param ID_Subparcela Identificador de Unidad Constructiva que corresponde a esta construccion
		* @return Devuelve el identificador de construcción insertada en la base de datos. Devuelve 0 cuando se han producido errores y la construccion no se ha insertado
	*/
   public String IncluirConstruccion (String Linea, int ID_Unidad)
    {
      try
      {
      String[] columnNames = {"Tipo","Inicio","Fin"};
      Object[][] data = {
          {"0", "16","20"},
          {"1", "58", "62"},
          {"1", "62", "64"},
          {"1", "64", "67"},
          {"1", "67", "70"},
          {"1", "70", "73"},
          {"1", "73", "74"},
          {"0", "74", "78"},
          {"2", "78", "79"},
          {"0", "79", "86"},
          {"0", "86", "93"},
          {"0", "93", "100"},
          {"1", "100", "105"},
          {"1", "105", "106"},
          {"1", "106", "107"},
          {"1", "107", "110"},
          {"1", "110", "111"},
          {"0", "111", "114"},
          {"2", "114", "115"}};

        JTable Cons = new JTable(data, columnNames);


    //Falta el ID_Municipio

        String ID_Construccion= Linea.substring(2,20);
        ResultSet r = null;
        PreparedStatement ps = con.prepareStatement("catfinurbcountconstruccion");
        ps.setString(1, ID_Construccion);
        if(!ps.execute())
        {
        }
        else
        {
          r  = ps.getResultSet();
          while (r.next())
          {
             cuenta= Integer.parseInt(r.getString(1).toString());
          }
            app.closeConnection(con, ps,null,r);
        }
      PreparedStatement s = con.prepareStatement("catfinurbinsertarconstruccion");
      int n=0;
       String valor="";
       while(n<19)
        {
            n=n+1;
            int a=Integer.parseInt(Cons.getValueAt(n-1,1).toString());
            int b=Integer.parseInt(Cons.getValueAt(n-1,2).toString());
            valor= Linea.substring(a,b);
              switch (Integer.parseInt(Cons.getValueAt(n-1,0).toString())){
                case 0:
                   try
                  {
                     s.setInt(n,Integer.parseInt(valor));
                  }
                  catch(Exception excep)
                     {
                      return null;
                     }
                   break;
                case 1:
                    s.setString(n,valor);
                    break;
               case 2:
                    if (valor.equals("S"))
                      {s.setBoolean(n,true);
                       }
                    else
                       {s.setBoolean(n,false);
                        }
                    break;
                default:
                  break;

        }

        }
        s.setString(20, ID_Construccion);
        s.setInt(21, ID_Unidad);
        Calendar cal=Calendar.getInstance();
        s.setDate(22,new java.sql.Date(cal.getTime().getTime()));
        if (cuenta==0)
        {
          s.executeUpdate();
          app.closeConnection(con, ps,null,null);
        }
        else
        {
          return null;
        }
        return ID_Construccion;
      }catch (Exception ex)
      {
        ex.printStackTrace();
        return null;
      }//catch
     }
/**
    * Elimina una construccion de la base de datos
    * @param idConstruccion - Identificador de la construccion a eliminar
  */

  public void eliminarConstruccion (String idConstruccion)
  {
    if(!idConstruccion.equals(null))
    {
      try
      {
        PreparedStatement ps = con.prepareStatement("catfinurbeliminarconstruccion");
        ps.setString(1,idConstruccion);
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
