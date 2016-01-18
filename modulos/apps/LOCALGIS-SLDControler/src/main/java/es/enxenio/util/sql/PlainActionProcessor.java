/**
 * PlainActionProcessor.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package es.enxenio.util.sql;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import es.enxenio.util.exceptions.InternalErrorException;
import es.enxenio.util.exceptions.ModelException;

/**
 * A utility class to execute plain actions.
 */
public final class PlainActionProcessor {

	private PlainActionProcessor() {}

	public final static Object process(DataSource dataSource, 
		NonTransactionalPlainAction action)
			throws ModelException, InternalErrorException {

		Connection connection = null;
		
		try {
						
			connection = dataSource.getConnection();			
			
			return action.execute(connection);
			
		} catch (SQLException e) {
			throw new InternalErrorException(e);
		} finally {
			GeneralOperations.closeConnection(connection);
		}
		
	}	
	
	/**
	 * Executes a transactional plain action over a connection with
	 * transaction isolation level set to "transaction serializable". If the
	 * action throws any exception, the transaction is rolled back.
	 */
	public final static Object process(DataSource dataSource,
		TransactionalPlainAction action)
		throws ModelException, InternalErrorException {
		
		Connection connection = null;
		boolean commited = false;
		
		try {
		
			/* 
			 * Get a connection with autocommit to "false" and isolation
			 * level to "TRANSACTION_SERIALIZABLE".*/ 
			 
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			connection.setTransactionIsolation(
				Connection.TRANSACTION_SERIALIZABLE);
			
			/* Execute action. */
			Object result = action.execute(connection);
			
			/* Commit transaction.*/ 
			connection.commit();
			commited = true;
			
			/* Return "result". */
			return result;
			
		} catch(SQLException e) {
			throw new InternalErrorException(e);
		} finally {
			try {
				if (connection != null) {
					if (!commited) {
						connection.rollback();
					}
					connection.close();
				}
			} catch (SQLException e) {
				throw new InternalErrorException(e);
			}
		}
						
	}

}
