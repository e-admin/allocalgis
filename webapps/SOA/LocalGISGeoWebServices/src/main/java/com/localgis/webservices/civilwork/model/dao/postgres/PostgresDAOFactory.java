/**
 * PostgresDAOFactory.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.civilwork.model.dao.postgres;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.localgis.webservices.civilwork.model.dao.DAOFactory;
import com.localgis.webservices.civilwork.model.dao.ILocalGISInterventionsDAO;
import com.localgis.webservices.civilwork.model.dao.ILocalGISNotesDAO;
import com.localgis.webservices.civilwork.model.dao.ILocalGISPlanesObraDAO;
import com.localgis.webservices.civilwork.model.dao.IUserValidationDAO;
import com.localgis.webservices.civilwork.model.dao.generic.UserValidationDAO;
import com.localgis.webservices.civilwork.model.dao.postgres.warnings.PostgresLocalGISInterventionsDAO;
import com.localgis.webservices.civilwork.model.dao.postgres.warnings.PostgresLocalGISNotesDAO;
import com.localgis.webservices.civilwork.model.dao.postgres.warnings.PostgresLocalGISPlanesObraDAO;



public class PostgresDAOFactory extends DAOFactory
{
    public static final String DRIVER = "";
    public static final String DURL = "";
    
    
    public Connection getConnection() throws SQLException, NamingException
    {
		return getDataSource().getConnection();

    }
    
    public DataSource getDataSource() throws NamingException 
    {
        Context initContext = new InitialContext();
        Context envContext  = (Context)initContext.lookup("java:/comp/env");
        return (DataSource)envContext.lookup("postgresql");
    }


	@Override
	public IUserValidationDAO getUserValidationDAO() {
		return new UserValidationDAO();
	}

	@Override
	public ILocalGISInterventionsDAO getLocalGISInterventionsDAO() {
		
		return new PostgresLocalGISInterventionsDAO();
	}

	@Override
	public ILocalGISNotesDAO getLocalGISNotesDAO() {
		// TODO Auto-generated method stub
		return new PostgresLocalGISNotesDAO();
	}

	@Override
	public ILocalGISPlanesObraDAO getLocalGISPlanesObraDAO() {
		// TODO Auto-generated method stub
		return new PostgresLocalGISPlanesObraDAO();
	}


    
}
