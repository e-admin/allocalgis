package es.enxenio.util.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import es.enxenio.util.exceptions.InternalErrorException;

public final class GeneralOperations {

	private GeneralOperations() {}
	
	/**
     * It closes a <code>ResultSet</code> if not <code>null</code>.
     */
	public static void closeResultSet(ResultSet resultSet) 
		throws InternalErrorException {
		
		if (resultSet != null) {		
			try {
				resultSet.close();
			} catch (SQLException e) {
				throw new InternalErrorException(e);
			}		
		}
		
	}

	/**
     * It closes a <code>Statement</code> if not <code>null</code>.
     */
	public static void closeStatement(Statement statement) 
		throws InternalErrorException {
		
		if (statement != null) {		
			try {
				statement.close();
			} catch (SQLException e) {
				throw new InternalErrorException(e);
			}		
		}
		
	}
	
	/**
     * It closes a <code>Connection</code> if not <code>null</code>.
     */
	public static void closeConnection(Connection connection)
		throws InternalErrorException {
		
		if (connection != null) {		
			try {
				connection.close();
			} catch (SQLException e) {
				throw new InternalErrorException(e);
			}		
		}
		
	}

}
