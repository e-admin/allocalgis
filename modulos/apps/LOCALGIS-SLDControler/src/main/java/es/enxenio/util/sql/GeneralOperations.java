/**
 * GeneralOperations.java
 * © MINETUR, Government of Spain
 * This program is part of LocalGIS
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
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
