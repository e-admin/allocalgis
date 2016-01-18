/**
 * UserValidationDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.geomarketing.model.dao.generic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.geopista.server.administradorCartografia.ACException;
import com.localgis.webservices.geomarketing.model.dao.IUserValidationDAO;
import com.localgis.webservices.util.ConnectionUtilities;

public class UserValidationDAO implements IUserValidationDAO {
	
	
	//private static Integer LOCALGIS_GEOMARKETING_DATOS_GENERAL = 10150;
	//private static Integer LOCALGIS_GEOMARKETING_DATOS_GEOMARKETING = 10151;
	public static String LOCALGIS_GEOMARKETING_DATOS_GENERALES = "localgis.geomarketing.obtenerdatosgenerales";
	public static String LOCALGIS_GEOMARKETING_DATOS_GEOMARKETING = "localgis.geomarketing.obtenerdatosgeomarketing";
	public static Integer LOCALGIS_GEOMARKETING_ID_GEOPISTA_LAYER_LEER = 871;
	public static String LOCALGIS_GEOMARKETING_GEOPISTA_LAYER_LEER = "Geopista.Layer.Leer";
	

	@Override
	public boolean obtenerDatosGenerales(Connection connection, Integer userId)	throws SQLException {
		Integer idGeomarketing = getAclIdGeomarketing(connection);
		Integer idDatosGenerales = getIdPerm(connection, UserValidationDAO.LOCALGIS_GEOMARKETING_DATOS_GENERALES);
		return hasPerm(connection, userId, idGeomarketing, idDatosGenerales);
	}



	@Override
	public boolean obtenerDatosGeomarketing(Connection connection,Integer userId) throws SQLException {
		Integer idGeomarketing = getAclIdGeomarketing(connection);
		Integer idDatosGomarketing = getIdPerm(connection, UserValidationDAO.LOCALGIS_GEOMARKETING_DATOS_GEOMARKETING);
		return hasPerm(connection, userId, idGeomarketing, idDatosGomarketing);
	}
	public Integer getUserId(Connection connection,String userName,Integer idMunicipio) throws SQLException{
		Integer userId = null;
		String sqlQuery = 
			"SELECT iuseruserhdr.id  " + 
			"FROM iuseruserhdr " + 
			"LEFT JOIN entidades_municipios ON entidades_municipios.id_entidad = iuseruserhdr.id_entidad " +  
			"WHERE (entidades_municipios.id_municipio = ? OR iuseruserhdr.id_entidad = 0) AND UPPER(iuseruserhdr.name) = UPPER(?)";
		if(logger.isDebugEnabled())logger.debug("Calculating UserId : " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, idMunicipio);
			preparedStatement.setString(2, userName);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				userId = new Integer(rs.getInt(1));
				if(logger.isDebugEnabled())logger.debug("User Id = " + userId);
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return userId;
	}
	public Integer getUserId(Connection connection,String userName) throws SQLException{
		Integer userId = null;
		String sqlQuery = 
			"SELECT iuseruserhdr.id  " + 
			"FROM iuseruserhdr " + 
			"WHERE UPPER(iuseruserhdr.name) = UPPER(?)";
		if(logger.isDebugEnabled())logger.debug("Calculating UserId : " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setString(1, userName);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				userId = new Integer(rs.getInt(1));
				if(logger.isDebugEnabled())logger.debug("User Id = " + userId);
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return userId;
	}
	public Integer getActiveAndValidatedUserId(Connection connection,String userName,String password) throws SQLException, ACException{
		Integer userId = null;
		String sqlQuery = 
			"SELECT iuseruserhdr.id  " + 
			"FROM iuseruserhdr " +   
			"WHERE UPPER(name) = UPPER(?) AND password = ?";
		if(logger.isDebugEnabled())logger.debug("Calculating UserId : " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, password);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				userId = new Integer(rs.getInt(1));
				if(logger.isDebugEnabled())logger.debug("User Id = " + userId);
		    }else
		    	throw new ACException("Wrong user/password");
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return userId;
	}
	
	public boolean hasPerm(Connection connection,Integer userId,Integer idAcl,Integer idPerm) throws SQLException{
		boolean hasPerm = false;
		String sqlQuery = "SELECT idperm,idacl FROM (SELECT idperm,idacl,min(aplica) as aplica FROM " + 
		"(" +
		"SELECT idperm,idacl,aplica from r_usr_perm WHERE userid = ? " + 
		"UNION " + 
		"select idperm,idacl,1 as aplica from iusergroupuser,r_group_perm where iusergroupuser.groupid = r_group_perm.groupid AND userid = ? " +  
		") as permlist WHERE idacl = ? and idperm = ? group by idperm,idacl) AS fQuery WHERE aplica = 1"; 
		if(logger.isDebugEnabled())logger.debug("Calculating perm : " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, userId);
			preparedStatement.setInt(2, userId);
			preparedStatement.setInt(3, idAcl);
			preparedStatement.setInt(4, idPerm);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				hasPerm = true;
				if(logger.isDebugEnabled())logger.debug("User has perm!");
		    }else{
		    	if(logger.isDebugEnabled())logger.debug("User has no perm!");
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return hasPerm;
	}
	
	private static Logger logger = Logger.getLogger(UserValidationDAO.class);




	@Override
	public Integer getAclIdGeomarketing(Connection connection) throws SQLException {
		String sqlQuery = "select idacl from acl where name = 'Servicios de Geomarketing'"; 
		if(logger.isDebugEnabled())logger.debug("Calculating perm : " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer result = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				result = rs.getInt("idacl");
				if(logger.isDebugEnabled())logger.debug("User has perm!");
		    }else{
		    	if(logger.isDebugEnabled())logger.debug("User has no perm!");
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return result;
	}
//select idperm from usrgrouperm where def = 'localgis.geomarketing.obtenerdatosgenerales'



	@Override
	public Integer getIdPerm(Connection connection, String permName)
			throws SQLException {
		String sqlQuery = "select idperm from usrgrouperm where def = ?"; 
		if(logger.isDebugEnabled())logger.debug("Calculating perm : " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer result = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setString(1, permName);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				result = rs.getInt("idperm");
				if(logger.isDebugEnabled())logger.debug("idPerm found!");
		    }else{
		    	if(logger.isDebugEnabled())logger.debug("idPerm not found!");
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return result;
	}



	@Override
	public Integer getIdAclFromIdLayer(Connection connection, Integer idLayer)
			throws SQLException {
		String sqlQuery = "select acl from layers where id_Layer = ?"; 
		if(logger.isDebugEnabled())logger.debug("Calculating perm : " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer result = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, idLayer);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				result = rs.getInt("acl");
				if(logger.isDebugEnabled())logger.debug("ACL: " + result);
		    }else{
		    	if(logger.isDebugEnabled())logger.debug("idLayer error!");
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return result;
	}



	@Override
	public boolean readLayer(Connection connection, Integer userId,Integer idLayer)
			throws SQLException {
		Integer idAclLayer = getIdAclFromIdLayer(connection, idLayer);
		return hasPerm(connection, userId, idAclLayer, UserValidationDAO.LOCALGIS_GEOMARKETING_ID_GEOPISTA_LAYER_LEER);
	}
	
	



	
}
