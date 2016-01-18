/**
 * PostGISDAOFactoryWS.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.model.dao.postgis;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import com.localgis.model.dao.DAOFactoryWS;
import com.localgis.model.dao.ISOALocalGISDAOWS;
import com.localgis.model.dao.postgis.soa.PostGISSOALocalGISDAOWS;



public class PostGISDAOFactoryWS extends DAOFactoryWS
{
    public static final String DRIVER = "";
    public static final String DURL = "";


    public Connection getConnection(DataSource dataSource) throws SQLException
    {
        return dataSource.getConnection();
    }    
    
    
    public DataSource getDataSource() throws NamingException 
    {
        Context initContext = new InitialContext();
        Context envContext  = (Context)initContext.lookup("java:/comp/env");
        return (DataSource)envContext.lookup("jdbc/postgresql");
    }


	public ISOALocalGISDAOWS getSOALocalGISDAO() throws NamingException {
		
		return new PostGISSOALocalGISDAOWS();
	}


	
	




    


   

    


    

    
}
