/**
 * 
 */
package com.geopista.server.database;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * @author seilagamo
 * 
 */
public class COperacionesMunicipios {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger
            .getLogger(COperacionesMunicipios.class);

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
    
    public static int  getSRID(int id_municipio) {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;        
        int srid = 0;
        
        
        try {
            //logger.debug("Inicio.");
        
            //****************************************
            //** Obtener una conexion de la base de datos
            //****************************************************
            connection = CPoolDatabase.getConnection();
            if (connection == null) {
                logger.warn("Cannot get connection");
                return -1;
            }
            
            String sql = "select * from municipios where id = ?";
            ps = connection.prepareStatement(sql);
            ps.setInt(1, id_municipio);
            rs = ps.executeQuery();
            while (rs.next()) {
                srid = rs.getInt("srid_proyeccion");
            }
            //Porque no se cerraba la conexion??
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
