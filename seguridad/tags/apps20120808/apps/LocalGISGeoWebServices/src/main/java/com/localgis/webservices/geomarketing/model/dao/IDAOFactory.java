package com.localgis.webservices.geomarketing.model.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.sql.DataSource;

public interface IDAOFactory
{
    public Connection getConnection() throws SQLException, NamingException;
    public DataSource getDataSource() throws NamingException;
    public ILocalGISGeoMarketingDAO getLocalGISGeoMarketingDAO();
}
