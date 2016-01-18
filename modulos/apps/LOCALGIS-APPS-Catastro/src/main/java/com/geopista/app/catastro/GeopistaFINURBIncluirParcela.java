/**
 * GeopistaFINURBIncluirParcela.java
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

/** Esta clase, contiene los métodos necesarios para trabajar con la tabla parcelas.
 *  Contiene métodos que Insertan/Eliminan datos de parcelas dependiendo de que la
 *  información en el fichero de texto sobre parcelas sea correcta o incorrecta.
 */

public class GeopistaFINURBIncluirParcela
{
  private static ApplicationContext app = AppContext.getApplicationContext();

  public Connection con = null;

  public GeopistaFINURBIncluirParcela()
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
   * Inserta en la tabla parcelas los datos encontrados en el fichero de texto que el usuario ha seleccionado
   * para importar.
   * @param Linea - Línea del fichero que contiene una entrada sobre parcelas
   * @return El identificador de la Parcela que ha insertado en la base de datos. Devuelve 0 cuando se han producido errores y la parcela no se ha insertado
*/

  public int IncluirParcela (String Linea)
  {
    try
    {
      String[] columnNames = {"Tipo","Inicio","Fin"};
      Object[][] data =
      {
          {"1", "2","16"},//referencia_catastral
          {"0", "56","58"},//id_distrito
          {"1", "58","60"},//codigo_entidad_menor
          {"0", "60","65"},//id_via
          {"0", "95","99"},//primer_numero
          {"1", "99","100"},//primera_letra
          {"0", "100","104"},//segundo_numero
          {"1", "104","105"},//segunda_letra
          {"0", "105","110"},//kilometro
          {"1", "110","114"},//bloque
          {"1", "114","139"},//direccion_no_estructurada
          {"0", "139","144"},//codigo_postal
          {"0", "144","151"},//superficie_solar
          {"0", "151","158"},//superficie_construida_total
          {"0", "158","165"},//superficie_construida_sobrerasante
          {"0", "165","172"},//superficie_bajo_sobrerasante
          {"0", "172","179"},//superficie_cubierta
          {"0", "179","183"},//anio_aprobacion
          {"0", "183","185"},//codigo_calculo_valor
          {"1", "185","186"}};//modalidad_reparto

        JTable parcela = new JTable(data, columnNames);

        ResultSet r = null;
        PreparedStatement ps = con.prepareStatement("catfinurbmaxparcelas");
        int ID_Parcela=0;

        if(ps.execute())
        {
          r  = ps.getResultSet();
          while( r.next())
          {
              ID_Parcela= Integer.parseInt(r.getString(1).toString());
          }
        }
           app.closeConnection(con, ps,null,r);


        //Falta el ID_Municipio

        ps = con.prepareStatement("catfinurbinsertarparcelas");

//        ID_Parcela=ID_Parcela+1;
        int n=0;
        String valor="";
        while(n<20)
        {
            n=n+1;
            int a=Integer.parseInt(parcela.getValueAt(n-1,1).toString());
            int b=Integer.parseInt(parcela.getValueAt(n-1,2).toString());
            valor= Linea.substring(a,b);
            switch (Integer.parseInt(parcela.getValueAt(n-1,0).toString())){
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
              break;
            default:
              break;
          }//try
        }//while

        ps.setInt(21, ID_Parcela);
        ps.setString(22,"U");
        Calendar cal=Calendar.getInstance();
        ps.setDate(23,new java.sql.Date(cal.getTime().getTime()));
        ps.setInt(24,5);
        try
        {
          ps.executeUpdate();
            app.closeConnection(con, ps,null,null);

        }catch(Exception es)
        {
          return 0;
        }

        return ID_Parcela;

      }catch (Exception ex)
      {
        return 0;
      }//catch
     }
/**
    * Elimina una parcela de la base de datos
    * @param idparcela - Identificador de la parcela a eliminar
  */

 public void eliminarParcela (int idparcela)
 {
  if(idparcela!=0)
  {
    try{
      PreparedStatement ps = con.prepareStatement("catfinurbeliminarparcelas");
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