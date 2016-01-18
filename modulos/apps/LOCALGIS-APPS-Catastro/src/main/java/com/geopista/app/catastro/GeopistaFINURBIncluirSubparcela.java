/**
 * GeopistaFINURBIncluirSubparcela.java
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

/** Esta clase, contiene los métodos necesarios para trabajar con la tabla subparcelas.
 *  Contiene métodos que Insertan/Eliminan datos de subparcelas dependiendo de que la
 *  información en el fichero de texto sobre subparcelas sea correcta o incorrecta.
 */

public class GeopistaFINURBIncluirSubparcela
{


 private static ApplicationContext app = AppContext.getApplicationContext();

 public Connection con = null;

  public GeopistaFINURBIncluirSubparcela()
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
  		* Inserta en la tabla subparcelas los datos encontrados en el fichero de texto que el usuario ha seleccionado
  		* para importar.
  		* @param Linea Es la línea encontrada en el fichero de texto que contiene los datos de subparcelas
  		* @param ID_Parcela Identificador de Parcela que corresponde a esta Subparcela
  		* @return Devuelve el identificador de subparcela insertada en la base de datos. Devuelve 0 cuando se han producido errores y la subparcela no se ha insertado
	*/
    public int IncluirSubparcela (String Linea, int ID_Parcela)
    {
      try
      {
      String[] columnNames = {"Tipo","Inicio","Fin"};
      Object[][] data = {
          {"0", "16","20"},//numero de orden
          {"0", "58","63"},//longitud fachada
          {"1", "63","65"},//tipo fachada
          {"0", "65","72"},//superficie elemento
          {"0", "72","75"},//fondo elemento suelo
          {"0", "75","80"},//id tramo via
          {"1", "83","84"},//codigo tipo valor
          {"1", "84","85"},//numero fachadas
          {"2", "85","86"},//corrector longitud fachada
          {"2", "86","87"},//corrector forma irregular
          {"2", "87","88"},//corrector desmonte excesivo
          {"2", "88","89"},//corrector profundidad firme
          {"2", "89","90"},//corrector fondo excesivo
          {"2", "90","91"},//corrector superficie distinta
          {"0", "93","96"},//corrector apreciacion
          {"2", "96","97"},//corrector depreciacion
          {"0", "98","101"},//corrector cargas singulares
          {"2", "101","102"},//corrector situaciones especiales
          {"2", "102","103"},//corrector uso no indicativo
          {"1", "103","104"},//agua
          {"1", "104","105"},//electricidad
          {"1", "105","106"},//alumbrado
          {"1", "106","107"},//desmonte
          {"1", "107","108"},//pavimentacion
          {"1", "108","109"}};//alcantarillado

        JTable subparcela = new JTable(data, columnNames);
        int ID_Subparcela=0;
        ResultSet r = null;
        PreparedStatement ps = con.prepareStatement("catfinurbmaxsubparcela");

          if(!ps.execute())
          {
          }
          else
          {
            r  = ps.getResultSet();

            while( r.next())
            {
              ID_Subparcela= Integer.parseInt(r.getString(1).toString());
            }//del while
                 app.closeConnection(con, ps,null,r);

          }

    //Falta el ID_Municipio
       ps = con.prepareStatement("catfinurbinsertarsubparcelas");

    //    ID_Subparcela=ID_Subparcela+1;
        int n=0;
       String valor="";
        while(n<25)
        {
            n=n+1;
            int a=Integer.parseInt(subparcela.getValueAt(n-1,1).toString());
            int b=Integer.parseInt(subparcela.getValueAt(n-1,2).toString());
            valor= Linea.substring(a,b);
            switch (Integer.parseInt(subparcela.getValueAt(n-1,0).toString())){
             case 0:
               try
              {
                 ps.setInt(n,Integer.parseInt(valor));
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

            default:
              break;

        }}

        ps.setInt(26, ID_Subparcela);//id_subparcela
        ps.setInt(27, ID_Parcela);//id_parcela
        Calendar cal=Calendar.getInstance();
        ps.setDate(28,new java.sql.Date(cal.getTime().getTime()));
        ps.executeUpdate();
          app.closeConnection(con, ps,null,null);
        return ID_Subparcela;
      }catch (Exception ex)
      {
        ex.printStackTrace();
          return 0;
      }//catch
     }

/**
    * Elimina una subparcela de la base de datos
    * @param idsubparcela - Identificador de la subparcela a eliminar
  */

  public void eliminarSubParcela (int idsubparcela)
  {
    if(idsubparcela!=0)
    {
      try
      {
        PreparedStatement ps = con.prepareStatement("catfinurbeliminarsubparcelas");
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