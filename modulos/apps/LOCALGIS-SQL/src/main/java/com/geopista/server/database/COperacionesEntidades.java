/**
 * COperacionesEntidades.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.geopista.server.database;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author seilagamo
 * 
 */
public class COperacionesEntidades {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
            .getLogger(COperacionesEntidades.class);

    private static boolean safeClose(ResultSet rs, Statement statement, Connection connection) {
        try {
            if (connection != null && !connection.isClosed())
                connection.commit();
        } catch (SQLException e) {
            logger.error("Can't close connection", e);
        }
        try {
            if (rs != null)
                rs.close();
        } catch (Exception e) {
            logger.error("Can't close result set", e);
        }
        try {
            if (statement != null)
                statement.close();
        } catch (Exception e) {
            logger.error("Can't close connection", e);
        }
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                CPoolDatabase.releaseConexion();
            }
        } catch (SQLException e) {
            logger.error("Can't close connection", e);
        }
        return true;

    }

    private static boolean safeClose(ResultSet rs, Statement statement,
            PreparedStatement preparedStatement, Connection connection) {
        try {
            if (preparedStatement != null)
                preparedStatement.close();
        } catch (Exception e) {
            logger.error("Can't close prepared statement", e);
        }
        return safeClose(rs, statement, connection);

    }
    
    public static int  getSRID(int id_entidad) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;        
        int srid = 0;
        
        
        try {
            logger.debug("Inicio.");
        
            //****************************************
            //** Obtener una conexion de la base de datos
            //****************************************************
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("Cannot get connection");
                return -1;
            }
            
            String sql = "select * from entidad_supramunicipal where id_entidad = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id_entidad);
            rs = ps.executeQuery();
            while (rs.next()) {
                srid = rs.getInt("srid");
            }
            //¿Porque no se cerraba la conexion?
            //safeClose(null, null, ps, null);
            safeClose(null, null, ps, connection);
        } catch (Exception ex) {
            //safeClose(null, null, ps, null);
            safeClose(null, null, ps, connection);
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            logger.error("Exception: " + sw.toString());
            return -1;
        }
        
        return srid;
    }
}

