/**
 * UserDAO.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.validation.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.satec.localgismobile.server.validation.vo.PermissionVO;
import es.satec.localgismobile.server.validation.vo.UserVO;

public class UserDAO {

	private static final Logger logger = Logger.getLogger(UserDAO.class);

	private static final String SQL_QUERY_FINDBYLOGIN = "SELECT id, name, nombrecompleto, password, mail FROM iuseruserhdr WHERE name ILIKE ?";

	private static final String SQL_QUERY_CHECKACCESSPERMISSION = "SELECT idacl, idperm FROM r_group_perm, iusergroupuser " + 
		"WHERE r_group_perm.GROUPID = iusergroupuser.GROUPID AND iusergroupuser.userid=? AND idperm||'-'||idacl NOT IN (" +
			"SELECT idperm||'-'||idacl FROM r_usr_perm WHERE r_usr_perm.userid=? AND r_usr_perm.aplica=0) " +
		"AND idacl=? AND idperm=? " +
		"UNION SELECT idacl,idperm FROM r_usr_perm WHERE r_usr_perm.userid=? AND (r_usr_perm.aplica<>0 OR r_usr_perm.aplica IS NULL) " +
		"AND idacl=? AND idperm=?";
	
	private static final String SQL_QUERY_FINDLAYERPERMISSIONS = "SELECT name, idperm " +
		"FROM (SELECT idacl, idperm FROM r_group_perm, iusergroupuser " +
			"WHERE r_group_perm.GROUPID = iusergroupuser.GROUPID AND iusergroupuser.userid=? AND idperm||'-'||idacl NOT IN (" +
				"SELECT idperm||'-'||idacl FROM r_usr_perm WHERE r_usr_perm.userid=? AND r_usr_perm.aplica=0) " +
				"UNION SELECT idacl,idperm FROM r_usr_perm WHERE r_usr_perm.userid=? AND (r_usr_perm.aplica<>0 OR r_usr_perm.aplica IS NULL) " +
		") as acl_permisos, layers l " +
		"WHERE l.acl = acl_permisos.idacl " +
		"ORDER BY id_layer, idperm";

	public UserDAO() {
	}

	public UserVO findByLoginName(Connection conn, String loginName) throws SQLException {
		UserVO user = null;

		logger.debug("Ejecutando findByLoginName");

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(SQL_QUERY_FINDBYLOGIN);
			ps.setString(1, loginName);
			rs = ps.executeQuery();
			if(rs != null) {
				if (rs.next()) {
					int i=1;
					user = new UserVO();
					user.setId(rs.getInt(i++));
					user.setLogin(rs.getString(i++));
					user.setCompleteName(rs.getString(i++));
					user.setPassword(rs.getString(i++));
					user.setMail(rs.getString(i++));
				}
				rs.close();
				ps.close();
			}
		} catch (SQLException e) {
			logger.error("DataBase Error", e);
			throw e;
		} finally {
			try {
				if(rs != null) rs.close();
			} catch (SQLException e) {
				logger.error("Error closing result set", e);
			}
			try {
				if(ps != null) ps.close();
			} catch (SQLException e) {
				logger.error("Error closing prepared statement", e);
			}
		}
		return user;
	}

	public boolean checkAccessPermission(Connection conn, int userId, int idAcl, int idPerm) throws SQLException {
		logger.debug("Ejecutando checkAccessPermission");

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(SQL_QUERY_CHECKACCESSPERMISSION);
			ps.setInt(1, userId);
			ps.setInt(2, userId);
			ps.setInt(3, idAcl);
			ps.setInt(4, idPerm);
			ps.setInt(5, userId);
			ps.setInt(6, idAcl);
			ps.setInt(7, idPerm);
			rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			logger.error("DataBase Error", e);
			throw e;
		} finally {
			try {
				if(rs != null) rs.close();
			} catch (SQLException e) {
				logger.error("Error closing result set", e);
			}
			try {
				if(ps != null) ps.close();
			} catch (SQLException e) {
				logger.error("Error closing prepared statement", e);
			}
		}
	}

	public Vector findLayerPermissions(Connection conn, int userId) throws SQLException {
		Vector permissions = new Vector();

		logger.debug("Ejecutando findLayerPermissions");

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(SQL_QUERY_FINDLAYERPERMISSIONS);
			ps.setInt(1, userId);
			ps.setInt(2, userId);
			ps.setInt(3, userId);
			rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					int i = 1;
					PermissionVO p = new PermissionVO();
					p.setLayer(rs.getString(i++));
					p.setIdPerm(rs.getInt(i++));
					permissions.addElement(p);
				}
				rs.close();
				ps.close();
			}
		} catch (SQLException e) {
			logger.error("DataBase Error", e);
			throw e;
		} finally {
			try {
				if(rs != null) rs.close();
			} catch (SQLException e) {
				logger.error("Error closing result set", e);
			}
			try {
				if(ps != null) ps.close();
			} catch (SQLException e) {
				logger.error("Error closing prepared statement", e);
			}
		}
		return permissions;
	}

}
