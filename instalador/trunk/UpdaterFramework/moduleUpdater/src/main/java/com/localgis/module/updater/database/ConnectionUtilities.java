package com.localgis.module.updater.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;





public class ConnectionUtilities
{
	private static final Logger log=Logger.getLogger(ConnectionUtilities.class.toString());;
	
    
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
        
        log.info("CIERRE CONEXION BBDD");
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
/**
 * ConnectionUtilities.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
