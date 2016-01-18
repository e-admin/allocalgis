/**
 * GeopistaFINCARUIncluirTitular.java
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

import javax.swing.JTable;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;

/** Esta clase, contiene los métodos necesarios para trabajar con la tabla titularesrusticos.
 *  Contiene métodos que Insertan/Eliminan datos de titulares dependiendo de que la
 *  información en el fichero de texto sobre titulares rústicos sea correcta o incorrecta.
 */
public class GeopistaFINCARUIncluirTitular
{
	private static ApplicationContext app = AppContext.getApplicationContext();

   public Connection con = null;
   public GeopistaFINCARUIncluirTitular()
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
		* Inserta en la tabla titularesrusticos los datos encontrados en el fichero de texto que el usuario ha seleccionado
		* para importar.
		* @param Linea Es la línea encontrada en el fichero de texto que contiene los datos de titulares
		* @return Devuelve el identificador del titular insertado en la base de datos. Devuelve 0 cuando se han producido errores y el titular no se ha insertado
	*/
   public int IncluirTitularRustico (String Linea)
    {
      try
      {
        String[] columnNames = {"Tipo","Inicio","Fin"};
        Object[][] data =
        {
            {"1", "11","18"},
            {"0", "18","23"},//coeficiente de participación
            {"1", "23","83"},
            {"1", "83","84"},
            {"1", "84","92"},//nif
            {"1", "92","93"},
            {"1", "93","94"},
            {"0", "94","96"},
            {"0", "96","99"},
            {"0", "99","104"},
            {"0", "129","133"},
            {"1", "133","134"},
            {"0", "134","138"},
            {"1", "138","139"},
            {"0", "139","144"},//kilometro(5,2)
            {"1", "144","148"},
            {"1", "148","173"},
            {"1", "173","175"},
            {"1", "175","178"},
            {"1", "178","181"},
            {"0", "181","186"}};

          JTable Titular = new JTable(data, columnNames);
          int ID_Titular=0;
          boolean NuevoTitular=false;

          ResultSet r = null;
          String nif= Linea.substring(84,92);

          r = null;
          PreparedStatement ps = con.prepareStatement("catfincaruidtitular");
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
              ps = con.prepareStatement("catfincarumaxtitular");
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

//                ID_Titular=ID_Titular+1;
              }
          }
          else { NuevoTitular=false;}

        //1º se insertan los titulares
        PreparedStatement s = null;
          if (NuevoTitular==true)
          {
            s = con.prepareStatement("catfincaruinsertartitular");
          }
          else
          {
            s = con.prepareStatement("catfincaruactualizartitular");
    
          }
          int n=0;
          String valor="";
          while(n<21)
          {
              n=n+1;
              int a=Integer.parseInt(Titular.getValueAt(n-1,1).toString());
              int b=Integer.parseInt(Titular.getValueAt(n-1,2).toString());
              valor= Linea.substring(a,b);
              switch (Integer.parseInt(Titular.getValueAt(n-1,0).toString())){
              case 0:
                try
                {
                  if((a==139)||(a==18))
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
          s.setInt(22, ID_Titular);
          s.executeUpdate();
          app.closeConnection(con, s,null,null);

          return ID_Titular;
        }catch (Exception ex)
        {
           ex.printStackTrace();
          return 0;
        }//catch

       }

}
