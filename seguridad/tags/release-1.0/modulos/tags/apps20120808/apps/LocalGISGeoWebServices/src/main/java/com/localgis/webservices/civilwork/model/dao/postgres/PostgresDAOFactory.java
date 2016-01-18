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
import com.localgis.webservices.civilwork.model.dao.IUserValidationDAO;
import com.localgis.webservices.civilwork.model.dao.generic.UserValidationDAO;
import com.localgis.webservices.civilwork.model.dao.postgres.warnings.PostgresLocalGISInterventionsDAO;
import com.localgis.webservices.civilwork.model.dao.postgres.warnings.PostgresLocalGISNotesDAO;



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


    
}
