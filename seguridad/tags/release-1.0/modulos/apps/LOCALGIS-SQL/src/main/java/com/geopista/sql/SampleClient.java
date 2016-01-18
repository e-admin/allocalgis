package com.geopista.sql;

import java.sql.*;

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
