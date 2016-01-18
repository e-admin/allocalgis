package com.geopista.ui.plugin.external;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import java.sql.*;

import com.geopista.app.AppContext;
import com.geopista.protocol.administrador.EncriptarPassword;

public class ExternalDataSourceDAO {

	
	private final static String idColumnName = "ID_DataSource";
	private final static String nameColumnName = "nombre";
	private final static String driverColumnName = "driver";
	private final static String urlColumnName = "cadena_conexion";
	private final static String usernameColumnName = "username";
	private final static String passwordColumnName = "password";
	
	private final static String listQuery = "listQueryExternalDataSource"; 
	private final static String insertQuery = "insertQueryExternalDataSource"; 
	private final static String findQuery = "findQueryExternalDataSource";
	private final static String deleteQuery ="deleteQueryExternalDataSource";
	private final static String updateQuery = "updateQueryExternalDataSource";
	private final static String nextValQuery = "nextValQueryPostgres";
	private static EncriptarPassword encrypt = new EncriptarPassword();
	
	private static AppContext aplicacion = (AppContext) AppContext.getApplicationContext();
	
	private Connection getConnection() {
		try {
			//Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/geopista","geopista","geopista");
			Connection connection = aplicacion.getConnection(); 
			return connection;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void closeConnection(Connection connection) {
		try {
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ExternalDataSourceDAO() {

	}
	
	public Vector list() {
		Connection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(listQuery);
			preparedStatement.setInt(1, AppContext.getIdMunicipio());
			ResultSet resultSet = preparedStatement.executeQuery();
			Vector vector = new Vector();
			while (resultSet.next()) {
				int id = resultSet.getInt(idColumnName);
				String name = resultSet.getString(nameColumnName);
				String driver = resultSet.getString(driverColumnName);
				String url = resultSet.getString(urlColumnName);
				String username = resultSet.getString(usernameColumnName);
				String password = resultSet.getString(passwordColumnName);

				ExternalDataSource dataSource = new ExternalDataSource();
				dataSource.setId(id);
				dataSource.setConnectString(url);
				dataSource.setName(name);
				dataSource.setDriver(driver);
				dataSource.setUserName(username);
				dataSource.setPassword(encrypt.undoEncrip(password));
				vector.add(dataSource);
			}
			return vector;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection!=null) {
				closeConnection(connection);
			}
		}		
		
	}
	
	public void insert(ExternalDataSource externalDataSource) {
		Connection connection = getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement(nextValQuery);
			ResultSet resultSet = statement.executeQuery();
			int nextval = 0;
			if (resultSet.next()) {
				nextval = resultSet.getInt(1);
				nextval++;
				PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
				preparedStatement.setInt(1, nextval);
				preparedStatement.setString(2, externalDataSource.getName());
				preparedStatement.setString(3, externalDataSource.getDriver());
				preparedStatement.setString(4, externalDataSource.getConnectString());			
				preparedStatement.setString(5, externalDataSource.getUserName());
				preparedStatement.setString(6, encrypt.doEncrip(externalDataSource.getPassword()));
				preparedStatement.setInt(7, AppContext.getIdMunicipio());
				preparedStatement.execute();
			}
						

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection!=null) {
				closeConnection(connection);
			}
		}
	}
	
	public ExternalDataSource find(String nameExternalDataSource) {
		Connection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(findQuery);
			preparedStatement.setString(1, nameExternalDataSource);
			preparedStatement.setInt(2, AppContext.getIdMunicipio());
			ResultSet resultSet = preparedStatement.executeQuery();	
			if (resultSet.next()) {
				int id = resultSet.getInt(idColumnName);
				String name = resultSet.getString(nameColumnName);
				String driver = resultSet.getString(driverColumnName);
				String url = resultSet.getString(urlColumnName);
				String username = resultSet.getString(usernameColumnName);
				String password = resultSet.getString(passwordColumnName);
				ExternalDataSource dataSource = new ExternalDataSource();
				dataSource.setId(id);
				dataSource.setConnectString(url);
				dataSource.setName(name);
				dataSource.setDriver(driver);
				dataSource.setUserName(username);
				dataSource.setPassword(encrypt.undoEncrip(password));
				return dataSource;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (connection!=null) {
				closeConnection(connection);
			}
		}
		return null;
	}
	
	public void delete(int id) {
		Connection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
			preparedStatement.setInt(1, id);
			preparedStatement.executeUpdate();	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} finally {
			if (connection!=null) {
				closeConnection(connection);
			}
		}
	}
	
	public void update(ExternalDataSource externalDataSource) {
		Connection connection = getConnection();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
			preparedStatement.setString(1, externalDataSource.getName());
			preparedStatement.setString(2, externalDataSource.getDriver());
			preparedStatement.setString(3, externalDataSource.getConnectString());			
			preparedStatement.setString(4, externalDataSource.getUserName());
			preparedStatement.setString(5, encrypt.doEncrip(externalDataSource.getPassword()));
			preparedStatement.setInt(6, externalDataSource.getId());
			preparedStatement.executeUpdate();			

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection!=null) {
				closeConnection(connection);
			}
		}
	}
	
	public static void main(String[] args) {
		ExternalDataSourceDAO externalDataSourceDAO = new ExternalDataSourceDAO();
		ExternalDataSource dataSource = new ExternalDataSource();
		dataSource.setName("Oracle");
		dataSource.setDriver("Driver");
		dataSource.setConnectString("URL");
		dataSource.setUserName("User");
		dataSource.setPassword("Password");
		externalDataSourceDAO.insert(dataSource);
		Vector v = externalDataSourceDAO.list();
		System.out.println(v.size());
		System.out.println(externalDataSourceDAO.find("Oracle"));
		dataSource.setConnectString("Adolfo");
		dataSource.setId(2);
		externalDataSourceDAO.update(dataSource);
	}

	
}
