/**
 * GeopistaSQL.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.geopista.app.AppContext;
import com.geopista.util.ApplicationContext;

/**
 * GeopistaSQL : Función que encapsula la realización de consultas
 * mediante la tabla query_catalog
 */
public class GeopistaSQL {
    public static ApplicationContext appContext=AppContext.getApplicationContext();
    public GeopistaSQL() {
        
    }
    
    /**
     * Query(String queryName, Connection conn)  [DEPRECATED]
     * 
     * Realiza un SELECT sin parámetros
     * 
     * @param queryName : Nombre de la consulta de la tabla query_catalog a ejecutar
     * @param conn : Objeto de conexión a la Base de Datos
     * @return Devuelve un objeto ResultSet producto de la ejecución de la consulta
     * 
     */
    public static ResultSet Query(String queryName, Connection conn)
    {
      ResultSet rsQuery = null;
      ResultSet rs = null;
      PreparedStatement ps = null;
      try {
            

            // Para crear un PreparedStatement lo haremos con la ID del catalogo
            // en vez de la sentencia SQL.
            ps = conn.prepareStatement(queryName);
            

            
            if (!ps.execute()) {
                System.err.println("Error en conexion o en esquema de BD!");
            } else {
               
                rs = ps.getResultSet();
                rsQuery = rs;

                // Como no hay conexiones permanentes no es necesario cerrar los
                // objetos, pero se puede hacer por compatibilidad.
                
            }

        
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally 
        {
        
          appContext.closeConnection(conn,ps,null,rs);
          return rsQuery;
        }
    }
    /**
     * Query(String queryName, String params, Connection conn)
     * 
     * Realiza un SELECT con parámetros
     * 
     * @param queryName : Nombre de la consulta de la tabla query_catalog a ejecutar
     * @param params : Es un objeto String en el que cada uno de los parámetros debe ir separado por ; (punto y coma)
     * @param conn : Objeto de conexión a la Base de Datos
     * @return DeDevuelve un objeto ResultSet producto de la ejecución de la consulta
     */
    public static ResultSet Query(String queryName, String params, Connection conn)
    {
      ResultSet rsQuery = null;
      ResultSet rs = null;
      PreparedStatement ps = null;
      try {
            

            // Para crear un PreparedStatement lo haremos con la ID del catalogo
            // en vez de la sentencia SQL.

            
            ps = conn.prepareStatement(queryName);
            
            if (params.length()!=0)
            {
              // Tratamiento de la cadena de parámetros
              String [] temp = null;
              temp = params.split("\\;");
              
              
              for (int i = 0 ; i < temp.length ; i++) {
                  ps.setString(i+1,temp[i]);
              } 
  
            }

            
            if (!ps.execute()) {
                System.err.println("Error en conexion o en esquema de BD!");
            } else {
               
                rs = ps.getResultSet();
                rsQuery = rs;
     
            }

       
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally 
        {
          appContext.closeConnection(conn,ps,null,rs);
          return rsQuery;
        }
    }

     /**
     * Columns(String queryName, String params, Connection conn)
     * 
     * Realiza un SELECT y después recupera los nombres de los campos
     * 
     * @param queryName : Nombre de la consulta de la tabla query_catalog a ejecutar
     * @param params : Es un objeto String en el que cada uno de los parámetros debe ir separado por ; (punto y coma)
     * @param conn : Objeto de conexión a la Base de Datos
     * @return Devuelve un objeto String[] producto de la ejecución de la consulta con los nombres de los campos
     */
    public static String[] Columns(String queryName, String params, Connection conn)
    {    
      String [] columnNames = null;
      ResultSet rs = null;
      PreparedStatement ps = null;
      try {
            

            // Para crear un PreparedStatement lo haremos con la ID del catalogo
            // en vez de la sentencia SQL.

            
            ps = conn.prepareStatement(queryName);
            
            if (params.length()!=0)
            {
              // Tratamiento de la cadena de parámetros
              String [] temp = null;
              temp = params.split("\\;");
              
              
              for (int i = 0 ; i < temp.length ; i++) {
                  ps.setString(i+1,temp[i]);
              } 
  
            }

            
            if (!ps.execute()) {
                System.err.println("Error en conexion o en esquema de BD!");
            } else {
               
                rs = ps.getResultSet();
                // Obtener los metadatos
                ResultSetMetaData rsmd = rs.getMetaData();
                int numColumns = rsmd.getColumnCount();
    
                // Obtener el nombre de las columnas
                String columns ="";
                for (int i=1; i<numColumns+1; i++) {
                    if(!(rsmd.getColumnName(i).equals("GEOMETRY")))
                    {
                      columns += rsmd.getColumnName(i)+";";
                    }
                    
                    
                }
                
                columnNames = columns.split("\\;");
                
            }




       
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally 
        {
          appContext.closeConnection(conn,ps,null,rs);
          return columnNames;
        }
        
    }
    /**
     * UpdateInsert(String queryName, String params, Connection conn)
     * 
     * Realiza un INSERT,UPDATE O DELETE con parámetros
     * 
     * @param queryName : Nombre de la consulta de la tabla query_catalog a ejecutar
     * @param params : Es un objeto String en el que cada uno de los parámetros debe ir separado por ; (punto y coma)
     * @param conn : Objeto de conexión a la Base de Datos
     * @return No devuelve nada
     */
    
    public static void UpdateInsert(String queryName, String params, Connection conn)
    {
      PreparedStatement ps = null;
      try {
            

            // Para crear un PreparedStatement lo haremos con la ID del catalogo
            // en vez de la sentencia SQL.

            
            ps = conn.prepareStatement(queryName);
            
            
            
            if (params.length()!=0)
            {
              // Tratamiento de la cadena de parámetros
              String [] temp = null;
              temp = params.split("\\;");
              
              
              for (int i = 0 ; i < temp.length ; i++) {
                  ps.setString(i+1,temp[i]);
              } 
  
            }

            ps.executeUpdate();
          
            
       
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally 
        {
          
          appContext.closeConnection(conn,ps,null,null);
        }
    }
    

}
