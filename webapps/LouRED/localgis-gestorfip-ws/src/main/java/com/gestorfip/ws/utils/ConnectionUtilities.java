package com.gestorfip.ws.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import com.gestorfip.ws.servidor.OperacionesBBDD;



public class ConnectionUtilities
{
	static Logger logger = Logger.getLogger(OperacionesBBDD.class);
    
    public static void closeConnection(Connection connection)
    {
        if (connection != null) {        
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public static void closeConnection(Connection connection, PreparedStatement preparedStatement, ResultSet rs)
    {
        if(rs!=null)
            try
        {
                rs.close();
        }catch(Exception e){}
        
        if(preparedStatement!=null)
            try
        {
                preparedStatement.close();
        }catch(Exception e){}
        
        if(connection!=null)
            try
        {
                connection.close();
        }catch(Exception e){}
        
//        logger.info("CIERRE CONEXION BBDD");
    }
    
    public static void closeConnection(Connection connection, Statement statement, ResultSet rs)
    {
        if(rs!=null)
            try
        {
                rs.close();
        }catch(Exception e){}
        
        if(statement!=null)
            try
        {
            	statement.close();
        }catch(Exception e){}
        
        if(connection!=null)
            try
        {
                connection.close();
        }catch(Exception e){}
    }
    
    
}
