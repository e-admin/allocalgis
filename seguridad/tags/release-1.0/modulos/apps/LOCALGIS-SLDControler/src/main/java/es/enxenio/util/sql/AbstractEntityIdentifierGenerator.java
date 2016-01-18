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
