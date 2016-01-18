/**
 * UserValidationDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.civilwork.model.dao.generic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.localgis.webservices.civilwork.model.dao.IUserValidationDAO;
import com.localgis.webservices.civilwork.util.ConnectionUtilities;
import com.localgis.webservices.civilwork.util.LocalGISUser;

public class UserValidationDAO implements IUserValidationDAO {
	
	
	public static String LOCALGIS_ESPACIOPUBLICO_ACL = "Gestion de Espacio Publico";
	public static String LOCALGIS_ESPACIOPUBLICO_LOGIN = "localgis.espaciopublico.login";
	public static String LOCALGIS_ESPACIOPUBLICO_ADMINISTRACION = "localgis.espaciopublico.administracion";
	public static String LOCALGIS_ESPACIOPUBLICO_NOTAS_CONSULTA = "localgis.espaciopublico.notas.consulta";
	public static String LOCALGIS_ESPACIOPUBLICO_NOTAS_ALTA = "localgis.espaciopublico.notas.alta";
	public static String LOCALGIS_ESPACIOPUBLICO_NOTAS_ELIMINAR = "localgis.espaciopublico.notas.eliminar";
	public static String LOCALGIS_ESPACIOPUBLICO_AVISOS_CONSULTA = "localgis.espaciopublico.intervenciones.consulta";
	public static String LOCALGIS_ESPACIOPUBLICO_AVISOS_ALTA = "localgis.espaciopublico.intervenciones.alta";
	public static String LOCALGIS_ESPACIOPUBLICO_AVISOS_MODIFICACION = "localgis.espaciopublico.intervenciones.modificacion";
	public static String LOCALGIS_ESPACIOPUBLICO_AVISOS_POSTPONER = "localgis.espaciopublico.intervenciones.postponer";
	public static String LOCALGIS_ESPACIOPUBLICO_AVISOS_DESCARTAR = "localgis.espaciopublico.intervenciones.descartar";
	public static String LOCALGIS_ESPACIOPUBLICO_AVISOS_ELIMINAR = "localgis.espaciopublico.intervenciones.eliminar";
	public static String LOCALGIS_ESPACIOPUBLICO_PLANESOBRA_CONSULTA = "localgis.espaciopublico.planesobra.consulta";
	public static String LOCALGIS_ESPACIOPUBLICO_PLANESOBRA_ALTA = "localgis.espaciopublico.planesobra.alta";
	public static String LOCALGIS_ESPACIOPUBLICO_PLANESOBRA_ELIMINAR = "localgis.espaciopublico.planesobra.eliminar";

	
	@Override
	public boolean login(Connection connection, Integer userId)
			throws SQLException {
		Integer idAcl = getIdAclNotes(connection);
		Integer idPerm = getIdPermFromName(connection, UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_LOGIN);
		return hasPerm(connection, userId, idAcl, idPerm);
	}

	
	
	@Override
	public boolean administration(Connection connection, Integer userId)
			throws SQLException {
		Integer idAcl = getIdAclNotes(connection);
		Integer idPerm = getIdPermFromName(connection, UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_ADMINISTRACION);
		return hasPerm(connection, userId, idAcl, idPerm);
		}

	@Override
	public boolean notesDelete(Connection connection, Integer userId)
			throws SQLException {
		Integer idAcl = getIdAclNotes(connection);
		Integer idPerm = getIdPermFromName(connection, UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_NOTAS_ELIMINAR);
		return hasPerm(connection, userId, idAcl, idPerm);
	}

	@Override
	public boolean notesQuery(Connection connection, Integer userId)
			throws SQLException {
		Integer idAcl = getIdAclNotes(connection);
		Integer idPerm = getIdPermFromName(connection, UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_NOTAS_CONSULTA);
		return hasPerm(connection, userId, idAcl, idPerm);
	}

	
	@Override
	public boolean warningsDelete(Connection connection, Integer userId)
			throws SQLException {
		Integer idAcl = getIdAclNotes(connection);
		Integer idPerm = getIdPermFromName(connection, UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_AVISOS_ELIMINAR);
		return hasPerm(connection, userId, idAcl, idPerm);
	}

	@Override
	public boolean warningsDiscard(Connection connection, Integer userId)
			throws SQLException {
		Integer idAcl = getIdAclNotes(connection);
		Integer idPerm = getIdPermFromName(connection, UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_AVISOS_DESCARTAR);
		return hasPerm(connection, userId, idAcl, idPerm);
	}

	@Override
	public boolean warningsModify(Connection connection, Integer userId)
			throws SQLException {
		Integer idAcl = getIdAclNotes(connection);
		Integer idPerm = getIdPermFromName(connection, UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_AVISOS_MODIFICACION);
		return hasPerm(connection, userId, idAcl, idPerm);
	}

	@Override
	public boolean warningsPostpone(Connection connection, Integer userId)
			throws SQLException {
		Integer idAcl = getIdAclNotes(connection);
		Integer idPerm = getIdPermFromName(connection, UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_AVISOS_POSTPONER);
		return hasPerm(connection, userId, idAcl, idPerm);
	}

	@Override
	public boolean warningsQuery(Connection connection, Integer userId)
			throws SQLException {
		Integer idAcl = getIdAclNotes(connection);
		Integer idPerm = getIdPermFromName(connection, UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_AVISOS_CONSULTA);
		return hasPerm(connection, userId, idAcl, idPerm);
	}
	protected Integer getUserId(Connection connection,String userName,Integer idMunicipio) throws SQLException{
		Integer userId = null;
		String sqlQuery = 
			"SELECT iuseruserhdr.id  " + 
			"FROM iuseruserhdr " + 
			"LEFT JOIN entidades_municipios ON entidades_municipios.id_entidad = iuseruserhdr.id_entidad " +  
			"WHERE (entidades_municipios.id_municipio = ? OR iuseruserhdr.id_entidad = 0) AND iuseruserhdr.name = ?";
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
	protected boolean hasPerm(Connection connection,Integer userId,Integer idAcl,Integer idPerm) throws SQLException{
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
	public boolean notesNew(Connection connection, Integer userId)
			throws SQLException {
		Integer idAcl = getIdAclNotes(connection);
		Integer idPerm = getIdPermFromName(connection, UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_NOTAS_ALTA);
		return hasPerm(connection, userId, idAcl, idPerm);
	}



	@Override
	public boolean warningsNew(Connection connection, Integer userId)
			throws SQLException {
		Integer idAcl = getIdAclNotes(connection);
		Integer idPerm = getIdPermFromName(connection, UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_AVISOS_ALTA);
		return hasPerm(connection, userId, idAcl, idPerm);
	}
	@Override
	public ArrayList<LocalGISUser> getAdminUserList(Connection connection,Integer idMunicipio) throws SQLException{
		ArrayList<LocalGISUser> users = new ArrayList<LocalGISUser>();
		Integer idAcl = getIdAclNotes(connection);
		Integer idPerm = getIdPermFromName(connection, UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_LOGIN);
		String sqlQuery = "SELECT name,userid,idperm,idacl,finalQuery.id_entidad,entidades_municipios.id_municipio FROM " +
						"(SELECT iuseruserhdr.name,iuseruserhdr.id_entidad,permlist.userid,permlist.idperm,permlist.idacl,min(permlist.aplica) as aplica FROM ( " +
						"SELECT userid,idperm,idacl,aplica from r_usr_perm " + 
						"UNION " + 
						"SELECT userid,idperm,idacl,1 as aplica from iusergroupuser,r_group_perm " + 
						"WHERE iusergroupuser.groupid = r_group_perm.groupid " +
						") as permlist,iuseruserhdr WHERE iuseruserhdr.id = permlist.userid AND iuseruserhdr.borrado = 0 AND permlist.idacl = ? AND permlist.idperm = ? " + 
						"group by iuseruserhdr.name,iuseruserhdr.id_entidad,permlist.idperm,permlist.idacl,permlist.userid " +
						"ORDER BY permlist.userid,permlist.idacl,permlist.idperm) AS finalQuery " +
						"LEFT JOIN entidades_municipios ON finalQuery.id_entidad = entidades_municipios.id_Entidad WHERE aplica = 1 AND (finalQuery.id_entidad = 0 OR entidades_municipios.id_municipio = ?) ";
		if(logger.isDebugEnabled())logger.debug("Calculating perm : " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, idAcl);
			preparedStatement.setInt(2, idPerm);
			preparedStatement.setInt(3, idMunicipio);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			while (rs.next()){
				LocalGISUser user = new LocalGISUser();
				user.setName(rs.getString("name"));
				user.setUserId(rs.getInt("userid"));
				users.add(user);
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return users;
	}
	@Override
	public ArrayList<LocalGISUser> getUser(Connection connection,Integer idMunicipio,Integer userId) throws SQLException{
		ArrayList<LocalGISUser> users = new ArrayList<LocalGISUser>();
		Integer idAcl = getIdAclNotes(connection);
		Integer idPerm = getIdPermFromName(connection, UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_LOGIN);
		String sqlQuery = "SELECT name,userid,idperm,idacl,finalQuery.id_entidad,entidades_municipios.id_municipio FROM " +
						"(SELECT iuseruserhdr.name,iuseruserhdr.id_entidad,permlist.userid,permlist.idperm,permlist.idacl,min(permlist.aplica) as aplica FROM ( " +
						"SELECT userid,idperm,idacl,aplica from r_usr_perm " + 
						"UNION " + 
						"SELECT userid,idperm,idacl,1 as aplica from iusergroupuser,r_group_perm " + 
						"WHERE iusergroupuser.groupid = r_group_perm.groupid " +
						") as permlist,iuseruserhdr WHERE iuseruserhdr.id = permlist.userid AND iuseruserhdr.borrado = 0 AND permlist.idacl = ? AND permlist.idperm = ? " + 
						"group by iuseruserhdr.name,iuseruserhdr.id_entidad,permlist.idperm,permlist.idacl,permlist.userid " +
						"ORDER BY permlist.userid,permlist.idacl,permlist.idperm) AS finalQuery " +
						"LEFT JOIN entidades_municipios ON finalQuery.id_entidad = entidades_municipios.id_Entidad WHERE aplica = 1 AND (finalQuery.id_entidad = 0 OR entidades_municipios.id_municipio = ?) AND userid = ?";
		if(logger.isDebugEnabled())logger.debug("Calculating perm : " + sqlQuery);
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setInt(1, idAcl);
			preparedStatement.setInt(2, idPerm);
			preparedStatement.setInt(3, idMunicipio);
			preparedStatement.setInt(4, userId);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				LocalGISUser user = new LocalGISUser();
				user.setName(rs.getString("name"));
				user.setUserId(rs.getInt("userid"));
				users.add(user);
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return users;
	}



	@Override
	public Integer getIdAclNotes(Connection connection) throws SQLException {
		String sqlQuery = "SELECT IDACL FROM ACL WHERE NAME = ?";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer idAcl = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setString(1, UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_ACL);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				idAcl = rs.getInt("idacl");  
				if(logger.isDebugEnabled())logger.debug("ACL " + UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_ACL + " found!" );
		    }else{
		    	if(logger.isDebugEnabled())logger.debug("ACL " + UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_ACL + " Not found. Check acl table! " );
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return idAcl;
	}



	@Override
	public Integer getIdPermFromName(Connection connection, String description) throws SQLException {
		String sqlQuery = "select idperm from usrgrouperm where def = ?";
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Integer idPerm = null;
		try{
			if(logger.isDebugEnabled())logger.debug("Starting Statement");
			preparedStatement = connection.prepareStatement(sqlQuery);
			preparedStatement.setString(1, description);
			if(logger.isDebugEnabled())logger.debug("Starting ResultSet");
			rs = preparedStatement.executeQuery();
			if (rs.next()){
				idPerm = rs.getInt("idperm");
				if(logger.isDebugEnabled())logger.debug("Perm found!");
		    }else{
		    	if(logger.isDebugEnabled())logger.debug(description + " perm not found. Check database table!");
		    }
		}finally{
			if(logger.isDebugEnabled())logger.debug("Closing Statement & resultset");
			ConnectionUtilities.closeConnection(null, preparedStatement, rs);
		}
		return idPerm;
	}
	
	@Override
	public boolean planesObraNew(Connection connection, Integer userId)
			throws SQLException {
		Integer idAcl = getIdAclNotes(connection);
		Integer idPerm = getIdPermFromName(connection, UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_PLANESOBRA_ALTA);
		return hasPerm(connection, userId, idAcl, idPerm);
	}
	
	@Override
	public boolean planesObraDelete(Connection connection, Integer userId)
			throws SQLException {
		Integer idAcl = getIdAclNotes(connection);
		Integer idPerm = getIdPermFromName(connection, UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_PLANESOBRA_ELIMINAR);
		return hasPerm(connection, userId, idAcl, idPerm);
	}

	@Override
	public boolean planesObraQuery(Connection connection, Integer userId)
			throws SQLException {
		Integer idAcl = getIdAclNotes(connection);
		Integer idPerm = getIdPermFromName(connection, UserValidationDAO.LOCALGIS_ESPACIOPUBLICO_PLANESOBRA_CONSULTA);
		return hasPerm(connection, userId, idAcl, idPerm);
	}
}
