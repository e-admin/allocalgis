/**
 * AbstractEntityIdentifierGenerator.java
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

import es.enxenio.util.configuration.ConfigurationParametersManager;
import es.enxenio.util.exceptions.InternalErrorException;

/**
 * A template implementation of <code>EntityIdentifierGenerator</code>.
 * It implements <code>nextIdentifier(Connection, String)</code> by using
 * the abstract method <code>getQueryString(String)</code>, that must be
 * provided by concrete classes.
 * <p>
 * Required configuration properties:
 * <ul>
 * <li> <code>AbstractEntityIdentifierGenerator/sequences/<b>entityName</b>
 * </code> per entity: it specifies the name of the sequence for the entity
 * <code><b>entityName</b></code>.</li>
 * </ul>
 */
public abstract class AbstractEntityIdentifierGenerator 
	implements EntityIdentifierGenerator {

	protected AbstractEntityIdentifierGenerator() {}

	protected abstract String getQueryString(String sequenceName);
	
	public Long nextIdentifier(Connection connection, 
		String entityName) throws InternalErrorException {
	
		Statement statement = null;
		ResultSet resultSet = null;

        try {
		
			/* Get sequence name. */
			String sequenceName = ConfigurationParametersManager.getParameter(
				"AbstractEntityIdentifierGenerator/sequence-" + entityName);

            /* Get the next identifier. */
            String queryString =  getQueryString(sequenceName);
			statement = connection.createStatement();   
            resultSet = statement.executeQuery(queryString);
            if (!resultSet.next()) {
                throw new SQLException("Sequence '" + sequenceName + "'" +
					" does not return next value");
            }

            return new Long(resultSet.getLong(1));

		} catch (Exception e) {		
			throw new InternalErrorException(e);
        } finally {
            GeneralOperations.closeResultSet(resultSet);
            GeneralOperations.closeStatement(statement);
        }
		
	}
	
}
