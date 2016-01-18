package com.geopista.app.backup;

//import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.prefs.Preferences;

//import javax.crypto.BadPaddingException;
//import javax.crypto.Cipher;
//import javax.crypto.IllegalBlockSizeException;
//import javax.crypto.SecretKey;
//import javax.crypto.SecretKeyFactory;
//import javax.crypto.spec.DESKeySpec;

public class SecurityManager {
	
	private static final String GEOPISTA_PACKAGE = "/com/geopista/app";
	
	private static Connection getConnection(String user, String password) {
		Preferences pref = Preferences.userRoot().node(GEOPISTA_PACKAGE);
		String url = pref.get("conexion.url", "");

		Connection connection = null;
		try {
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return connection;
	}
	
	public static boolean isAuthorized(String user,String password) {
		Connection connection = getConnection(user, password);
		try {
			if (connection != null)
				return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

}
