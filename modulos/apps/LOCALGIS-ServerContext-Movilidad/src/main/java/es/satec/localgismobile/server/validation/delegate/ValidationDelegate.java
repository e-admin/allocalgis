/**
 * ValidationDelegate.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.satec.localgismobile.server.validation.delegate;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Vector;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.servlet.ServletContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.geopista.protocol.administrador.EncriptarPassword;
import com.geopista.server.database.CPoolDatabase;

import es.satec.localgismobile.server.validation.dao.UserDAO;
import es.satec.localgismobile.server.validation.exceptions.IncorrectPasswordException;
import es.satec.localgismobile.server.validation.exceptions.PermissionDeniedException;
import es.satec.localgismobile.server.validation.vo.PermissionVO;
import es.satec.localgismobile.server.validation.vo.UserVO;

public class ValidationDelegate {
	
	private static final Logger logger = Logger.getLogger(ValidationDelegate.class);

	private ServletContext servletContext;
	
	public ValidationDelegate(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public UserVO login(String loginName, String password, int idPerm, int idAcl) throws IncorrectPasswordException, PermissionDeniedException {
		UserVO user = null;
		Connection conn = null;
		
		try {
			UserDAO dao = new UserDAO();
			conn = getConnection();
			// busqueda del usuario
			user = dao.findByLoginName(conn, loginName);
			if (user!=null) {
				// Comprobacion de la contraseña
				String encryptedPassword=null;       
				try {
					EncriptarPassword ec=new EncriptarPassword(EncriptarPassword.TYPE2_ALGORITHM);
					encryptedPassword=ec.encriptar(new String(password));
				} catch (Exception e) {
					encryptedPassword=new String(password);
				}
				if (encryptedPassword.equals(user.getPassword())) {
					// Comprobacion de permisos
					if (dao.checkAccessPermission(conn, user.getId(), idAcl, idPerm)) {
						Vector foundPermissions = dao.findLayerPermissions(conn, user.getId());
						// Rellenar el hashmap de permisos
						HashMap permissions = new HashMap();
						Enumeration en = foundPermissions.elements();
						while (en.hasMoreElements()) {
							PermissionVO p = (PermissionVO) en.nextElement();
							Collection layerPerm = (Collection) permissions.get(p.getLayer());
							if (layerPerm == null) {
								layerPerm = new ArrayList();
								permissions.put(p.getLayer(), layerPerm);
							}
							layerPerm.add(new Integer(p.getIdPerm()));
						}
						user.setPermissions(permissions);
					}
					else {
						logger.debug("El usuario no tiene permiso para ejecutar la aplicación");
						user=null;
						throw new PermissionDeniedException();
					}
				}
				else {
					logger.debug("La contraseña es incorrecta");
					user = null;
					throw new IncorrectPasswordException();
				}
			}
			else {
				logger.debug("El usuario no existe");
				throw new IncorrectPasswordException();
			}
		} catch (PermissionDeniedException e) {
			throw e;
		} catch (IncorrectPasswordException e) {
			throw e;
		} catch (Exception e) {
			logger.error("Error al validar el usuario", e);
			user = null;
		} finally {
			if (conn!=null)
				try {
					conn.close();
				} catch (SQLException e) {}
		}
		
		return user;
	}

	/**
	 * Cifra la password siguiendo el cifrado de geopista
	 * @param password Password a cifrar
	 * @return La password cifrada
	 * @throws Exception Si ocurre algún error
	 */
	public String encrytGeopistaPassword(String password) throws Exception {
	    Cipher ecipher;
	    String semilla = "GEOPISTA";
	    byte[] rawkey = semilla.getBytes("UTF8");
	    DESKeySpec keyspec = new DESKeySpec(rawkey);
	    SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("DES");
	    SecretKey key = keyfactory.generateSecret(keyspec);
	    byte[] utf8 = password.getBytes("UTF8");
	    ecipher = Cipher.getInstance("DES");
	    ecipher.init(Cipher.ENCRYPT_MODE, key);
	    byte[] enc = ecipher.doFinal(utf8);
	    //return new sun.misc.BASE64Encoder().encode(enc);
	    return new String(new Base64().encode(enc));
    }

	private Connection getConnection() throws SQLException {
		//otra manera de obtener la conexión sería
//		import java.sql.*;
//		import javax.naming.*;
//		import javax.sql.*;
//
//
//		  /** Uses DriverManager.  */
//		  static Connection getSimpleConnection() {
//		    //See your driver documentation for the proper format of this string :
//		String BBDD = ConfigurationManager.getApplicationProperty(PROP_GEOPISTA_CON_BBDD);
//		    String DB_CONN_STRING = "jdbc:postgresql://"+BBDD+"/geopista_supramun";
//		    //Provided by your driver documentation. In this case, a MySql driver is used : 
//		    String DRIVER_CLASS_NAME = "org.postgresql.Driver";
//		    String USER_NAME = "geopista";
//		    String PASSWORD = "geopista";
//		    
//		    Connection result = null;
//		    try {
//		       Class.forName(DRIVER_CLASS_NAME).newInstance();
//		    }
//		    catch (Exception ex){
//		       log("Check classpath. Cannot load db driver: " + DRIVER_CLASS_NAME);
//		    }
//
//		    try {
//		      result = DriverManager.getConnection(DB_CONN_STRING, USER_NAME, PASSWORD);
//		    }
//		    catch (SQLException e){
//		       log( "Driver loaded, but cannot connect to db: " + DB_CONN_STRING);
//		    }
//		    return result;
//		  }
		//return null;
		return CPoolDatabase.getConnection();
		//return DataSourceLocator.getDataSource(Global.LOCALGIS_MOBILE_DS).getConnection();
	}
	
}
