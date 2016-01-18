/**
 * SampleClient.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Ejemplo de cliente para la API geopistaSQL. Requiere que haya dos sentencias
 * en la tabla query_catalog. Una llamada "insert" con siguiente
 * texto: "insert into query_catalog (id,query) values (?,?)", y otra
 * "catalog" con este: "select * from query_catalog"
 */
public class SampleClient {
    public static void main(String[] args) {
        try {

            //Primero hay que autenticarse
            //com.geopista.security.SecurityManager.setsUrl("http://localhost:8080/licencias/");
            com.geopista.security.SecurityManager.setsUrl("http://212.22.38.19:8081/licencias/");

            com.geopista.security.SecurityManager.login("SYSSUPERUSER", "SYSPASSWORD",
                                                               "Administracion");


            // La API se inicializa como cualquier otro driver JDBC.
            Class.forName("com.geopista.sql.GEOPISTADriver");

            // A partir del segundo ':", la cadena de conexion sera la URL del servlet
            String sConn = "jdbc:pista:http://212.22.38.19:8081/licencias/CServletDB";
            Connection conn = DriverManager.getConnection(sConn);

            // Para crear un PreparedStatement lo haremos con la ID del catalogo
            // en vez de la sentencia SQL.
            PreparedStatement ps = conn.prepareStatement("catalog");
            ResultSet rs = null;

            // Probaremos la consulta y el resultset ejecutando un SELECT
            if (!ps.execute()) {
                System.err.println("Error en conexion o en esquema de BD!");
            } else {
                System.out.println("Sentencias en query_catalog:");
                rs = ps.getResultSet();
                while (rs.next()) {
                    System.out.println(rs.getString("id") + " : " + rs.getString("query"));
                }

                // Ahora insertaremos una nueva sentencia usando la sentencia existente
                //  "insert".
                ps = conn.prepareStatement("insert");
                ps.setString(1, "delete");
                ps.setString(2, "delete from query_catalog where id=?");
                System.out.println("Insertando sentencia..." + ps.executeUpdate());

                // Ejecutamos la sentencia nueva, que se borra a si misma para que se
                // pueda repetir la prueba mas veces...
                ps = conn.prepareStatement("delete");
                ps.setString(1, "delete");
                System.out.println("Eliminando sentencia..." + ps.executeUpdate());

                // Como no hay conexiones permanentes no es necesario cerrar los
                // objetos, pero se puede hacer por compatibilidad.
                rs.close();
                ps.close();
                conn.close();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e){
           e.printStackTrace();
        }

    }

}
