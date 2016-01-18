package es.enxenio.util.sql;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.sql.DataSource;

import es.enxenio.util.configuration.ConfigurationParametersManager;

/**
 * A non-pooled implementation of a <code>DataSource</code>.
 * <p>
 * <b>WARNING:</b> The only method implemented of <code>DataSource</code> is
 * <code>getConnection()</code>. Rest of methods throw the unchecked exception 
 * <code>UnsupportedOperationException</code>.
 * <p>
 * Required configuration properties:
 * <ul>
 * <li><code>SimpleDataSource/driverClassName</code>: it must specify the full
 * class name of the JBDC driver.</li>
 * <li><code>SimpleDataSource/url</code>: the database URL (without user and
 * password).</li>
 * <li><code>SimpleDataSource/user</code>: the database user.</li>
 * <li><code>SimpleDataSource/password</code>: the user's password.</li>
 * </ul>
 */
public class SimpleDataSource implements DataSource {

	private static final String DRIVER_CLASS_NAME_PARAMETER = 
		"SimpleDataSource/driverClassName";
	private static final String URL_PARAMETER = 
		"SimpleDataSource/url";
	private static final String USER_PARAMETER = 
		"SimpleDataSource/user";
	private static final String PASSWORD_PARAMETER = 
		"SimpleDataSource/password";
		
	private static String url;
	private static String user;
	private static String password;
		
	static {

		try {
	
			/* Read configuration parameters. */
			String driverClassName = 
				ConfigurationParametersManager.getParameter(
					DRIVER_CLASS_NAME_PARAMETER);
			url = ConfigurationParametersManager.getParameter(URL_PARAMETER);
			user = ConfigurationParametersManager.getParameter(USER_PARAMETER);
			password = ConfigurationParametersManager.getParameter(
				PASSWORD_PARAMETER);

			/* Load driver. */	
			Class.forName(driverClassName);
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(url, user, password);
	}
		
	public Connection getConnection(String username, String password)
		throws SQLException {
		
		throw new UnsupportedOperationException("Not implemented");
		
	}

	public PrintWriter getLogWriter() throws SQLException {	
		throw new UnsupportedOperationException("Not implemented");	
	}

	public void setLogWriter(PrintWriter out) throws SQLException {
		throw new UnsupportedOperationException("Not implemented");
	}

	public void setLoginTimeout(int seconds) throws SQLException {
		throw new UnsupportedOperationException("Not implemented");
	}

	public int getLoginTimeout() throws SQLException {
		throw new UnsupportedOperationException("Not implemented");
	}

	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
