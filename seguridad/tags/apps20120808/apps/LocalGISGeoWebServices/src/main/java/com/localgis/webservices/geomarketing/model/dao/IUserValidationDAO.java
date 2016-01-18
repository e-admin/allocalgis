package com.localgis.webservices.geomarketing.model.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.geopista.server.administradorCartografia.ACException;


public interface IUserValidationDAO
{
	public boolean obtenerDatosGenerales(Connection connection,Integer userId) throws SQLException;
	public boolean obtenerDatosGeomarketing(Connection connection,Integer userId) throws SQLException;
	public boolean readLayer(Connection connection,Integer userId,Integer idLayer)throws SQLException;
	public boolean hasPerm(Connection connection,Integer userId,Integer idAcl,Integer idPerm) throws SQLException;
	public Integer getAclIdGeomarketing(Connection connection) throws SQLException;
	public Integer getIdPerm(Connection connection,String permName) throws SQLException;
	public Integer getUserId(Connection connection,String userName,Integer idMunicipio) throws SQLException;
	public Integer getUserId(Connection connection,String userName) throws SQLException;
	public Integer getIdAclFromIdLayer(Connection connection, Integer idLayer) throws SQLException;
	public Integer getActiveAndValidatedUserId(Connection connection,String userName,String password) throws SQLException, ACException;
}


