package com.localgis.webservices.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class ConnectionUtilities
{

    
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
    }
    
    
}
