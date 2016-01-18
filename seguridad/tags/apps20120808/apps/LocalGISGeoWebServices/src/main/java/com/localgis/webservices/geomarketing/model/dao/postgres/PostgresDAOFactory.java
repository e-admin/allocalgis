package com.localgis.webservices.geomarketing.model.dao.postgres;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.localgis.webservices.geomarketing.model.dao.DAOFactory;
import com.localgis.webservices.geomarketing.model.dao.ILocalGISGeoMarketingDAO;



public class PostgresDAOFactory extends DAOFactory
{
    
    
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
	public ILocalGISGeoMarketingDAO getLocalGISGeoMarketingDAO() {
		return new PostgresLocalGISGeoMarketingDAO();
	}


    
}
