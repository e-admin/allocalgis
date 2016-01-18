/**
 * UserValidationLN.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.localgis.webservices.geomarketing.ln;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.geopista.protocol.administrador.EncriptarPassword;
import com.geopista.server.administradorCartografia.ACException;
import com.localgis.webservices.geomarketing.model.dao.DAOFactory;
import com.localgis.webservices.geomarketing.model.dao.IUserValidationDAO;
import com.localgis.webservices.geomarketing.model.dao.generic.UserValidationDAO;
import com.localgis.webservices.util.ConnectionUtilities;

public class UserValidationLN {
	private static Logger logger = Logger.getLogger(UserValidationLN.class);
	public int getActiveAndValidatedUserId(String userName,String password) throws ACException, NamingException{
		EncriptarPassword processPasswd = new EncriptarPassword(EncriptarPassword.TYPE2_ALGORITHM);
		String encryptedPassword;
		try {
			encryptedPassword = processPasswd.encriptar(password);
		} catch (Exception e1) {
			throw new ACException("Error password");
		}
		if(logger.isDebugEnabled())logger.debug("testing user/password validation");
		Connection connection = null;
		Integer userId = null;
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory();
	        if(logger.isDebugEnabled())logger.debug("Opening connection");
	        connection = daoFactory.getConnection();
	        IUserValidationDAO userValidationDAO = new UserValidationDAO();
            
	        userId = userValidationDAO.getActiveAndValidatedUserId(connection, userName, encryptedPassword);
		}catch (SQLException e){
			logger.error("Error reading warning list",e);
		}finally {
			ConnectionUtilities.closeConnection(connection);
			if(logger.isDebugEnabled())logger.debug("Closing connection");
		}
		return userId;
	}
	public int getUserId(String userName) throws NamingException{
		if(logger.isDebugEnabled())logger.debug("testing user/password validation");
		Connection connection = null;
		Integer userId = null;
		try {
			DAOFactory daoFactory = DAOFactory.getDAOFactory();
	        if(logger.isDebugEnabled())logger.debug("Opening connection");
	        connection = daoFactory.getConnection();
	        IUserValidationDAO userValidationDAO = new UserValidationDAO();
            
	        userId = userValidationDAO.getUserId(connection, userName);
		}catch (SQLException e){
			logger.error("Error reading warning list",e);
		}finally {
			ConnectionUtilities.closeConnection(connection);
			if(logger.isDebugEnabled())logger.debug("Closing connection");
		}
		return userId;
	}
}
